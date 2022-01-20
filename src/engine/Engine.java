/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: engine/Engine.java 2015-03-11 buixuan.
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
import specifications.PhantomService;

import java.util.Timer;
import java.util.TimerTask;

import data.ia.HealthyApple;
import data.ia.SnakePart;

import java.util.Random;
import java.util.ArrayList;

public class Engine implements EngineService, RequireDataService{
  private static final double friction=HardCodedParameters.friction,
                              heroesStep=HardCodedParameters.heroesStep,
                              phantomStep=HardCodedParameters.phantomStep;
  private Timer engineClock;
  private DataService data;
  private User.COMMAND command;
  private Random gen;
  private boolean moveLeft,moveRight,moveUp,moveDown,running;
  private double heroesVX,heroesVY;
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
    heroesVX = 0;
    heroesVY = 0;
    
  }

  @Override
  public void start(){
    engineClock.schedule(new TimerTask(){
      public void run() {
    	  
    	  running = data.getRunning();
//        if (gen.nextInt(10)<3) spawnPhantom();
    	  
          if(data.getRound()==3) {
              createWall();
           }
        if (data.getApples().isEmpty()) spawnApple();
        snakeParts = data.getSnakeParts();

        if(data.getRound()>=2) {
            if (data.getPoisonousApples().isEmpty()) spawnPoisonousApple();
//            if (data.getHealthyApples()==null) spawnHealthyApple();
        }
        
        
        int score=0;
//        updateSpeedHeroes();
        
        updateCommandHeroes();
        updatePositionSnakeParts();
        ArrayList<PhantomService> phantoms = new ArrayList<PhantomService>();
        ArrayList<AppleService> apples = new ArrayList<AppleService>();
        AppleService healtyapple = data.getHealthyApples();
        ArrayList<AppleService> poisonousApples = new ArrayList<AppleService>();
        ArrayList<ObstacleService> walls = data.getWalls();

//        addApples(apples, poisonousApples, healtyapple, score);
        
        for (AppleService a:data.getApples()) {
            if (collisionSnakeApple(a)){
                data.setSoundEffect(Sound.SOUND.HeroesGotHit);
                addNewPart();
                //Position newSnakePart = new Position(x, y);
                score+=10;
              } else {
                if (a.getPosition().x>0) apples.add(a);
              }
        }
        
        for (AppleService a:data.getPoisonousApples()) {
            if (collisionSnakeApple(a)){
                data.setSoundEffect(Sound.SOUND.HeroesGotHit);
                removeLastPart();
                //Position newSnakePart = new Position(x, y);
                score=-15;
              } else {
                if (a.getPosition().x>0) poisonousApples.add(a);
              }
        }
        snakeParts = data.getSnakeParts();


        data.setSoundEffect(Sound.SOUND.None);

        for (PhantomService p:data.getPhantoms()){
          if (p.getAction()==PhantomService.MOVE.LEFT) moveLeft(p);
          if (p.getAction()==PhantomService.MOVE.RIGHT) moveRight(p);
          if (p.getAction()==PhantomService.MOVE.UP) moveUp(p);
          if (p.getAction()==PhantomService.MOVE.DOWN) moveDown(p);

          if (collisionHeroesPhantom(p)){
            data.setSoundEffect(Sound.SOUND.HeroesGotHit);
            score++;
          } else {
            if (p.getPosition().x>0) phantoms.add(p);
          }
        }
        	levelUp();


        data.addScore(score);

        data.setPhantoms(phantoms);
        
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
              //Position newSnakePart = new Position(x, y);
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
              //Position newSnakePart = new Position(x, y);
              score-=5;
            } else {
              if (a.getPosition().x>0) poisonousApples.add(a);
            }
      }
	  }
	
}

  private void levelUp() {
      if (data.getScore()>=50 && data.getRound()==1) {
    	  data.addRound(1);
      }
      if (data.getScore()>=60 && data.getRound()==2) {
    	  data.addRound(1);
      }
}
  
  @Override
  public void stop(){
    engineClock.cancel();
  }

  @Override
  public void setHeroesCommand(User.COMMAND c){
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
  public void releaseHeroesCommand(User.COMMAND c){
    /*if (c==User.COMMAND.LEFT) moveLeft=false;
    if (c==User.COMMAND.RIGHT) moveRight=false;
    if (c==User.COMMAND.UP) moveUp=false;
    if (c==User.COMMAND.DOWN) moveDown=false;*/
  }

  private void updateSpeedHeroes(){
    heroesVX*=friction;
    heroesVY*=friction;
  }

  private void updateCommandHeroes(){
    if (moveLeft) {heroesVX=-heroesStep;heroesVY=0; }
    if (moveRight) { heroesVX=heroesStep;heroesVY=0; }
    if (moveUp) { heroesVY=-heroesStep;heroesVX=0; }
    if (moveDown) {heroesVY=heroesStep;heroesVX=0; }
  }
  
  private void updatePositionHeroes(){
	SnakeService snakeHead = snakeParts.get(0);
	Position p = new Position(snakeHead.getPosition().x+heroesVX,snakeHead.getPosition().y+heroesVY);
	snakeParts.set(0,new SnakePart(p));
    data.getSnakeParts().set(0, new SnakePart(p));
    if (snakeHead.getPosition().x<0) snakeParts.set(0,new SnakePart(new Position(HardCodedParameters.defaultWidth+(heroesVX), snakeHead.getPosition().y)));
    if (snakeHead.getPosition().y<0) snakeParts.set(0,new SnakePart(new Position(snakeHead.getPosition().x,HardCodedParameters.defaultHeight*.7
    		)));
    if (snakeHead.getPosition().x>HardCodedParameters.defaultWidth-(heroesVX*2)) snakeParts.set(0,new SnakePart(new Position(0,snakeHead.getPosition().y)));
    if (snakeHead.getPosition().y>HardCodedParameters.defaultHeight*.7) snakeParts.set(0,new SnakePart(new Position(snakeHead.getPosition().x,0)));
  
  }

  private void addNewPart() {
	  
	  SnakeService lastSnakePart = snakeParts.get((snakeParts.size() -1));
	  SnakeService snakeHead = snakeParts.get(0);
	  Position p = new Position(snakeHead.getPosition().x+heroesVX, snakeHead.getPosition().y+heroesVY);
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
  
  
  private void spawnPhantom(){
    int x=(int)(HardCodedParameters.defaultWidth*.9);
    int y=0;
    boolean cont=true;
    while (cont) {
      y=(int)(gen.nextInt((int)(HardCodedParameters.defaultHeight*.6))+HardCodedParameters.defaultHeight*.1);
      cont=false;
      for (PhantomService p:data.getPhantoms()){
        if (p.getPosition().equals(new Position(x,y))) cont=true;
      }
    }
    data.addPhantom(new Position(x,y));
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

  private void moveLeft(PhantomService p){	
	  p.setPosition(new Position(p.getPosition().x-phantomStep,p.getPosition().y));
  }

  private void moveRight(PhantomService p){
	  p.setPosition(new Position(p.getPosition().x+phantomStep,p.getPosition().y));
  }

  private void moveUp(PhantomService p){
	  p.setPosition(new Position(p.getPosition().x,p.getPosition().y-phantomStep));
  }

  private void moveDown(PhantomService p){
	  p.setPosition(new Position(p.getPosition().x,p.getPosition().y+phantomStep));
  }

  private boolean collisionHeroesPhantom(PhantomService p){
    return (
      (data.getHeroesPosition().x-p.getPosition().x)*(data.getHeroesPosition().x-p.getPosition().x)+
      (data.getHeroesPosition().y-p.getPosition().y)*(data.getHeroesPosition().y-p.getPosition().y) <
      0.25*(data.getHeroesWidth()+data.getPhantomWidth())*(data.getHeroesWidth()+data.getPhantomWidth())
    );
  }
  
  private boolean collisionSnakeApple(AppleService se){
	    return (
	      (snakeParts.get(0).getPosition().x-se.getPosition().x)*(snakeParts.get(0).getPosition().x-se.getPosition().x)+
	      (snakeParts.get(0).getPosition().y-se.getPosition().y)*(snakeParts.get(0).getPosition().y-se.getPosition().y) <
	      0.25*(data.getHeroesWidth()+data.getAppleWidth())*(data.getHeroesWidth()+data.getAppleWidth())
	    );
	  }
  
  private boolean collisionSnakeWall(ObstacleService wall){
	    return (
	      (snakeParts.get(0).getPosition().x-wall.getPosition().x)*(snakeParts.get(0).getPosition().x-wall.getPosition().x)+
	      (snakeParts.get(0).getPosition().y-wall.getPosition().y)*(snakeParts.get(0).getPosition().y-wall.getPosition().y) <
	      0.25*(data.getHeroesWidth()+data.getAppleWidth())*(data.getHeroesWidth()+data.getAppleWidth())
	    );
	  }
  
  private boolean collisionSnakePartApple(double x, double y , SnakeService se){
	    return (
	      (x-se.getPosition().x)*(x-se.getPosition().x)+
	      (y-se.getPosition().y)*(y-se.getPosition().y) <
	      0.25*(data.getHeroesWidth()+data.getAppleWidth())*(data.getHeroesWidth()+data.getAppleWidth())
	    );
	  }

  private boolean collisionSnakeWSnake(SnakeService sp){
	    return (
	      (snakeParts.get(0).getPosition().x-sp.getPosition().x)*(snakeParts.get(0).getPosition().x-sp.getPosition().x)+
	      (snakeParts.get(0).getPosition().y-sp.getPosition().y)*(snakeParts.get(0).getPosition().y-sp.getPosition().y) <
	      0.25*(data.getHeroesWidth()+data.getHeroesWidth())*(data.getHeroesWidth()+data.getHeroesWidth())
	    );
	  }
  
  
  private boolean collisionHeroesPhantoms(){
    for (PhantomService p:data.getPhantoms()) if (collisionHeroesPhantom(p)) return true; return false;
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
