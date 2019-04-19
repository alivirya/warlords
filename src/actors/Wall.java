package actors;
import framework.GameObject;
/*
 * Creates a stationary wall object
 */
public class Wall extends GameObject {
	private boolean destroyed;
	private int lives;
	
	final int DEFAULT_LIVES = 2;
	final int SUPER_LIVES = 3;
	
	public Wall() { // Default constructor
		super(5,5,0,0);
		lives = 2;

	}
	
	public Wall(int h, int w, int x, int y) { // Constructor for providing position and size
		super(w,h,x,y);
		lives = 2;
	}
	
	public void setHit(boolean hit) {   // Set the destruction to true
		if (hit) { 
			lives--;
		} //if the wall is hit, decrease the life of the wall
		if (lives == 0) {
			destroyed = true; //if the wall has 0 life, then it is destroyed
		}
	}
	
	// Set the lives of the wall to be default
	public void setStandardWall() {
		this.lives = DEFAULT_LIVES;
	}
	
	 // Set the wall to have super lives
	public void setSuperWall() {
		this.lives = SUPER_LIVES;
	}
	
	// Set the number of lives the wall has
	public void setLives(int lives) {
		this.lives = lives;
	}
	
    /***
     * Determine how many lives the wall has left.
     *@return this value
     */
	public int getLives() {
		return this.lives;
	}
    /***
     * Determine if this wall has been destroyed.
     *
     * @return true if the ball has collided with this wall. Otherwise, return false.
     */
    public boolean isDestroyed(){
    	return destroyed;
    }
    

}
