package warlordstest;

import junit.framework.TestSuite;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import framework.*;
import framework.PlayerContainer.nation;
import framework.PlayerContainer.playerType;
import framework.PlayerContainer.pos;
import actors.*;


public class WarlordsTest extends TestSuite {

    private Game game;
    private Ball ball;
    private PlayerContainer player1, player2, player3;
	private int PADDLE_WIDTH = 60;
	private int PADDLE_HEIGHT = 58;
	private int WARLORD_WIDTH = 199;
	private int WARLORD_HEIGHT = 140;
	private int CORNER_POS_X = WARLORD_WIDTH + (2 * PADDLE_WIDTH + 15);
	private int CORNER_POS_Y = WARLORD_HEIGHT + (2 * PADDLE_HEIGHT - 5);
	private int displayWidth = 1024;
	private int displayHeight = 768;

    @Before
    public void setUp(){

    	ball = new Ball();
    	player1 = new PlayerContainer(playerType.PLAYER_HUMAN, pos.TOP_LEFT, nation.NATION_PIRATE);
    	player2 = new PlayerContainer(playerType.PLAYER_HUMAN, pos.TOP_RIGHT, nation.NATION_PIRATE);
    	player3 = new PlayerContainer(playerType.PLAYER_HUMAN, pos.BOTTOM_LEFT, nation.NATION_PIRATE);
    	game = new Game(ball, player1, player2, player3);
        
    }

    @Test
    public void testNationEffects(){
    	/* This test checkes that the nations have their respective power ups */
    	this.player1.setNation(nation.NATION_BRITAIN);
 
    	assertTrue("The paddle should be the default velocity", this.player1.paddle.getXVelocity() == 10 && this.player1.paddle.getYVelocity() == 10);
    	

        this.player1.setNation(nation.NATION_PIRATE);
        
        assertTrue("The paddles velocity should increase by 3", this.player1.paddle.getXVelocity() == 13 && this.player1.paddle.getYVelocity() ==  13);
        
        //Pirates special ability is a faster paddle, thus it must have increased just by changing the nation
        
        this.player1.setNation(nation.NATION_BRITAIN);
        
        //Britains warlord must have two lives
        
        assertTrue("The players warlord should have two lives", this.player1.warlord.getLives() == 2);
        
        this.ball.setXPos(500);
        this.ball.setYPos(495);
        this.ball.setXVelocity(10);
        this.ball.setYVelocity(10);

        this.player1.warlord.setXPos(500);
        this.player1.warlord.setYPos(500);
        
        this.game.tick();
        
        //As the warlord is hit, the players life should decrease to 1
        assertTrue("The players warlord should have 1 life left now", this.player1.warlord.getLives() == 1);
    }
    
    @Test
    public void testGameWinConditions() {
    	/* This test checks that the winner is who it should be */
        this.ball.setXPos(500);
        this.ball.setYPos(495);
        this.ball.setXVelocity(10);
        this.ball.setYVelocity(10);

        this.player1.warlord.setXPos(500);
        this.player1.warlord.setYPos(500);

        //Whern nothing happens, there should be no winner
        assertTrue("There should not be a winner", this.game.getWinner() == 0);
        
        this.game.tick();
        
        //Player1 is hit, thus as it has 1 life left, it should now be dead
        assertTrue("Player 1 Should be dead", this.player1.warlord.isDead() == true);
        
        //Set the ball up so that the ball will hit player 3s warlord
        this.ball.setXPos(500);
        this.ball.setYPos(495);
        this.ball.setXVelocity(10);
        this.ball.setYVelocity(10);

        this.player3.warlord.setXPos(500);
        this.player3.warlord.setYPos(500);
        
        this.game.tick();

        //As both other players are dead, player 2 should be the winner
        assertTrue("Player 2 should be the winner", this.game.getWinner() == 2);

    }
    
    @Test
    public void testPaddleMovement() {
    	// This test is to check that the paddle is moving in the L position it should
    	
    	//set the player 1s paddle position to be in the corner
        this.player1.paddle.setXPos(CORNER_POS_X);
        this.player1.paddle.setYPos(CORNER_POS_Y);
        
        //if the player direction is positive, then it should move up in the corner
        this.game.movePaddle(player1, 1);
        this.player1.paddle.applyBuffers();

        assertTrue("Paddle should move up now", this.player1.paddle.getYPos() < CORNER_POS_Y);
        
        //Set the players direction to be before the corner horizontally
        this.player1.paddle.setXPos(0);
        this.player1.paddle.setYPos(CORNER_POS_Y);

        this.game.movePaddle(player1, 1);
        this.player1.paddle.applyBuffers();
        
        //If the players direction is potiive, it should move to the right
        assertTrue("The paddle should move to the right", this.player1.paddle.getXPos() > 0);

    }
    @Test
    public void testResetVariables() {
    	//This function checks that the players positioning in the screen is right just by changing the position of the player
    	//and by resetting the variables 
    	this.player1.setPosition(pos.BOTTOM_RIGHT);
    	this.game.resetVariables();

        assertTrue("The players warlord should be at the bottom right", this.player1.warlord.getXPos() == displayWidth-WARLORD_WIDTH && this.player1.warlord.getYPos() == displayHeight-WARLORD_HEIGHT);

    	this.player1.setPosition(pos.TOP_LEFT);
    	this.game.resetVariables();
        assertTrue("The players warlord should be at the top left", this.player1.warlord.getXPos() == 0 && this.player1.warlord.getYPos() == 0);

    }
    
    
    @Test
    public void testStoryMode() {
    	//Test the story mode works as needed
        this.ball.setXPos(500);
        this.ball.setYPos(495);
        this.ball.setXVelocity(10);
        this.ball.setYVelocity(10);

        this.player1.warlord.setXPos(500);
        this.player1.warlord.setYPos(500);
        
        this.game.tick();
    	//In a regular game, if player 1 is killed, the game should not be over if there are other players in the game
        assertTrue("The game should not be over", this.game.isFinished() == false);
        assertTrue("player 1 should be dead", this.player1.warlord.isDead() == true);
                
    	game.setStoryMode(true);
    	
    	this.game.checkIfGameOver(game.storyMode());
    	
    	//In story mode, if player 1 is dead then the game should be over
    	assertTrue("The game should now be over", this.game.isFinished() == true);
    	assertTrue("Story mode should be lost", this.game.lostStoryMode() == true);
    	
    }
    



}
