package actors;
import framework.DynamicGameObject;

/*
 * Creates a dynamic ball object
 */
public class Ball extends DynamicGameObject {
	
	//set the ball variables by calling the parent constructors
    public Ball(){
    	super(5,5,0,0);
    }
}
