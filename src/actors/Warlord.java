package actors;
import framework.GameObject;

/*
 * Creates a stationary Warlord object
 */
public class Warlord extends GameObject {
	private boolean killed = false;
	private boolean won = false;
	private int lives;
	
	final int DEFAULT_LIVES = 1;
	final int SUPER_LIVES = 2;
	
	
	public Warlord() {  // Default constructor
		super(5,5,0,0);
		lives = 1;
	}
	
	public Warlord(int w, int h, int x, int y) { // Constructor for providing position and size
		super(w,h,x,y);
		lives = 1;
	}
	
	//if the warlord is collided, than the number of lives decreases
	public void setKilled(boolean hit) {
		if (hit) {
			lives--;
		}
		if (lives == 0) {
			killed = true;
		}
	}
    /***
     * Determine if this warlord has been killed.
     *
     * @return true if the ball has collided with this warlord, and the warlord has 0 lives. Otherwise, return false.
     */
    public boolean isDead() {
    	return killed;
    }

    /***
     * Determine if this warlord is the winner of the game. Results need only be valid before the start and after the end of a game tick.
     *
     * @return true if all other warlords are dead, or if time has run out and this warlord has the highest number of undestroyed walls. Otherwise, return false.
     */
    public boolean hasWon() {
    	return won;
    }
    
    // For when the player is a winner
    public void setWon(boolean i) {
    	won = i;
    }
    
    //get the amount of lives the warlord has
    public int getLives() {
    	return lives;
    }

    // Set the lives of the warlord to be default
    public void setStandardWarlord() {
		this.lives = DEFAULT_LIVES;
	}
	
    // Set the warlord to have super lives
	public void setSuperWarlord() {
		this.lives = SUPER_LIVES;
	}

}