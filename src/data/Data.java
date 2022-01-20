/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: data/Data.java 2015-03-11 buixuan.
 * ******************************************************/
package data;

import tools.HardCodedParameters;
import tools.Position;
import tools.Sound;
import specifications.AppleService;
import specifications.DataService;
import specifications.PhantomService;
import specifications.SnakeService;
import specifications.MotionlessService;
import specifications.ObstacleService;
import data.ia.MoveLeftPhantom;
import data.ia.PoisonousApple;
import data.ia.SnakePart;
import data.ia.Wall;
import data.ia.Apple;
import data.ia.HealthyApple;

import java.util.ArrayList;

public class Data implements DataService{
  //private Heroes hercules;
  private Position heroesPosition;
  private int stepNumber, score,round;
  private ArrayList<PhantomService> phantoms;
  private ArrayList<ObstacleService> walls;
  private ArrayList<AppleService> apple;
  private ArrayList<AppleService> poisonousApple;
  private AppleService healthyApple;
  

  private ArrayList<SnakeService> snakeParts;
  private double heroesWidth,heroesHeight,phantomWidth,phantomHeight,appleWidth,appleHeight;
  private Sound.SOUND sound;
  private boolean running;

  public Data(){}

  @Override
  public void init(){
    //hercules = new Heroes;
    heroesPosition = new Position(HardCodedParameters.heroesStartX,HardCodedParameters.heroesStartY);
    phantoms = new ArrayList<PhantomService>();
    walls = new ArrayList<ObstacleService>();
    snakeParts = new ArrayList<SnakeService>();
    snakeParts.add(new SnakePart(heroesPosition));
    stepNumber = 0;
    score = 0;
    apple = new ArrayList<AppleService>();
    poisonousApple = new ArrayList<AppleService>();
    healthyApple = new HealthyApple(null); 
    running= true;
    round = 1;
    heroesWidth = HardCodedParameters.heroesWidth;
    heroesHeight = HardCodedParameters.heroesHeight;
    phantomWidth = HardCodedParameters.phantomWidth;
    phantomHeight = HardCodedParameters.phantomHeight;
    appleWidth = HardCodedParameters.phantomWidth;
    appleHeight = HardCodedParameters.phantomHeight;
    
    sound = Sound.SOUND.None;
  }

  @Override
  public Position getHeroesPosition(){ return heroesPosition; }
  
  @Override
  public double getHeroesWidth(){ return heroesWidth; }
  
  @Override
  public double getHeroesHeight(){ return heroesHeight; }
  
  @Override
  public double getPhantomWidth(){ return phantomWidth; }
  
  @Override
  public double getPhantomHeight(){ return phantomHeight; }
  
  @Override
  public double getAppleHeight(){ return appleHeight; }
  
  @Override
  public double getAppleWidth(){ return appleWidth; }

  @Override
  public int getStepNumber(){ return stepNumber; }
  
  @Override
  public int getScore(){ return score; }
  
  @Override
  public int getRound(){ return round; }
  
  @Override
  public boolean getRunning(){ return running; }

  @Override
  public ArrayList<PhantomService> getPhantoms(){ return phantoms; }
  
  @Override
  public ArrayList<AppleService> getApples(){ return apple; }
  
  @Override
  public ArrayList<SnakeService> getSnakeParts() { return snakeParts; }
  
  @Override
  public Sound.SOUND getSoundEffect() { return sound; }

  @Override
  public void setHeroesPosition(Position p) { heroesPosition=p; }
  
  @Override
  public void setSnakePartPosition(int index,Position p) { this.snakeParts.set(index, new SnakePart(p)); }
  
  @Override
  public void setStepNumber(int n){ stepNumber=n; }
  
  @Override
  public void setRunning(boolean running){ this.running=running; }
  
  @Override
  public void setPhantoms(ArrayList<PhantomService> phantoms) { this.phantoms=phantoms; }
  
  @Override
  public void setSoundEffect(Sound.SOUND s) { sound=s; }
  
  @Override
  public void setApple(ArrayList<AppleService> apple) { this.apple=apple; }
  
  @Override
  public void setSnakeParts(ArrayList<SnakeService> snakepart) { this.snakeParts = snakepart; }
  
  @Override
  public void addScore(int score){ this.score+=score; }

  @Override
  public void addPhantom(Position p) { phantoms.add(new MoveLeftPhantom(p)); }
  
  @Override
  public void addApple(Position p) {apple.add(new Apple(p)); }
  
  @Override
  public void addSnakePart(Position p) { snakeParts.add(new SnakePart(p)); }

  @Override
  public void addRound(int n){ this.round+=n; }

@Override
public ArrayList<AppleService> getPoisonousApples() {
	// TODO Auto-generated method stub
	return poisonousApple;
}

@Override
public AppleService getHealthyApples() {
	// TODO Auto-generated method stub
	return healthyApple;
}

@Override
public void addPoisonousApple(Position p) {
	// TODO Auto-generated method stub
	this.poisonousApple.add(new PoisonousApple(p));
}

@Override
public void setPoisonousApple(ArrayList<AppleService> apple) {
	this.poisonousApple=apple;	
}

@Override
public void setHealthyApple(Position p) {
	// TODO Auto-generated method stub
	this.healthyApple=new HealthyApple(p);
}

@Override
public ArrayList<ObstacleService> getWalls() {
	// TODO Auto-generated method stub
	return this.walls;
}

@Override
public void addWall(Position p) {
	// TODO Auto-generated method stub
	this.walls.add(new Wall(p));
}

@Override
public void setWalls(ArrayList<ObstacleService> walls) {
	// TODO Auto-generated method stub
	this.walls=walls;
}
  
}
