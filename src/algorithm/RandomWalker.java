/* ******************************************************
 * Project Snake - Ashanth CHANDRAMOHAN
 * ******************************************************/
package algorithm;

import tools.User;

import specifications.AlgorithmService;
import specifications.EngineService;
import specifications.RequireEngineService;

import java.util.Random;

public class RandomWalker implements AlgorithmService, RequireEngineService{
  private EngineService engine;
  private Random gen;

  public RandomWalker(){}

  @Override
  public void bindEngineService(EngineService service){
    engine = service;
  }

  @Override
  public void init(){
    gen = new Random();
  }

  @Override
  public void activation(){
    engine.setSnakeCommand(User.COMMAND.NONE);
  }
  
  @Override
  public void stepAction(){
    switch (gen.nextInt(4)){
      case 0:
        engine.setSnakeCommand(User.COMMAND.LEFT);
        break;
      case 1:
        engine.setSnakeCommand(User.COMMAND.RIGHT);
        break;
      case 2:
        engine.setSnakeCommand(User.COMMAND.UP);
        break;
      default:
        engine.setSnakeCommand(User.COMMAND.DOWN);
        break;
    }
  }
}
