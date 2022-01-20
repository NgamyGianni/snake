/* ******************************************************
 * Project Snake - Ashanth CHANDRAMOHAN
 * ******************************************************/
package specifications;

import tools.User;

public interface EngineService{
  public void init();
  public void start();
  public void stop();
  public void setSnakeCommand(User.COMMAND c);
  public void releaseSnakeCommand(User.COMMAND c);
}
