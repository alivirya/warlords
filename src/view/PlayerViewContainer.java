package view;
import framework.PlayerContainer;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/*
 * This creates a set of sprites for the player. This includes the walls, paddle and warlord
 */
public class PlayerViewContainer {
	//set up the variables that the PlayerViewContainer owns
	private Image paddleImage;
	private Image warlordImage;
	private Image wallImageClean, superWall, wallImageCrack2, superWarlordDecal;
	public SpriteBase paddle;
	public SpriteBase warlord;
	public SpriteBase[] wall = new SpriteBase[16];
	private PlayerContainer player;
	
	//set up the playerViewContainer depending on the player
	public PlayerViewContainer(Pane pane, PlayerContainer player) {
		
		this.player = player;
		
		//set up all the images for the specific player
		switch (player.getNation()) {
		case NATION_PIRATE:
			warlordImage = new Image(this.getClass().getResource("/img/warlord_pir.png").toExternalForm());
			paddleImage = new Image(this.getClass().getResource("/img/boat_pir.png").toExternalForm());
		break;
		case NATION_SPAIN:
			warlordImage = new Image(this.getClass().getResource("/img/warlord_spa.png").toExternalForm());
			paddleImage = new Image(this.getClass().getResource("/img/boat_spa.png").toExternalForm());
		break;
		case NATION_BRITAIN:
			warlordImage = new Image(this.getClass().getResource("/img/warlord_eng.png").toExternalForm());
			paddleImage = new Image(this.getClass().getResource("/img/boat_eng.png").toExternalForm());
		break;
		case NATION_FRANCE:
			warlordImage = new Image(this.getClass().getResource("/img/warlord_fra.png").toExternalForm());
			paddleImage = new Image(this.getClass().getResource("/img/boat_fra.png").toExternalForm());
		break;
		}
		
		warlord = new SpriteBase(pane, warlordImage, 100, 100);		// create a sprite of the other objects
		paddle = new SpriteBase(pane, paddleImage, 100, 100);
		
		
		superWarlordDecal = new Image(this.getClass().getResource("/img/warlord_super.png").toExternalForm()); // Create wall sprite decal for the super warlords
		wallImageClean = new Image(this.getClass().getResource("/img/wall.png").toExternalForm()); // Create wall sprite
		superWall = new Image(this.getClass().getResource("/img/super_wall.png").toExternalForm()); // Create wall sprite decal for the super wall
		wallImageCrack2 = new Image(this.getClass().getResource("/img/wallcrack2.png").toExternalForm()); // Create wall sprite decal for a cracked wall
    	for (int i = 0; i < 16; i++) {
    		wall[i] = new SpriteBase(pane, wallImageClean, 100, 100);
    	}
		
	}
	
	//update the visual of the wall
	public void updateWarlordVisual() {
		if (player.warlord.getLives() > 1) {
			warlord.setDecalImage(superWarlordDecal);
		} 
		else {
			warlord.removeDecal();
		}
	}
	
	//update the visual of the wall
	public void updateWallVisual(int wallIndex) {
		if (player.walls[wallIndex].getLives() == 1) {
				wall[wallIndex].setDecalImage(wallImageCrack2);
		} 
		else if (player.walls[wallIndex].getLives() == 3) {
			wall[wallIndex].setDecalImage(superWall);
		}
		else {
			wall[wallIndex].removeDecal();

	}
	}

}
