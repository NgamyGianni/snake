/* ******************************************************
 * Project Snake - Ashanth CHANDRAMOHAN
 * ******************************************************/
package specifications;

import tools.Position;
import tools.Sound;

import java.util.ArrayList;

public interface ReadService {
  public Position getSnakeHeadPosition();
  public double getSnakeWidth();
  public double getSnakeHeight();
  public double getAppleWidth();
  public double getAppleHeight();
  public int getStepNumber();
  public int getScore();
  public boolean getRunning();
  public int getRound();
  public ArrayList<SnakeService> getSnakeParts();
  public ArrayList<ObstacleService> getWalls();
  public ArrayList<AppleService> getApples();
  public ArrayList<AppleService> getPoisonousApples();
  public AppleService getHealthyApples();
  public Sound.SOUND getSoundEffect();
}
