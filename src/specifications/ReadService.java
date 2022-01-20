/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: specifications/ReadService.java 2015-03-11 buixuan.
 * ******************************************************/
package specifications;

import tools.Position;
import tools.Sound;

import java.util.ArrayList;

public interface ReadService {
  public Position getHeroesPosition();
  public double getHeroesWidth();
  public double getHeroesHeight();
  public double getPhantomWidth();
  public double getPhantomHeight();
  public double getAppleWidth();
  public double getAppleHeight();
  public int getStepNumber();
  public int getScore();
  public boolean getRunning();
  public int getRound();
  public ArrayList<SnakeService> getSnakeParts();
  public ArrayList<ObstacleService> getWalls();
  public ArrayList<PhantomService> getPhantoms();
  public ArrayList<AppleService> getApples();
  public ArrayList<AppleService> getPoisonousApples();
  public AppleService getHealthyApples();
  public Sound.SOUND getSoundEffect();
}
