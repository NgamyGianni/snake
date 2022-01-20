/* ******************************************************
 * Project Snake - Ashanth CHANDRAMOHAN
 * ******************************************************/
package data.ia;

import tools.Position;
import specifications.AppleService;
import specifications.SnakeService;

public class SnakePart implements SnakeService{
  private Position position;

  public SnakePart(Position p){ position=p; }

  @Override
  public Position getPosition() { return position; }

  @Override
  public void setPosition(Position p) { position=p; }
}
