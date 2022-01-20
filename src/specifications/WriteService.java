/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: specifications/WriteService.java 2015-03-11 buixuan.
 * ******************************************************/
package specifications;

import tools.Position;
import tools.Sound;

import java.util.ArrayList;

public interface WriteService {
  public void setHeroesPosition(Position p);
  public void setSnakePartPosition(int index,Position p);
  public void setStepNumber(int n);
  public void addRound(int n);
  public void setRunning(boolean running);
  public void addSnakePart(Position p);
  public void addWall(Position p);
  public void addPhantom(Position p);
  public void setPhantoms(ArrayList<PhantomService> phantoms);
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
