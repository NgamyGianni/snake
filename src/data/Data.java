/* ******************************************************
 * Project Snake - Ashanth CHANDRAMOHAN
 * ******************************************************/
package data;

import tools.HardCodedParameters;
import tools.Position;
import tools.Sound;
import specifications.AppleService;
import specifications.DataService;
import specifications.SnakeService;
import specifications.ObstacleService;
import data.ia.PoisonousApple;
import data.ia.SnakePart;
import data.ia.Wall;
import data.ia.Apple;
import data.ia.HealthyApple;

import java.util.ArrayList;

public class Data implements DataService{
  private Position snakeHeadPosition;
  private int stepNumber, score,round;
  private ArrayList<ObstacleService> walls;
  private ArrayList<AppleService> apple;
  private ArrayList<AppleService> poisonousApple;
  private AppleService healthyApple;
  

  private ArrayList<SnakeService> snakeParts;
  private double snakeWidth,snakeHeight,appleWidth,appleHeight;
  private Sound.SOUND sound;
  private boolean running;

  public Data(){}

  @Override
  public void init(){
    snakeHeadPosition = new Position(HardCodedParameters.snakeStartX,HardCodedParameters.snakeStartY);
    walls = new ArrayList<ObstacleService>();
    snakeParts = new ArrayList<SnakeService>();
    snakeParts.add(new SnakePart(snakeHeadPosition));
    stepNumber = 0;
    score = 0;
    apple = new ArrayList<AppleService>();
    poisonousApple = new ArrayList<AppleService>();
    healthyApple = new HealthyApple(null); 
    running= true;
    round = 1;
    snakeWidth = HardCodedParameters.snakeWidth;
    snakeHeight = HardCodedParameters.snakeHeight;
    appleWidth = HardCodedParameters.appleWidth;
    appleHeight = HardCodedParameters.appleHeight;
    
    sound = Sound.SOUND.None;
  }

  @Override
  public Position getSnakeHeadPosition(){ return snakeHeadPosition; }
  
  @Override
  public double getSnakeWidth(){ return snakeWidth; }
  
  @Override
  public double getSnakeHeight(){ return snakeHeight; }
  
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
  public ArrayList<AppleService> getApples(){ return apple; }
  
  @Override
  public ArrayList<SnakeService> getSnakeParts() { return snakeParts; }
  
  @Override
  public Sound.SOUND getSoundEffect() { return sound; }

  @Override
  public void setSnakeHeadPosition(Position p) { snakeHeadPosition=p; }
  
  @Override
  public void setSnakePartPosition(int index,Position p) { this.snakeParts.set(index, new SnakePart(p)); }
  
  @Override
  public void setStepNumber(int n){ stepNumber=n; }
  
  @Override
  public void setRunning(boolean running){ this.running=running; }
  
  @Override
  public void setSoundEffect(Sound.SOUND s) { sound=s; }
  
  @Override
  public void setApple(ArrayList<AppleService> apple) { this.apple=apple; }
  
  @Override
  public void setSnakeParts(ArrayList<SnakeService> snakepart) { this.snakeParts = snakepart; }
  
  @Override
  public void addScore(int score){ this.score+=score; }
  
  @Override
  public void addApple(Position p) {apple.add(new Apple(p)); }
  
  @Override
  public void addSnakePart(Position p) { snakeParts.add(new SnakePart(p)); }

  @Override
  public void addRound(int n){ this.round+=n; }

@Override
public ArrayList<AppleService> getPoisonousApples() { return poisonousApple; }

@Override
public AppleService getHealthyApples() { return healthyApple; }

@Override
public void addPoisonousApple(Position p) { this.poisonousApple.add(new PoisonousApple(p)); }

@Override
public void setPoisonousApple(ArrayList<AppleService> apple) { this.poisonousApple=apple; }

@Override
public void setHealthyApple(Position p) { this.healthyApple=new HealthyApple(p); }

@Override
public ArrayList<ObstacleService> getWalls() { return this.walls; }

@Override
public void addWall(Position p) { this.walls.add(new Wall(p)); }

@Override
public void setWalls(ArrayList<ObstacleService> walls) { this.walls=walls; }

}
