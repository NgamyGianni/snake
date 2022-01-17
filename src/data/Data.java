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
import specifications.MotionlessService;

import data.ia.MoveLeftPhantom;
import data.ia.Apple;

import java.util.ArrayList;

public class Data implements DataService{
  //private Heroes hercules;
  private Position heroesPosition;
  private int stepNumber, score;
  private ArrayList<PhantomService> phantoms;
  private ArrayList<AppleService> apple;
  private double heroesWidth,heroesHeight,phantomWidth,phantomHeight,appleWidth,appleHeight;
  private Sound.SOUND sound;

  public Data(){}

  @Override
  public void init(){
    //hercules = new Heroes;
    heroesPosition = new Position(HardCodedParameters.heroesStartX,HardCodedParameters.heroesStartY);
    phantoms = new ArrayList<PhantomService>();
    		
    stepNumber = 0;
    score = 0;
    apple = new ArrayList<AppleService>();

    heroesWidth = HardCodedParameters.heroesWidth;
    heroesHeight = HardCodedParameters.heroesHeight;
    phantomWidth = HardCodedParameters.phantomWidth;
    phantomHeight = HardCodedParameters.phantomHeight;
    appleWidth = HardCodedParameters.phantomWidth*1.2;
    appleHeight = HardCodedParameters.phantomHeight*1.2;
    
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
  public ArrayList<PhantomService> getPhantoms(){ return phantoms; }
  
  @Override
  public ArrayList<AppleService> getApples(){ return apple; }
  
  @Override
  public Sound.SOUND getSoundEffect() { return sound; }

  @Override
  public void setHeroesPosition(Position p) { heroesPosition=p; }
  
  @Override
  public void setStepNumber(int n){ stepNumber=n; }
  
  @Override
  public void addScore(int score){ this.score+=score; }

  @Override
  public void addPhantom(Position p) { phantoms.add(new MoveLeftPhantom(p)); }
  
  @Override
  public void setPhantoms(ArrayList<PhantomService> phantoms) { this.phantoms=phantoms; }
  
  @Override
  public void setSoundEffect(Sound.SOUND s) { sound=s; }
  
  @Override
  public void addApple(Position p) {apple.add(new Apple(p)); }

  @Override
  public void setApple(ArrayList<AppleService> apple) { this.apple=apple; }
}
