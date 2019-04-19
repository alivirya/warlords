package framework;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import actors.Ball;
import actors.Paddle;
import actors.Wall;
import actors.Warlord;
import framework.PlayerContainer.nation;
import framework.PlayerContainer.playerType;
import framework.PlayerContainer.pos;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class Game {
	//set the dimensions of the game 
	private int displayWidth = 1024;
	private int displayHeight = 768;
	
	private int timeRemaining = 0;
	private boolean gameFinished = false;
	private boolean ghosting = false;
	private int winner = 0;
	
	private boolean lostStoryMode = false;
	private boolean storyMode = false;
	
	final private int PADDLE_WIDTH = 60;
	final private int PADDLE_HEIGHT = 58;
	
	final private int BALL_SIZE = 32;

	final private int WARLORD_WIDTH = 199;
	final private int WARLORD_HEIGHT = 140;
	
	final private int WALL_WIDTH = 28;
	final private int WALL_HEIGHT = 26;
	
	final private int CORNER_POS_X = WARLORD_WIDTH + (2 * PADDLE_WIDTH - 15);
	final private int CORNER_POS_Y = WARLORD_HEIGHT + (2 * PADDLE_HEIGHT - 5);

	
	//declare the basic entities for prototyping
	public Ball ball;
	public PlayerContainer player1, player2, player3, player4;

	//declare the audio sounds
	AudioClip hitSound = new AudioClip(new File("resources/sound/hit.wav").toURI().toString()); 
	AudioClip warlordHitSound = new AudioClip(new File("resources/sound/warlord_hit.wav").toURI().toString()); 
	AudioClip deflectSound = new AudioClip(new File("resources/sound/fire.wav").toURI().toString());
	
	//declare the lists where the variables are held in 
	public List<Ball> ballList = new ArrayList<Ball>();
	public List<Paddle> paddleList = new ArrayList<Paddle>();
	public List<Wall> wallList = new ArrayList<Wall>();
	public List<Warlord> warlordList = new ArrayList<Warlord>();
	public List<PlayerContainer> playerList = new ArrayList<PlayerContainer>();
	public List<Integer> scoreList = new ArrayList<>();
	public List<Integer> playerIdentifier = new ArrayList<>();
	List<Integer> randomNum = new ArrayList<>();

	//set up a constructor for tests
	public Game(Ball ball2, PlayerContainer player1, PlayerContainer player2, PlayerContainer player3) {    
		timeRemaining = 2 * 60 * 60;
		//add all the variables to the lists that they belong in
		ballList.add(ball2);
		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		paddleList.add(player1.paddle);
		paddleList.add(player2.paddle);
		paddleList.add(player3.paddle);
		warlordList.add(player1.warlord);
		warlordList.add(player2.warlord);
		warlordList.add(player3.warlord);

		for (int i = 0; i < 16; i++) {
			wallList.add(player1.walls[i]);
			wallList.add(player2.walls[i]);
			wallList.add(player3.walls[i]);

		}
		
		//set up the score list
		for (int i = 0; i < playerList.size(); i++) {
			scoreList.add(0);
			playerIdentifier.add(i);
			randomNum.add(1);
		}
	}
	
	public Game(int ballSpeed, boolean ghosting, boolean mode, int humanPlayers, int AIPlayers) {
		this.ghosting = ghosting;
		timeRemaining = 2 * 60 * 60;
		ball = new Ball();
		ballList.add(ball);

		
		//set ball height and width variables
		for (Ball temp : ballList) {
			temp.setHeight(32);
			temp.setWidth(32); 
		}
		//set up the players to be all PLAYER_NONE in default, and create the players depending on how many there are
		if (humanPlayers+AIPlayers > 0) {
			player1 = new PlayerContainer(playerType.PLAYER_NONE, pos.TOP_LEFT, nation.NATION_PIRATE);
			playerList.add(player1);
			if (humanPlayers+AIPlayers > 1) {
				player2 = new PlayerContainer(playerType.PLAYER_NONE, pos.BOTTOM_RIGHT, nation.NATION_FRANCE);
				playerList.add(player2);
				if (humanPlayers+AIPlayers >2) {
					player3 = new PlayerContainer(playerType.PLAYER_NONE, pos.TOP_RIGHT, nation.NATION_BRITAIN);
					playerList.add(player3);
					if (humanPlayers+AIPlayers>3) {
						player4 = new PlayerContainer(playerType.PLAYER_NONE, pos.BOTTOM_LEFT, nation.NATION_SPAIN);
						playerList.add(player4);
					}
				}
			} 
		} 
		
		//set up the human and AI players depending on the number of human and AI players		
		if (humanPlayers > 0) {
			player1.setPlayerType(playerType.PLAYER_HUMAN);
			if (AIPlayers > 0) {
				player2.setPlayerType(playerType.PLAYER_AI);
				if (AIPlayers > 1) {
					player3.setPlayerType(playerType.PLAYER_AI);
					if (AIPlayers > 2) {
						player4.setPlayerType(playerType.PLAYER_AI);
					}
				}
			}
			if (humanPlayers > 1) {
				player2.setPlayerType(playerType.PLAYER_HUMAN);
				if (AIPlayers > 0) {
					player3.setPlayerType(playerType.PLAYER_AI);
					if (AIPlayers > 1) {
						player4.setPlayerType(playerType.PLAYER_AI);
					}
				}
				if (humanPlayers > 2) {
					player3.setPlayerType(playerType.PLAYER_HUMAN);
					if (AIPlayers == 1) { 
						player4.setPlayerType(playerType.PLAYER_AI);
					}
					if (humanPlayers == 4) {
						player4.setPlayerType(playerType.PLAYER_HUMAN);
					}
				}
			}
		} 
		if (humanPlayers == 0) {
			if (AIPlayers > 0) {
				player1.setPlayerType(playerType.PLAYER_AI);
				if (AIPlayers > 1) {
					player2.setPlayerType(playerType.PLAYER_AI);
					if (AIPlayers > 2) {
						player3.setPlayerType(playerType.PLAYER_AI);
						if (AIPlayers > 3) {
							player4.setPlayerType(playerType.PLAYER_AI);
						}
					}
				}
			}
		}
		
		
		//set up the paddles, warlords and walls
		for (PlayerContainer temp : playerList) {
			paddleList.add(temp.paddle);
			warlordList.add(temp.warlord);
			for (int i = 0; i < 16; i++)
			{
				wallList.add(temp.walls[i]);
			}
		}
		//set up the ball velocity
		for (Ball temp : ballList) {
		temp.setXVelocity(ballSpeed);
		temp.setYVelocity(ballSpeed);
		}
		
		//set up the scorelist and player identifier list, and random number generator
		for (int i = 0; i < playerList.size(); i++) {
			scoreList.add(0);
			playerIdentifier.add(i);
			randomNum.add(1);
		}
	}

	//reset the ball position, and set a random velocity
	public void resetBall () {	 
		//set the ball to be in the middle of the screen
		for (Ball temp : ballList) {
			temp.setXPos(displayWidth / 2 - (BALL_SIZE / 2));
			temp.setYPos(displayHeight / 2 - (BALL_SIZE / 2));
			temp.setXVelocity(temp.getXVelocity() * (Math.random() < 0.5 ? -1 : 1));
			temp.setYVelocity(temp.getYVelocity() * (Math.random() < 0.5 ? -1 : 1)); //Start ball moving in random direction!
		}
	}
	
	//reset all of the screen variables 
	public void resetVariables() {
		//set the game to be not finished 
		gameFinished = false;
		
		//set up the players in the playerList depending on how many players exist in the game
		for (PlayerContainer temp : playerList) {
			if (temp.getPlayerType() != playerType.PLAYER_NONE) {
				
				//set the dimensions of our objects
				for (int i = 0; i < 16; i++)
				{
					temp.walls[i].setWidth(WALL_WIDTH);
					temp.walls[i].setHeight(WALL_HEIGHT);
				}
				
				temp.paddle.setWidth(PADDLE_WIDTH);
				temp.paddle.setHeight(PADDLE_HEIGHT);
				temp.warlord.setWidth(WARLORD_WIDTH);
				temp.warlord.setHeight(WARLORD_HEIGHT);
				
				//set up the position of the variables depending on where they are in the screen
				switch(temp.getPosition()) {
					case TOP_LEFT: 
						temp.paddle.setXPos(CORNER_POS_X);			// paddle positions
						temp.paddle.setYPos(CORNER_POS_Y);
						temp.warlord.setXPos(0);
						temp.warlord.setYPos(0);						// warlord positions
						for (int i = 0; i < 8; i++) {
							temp.walls[i].setXPos(temp.warlord.getWidth() + 23);
							temp.walls[i].setYPos(0 + (WALL_HEIGHT * i));
						}																// Set the wall positions
						for (int i = 0; i <8; i++) {
							temp.walls[i+8].setXPos(0 + (WALL_WIDTH * i));
							temp.walls[i+8].setYPos(temp.warlord.getHeight() + 42);
						}
						break;
					case BOTTOM_LEFT:
						temp.paddle.setXPos(CORNER_POS_X);
						temp.paddle.setYPos(displayHeight - CORNER_POS_Y - PADDLE_HEIGHT);   	// paddle positions
						temp.warlord.setXPos(0);
						temp.warlord.setYPos(displayHeight - temp.warlord.getHeight());			// warlord positions
						for (int i = 0; i < 8; i++) {
							temp.walls[i].setXPos(temp.warlord.getWidth() + 24);
							temp.walls[i].setYPos(displayHeight+(WALL_HEIGHT*i) -WALL_HEIGHT -WARLORD_HEIGHT - 45);		// Set the wall positions
						}
						for (int i = 0; i <8; i++) {
							temp.walls[i+8].setXPos(0 + (WALL_WIDTH*i));
							temp.walls[i+8].setYPos(displayHeight- temp.warlord.getHeight() -45 -WALL_HEIGHT);
						}
						break;
					case TOP_RIGHT: 
						temp.paddle.setXPos(displayWidth - CORNER_POS_X - PADDLE_WIDTH);			// paddle positions
						temp.paddle.setYPos(CORNER_POS_Y);
						temp.warlord.setYPos(0);
						temp.warlord.setXPos(displayWidth - temp.warlord.getWidth());				// warlord positions
						for (int i = 0; i < 8; i++) {
							temp.walls[i].setXPos(displayWidth - temp.warlord.getWidth() -24 -WALL_WIDTH);		// Set the wall positions
							temp.walls[i].setYPos(0 + (WALL_HEIGHT * i));
						}
						for (int i = 0; i <8; i++) {
							temp.walls[i+8].setXPos(displayWidth-(WALL_WIDTH* i ) - WALL_WIDTH);
							temp.walls[i+8].setYPos(temp.warlord.getHeight() + 42);
						}
						break;
					case BOTTOM_RIGHT: 						
						temp.paddle.setXPos(displayWidth - CORNER_POS_X - PADDLE_WIDTH);				// paddle positions
						temp.paddle.setYPos(displayHeight - CORNER_POS_Y - PADDLE_HEIGHT);
						temp.warlord.setXPos(displayWidth - temp.warlord.getWidth());
						temp.warlord.setYPos(displayHeight - temp.warlord.getHeight());				// warlord positions
						for (int i = 0; i < 8; i++) {
							temp.walls[i].setXPos(displayWidth - temp.warlord.getWidth() -WALL_WIDTH -24);
							temp.walls[i].setYPos(displayHeight + (WALL_HEIGHT * i) - WALL_HEIGHT-WARLORD_HEIGHT -45);   // Set the wall positions
						}
						for (int i = 0; i <8; i++) {
							temp.walls[i+8].setXPos(displayWidth - (WALL_WIDTH*i) - WALL_WIDTH);
							temp.walls[i+8].setYPos(displayHeight- temp.warlord.getHeight() - WALL_HEIGHT -45);
						}
						break;
				}
				
				//set the buffer to apply 
				temp.paddle.setNextXPos(temp.paddle.getXPos());
				temp.paddle.setNextYPos(temp.paddle.getYPos());
			}
		}
		
	} 
	
	//move the paddle in the designated direction required
	public void movePaddle(PlayerContainer player, int direction) {
		//get the position of the paddle
		int paddleX = player.paddle.getXPos();
		int paddleY = player.paddle.getYPos();
		
		/*Set the movement of the paddle to follow the path depending on where it is in the view
		 * 
		 * If the player is horizontally before the corner position, then the paddle moves horizontally
		 * Else, the paddle will move vertically 
		 */
		switch (player.getPosition()) {
			case TOP_LEFT:
				if ((paddleX >= CORNER_POS_X && paddleY <= CORNER_POS_Y)) {
					player.paddle.setNextXPos(CORNER_POS_X);		 // reset width
					player.paddle.setNextYPos(paddleY - (direction * player.paddle.getYVelocity()));
				}
				else if (paddleX  < CORNER_POS_X || paddleY > CORNER_POS_Y) {
					player.paddle.setNextYPos(CORNER_POS_Y);				// reset height
					player.paddle.setNextXPos(player.paddle.getXPos() + (direction * player.paddle.getXVelocity()));
				}
			break;
			case TOP_RIGHT:
				if ((paddleX <= displayWidth - CORNER_POS_X - PADDLE_WIDTH && paddleY <= CORNER_POS_Y)) {
					player.paddle.setNextXPos(displayWidth - CORNER_POS_X - PADDLE_WIDTH); 			// reset width
					player.paddle.setNextYPos(paddleY - (direction * player.paddle.getYVelocity()));
				}
				else if (paddleX  < CORNER_POS_X -displayWidth - PADDLE_WIDTH|| paddleY < displayWidth) {
					player.paddle.setNextYPos(CORNER_POS_Y);				// reset height
					player.paddle.setNextXPos(player.paddle.getXPos() - (direction * player.paddle.getXVelocity()));
				}
			break;
			case BOTTOM_LEFT:
				if ((paddleX <= CORNER_POS_X && paddleY <= displayHeight - CORNER_POS_Y - PADDLE_HEIGHT)) {
					player.paddle.setNextYPos(displayHeight - CORNER_POS_Y - PADDLE_HEIGHT);				// reset height
					player.paddle.setNextXPos(player.paddle.getXPos() - (direction * player.paddle.getXVelocity()));
				}
				else if (paddleX  > CORNER_POS_X || paddleY > displayHeight - CORNER_POS_Y - PADDLE_HEIGHT) {
					player.paddle.setNextXPos(CORNER_POS_X); 					// reset width
					player.paddle.setNextYPos(paddleY - (direction * player.paddle.getYVelocity()));
				}
			break;
			case BOTTOM_RIGHT:
				if ((paddleX >=displayWidth - CORNER_POS_X - PADDLE_WIDTH && paddleY <= displayHeight - CORNER_POS_Y - PADDLE_HEIGHT)) {
					player.paddle.setNextYPos(displayHeight - CORNER_POS_Y - PADDLE_HEIGHT);				// reset height
					player.paddle.setNextXPos(player.paddle.getXPos() + (direction * player.paddle.getXVelocity()));
				}
				else if (paddleX  < displayWidth - CORNER_POS_X - PADDLE_WIDTH || paddleY > displayHeight - CORNER_POS_Y - PADDLE_HEIGHT) {
					player.paddle.setNextXPos(displayWidth - CORNER_POS_X - PADDLE_WIDTH); 				// reset width
					player.paddle.setNextYPos(paddleY - (direction * player.paddle.getYVelocity()));
				}
			break;
		
		
		}
	}
	
	//get the width of the display
	public int getDisplayWidth () {
		return displayWidth;
	}
	
	//get the height of the display
	public int getDisplayHeight () {
		return  displayHeight;
	}
	
	//set the resolution of the screen
	public void setResolution(int width, int height) {
		displayWidth = width;
		displayHeight = height;
	}

	
	//check all forms of collision
	private void checkCollisions() {
		//check if the objects are in the boundary of the screen
		for (Ball temp : ballList) {
			checkObjectInBoundary(temp);
		}
		for (Paddle temp : paddleList) {
			checkObjectInBoundary(temp);
		}
		for (Wall temp : wallList) {
			checkObjectInBoundary(temp);
		}
		for (Warlord temp : warlordList) {
			checkObjectInBoundary(temp);
		}
		
		//check collisions of objects and the ball
		checkPaddleCollision();
		checkWallCollision();
		checkWarlordCollision();
		
		//check collision of the side of the screen
		checkBoundaryCollision();
	}
	private void checkObjectInBoundary(GameObject object) {
		//check if the object is in the boundaries of the x position
		if (object.getXPos() + object.getWidth() > displayWidth) {
			object.setXPos(displayWidth - object.getWidth());
		}
		if (object.getXPos() < 0) {   // Reset ball pos if far outside
			object.setXPos(0);
		}
		
		//check if the object is in the boundaries of the y position
		if (object.getYPos() + object.getHeight() > displayHeight) {
			object.setYPos(displayHeight - object.getHeight());
		}
		if (object.getYPos() < 0) {
			object.setYPos(0);
		}
	}
	
	private void checkBoundaryCollision() {
		for (Ball temp : ballList) {
			int nextBallXPos = temp.getXPos() + temp.getXVelocity(); // Possible next position of ball, needed for calculations
			int nextBallYPos = temp.getYPos() + temp.getYVelocity();
			int ballWidth = temp.getWidth();			// We include the width as the ball has a width and height extending from the top left corner, so this gives us the true edge of the ball
			int ballHeight = temp.getHeight();
			
			if (nextBallXPos +  ballWidth > displayWidth || nextBallXPos < 0) { // If ball is heading out of bounds, we reverse that component of velocity
				temp.setXVelocity(temp.getXVelocity() * -1);
				hitSound.play();
			}
			if (nextBallYPos +  ballHeight > displayHeight || nextBallYPos < 0) {
				temp.setYVelocity(temp.getYVelocity() * -1);
				hitSound.play();
			}
			temp.setXPos(temp.getXPos() + temp.getXVelocity());		// Update the ball position
			temp.setYPos(temp.getYPos() + temp.getYVelocity());
		}
		
	}
	
	private void checkSideofCollision(GameObject object) {
		for (Ball temp : ballList)
		{			
			if (temp.getBounds().intersects(object.getBounds())) {
				temp.setXPos(temp.getXPos()-temp.getXVelocity());
				temp.setYPos(temp.getYPos()-temp.getYVelocity());
			}
			//check if the ball is above the paddle
			if (temp.getYPos()+temp.getHeight() <= object.getYPos()) {
				temp.setYVelocity(temp.getYVelocity() *-1);
			} else if (object.getYPos()+object.getHeight() <= temp.getYPos()) { //check if the ball is below the paddle
				temp.setYVelocity(temp.getYVelocity()*-1);
			} 
			
			//check if the ball is to the left of the paddle
			if (temp.getXPos() + temp.getWidth() <= object.getXPos()) {
				temp.setXVelocity(temp.getXVelocity()*-1);
			} else if (object.getXPos() + object.getWidth() <= temp.getXPos()) { //check if the ball is to the right of the paddle
				temp.setXVelocity(temp.getXVelocity()*-1);
			}

		}
	}
	
	private boolean checkBasicCollision(GameObject object) {
		for (Ball temp : ballList){
			//create a polygon that covers the pathway of the ball as it moves
			Polygon line = new Polygon();
			
			line.getPoints().addAll(new Double[] {
					(double) temp.getXPos(), (double) temp.getYPos(),
					(double) temp.getXPos(), (double) temp.getYPos() + temp.getHeight(),
					(double) temp.getXPos() + temp.getXVelocity(), (double) temp.getYPos() + temp.getYVelocity() + temp.getHeight(),
					(double) temp.getXPos() + temp.getXVelocity() + temp.getWidth(), (double) (temp.getYPos() + temp.getYVelocity() + temp.getHeight()),
					(double) temp.getXPos() + temp.getXVelocity() + temp.getWidth(), (double) temp.getYPos() + temp.getYVelocity(),
					(double) temp.getXPos() + temp.getWidth(), (double) temp.getYPos()
			});
			
			
			//if the object is within the pathway or within the bounds of the ball, then it must reflect accordingly
			if ((object.getBounds().intersects(line.getBoundsInParent())) || (object.getBounds().intersects(temp.getBounds()))) {
				checkSideofCollision(object);
				return true;
			} 
		}
		return false;
	}
	
	//check if the ball has collided with the paddle, play sound if so
	private void checkPaddleCollision() {
		for (PlayerContainer player : playerList) {
			if (!player.warlord.isDead()) {
				if (checkBasicCollision(player.paddle)) {
					deflectSound.play();
				} 
			}
		}
	}
	
	//check if the ball has collied with the wall
	private void checkWallCollision() {
		for (Wall temp : wallList) {
			if (!temp.isDestroyed()) {
				if (checkBasicCollision(temp)) {   
					deflectSound.play(); 		//play sound if collision has occured
					temp.setHit(true); 		//set the wall as being destroyed
				}
			}
		}
	}
	
	//check if the ball has collided with the warlord
	private void checkWarlordCollision() {
		for (Warlord temp : warlordList){
			if (!temp.isDead()) {
				if (checkBasicCollision(temp)) {
					warlordHitSound.play();
					temp.setKilled(true); 			//set the warlord to be killed
					checkIfGameOver(storyMode); 				//check if the game is over
				}
			} 
	    } 
	}
	
	//one 'second' in the game
	public void tick() {
		//decrease the time
		timeRemaining --;
		//check if the game is over, and set a winner
		if (timeRemaining <= 0) {
			lostStoryMode = false;
			//set the game to be finished if the game is over
			gameFinished = true;
			
			//check the number of walls of the given player if they are still in the game
			//If there still is a player with the same number of walls, then the game is tied. Otherwise, the game should be lost.
			int pos = 0;
			int num = 0;
			boolean tied = false;
			for (int j = 0; j < playerList.size(); j++) {
				if ((!playerList.get(j).warlord.isDead()) && (playerList.get(j).getWalls() >= num)) {
					if (playerList.get(j).getWalls() == num) {
						tied = true;
					} else {
						num = playerList.get(j).getWalls();
						pos = j;
						tied = false;
					}
				}
			}
			
			//if the game isn't tied, then set the player with the given position to be won
			//However, if the player is in story mode, then if the winner is not the first player, then the game is lost
			if (!tied) {
				playerList.get(pos).warlord.setWon(true);
				setWinner(pos + 1);
				if (storyMode && (getWinner() != 1)) {
					setWinner(0);
					playerList.get(pos).warlord.setWon(false);
					lostStoryMode = true;
				}
			}
			//if the game is tied, then set the game winner to be player 5
			if (tied) {
				setWinner(5);
			}
			
			//set what happens in the game if the game isn't finished
		} else if (!gameFinished){
			//set the AI to move
			AIMovement();
			//set the paddle to move, so that the paddle moves in the tick
			for (Paddle temp : paddleList) {
				temp.applyBuffers();
			}
			checkCollisions();	 //if not over, check collisions
			updateScores();
		}
	}
	// Check to see if a player has won when a player dies
		public void checkIfGameOver(Boolean storyMode) {
			//if story mode is on, if player 1 dies, the game is automatically over even if there are other AIs still in the game
			if (storyMode) {
				lostStoryMode = false;
				if (playerList.get(0).warlord.isDead()) {
					lostStoryMode = true;
					gameFinished = true;
				} else {
					//if player1 isn't dead, check if all other players are dead, and set player1 as the winner if so
					int i = 0;
					for (PlayerContainer player : playerList) {
						if (!player.warlord.isDead()) {    // Create a count of alive players
							i++;
						}
					}
					if (i == 1) {
							gameFinished = true;
							playerList.get(0).warlord.setWon(true);
							setWinner(1);
					}
				}
			} else {
				//in the case that the game isn't over, set the game to be over if there is only one remaining player in the game
				//set that player as the winner of the game
				int i = 0;
				for (PlayerContainer player : playerList) {
					if (!player.warlord.isDead()) {    // Create a count of alive players
						i++;
					}
				}
				int j = 0;
				for (PlayerContainer player : playerList) {
					if ((i == 1) && (!player.warlord.isDead())) {    // Create a count of alive players
						setWinner(j+1);
						player.warlord.setWon(true);
						gameFinished = true;
					}
					j++;
				}
			}
		}
	
	//AI MOVEMENT
	private void AIMovement() {
		//Set up a random number between 1 and -1 for the direction of the paddle to move if the ball is not moving in the direction of its walls
		Random rand = new Random();
		Line line = new Line();
		for (int i = 0; i < playerList.size(); i++) {
			randomNum.set(i,rand.nextInt((1 - (-1)) + 1) -1);
		}

		//set a up variables to set up the line of the pathway of the ball
		int difference = 0;
		int xDirection = 0;
		int yDirection = 0;
		int index = 0;
		for (PlayerContainer temp : playerList) {
			if (temp.getPlayerType() == playerType.PLAYER_AI) {
				//set the player to hold the position that it is moving if it is moving randomly so it doesn't spaz out 
				if (temp.paddle.getHold() == 10) {
					temp.paddle.setHold(0);
				}
				if (temp.paddle.getHold() == 0) {
					temp.paddle.setDirection(randomNum.get(index));
				}
				
				//for all the balls in the ball list
				for (Ball i : ballList) {
					//set the line of the balls trajectory depending on the speed of the paddle. 
					//calculate the maximum difference from the balls position to the corner of one of the screens
					if ((i.getXVelocity() < 0) && (i.getYVelocity() <0)) {
						if (i.getXPos() > i.getYPos()) {
							difference = i.getYPos(); 
						} else {
							difference = i.getXPos();
						}
					} else if ((i.getXVelocity() < 0) && (i.getYVelocity() > 0)) {
						if (displayHeight - i.getYPos() > i.getXPos()) {
							difference = i.getXPos();
						} else {
							difference = displayHeight - i.getYPos();
						}
					} else if ((i.getXVelocity() > 0) && (i.getYVelocity() < 0)) {
						if (displayWidth - i.getXPos() > i.getYPos()) {
							difference = i.getYPos();
						} else {
							difference = displayWidth - i.getXPos();
						}
					} else if ((i.getXVelocity() > 0) && (i.getYVelocity() > 0)) {
						if (displayWidth - i.getXPos() > displayHeight - i.getYPos()) {
							difference = displayHeight-i.getYPos();
						} else {
							difference = displayWidth - i.getXPos();
						}
					}
					
					//get the direction of the ball to being positive or negative
					if (i.getXVelocity() > 0) {
						xDirection = 1;
					} else {
						xDirection = -1;
					}
					if (i.getYVelocity() > 0) {
						yDirection = 1;
					} else {
						yDirection = -1;
					}
					
					//set the line of trajetory of the ball
					line.setStartX(i.getXPos());
					line.setStartY(i.getYPos());
					line.setEndX(i.getXPos() + (xDirection*difference));
					line.setEndY(i.getYPos() + (yDirection*difference));
					
					//set a check to be false
					boolean check = false;
					//if the line intersects any of the walls of the warlord, then set a check to be true
					for (int j = 0; j < 16; j++) {
						if (line.getBoundsInParent().intersects(temp.walls[j].getBounds())) {
							check = true;
						}
					}
					
					//if the check is true, set the paddle to move in the direction of the ball
					if (check) {
						//if the paddle isn't intersecting the paddle, then move the paddle
						if (!line.getBoundsInParent().intersects(temp.paddle.getBounds())) {
							if (line.getEndY()>temp.paddle.getYPos()) {
								temp.paddle.setDirection(-1);
							} else if (line.getEndY() < temp.paddle.getYPos()){
								temp.paddle.setDirection(1);
							}
							if (line.getEndX()>temp.paddle.getXPos()) {
								temp.paddle.setDirection(1);
							} else if (line.getEndX() < temp.paddle.getXPos()) {
								temp.paddle.setDirection(-1);
							}
							movePaddle(temp, temp.paddle.getDirection()); 
						} else {
							//if the paddle manages to intersect the ball, move the paddle. 
							if (temp.paddle.getBounds().intersects(i.getBounds())) {
								temp.paddle.setDirection(randomNum.get(index));
								movePaddle(temp, temp.paddle.getDirection());
							}
							//Otherwise, if the paddle is in the trajetory of the ball, set it to stay
						}
					} else {
						//otherwise move the paddle in a random direction
						movePaddle(temp, temp.paddle.getDirection());
					}
					
				}
				temp.paddle.setHold(temp.paddle.getHold()+1);
				index++;
			}
		}
	}
	
	
	private void updateScores() {
		//check the number of non-destroyed walls for all the players in the game
		for (PlayerContainer temp : playerList) {
			temp.setnumWalls(0);
			for (int j = 0; j < 16; j++) {
				if (!temp.walls[j].isDestroyed()) {
					temp.setnumWalls(temp.getWalls()+1);
				}
			}
		}
		
		//update the score list
		for (int i = 0; i < playerList.size(); i++) {
			scoreList.set(i, playerList.get(i).getWalls());
		}

	}
	//return game finished
	public boolean isFinished() {
		return gameFinished;
	}
	
	//Force game to end 
	public void setFinished(boolean input) {
		this.gameFinished = input;
	}
	
	//set the amount of time remaining
	public void setTimeRemaining(int ticks) {
		timeRemaining = ticks;
	}
	
	//set the amount of time remaining
	public int getTimeRemaining() {
		return timeRemaining;
	}
	
	//set the winner of the game
	public void setWinner(int winner) {
		this.winner = winner;
	}
	
	//retrieve the winner of the game
	public int getWinner() {
		return winner;
	}
	
	//set the mode to be story mode
	public void setStoryMode(boolean storyMode) {
		this.storyMode = storyMode;
	}
	
	//return if the mode is story ode
	public boolean storyMode() {
		return storyMode;
	}
	
	//return whether or not the player has lost story mode
	public boolean lostStoryMode() {
		return lostStoryMode;
	}
	
	//return if the player is ghosting
	public boolean isGhosting() {
		return ghosting;
	}

}
