
package data.ia;

import tools.Position;
import specifications.AppleService;
import specifications.ObstacleService;

public class Wall implements ObstacleService{
  private Position position;

  public Wall(Position p){ position=p; }

  @Override
  public Position getPosition() { return position; }

  @Override
  public void setPosition(Position p) { position=p; }
}
