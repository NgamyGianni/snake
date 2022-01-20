/* ******************************************************
 * Project Snake - Ashanth CHANDRAMOHAN
 * ******************************************************/
package userInterface;

import tools.HardCodedParameters;
import tools.Position;
import specifications.ViewerService;
import specifications.ReadService;
import specifications.RequireReadService;
import specifications.SnakeService;
import specifications.AppleService;
import specifications.ObstacleService;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;

import data.ia.SnakePart;

public class Viewer implements ViewerService, RequireReadService{
  private static final int spriteSlowDownRate=HardCodedParameters.spriteSlowDownRate;
  private static final double defaultMainWidth=HardCodedParameters.defaultWidth,
                              defaultMainHeight=HardCodedParameters.defaultHeight;
  private ReadService data;
  private ArrayList<Rectangle2D> snakeAvatarViewports;
  private ArrayList<Integer> heroesAvatarXModifiers;
  private ArrayList<Integer> heroesAvatarYModifiers;
  private int snakeAvatarViewportIndex;
  private double xShrink,yShrink,shrink,xModifier,yModifier,heroesScale;

  public Viewer(){}
  
  @Override
  public void bindReadService(ReadService service){
    data=service;
  }

  @Override
  public void init(){
    xShrink=1;
    yShrink=1;
    xModifier=0;
    yModifier=0;
    
  }

  @Override
  public Parent getPanel(){
    shrink=Math.min(xShrink,yShrink);
    xModifier=.01*shrink*defaultMainHeight;
    yModifier=.01*shrink*defaultMainHeight;

    //Yucky hard-conding
    Rectangle map = new Rectangle(-2+shrink*defaultMainWidth,-.2*shrink*defaultMainHeight+shrink*defaultMainHeight);
    map.setFill(Color.WHITE);
    map.setStroke(Color.DIMGRAY);
    map.setStrokeWidth(.01*shrink*defaultMainHeight);
    map.setArcHeight(.04*shrink*defaultMainHeight);
    map.setTranslateX(xModifier);
    map.setTranslateY(yModifier);
    
    Text score = new Text(-0.1*shrink*defaultMainHeight+.5*shrink*defaultMainWidth,
                           -0.05*shrink*defaultMainWidth+shrink*defaultMainHeight,
                           "Score: "+data.getScore());
    score.setFont(new Font(.05*shrink*defaultMainHeight));

    Text level = new Text(-0.1*shrink*defaultMainHeight+.5*shrink*defaultMainWidth,
            -0.1*shrink*defaultMainWidth+shrink*defaultMainHeight,
            "Round: "+data.getRound());
    level.setFont(new Font(.05*shrink*defaultMainHeight));
    
    Text snakePartsCount = new Text(0.2*shrink*defaultMainHeight+.5*shrink*defaultMainWidth,
            -0.08*shrink*defaultMainWidth+shrink*defaultMainHeight,
            "Snake Parts Count: "+data.getSnakeParts().size());
    snakePartsCount.setFont(new Font(.05*shrink*defaultMainHeight));
    
    
    Group panel = new Group();
    panel.getChildren().addAll(map,score,level,snakePartsCount);

    ArrayList<AppleService> apples = data.getApples();
    AppleService apl;
    
    ArrayList<AppleService> poisonousApples = data.getPoisonousApples();
    AppleService poisApl;
    
    AppleService healthyApples = data.getHealthyApples();

    ArrayList<SnakeService> snakeParts = data.getSnakeParts();
    SnakeService sp;
    
    ArrayList<ObstacleService> walls = data.getWalls();
    ObstacleService wall;
    
    for (SnakeService snakeService : snakeParts) {
		sp = snakeService;
		Rectangle snakeAvatar =  new Rectangle(data.getSnakeWidth()*shrink,data.getSnakeHeight()*shrink,Color.DARKGREEN);
	    snakeAvatar.setTranslateX(shrink*sp.getPosition().x+
                shrink*xModifier+
                heroesScale*data.getSnakeWidth()
               );
	    snakeAvatar.setTranslateY(shrink*sp.getPosition().y+
                shrink*yModifier+
                heroesScale*data.getSnakeHeight()
               );
	      panel.getChildren().add(snakeAvatar);

	}
    for (int i=0; i<walls.size();i++){
		wall = walls.get(i);
		Rectangle wallAvatar =  new Rectangle(data.getSnakeWidth()*shrink,data.getSnakeHeight()*shrink,Color.rgb(150, 75, 0));
	    wallAvatar.setTranslateX(shrink*wall.getPosition().x+
                shrink*xModifier+
                heroesScale*data.getSnakeWidth()
               );
	    wallAvatar.setTranslateY(shrink*wall.getPosition().y+
                shrink*yModifier+
                heroesScale*data.getSnakeHeight()
               );
	      panel.getChildren().add(wallAvatar);

	}
    
    for (int i=0; i<apples.size();i++){
        apl=apples.get(i);
        double radius=.5*Math.min(data.getAppleWidth(),data.getAppleHeight());
        Circle appleAvatar = new Circle(radius,Color.rgb(255,0,0));
        appleAvatar.setEffect(new Lighting());
        appleAvatar.setTranslateX(shrink*apl.getPosition().x+shrink*xModifier+radius);
        appleAvatar.setTranslateY(shrink*apl.getPosition().y+shrink*yModifier+radius);
        panel.getChildren().add(appleAvatar);
        
      }

    for (int i=0; i<poisonousApples.size();i++){
        poisApl=poisonousApples.get(i);
        double radius=.5*Math.min(data.getAppleWidth(),data.getAppleHeight());
        Circle appleAvatar = new Circle(radius,Color.rgb(128,0,128));
        appleAvatar.setEffect(new Lighting());
        appleAvatar.setTranslateX(shrink*poisApl.getPosition().x+shrink*xModifier+radius);
        appleAvatar.setTranslateY(shrink*poisApl.getPosition().y+shrink*yModifier+radius);
        panel.getChildren().add(appleAvatar);
        
      }
    
//    	if(healthyApples!=null) {
//        healthyApples=data.getHealthyApples();
//        double radius=.5*Math.min(data.getAppleWidth(),data.getAppleHeight());
//        Circle appleAvatar = new Circle(radius,Color.rgb(0,255,0));
//        appleAvatar.setEffect(new Lighting());
//        appleAvatar.setTranslateX(shrink*healthyApples.getPosition().x+shrink*xModifier+radius);
//        appleAvatar.setTranslateY(shrink*healthyApples.getPosition().y+shrink*yModifier+radius);
//        panel.getChildren().add(appleAvatar);    
//    	}
    
    if(!data.getRunning()) {
    	Text endGame = new Text(defaultMainHeight*.2,defaultMainWidth*.2,
                "GAME OVER !");
        endGame.setFont(new Font(100));
        panel.getChildren().add(endGame);
    }

    return panel;
  }

  @Override
  public void setMainWindowWidth(double width){
    xShrink=width/defaultMainWidth;
  }
  
  @Override
  public void setMainWindowHeight(double height){
    yShrink=height/defaultMainHeight;
  }
}
