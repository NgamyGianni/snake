/* ******************************************************
 * Project Snake - Ashanth CHANDRAMOHAN
 * ******************************************************/
package tools;

public class Position {
  public double x,y;
  public Position(double x, double y){
    this.x=x;
    this.y=y;
  }
@Override
public String toString() {
	return "Position [x=" + x + ", y=" + y + "]";
  }
}
