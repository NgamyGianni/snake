/* ******************************************************
 * Project Snake - Ashanth CHANDRAMOHAN
 * ******************************************************/
package data.ia;

import tools.Position;
import specifications.AppleService;

public class PoisonousApple implements AppleService{
  private Position position;

  public PoisonousApple(Position p){ position=p; }

  @Override
  public Position getPosition() { return position; }

  @Override
  public void setPosition(Position p) { position=p; }
}
