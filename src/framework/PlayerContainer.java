package framework;
import actors.Paddle;
import actors.Wall;
import actors.Warlord;

/*
 * This acts as a container for the different game objects associated with a player
 */
public class PlayerContainer {

	//set up variables for the player container
	public Paddle paddle = new Paddle();
	public Warlord warlord = new Warlord();
	public Wall[] walls = new Wall[16];
	public nation playerNation;
	public playerType playerType;
	public pos position;
	final private int DEFAULT_PADDLE_VELOCITY = 10;
	public int numWalls;
	
	
	public PlayerContainer(playerType type, pos position, nation playersNation) {
		//set up the variables through the constructor
		this.playerType = type;
		
		for (int i = 0; i < 16; i++) {
			walls[i] = new Wall();	
		}
		
		setNation(playersNation);
		this.position= position;
		this.paddle.setXVelocity(DEFAULT_PADDLE_VELOCITY);		// Set default variables
		this.paddle.setYVelocity(DEFAULT_PADDLE_VELOCITY);
		this.numWalls = 0;
	
	}
	
	
	//set the different players to be enums
	public enum playerType {
		PLAYER_HUMAN,
		PLAYER_AI,
		PLAYER_NONE;
	}
	
	//set the different nations to be enums
	public enum nation {
		NATION_BRITAIN,
		NATION_SPAIN,
		NATION_FRANCE,
		NATION_PIRATE,
	}
	
	//set the different positions to be enums
	public enum pos {
		TOP_RIGHT,
		TOP_LEFT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT,
	}
	
	//set the nation of the player
	public void setNation(nation playerNation) {
		this.playerNation = playerNation;
		
		paddle.setXVelocity(DEFAULT_PADDLE_VELOCITY);	// Speed difference
		paddle.setYVelocity(DEFAULT_PADDLE_VELOCITY);
		resetWallLives();
		warlord.setStandardWarlord();
		
		switch (this.playerNation) {
			case NATION_PIRATE:
				paddle.setXVelocity(DEFAULT_PADDLE_VELOCITY + 3);	// Speed difference
				paddle.setYVelocity(DEFAULT_PADDLE_VELOCITY + 3);
			break;
			case NATION_BRITAIN:
				warlord.setSuperWarlord();
			break;
			case NATION_SPAIN:
				setSuperWalls();
		default:
			break;
		}
	}
	
	// Set every 4th wall to a super wall
	public void setSuperWalls() {
		for (int i = 0; i < 16; i = i + 4) {
			walls[i].setSuperWall();
		}
	}
	
	// set every wall to have standard lives
	public void resetWallLives() {
		for (int i = 0; i < 16; i++) {
			walls[i].setStandardWall();
		}
	}
	
	//retrieve the nation of the player
	public nation getNation() {
		return playerNation;
	}

	//set the type of the player
    public void setPlayerType(playerType player) {
    	this.playerType = player; 
    }
    
    //retrieve the type of the player
    public playerType getPlayerType() {
    	return playerType;
    }
    
    //set the position for the player
    public void setPosition(pos position) {
    	this.position = position;
    }
    
    //retrieve the position of the player
    public pos getPosition() {
    	return position;
    }
    
    //set the number of walls the player has
    public void setnumWalls(int numWalls) { 
    	this.numWalls = numWalls;
    }
    
    //retrieve the number of walls the player has
    public int getWalls() { 
    	return numWalls;
    }
	
}
