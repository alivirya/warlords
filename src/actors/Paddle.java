package actors;
import framework.DynamicGameObject;

/*
 * Creates a dynamic paddle object. There is option for a non default constructor and extra collision logic. 
 */
public class Paddle extends DynamicGameObject  {
	private int direction, hold;
	
	public Paddle() {  // Default constructor
		super(5,5,0,0);
		hold = 0;
	}
	
	public Paddle(int w, int h, int x, int y) { // Constructor for providing position and size
		super(w,h,x,y);
		hold = 0;
	}
	
    /***
     * Set the direction the paddle is moving
     *
     * +1 goes up, and -1 goes down
     */	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
    /***
     * Return the direction the paddle is moving
     *
     */	
	public int getDirection() {
		return direction;
	}
	
    /***
     *Set a variable to hold the direction of the paddle for a certain length
     * 
     */
	public void setHold(int hold) {
		this.hold = hold;
	}
	
    /***
     * Get the length of this hold
     */	
	public int getHold() {
		return hold;
	}


}