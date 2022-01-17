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
import specifications.AppleService;
import specifications.DataService;
import specifications.RequireDataService;
import specifications.PhantomService;

import java.util.Timer;
import java.util.TimerTask;
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
  private boolean moveLeft,moveRight,moveUp,moveDown;
  private double heroesVX,heroesVY;

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
        //System.out.println("Game step #"+data.getStepNumber()+": checked.");
        
        if (gen.nextInt(10)<3) spawnPhantom();
        if (data.getApples().isEmpty()) spawnApple();


        updateSpeedHeroes();
        updateCommandHeroes();
        updatePositionHeroes();

        ArrayList<PhantomService> phantoms = new ArrayList<PhantomService>();
        ArrayList<AppleService> apples = new ArrayList<AppleService>();

        int score=0;

        for (AppleService a:data.getApples()) {
            if (collisionSnakeApple(a)){
                data.setSoundEffect(Sound.SOUND.HeroesGotHit);
                //Position newSnakePart = new Position(x, y);
                score+=10;
              } else {
                if (a.getPosition().x>0) apples.add(a);
              }
        }

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

        data.addScore(score);

        data.setPhantoms(phantoms);
        
        data.setApple(apples);


        data.setStepNumber(data.getStepNumber()+1);
      }
    },0,HardCodedParameters.enginePaceMillis);
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
    if (c==User.COMMAND.RIGHT && !moveLeft) {
    	moveRight=true;
    	moveLeft=false;
    	moveUp=false;
    	moveDown=false;
    }
    if (c==User.COMMAND.UP && !moveDown) {
    	moveRight=false;
    	moveLeft=false;
    	moveUp=true;
    	moveDown=false;
    }
    if (c==User.COMMAND.DOWN && !moveUp) {
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
    if (moveLeft) heroesVX-=heroesStep;
    if (moveRight) heroesVX+=heroesStep;
    if (moveUp) heroesVY-=heroesStep;
    if (moveDown) heroesVY+=heroesStep;
  }
  
  private void updatePositionHeroes(){
    data.setHeroesPosition(new Position(data.getHeroesPosition().x+heroesVX,data.getHeroesPosition().y+heroesVY));
    if (data.getHeroesPosition().x<0) data.setHeroesPosition(new Position(HardCodedParameters.defaultWidth*.9, data.getHeroesPosition().y));
    if (data.getHeroesPosition().y<0) data.setHeroesPosition(new Position(data.getHeroesPosition().x,HardCodedParameters.defaultHeight*.7));
    if (data.getHeroesPosition().x>HardCodedParameters.defaultWidth*.9) data.setHeroesPosition(new Position(0,data.getHeroesPosition().y));
    if (data.getHeroesPosition().y>HardCodedParameters.defaultHeight*.7) data.setHeroesPosition(new Position(data.getHeroesPosition().x,0));
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
  
  private void spawnApple(){
	  
	    int x=0;
	    int y=0;
	    boolean cont=true;
	    while (cont) {
	      y=(int)(gen.nextInt((int)(HardCodedParameters.defaultHeight*.6))+HardCodedParameters.defaultHeight*.1);
	      x=(int)(gen.nextInt((int)(HardCodedParameters.defaultWidth*.8))+HardCodedParameters.defaultWidth*0.1);
	      cont=false;
	      for (AppleService p:data.getApples()){
	        if (p.getPosition().equals(new Position(x,y))) cont=true;
	      }
	    }
	    data.addApple(new Position(x,y));
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
	      (data.getHeroesPosition().x-se.getPosition().x)*(data.getHeroesPosition().x-se.getPosition().x)+
	      (data.getHeroesPosition().y-se.getPosition().y)*(data.getHeroesPosition().y-se.getPosition().y) <
	      0.25*(data.getHeroesWidth()+data.getAppleWidth())*(data.getHeroesWidth()+data.getAppleWidth())
	    );
	  }
  
  private boolean collisionHeroesPhantoms(){
    for (PhantomService p:data.getPhantoms()) if (collisionHeroesPhantom(p)) return true; return false;
  }
  
  private boolean collisionHeroesApples(){
	    for (AppleService se:data.getApples()) if (collisionSnakeApple(se)) return true; return false;
	  }
}
