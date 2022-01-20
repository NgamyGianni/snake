/* ******************************************************
 * Project Snake - Ashanth CHANDRAMOHAN
 * ******************************************************/
package specifications;

import tools.Position;
import tools.Sound;

import java.util.ArrayList;

public interface WriteService {
  public void setSnakeHeadPosition(Position p);
  public void setSnakePartPosition(int index,Position p);
  public void setStepNumber(int n);
  public void addRound(int n);
  public void setRunning(boolean running);
  public void addSnakePart(Position p);
  public void addWall(Position p);
  public void setWalls(ArrayList<ObstacleService> walls);
  public void addApple(Position p);
  public void addPoisonousApple(Position p);
  public void setPoisonousApple(ArrayList<AppleService> apple);
  public void setHealthyApple(Position p);
  public void setSnakeParts(ArrayList<SnakeService> snakepart);
  public void setApple(ArrayList<AppleService> apple);
  public void setSoundEffect(Sound.SOUND s);
  public void addScore(int score);
}
