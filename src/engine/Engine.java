/* ******************************************************
 * Project Snake - Ashanth CHANDRAMOHAN
 * ******************************************************/
package engine;

import tools.HardCodedParameters;
import tools.User;
import tools.Position;
import tools.Sound;

import specifications.EngineService;
import specifications.ObstacleService;
import specifications.AppleService;
import specifications.DataService;
import specifications.RequireDataService;
import specifications.SnakeService;

import java.util.Timer;
import java.util.TimerTask;

import data.ia.HealthyApple;
import data.ia.SnakePart;

import java.util.Random;
import java.util.ArrayList;

public class Engine implements EngineService, RequireDataService{
  private static final double friction=HardCodedParameters.friction,
                              snakeStep=HardCodedParameters.SnakeStep;
  
  private Timer engineClock;
  private DataService data;
  private User.COMMAND command;
  private Random gen;
  private boolean moveLeft,moveRight,moveUp,moveDown,running;
  private double snakeVX,snakeVY;
  ArrayList<SnakeService> snakeParts;

  public Engine(){}

  @Override
  public void bindDataService(DataService service){
    data=service;
  }
  
  @Override
  public void init(){
    engineClock = new Timer();
    command = User.COMMAND.NONE;
    gen = new Random();
    moveLeft = false;
    moveRight = false;
    moveUp = false;
    moveDown = false;
    snakeVX = 0;
    snakeVY = 0;
    
  }

  @Override
  public void start(){
    engineClock.schedule(new TimerTask(){
      public void run() {
    	  
    	  running = data.getRunning();    	  
          if(data.getRound()==3) createWall();
          if (data.getApples().isEmpty()) spawnApple();
          snakeParts = data.getSnakeParts();
          if(data.getRound()>=2) {
            if (data.getPoisonousApples().isEmpty()) spawnPoisonousApple();
//            if (data.getHealthyApples()==null) spawnHealthyApple();
          }
        
        int score=0;        
        updateCommandSnake();
        updatePositionSnakeParts();
        
        ArrayList<AppleService> apples = new ArrayList<AppleService>();
        AppleService healtyapple = data.getHealthyApples();
        ArrayList<AppleService> poisonousApples = new ArrayList<AppleService>();
        ArrayList<ObstacleService> walls = data.getWalls();

//        addApples(apples, poisonousApples, healtyapple, score);
        
        for (AppleService a:data.getApples()) {
            if (collisionSnakeApple(a)){
                data.setSoundEffect(Sound.SOUND.HeroesGotHit);
                addNewPart();
                score+=10;
              } else {
                if (a.getPosition().x>0) apples.add(a);
              }
        }
        
        for (AppleService a:data.getPoisonousApples()) {
            if (collisionSnakeApple(a)){
                data.setSoundEffect(Sound.SOUND.HeroesGotHit);
                removeLastPart();
                score=-15;
              } else {
                if (a.getPosition().x>0) poisonousApples.add(a);
              }
        }
        snakeParts = data.getSnakeParts();

        data.setSoundEffect(Sound.SOUND.None);

        levelUp();

        data.addScore(score);
        data.setApple(apples);
        data.setPoisonousApple(poisonousApples);
//        data.setHealthyApple(healtyapple.getPosition());
        data.setSnakeParts(snakeParts);
        data.setWalls(walls);
        data.setStepNumber(data.getStepNumber()+1);
        
        if(collisionSnakeHeadSnake()) {
        	running = false;
        }
        else if (collisionSnakeWalls()) {
        	running = false;
        }
        
        data.setRunning(running);
        
        if(!running) {
        	stop();
        }
        
      }
    },0,HardCodedParameters.enginePaceMillis);
 
  }
  
  public void addApples(ArrayList<AppleService> apples, ArrayList<AppleService> poisonousApples,AppleService healtyapple,int score) {
	  
	  if(!apples.isEmpty()) {
      for (AppleService a:apples) {
          if (collisionSnakeApple(a)){
              data.setSoundEffect(Sound.SOUND.HeroesGotHit);
              addNewPart();
              score+=10;
            } else {
              if (a.getPosition().x>0) apples.add(a);
            }
      }
	  }
	  if(!poisonousApples.isEmpty()) {
	      for (AppleService a:poisonousApples) {
	          if (collisionSnakeApple(a)){
	              data.setSoundEffect(Sound.SOUND.HeroesGotHit);
	              removeLastPart();
	              score-=5;
	            } 
	          else {
	              if (a.getPosition().x>0) poisonousApples.add(a);
	          }
	     }
	 }
}

  private void levelUp() {
      if (data.getScore()>=50 && data.getRound()==1) {
    	  data.addRound(1);
      }
      if (data.getScore()>=100 && data.getRound()==2) {
    	  data.addRound(1);
      }
}
  
  @Override
  public void stop(){
    engineClock.cancel();
  }

  @Override
  public void setSnakeCommand(User.COMMAND c){
    if (c==User.COMMAND.LEFT && !moveRight) {
    	moveLeft=true;
    	moveRight=false;
    	moveDown=false;
    	moveUp=false;
    }
    else if (c==User.COMMAND.RIGHT && !moveLeft) {
    	moveRight=true;
    	moveLeft=false;
    	moveUp=false;
    	moveDown=false;
    }
    else if (c==User.COMMAND.UP && !moveDown) {
    	moveRight=false;
    	moveLeft=false;
    	moveUp=true;
    	moveDown=false;
    }
    else if (c==User.COMMAND.DOWN && !moveUp) {
    	moveRight=false;
    	moveLeft=false;
    	moveUp=false;
    	moveDown=true;
    }
  }
  
  @Override
  public void releaseSnakeCommand(User.COMMAND c){
    /*if (c==User.COMMAND.LEFT) moveLeft=false;
    if (c==User.COMMAND.RIGHT) moveRight=false;
    if (c==User.COMMAND.UP) moveUp=false;
    if (c==User.COMMAND.DOWN) moveDown=false;*/
  }

  private void updateSpeedSnake(){
    snakeVX*=friction;
    snakeVY*=friction;
  }

  private void updateCommandSnake(){
    if (moveLeft) {snakeVX=-snakeStep;snakeVY=0; }
    if (moveRight) { snakeVX=snakeStep;snakeVY=0; }
    if (moveUp) { snakeVY=-snakeStep;snakeVX=0; }
    if (moveDown) {snakeVY=snakeStep;snakeVX=0; }
  }
  
  private void updatePositionHeroes(){
	SnakeService snakeHead = snakeParts.get(0);
	Position p = new Position(snakeHead.getPosition().x+snakeVX,snakeHead.getPosition().y+snakeVY);
	snakeParts.set(0,new SnakePart(p));
    data.getSnakeParts().set(0, new SnakePart(p));
    if (snakeHead.getPosition().x<0) snakeParts.set(0,new SnakePart(new Position(HardCodedParameters.defaultWidth+(snakeVX), snakeHead.getPosition().y)));
    if (snakeHead.getPosition().y<0) snakeParts.set(0,new SnakePart(new Position(snakeHead.getPosition().x,HardCodedParameters.defaultHeight*.7
    		)));
    if (snakeHead.getPosition().x>HardCodedParameters.defaultWidth-(snakeVX*2)) snakeParts.set(0,new SnakePart(new Position(0,snakeHead.getPosition().y)));
    if (snakeHead.getPosition().y>HardCodedParameters.defaultHeight*.7) snakeParts.set(0,new SnakePart(new Position(snakeHead.getPosition().x,0)));
  
  }

  private void addNewPart() {
	  
	  SnakeService snakeHead = snakeParts.get(0);
	  Position p = new Position(snakeHead.getPosition().x+snakeVX, snakeHead.getPosition().y+snakeVY);
	  snakeParts.add(new SnakePart(p));
  }
  
  private void removeLastPart() {
	  
	  SnakeService lastSnakePart = snakeParts.get((snakeParts.size() -1));
	  snakeParts.remove(lastSnakePart);
  }
  
  private void updatePositionSnakeParts(){
	  for (int i = snakeParts.size() - 1;i>0;i--) {
		SnakeService snakebeforePart = snakeParts.get(i-1);
			Position p = new Position(snakebeforePart.getPosition().x,snakebeforePart.getPosition().y);
			snakeParts.set(i,new SnakePart(p));
	  }
	  
	  updatePositionHeroes(); 
  }
  
  private void createWall() {
	    int x=180;
	    int y=120;	    
	    for (int i=0;i<4;i++) {
	    	y=HardCodedParameters.unitSize;
    		x+=HardCodedParameters.unitSize;
    		data.addWall(new Position(x, y));
    		data.addWall(new Position(x, y+40));
	    }
	  }

  private void spawnApple(){
	  
	    int x=0;
	    int y=0;
	    boolean cont=true;
	    boolean pla = true;
	    while (cont) {
	    	do {
	    		y=(int)(gen.nextInt((int)((HardCodedParameters.defaultHeight*.7)/HardCodedParameters.unitSize))*HardCodedParameters.unitSize);
	    		x=(int)(gen.nextInt((int)((HardCodedParameters.defaultWidth*.9)/HardCodedParameters.unitSize))*HardCodedParameters.unitSize);
	    		pla = collisionSnakePartsApple(x, y);
	    	}
	    	while(pla);
	    	
	   
	      cont=false;

	      for (AppleService p:data.getPoisonousApples()){
		    	  if (p.getPosition().equals(new Position(x,y))) cont=true;
		  }
  			for (AppleService p:data.getApples()){
  				if (p.getPosition().equals(new Position(x,y))) cont=true;
  			}
  			
  			for (ObstacleService w:data.getWalls()){
  				if (w.getPosition().equals(new Position(x,y))) cont=true;
  			}
		    }
	    data.addApple(new Position(x,y));
	  }
  
  private void spawnHealthyApple(){
	  
	    int x=0;
	    int y=0;
	    boolean cont=true;
	    while (cont) {
	    do {
	    	y=(int)(gen.nextInt((int)((HardCodedParameters.defaultHeight*.7)/HardCodedParameters.unitSize))*HardCodedParameters.unitSize);
	    	x=(int)(gen.nextInt((int)((HardCodedParameters.defaultWidth*.9)/HardCodedParameters.unitSize))*HardCodedParameters.unitSize);
	    	
			  if(!data.getPoisonousApples().isEmpty())
			      for (AppleService p:data.getPoisonousApples()){
				        if (!p.getPosition().equals(new Position(x,y))) cont=true;
				      }
			if(!data.getApples().isEmpty())
				for (AppleService p:data.getApples()){
					 if (!p.getPosition().equals(new Position(x,y))) cont=true;
				}
	    }
	    while(collisionSnakePartsApple(x, y));
	      
	      cont=false;     
		    
	    }
	    data.setHealthyApple(new Position(x,y));
	  }
  
  
  private void spawnPoisonousApple(){
	  
	    int x=0;
	    int y=0;
	    boolean cont=true;
	    while (cont) {
	    do {
	    	y=(int)(gen.nextInt((int)((HardCodedParameters.defaultHeight*.7)/HardCodedParameters.unitSize))*HardCodedParameters.unitSize);
	    	x=(int)(gen.nextInt((int)((HardCodedParameters.defaultWidth*.9)/HardCodedParameters.unitSize))*HardCodedParameters.unitSize);
	    }
	    while(collisionSnakePartsApple(x, y));
	      
	      cont=false;
	      
	      if(!data.getPoisonousApples().isEmpty())
		      for (AppleService p:data.getPoisonousApples()){
			        if (p.getPosition().equals(new Position(x,y))) cont=true;
			      }
			  if(!data.getApples().isEmpty())
			      for (AppleService p:data.getApples()){
				        if (p.getPosition().equals(new Position(x,y))) cont=true;
				      }
	    }
	    data.addPoisonousApple(new Position(x,y));
	  }

  private boolean collisionSnakeApple(AppleService se){
	    return (
	      (snakeParts.get(0).getPosition().x-se.getPosition().x)*(snakeParts.get(0).getPosition().x-se.getPosition().x)+
	      (snakeParts.get(0).getPosition().y-se.getPosition().y)*(snakeParts.get(0).getPosition().y-se.getPosition().y) <
	      0.25*(data.getSnakeWidth()+data.getAppleWidth())*(data.getSnakeWidth()+data.getAppleWidth())
	    );
	  }
  
  private boolean collisionSnakeWall(ObstacleService wall){
	    return (
	      (snakeParts.get(0).getPosition().x-wall.getPosition().x)*(snakeParts.get(0).getPosition().x-wall.getPosition().x)+
	      (snakeParts.get(0).getPosition().y-wall.getPosition().y)*(snakeParts.get(0).getPosition().y-wall.getPosition().y) <
	      0.25*(data.getSnakeWidth()+data.getAppleWidth())*(data.getSnakeWidth()+data.getAppleWidth())
	    );
	  }
  
  private boolean collisionSnakePartApple(double x, double y , SnakeService se){
	    return (
	      (x-se.getPosition().x)*(x-se.getPosition().x)+
	      (y-se.getPosition().y)*(y-se.getPosition().y) <
	      0.25*(data.getSnakeWidth()+data.getAppleWidth())*(data.getSnakeWidth()+data.getAppleWidth())
	    );
	  }

  private boolean collisionSnakeWSnake(SnakeService sp){
	    return (
	      (snakeParts.get(0).getPosition().x-sp.getPosition().x)*(snakeParts.get(0).getPosition().x-sp.getPosition().x)+
	      (snakeParts.get(0).getPosition().y-sp.getPosition().y)*(snakeParts.get(0).getPosition().y-sp.getPosition().y) <
	      0.25*(data.getSnakeWidth()+data.getSnakeWidth())*(data.getSnakeWidth()+data.getSnakeWidth())
	    );
	  }
  

  private boolean collisionHeroesApples(){
	    for (AppleService se:data.getApples()) if (collisionSnakeApple(se)) return true; return false;
	  }
  
  private boolean collisionSnakeWalls(){
	    for (ObstacleService wall:data.getWalls()) if (collisionSnakeWall(wall)) return true; return false;
	  }
  
  private boolean collisionSnakeHeadSnake(){
	  for (SnakeService sp:snakeParts) {
		  if(sp.getPosition()==snakeParts.get(0).getPosition()) {continue;}
		  if (collisionSnakeWSnake(sp)) {
			  return true;
		  }
	  }
	  return false;
	  }
  
  private boolean collisionSnakePartsApple(double x, double y){
	  if(snakeParts==null) return false; 
	  else {
	    for (SnakeService sp:snakeParts) if (collisionSnakePartApple(x, y, sp)) return true; return false;
	  }
	 }
}
