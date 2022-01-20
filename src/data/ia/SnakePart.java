/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: data/ia/MoveLeftPhantom.java 2015-03-11 buixuan.
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
