package framework;

/**
 * This provides movement properties to moving objects, e.g. the ball which inherits this
 */
abstract public class DynamicGameObject extends GameObject {
	//set the velocity variables
    public int velocityX;
    public int velocityY;
    private int movementBufferX = 0;
    private int movementBufferY = 0;

    //call the parent constructor for the basic game object parameters while
    //setting the velocity to 0
	public DynamicGameObject(int w, int h, int x, int y) {
		super(w,h,x,y);
		velocityX = 0;
		velocityY = 0;
	}
	
    /***
     *  Set the horizontal velocity of the ball to the given value.
     * @param dX
     */
	public void setXVelocity(int dX) {
    	velocityX = dX;
    }

    /***
     *  Set the vertical velocity of the ball to the given value.
     * @param dY
     */
	public void setYVelocity(int dY) {
    	velocityY = dY;
    }

    /***
     * @return the horizontal velocity of the ball.
     */
	public int getXVelocity() {
    	return velocityX;
    }

    /***
     * @return the vertical velocity of the ball.
     */
	public int getYVelocity() {
    	return velocityY;
    }
	
	// Buffers store the movement to be applied next tick
	
	//set a X buffer so the paddle moves with the tick
	public void setNextXPos (int x) {
		movementBufferX = x;
	}
	
	//set a Y buffer so the paddle moves with the tick
	public void setNextYPos (int y) {
		movementBufferY = y;
	}
	
	//apply the buffers
	public void applyBuffers() {
		setXPos(movementBufferX);
		setYPos(movementBufferY);
	}
}