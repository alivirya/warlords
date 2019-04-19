package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import actors.Ball;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.text.*;
import framework.Game;
import framework.InputListener;
import framework.PlayerContainer;
import framework.PlayerContainer.playerType;

public class GameStage extends Application {
	//set up all the view variables
	Pane playfieldLayer, bgLayer, islandLayer, textLayer, menuOverlay;
	Stage primaryStage;
	Image testImage;
	Image bgImage;
	Image testWall;
	Image islandImage;
	Scene gameScene, mainMenuScene, customMenuScene, storyMenuScene, settingsScreen;
	Font arcadeFont;
	Game game;
	MediaPlayer bgMusicPlayer, ambiencePlayer;
	InputListener inputListener, bgListener;
	int countDown, holdPause, ballSpeed;
	int humanPlayers = 0;
	int aiPlayers = 0;   
	int level = 0;
	int holdGame = 10;
	final int DISPLAY_WIDTH_MENU = 1024;     // The resolution of the menu
	final int DISPLAY_HEIGHT_MENU = 768;
	final int GHOST_STRENGTH = 8;
	
	int gameDisplayWidth = DISPLAY_WIDTH_MENU;
	int gameDisplayHeight = DISPLAY_HEIGHT_MENU;  // Default game res
	VBox optionsBox;
	
	boolean ghosting;
	boolean gameRunEnabled = false;
	boolean gamePaused = false;
	boolean storyMode = false;
	boolean quitEnabled = false;
	boolean lostStoryMode = false;

	@Override
	public void start(Stage primaryStage) {

    	
    	//show the game stage 
    	primaryStage.show();
    	primaryStage.setResizable(false);
    	primaryStage.setTitle("Warlords");    // window title

    	bgLayer = new Pane();
    	bgImage = new Image(this.getClass().getResource("/img/basic_water.png").toExternalForm());  // Add the bg image to a pane for all scenes
		SpriteBase bgImageSprite = new SpriteBase(bgLayer, bgImage, 0, 0);
    	
		
		initalizeMainMenuScene(primaryStage);  // Start by setting up the main menu screen
	}
	
	
	//This function switches the display to the main menu
	private void initalizeMainMenuScene(Stage primaryStage) {
			
		this.primaryStage = primaryStage;
		
		Pane mainBgLayer = new Pane();
    	Image skyImage = new Image(this.getClass().getResource("/img/sky_bg.png").toExternalForm());
    	SpriteBase bgImageSprite = new SpriteBase(mainBgLayer, skyImage, 0, 0);			// Create the bg image for the mney
		
		Group root = new Group();				// Create a group of panes (layers)
		mainMenuScene = new Scene(root, DISPLAY_WIDTH_MENU, DISPLAY_HEIGHT_MENU); 
		menuOverlay = new Pane();
		root.getChildren().add(mainBgLayer);
		root.getChildren().add(menuOverlay);		// Add the panes to the scene
		primaryStage.setScene(mainMenuScene);
		mainMenuScene.getStylesheets().add("main.css");
		
		
		DropShadow ds = new DropShadow();
		ds.setOffsetY(5.0f);					// Logo drop shadow
		ds.setColor(Color.color(0.3f, 0.3f, 0.3f));
		 
		
		Text t = new Text("WARLORDS");
		t.setEffect(ds);

		Font titleFont = Font.loadFont(this.getClass().getResource("/font/ka1.ttf").toExternalForm(), 104);	  // Load fonts
		arcadeFont = Font.loadFont(this.getClass().getResource("/font/joystix_monospace.ttf").toExternalForm(), 104);	
		
		t.setFont(titleFont);

    	
    	
    	VBox titleTextBOx = new VBox();
    	titleTextBOx.getChildren().add(t);
    	titleTextBOx.setAlignment(Pos.CENTER);
    	titleTextBOx.setPrefWidth(DISPLAY_WIDTH_MENU);			// Set up the text and text box for the title and add it to the overlay pane
    	titleTextBOx.setLayoutY(50);
    	t.setFill(Color.WHITE);
    	
    	menuOverlay.getChildren().add(titleTextBOx);
		
		
		VBox buttonBox = new VBox(50);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setPrefWidth(DISPLAY_WIDTH_MENU);
		menuOverlay.getChildren().add(buttonBox);
		buttonBox.setLayoutY(DISPLAY_HEIGHT_MENU / 2 -100);
		buttonBox.setStyle("-fx-font-size: 22px;");
		
		Button buttonCustom = new Button("Custom Mode");
		buttonBox.getChildren().add(buttonCustom);

    	Button buttonStory = new Button("Story Mode");				// Create the buttons and add them to a vertical box
    	buttonBox.getChildren().add(buttonStory);
    	
    	Button buttonMore = new Button("More Options");
    	buttonBox.getChildren().add(buttonMore);
    	
    	Button quitButton = new Button ("Quit");
    	buttonBox.getChildren().add(quitButton);
    	
    	buttonCustom.setMinWidth(300);
    	buttonStory.setMinWidth(300);		// Uniform width
    	buttonMore.setMinWidth(300);
    	quitButton.setMinWidth(300);
    	
    	quitButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {         // quit game when button pressed
		    	System.exit(0);
		    }
    	});
    	
    	buttonStory.setOnAction(e -> { 
		    	mainMenuScene = null;
		    	initalizeStoryScene(primaryStage, level);     // start the story scene when this button is pressed		    
		});
    	
    	buttonMore.setOnAction(e -> { 
	    	mainMenuScene = null;
	    	initalizeSettingScene(primaryStage);			 // start the setting scene when this button is pressed		   
    	});
    	
    	buttonCustom.setOnAction(e -> {
		    	mainMenuScene = null;
		    	storyMode = false;
		    	humanPlayers = 2;
		    	aiPlayers = 1;
		    	game = new Game(10, false, false, humanPlayers, aiPlayers); 	// Default values
		    	game.setResolution(gameDisplayWidth, gameDisplayHeight);
		    	game.setStoryMode(false); 
		    	initializeCustomGameScene(primaryStage);	    // Start the custom menu picker	
		});
    	
		Media sound2 = new Media(new File("resources/sound/ship_ambience.mp3").toURI().toString());
		ambiencePlayer = new MediaPlayer(sound2);
		ambiencePlayer.setVolume(ambiencePlayer.getVolume() * 0.4);			// Create an audio player for the story menu
	}
	
	// This function switches to the menu for the game options screen
	private void initalizeSettingScene(Stage primaryStage) {
		
		Group root = new Group();	// Create a group of panes (layers)
		settingsScreen = new Scene(root, DISPLAY_WIDTH_MENU, DISPLAY_HEIGHT_MENU); 

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(60);				// Create a grid pane to hold all of the display elements
        grid.setHgap(40);
        
        grid.setPrefWidth(DISPLAY_WIDTH_MENU);
        grid.setPrefHeight(DISPLAY_HEIGHT_MENU);		// Centre the grid
		grid.setAlignment(Pos.CENTER);
		root.getChildren().add(bgLayer);
		
		menuOverlay = new Pane();
		menuOverlay.getChildren().add(grid);		// Add the grid to the scene
		root.getChildren().add(menuOverlay);
		
		primaryStage.setScene(settingsScreen);
		settingsScreen.getStylesheets().add("main.css");

		
		
		Font menuFont = arcadeFont;
		
		Slider xSlider = new Slider();
		Slider ySlider = new Slider();
		Label yCaption = new Label("Height:");		// create sliders and labels for the resolution options 
	    Label xCaption = new Label("Width:");
	       
		
		Label xSliderValue = new Label(Double.toString(xSlider.getValue()));
		Label ySliderValue = new Label(Double.toString(ySlider.getValue()));  // Create labels to display the current value measured
		
		Button backButton = new Button("Apply");
		
		Label titleText = new Label("Set the Gameplay Screen Resolution");
		
		grid.setStyle("-fx-font-size: 22px;");
		yCaption.setStyle("-fx-text-fill: white");
		xCaption.setStyle("-fx-text-fill: white");			// Set font to white and size to 22
		xSliderValue.setStyle("-fx-text-fill: white");
		ySliderValue.setStyle("-fx-text-fill: white");

		
		
    	titleText.setTextAlignment(TextAlignment.LEFT);
    	titleText.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");
    	
        GridPane.setConstraints(titleText, 0, 0);
        GridPane.setColumnSpan(titleText, 2);				// Create a title that will be at the top of the menu
        grid.getChildren().add(titleText);
        //settingsScreen.setRoot(grid);
		
		

		xSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
	            	gameDisplayWidth = new_val.intValue();
	                xSliderValue.setText(String.format("%d", new_val.intValue()));
            }
        });																				// The sliders change the value of the width/height and update the labels
		ySlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	gameDisplayHeight = new_val.intValue();   
            	ySliderValue.setText(String.format("%d", new_val.intValue()));
            }
        });
		
				
		
		
		GridPane.setConstraints(xCaption, 0, 1);
        grid.getChildren().add(xCaption);
		
		GridPane.setConstraints(xSlider, 1, 1);
        grid.getChildren().add(xSlider);
        
        GridPane.setConstraints(xSliderValue, 2, 1);
        grid.getChildren().add(xSliderValue);
        													// Add the elements to the grid
        GridPane.setConstraints(yCaption, 0, 2);
        grid.getChildren().add(yCaption);
		
		GridPane.setConstraints(ySlider, 1, 2);
        grid.getChildren().add(ySlider);
        
        GridPane.setConstraints(ySliderValue, 2, 2);
        grid.getChildren().add(ySliderValue);
		
		xSlider.setMin(1024);
		xSlider.setMax(2560);
		xSlider.setBlockIncrement(1);
		
		ySlider.setMin(768);
		ySlider.setMax(1440);	
		ySlider.setBlockIncrement(1);
		
		xSlider.setValue(gameDisplayHeight);
	    ySlider.setValue(gameDisplayHeight);
		
		
		
		backButton.setStyle("-fx-font-size: 18px;");
		backButton.setPrefWidth(100);
		GridPane.setConstraints(backButton, 1, 3);
        grid.getChildren().add(backButton);
        GridPane.setHalignment(backButton, HPos.CENTER);			// Create a back button to return to the menu and add it to the grid
		
        backButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	initalizeMainMenuScene(primaryStage);
		    }
    	});

	}
	
	// This creates (and shows) the scene for the custom game options.  This is called when the custom option is selected in the menu
	private void initializeCustomGameScene(Stage primaryStage) {
		humanPlayers = 1; 
		aiPlayers = 1; 
		boolean mode = false;		// Set up default values
		ghosting = true;
		ballSpeed = 14;
		
		Group root = new Group();	// Create a group of panes (layers)
		customMenuScene = new Scene(root, DISPLAY_WIDTH_MENU, DISPLAY_HEIGHT_MENU); 

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(60);
        grid.setHgap(40);
        
        grid.setPrefWidth(DISPLAY_WIDTH_MENU); 		// Create a grid to hold the elements, centre it add add it to the pane
        grid.setPrefHeight(DISPLAY_HEIGHT_MENU);
		grid.setAlignment(Pos.CENTER);
		root.getChildren().add(bgLayer);
		
		menuOverlay = new Pane();
		menuOverlay.getChildren().add(grid);
		root.getChildren().add(menuOverlay);
		
		primaryStage.setScene(customMenuScene);
		customMenuScene.getStylesheets().add("main.css");
		
		Slider humanSlider = new Slider(0,2,0);
		Slider aiSlider = new Slider(0,4,0);
		Slider ballSlider = new Slider(10,20,16);
		Label aiSliderCaption = new Label("Number of AI Players: ");			// Create the sliders for ai/human player and ball speed
	    Label humanSliderCaption = new Label("Number of Human Players: ");
	    Label ballSliderCaption = new Label("Speed of the Ball:");
	    
	    Label ghostBoxCaption = new Label("Enable Ghosting Mode\n(This allows human\nplayers to affect the\nball's speed after death)");   // Create a checkbox and label for the ghosting option
	    CheckBox ghostBox = new CheckBox();
	    ghostBox.setSelected(ghosting);

	    humanSlider.setMajorTickUnit(2);
	    humanSlider.setMinorTickCount(1);
	    humanSlider.setBlockIncrement(1);
	    humanSlider.setSnapToTicks(true);
	    
	    aiSlider.setMajorTickUnit(2);			// Refine the sliders to move in the correct increments
	   	aiSlider.setMinorTickCount(1);
	    aiSlider.setBlockIncrement(1);
	    aiSlider.setSnapToTicks(true);
	    
	    ballSlider.setBlockIncrement(1);
	    
	    humanSlider.setValue(humanPlayers);
	    aiSlider.setValue(aiPlayers);					// Set the sliders to the default values
	    ballSlider.setValue(ballSpeed);
		
		Label humanSliderValue = new Label(String.format("%.0f", humanSlider.getValue()));
		Label aiSliderValue = new Label(String.format("%.0f", aiSlider.getValue()));		// Set the labels to the initial values
		Label ballSliderValue = new Label(String.format("%.0f", ballSlider.getValue()));
		
		Button confirmButton = new Button("Start!");
		
		Label titleText = new Label("Custom Game Set-Up\nMiniumum 2 Players, Maximum 4");
		
		grid.setStyle("-fx-font-size: 18px;");
		aiSliderCaption.setStyle("-fx-text-fill: white");
		humanSliderCaption.setStyle("-fx-text-fill: white");
		humanSliderValue.setStyle("-fx-text-fill: white");		// Set the text formatting
		aiSliderValue.setStyle("-fx-text-fill: white");
		ballSliderValue.setStyle("-fx-text-fill: white");
		ballSliderCaption.setStyle("-fx-text-fill: white");
		ghostBoxCaption.setStyle("-fx-text-fill: white");

    	titleText.setTextAlignment(TextAlignment.LEFT);
    	titleText.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");
    	
        GridPane.setConstraints(titleText, 0, 0);			// Add the first element to the grid
        GridPane.setColumnSpan(titleText, 2);
        grid.getChildren().add(titleText);
		
        ghostBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                Boolean old_val, Boolean new_val) {
                    ghosting = new_val;
            }
        });

		humanSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
	            humanPlayers = new_val.intValue();
	            humanSliderValue.setText(String.format("%d", new_val.intValue()));
	            if (humanPlayers + aiPlayers == 1) {
	            	aiSlider.setValue(aiPlayers + 1);
	            }
	            else if (humanPlayers + aiPlayers == 5) {
	            	aiSlider.setValue(aiPlayers - 1);		// Update both the logic of the human and ai sliders. Ensuring there is a max of 4 players and a minimum of 2
	            }
            }
        });
		aiSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	aiPlayers = new_val.intValue();   
            	aiSliderValue.setText(String.format("%d", new_val.intValue()));
	            if (humanPlayers + aiPlayers == 1) {
	            	humanSlider.setValue(humanPlayers + 1);
	            }
	            else if (humanPlayers + aiPlayers == 5) {
	            	humanSlider.setValue(humanPlayers - 1);		// Update both the logic of the human and ai sliders. Ensuring there is a max of 4 players and a minimum of 2
	            }
            } 
        });
		
		ballSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	ballSpeed = new_val.intValue();   
            	ballSliderValue.setText(String.format("%d", new_val.intValue()));			// Set the ball speed by slider position
            }
        });
		
				
		
		
		GridPane.setConstraints(humanSliderCaption, 0, 1);
        grid.getChildren().add(humanSliderCaption);
		
		GridPane.setConstraints(humanSlider, 1, 1);
        grid.getChildren().add(humanSlider);
        
        GridPane.setConstraints(humanSliderValue, 2, 1);
        grid.getChildren().add(humanSliderValue);
        
        GridPane.setConstraints(aiSliderCaption, 0, 2);
        grid.getChildren().add(aiSliderCaption);
		
		GridPane.setConstraints(aiSlider, 1, 2);
        grid.getChildren().add(aiSlider);
        
        GridPane.setConstraints(aiSliderValue, 2, 2);				// Add all the elements to the grid
        grid.getChildren().add(aiSliderValue);
        
        GridPane.setConstraints(ballSliderCaption, 0, 3);
        grid.getChildren().add(ballSliderCaption);
		
		GridPane.setConstraints(ballSlider, 1, 3);
        grid.getChildren().add(ballSlider);
        
        GridPane.setConstraints(ballSliderValue, 2, 3);
        grid.getChildren().add(ballSliderValue);
        
        GridPane.setConstraints(ghostBoxCaption, 0, 4);
        grid.getChildren().add(ghostBoxCaption);
        
        GridPane.setConstraints(ghostBox, 1, 4);
        grid.getChildren().add(ghostBox);
		
		confirmButton.setStyle("-fx-font-size: 18px;");
		confirmButton.setAlignment(Pos.CENTER);			// Create and add a confirm button
		confirmButton.setPrefWidth(150);
		GridPane.setConstraints(confirmButton, 1, 5);
        grid.getChildren().add(confirmButton);
		
		
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	game = new Game(ballSpeed, ghosting, mode, humanPlayers, aiPlayers);
		    	initializeGameScene(primaryStage);					// The confirm button starts the game according to the chosen parameters
		    }
    	});
	}
	
	
	// Here we create the 'scene' for the core gameplay
	private void initializeGameScene(Stage primaryStage) {
		game.setResolution(gameDisplayWidth, gameDisplayHeight);
		
		Group root = new Group();	// Create a group of panes (layers)
		root.setId("pane");
		
		root.getChildren().add(bgLayer);    // Add to the group
		
		islandLayer = new Pane();
		root.getChildren().add(islandLayer);
		
		playfieldLayer = new Pane();	// This layer is for the sprites that make up the gameplay
		root.getChildren().add(playfieldLayer);	   // Add to the group
		
		textLayer = new Pane();	// This layer is for the sprites that make up the gameplay
		root.getChildren().add(textLayer);	   // Add to the group
		
		
    	gameScene = new Scene(root, gameDisplayWidth, gameDisplayHeight);     // Create a scene that uses the group of panes and set to the window size
    	
    	primaryStage.setScene(gameScene);    // Set the scene to be displayed 
    	
    	
		// BG music player
		Media sound = new Media(new File("resources/sound/bg.mp3").toURI().toString());
		bgMusicPlayer = new MediaPlayer(sound);
		bgMusicPlayer.play();
		bgMusicPlayer.setVolume(bgMusicPlayer.getVolume() * 0.2);
		
    	initializeLevel();	// Now the scene is visually set up, we start the games mechanics 
	}
	
	// This shows the text "scroll" splash screens for the menu. The parameter 'level' essentially changes the text and the behaviour of the button
	private void initalizeStoryScene(Stage primaryStage, int level) {
		storyMode = true;
		Group root = new Group();	// Create a group of panes (layers)
		storyMenuScene = new Scene(root, DISPLAY_WIDTH_MENU, DISPLAY_HEIGHT_MENU); 
		menuOverlay = new Pane();
		Pane blackBG = new Pane();
		root.getChildren().add(blackBG);		// Add the panes to the scene
		root.getChildren().add(menuOverlay);
		primaryStage.setScene(storyMenuScene);
		storyMenuScene.getStylesheets().add("main.css");
		
		bgImage = new Image(this.getClass().getResource("/img/story_bg.png").toExternalForm());  // Add the bg image to a pane for all scenes
		SpriteBase bgStoryImageSprite = new SpriteBase(blackBG, bgImage, 0, 0);
		
		Text t = new Text("Null");
    	t.setFont(arcadeFont);
    	VBox tBox = new VBox();
    	tBox.getChildren().add(t);
    	tBox.setAlignment(Pos.CENTER);
    	t.setWrappingWidth(375);
    	t.setTextAlignment(TextAlignment.LEFT);		// Create the text box and center it properly to within the scroll sprite
    	tBox.setPrefWidth(DISPLAY_WIDTH_MENU);
    	t.setStyle("-fx-font-size: 21px;");
    	tBox.setLayoutY(70);
    	menuOverlay.getChildren().add(tBox);
    	
    	HBox buttonBox = new HBox();
    	Button button = new Button("OK");
    	buttonBox.getChildren().add(button);
    	buttonBox.setStyle("-fx-font-size: 28px;");
    	buttonBox.setAlignment(Pos.CENTER);
    	buttonBox.setPrefWidth(DISPLAY_WIDTH_MENU);			// These elements are added to an H box for formatting
    	buttonBox.setLayoutY(DISPLAY_HEIGHT_MENU - 120);
    	menuOverlay.getChildren().add(buttonBox);
    	
    	ambiencePlayer.play();
    	
		button.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	initializeGameScene(primaryStage); 				// Normally the button stops the music, sets the game mode and starts the game initialisation
		    	ambiencePlayer.stop();
		    	game.setStoryMode(storyMode);
		    }
    	});
		
		
		// Depending on the level below, we change the text to the different part of the story
		
    	switch (level) {
    		case -1:
    			level = 0;
    			t.setText("Failure\n\nYou have met your doom! Without your pirate island you are a husk. Looks like you have no choice but to head back to England."
    					+ " Dad might give you a job at his shoe shop or something...\n\nEND");
    			this.level = 1;
    			button.setOnAction(new EventHandler<ActionEvent>() {
    			    @Override public void handle(ActionEvent e) {
    			    	ambiencePlayer.stop();							// This failure screen is a special case so the button returns to the menu
    			    	initalizeMainMenuScene(primaryStage); 
    			    }
    	    	});
    		break;
    		case 0:
    			t.setText("The year is 1683. It is the height of the piracy golden age. You find yourself in the Carribean, a soup of opposing navies and"
    					+ " fortified islands bristling with cannons. For a 'golden age' you've had a hard time finding any gold.... The Spanish treasure trade still"
    					+ " goes through here, that might be a good start..." );
    			this.level = 1;
    			button.setOnAction(new EventHandler<ActionEvent>() {
    				@Override public void handle(ActionEvent e) {		// This screen only shows once so once its shown we skip to the next 'level'
    	    			initalizeStoryScene(primaryStage, 1);
    			    }
    	    	});
    		break;
    		case 1:
    			t.setText("The Beginning\n3 Nov 1683\n\nAs captain of the \"Salty Gherkin\", you've always been longing for a really big haul, and it looks like you found it."
    			+ " A Spanish treasure island stands alone off the coast of the fort you \"acquired\" last week, ripe for the plunder...");
    			humanPlayers = 1;
    			aiPlayers = 1;
    			game = new Game(14, false, false, humanPlayers, aiPlayers);
    			game.player1.setNation(PlayerContainer.nation.NATION_PIRATE);
    			game.player2.setNation(PlayerContainer.nation.NATION_SPAIN);
    		break;
    		case 2:
    			t.setText("The Escalation\n2 Dec 1683\n\nThe British have finally had enough. Your prior sinking of their precious tea cargo was tolerated, but this attack on "
    					+ "a fort so close to the mainland hasn't gone unnoticed. Take out the British forts before they take out yours!");
    			humanPlayers = 1;
    			aiPlayers = 2;
    			game = new Game(16, false, false, humanPlayers, aiPlayers);
    			game.player1.setNation(PlayerContainer.nation.NATION_PIRATE);
    			game.player2.setNation(PlayerContainer.nation.NATION_BRITAIN);
    			game.player3.setNation(PlayerContainer.nation.NATION_BRITAIN);
    		break;
    		case 3:
    			t.setText("Escargot\n1 Jan 1684\n\nHmm... maybe capturing both forts wasn't the best idea... One of your stray cannonballs seemed to have hit the only winemaker"
    					+ " in Guadeloupe and the French are very mad. Three of their finest forts turn their guns to you... ");
    			humanPlayers = 1;
    			aiPlayers = 3;
    			game = new Game(18, false, false, humanPlayers, aiPlayers);
    			game.player1.setNation(PlayerContainer.nation.NATION_PIRATE);
    			game.player2.setNation(PlayerContainer.nation.NATION_FRANCE);
    			game.player3.setNation(PlayerContainer.nation.NATION_FRANCE);
    			game.player4.setNation(PlayerContainer.nation.NATION_FRANCE);
    		break;
    		case 4:
    			t.setText("Victory\n6 Feb 1684\n\nThe great nations's forts lie in ruin and they run back to europe with their tails between their legs. You have proven yourself"
    					+ "one of history's great pirates. Time to enjoy your plunder..\n\nEND");
    			this.level = 1;
    			button.setOnAction(new EventHandler<ActionEvent>() {
    			    @Override public void handle(ActionEvent e) {
    			    	ambiencePlayer.stop();
    			    	initalizeMainMenuScene(primaryStage); 			// On the last level, the button returns us to the menu
    			    }
    	    	});
    		break;
    	}
		
	}
	
	// This generates the basic elements of the game, including the bg images
	private void initializeLevel() { 		
		List<PlayerViewContainer> playerViewContainerList = new ArrayList<PlayerViewContainer>();
    	if (humanPlayers + aiPlayers > 0) {
    		PlayerViewContainer player1View = new PlayerViewContainer(playfieldLayer, game.player1);  // Create player sprite set
    		playerViewContainerList.add(player1View);
    		if (humanPlayers + aiPlayers > 1) {
        		PlayerViewContainer player2View = new PlayerViewContainer(playfieldLayer, game.player2);  // Create player sprite set
        		playerViewContainerList.add(player2View);
        		if (humanPlayers + aiPlayers > 2) {
            		PlayerViewContainer player3View = new PlayerViewContainer(playfieldLayer, game.player3);  // Create player sprite set
            		playerViewContainerList.add(player3View);
            		if (humanPlayers + aiPlayers > 3) {
                		PlayerViewContainer player4View = new PlayerViewContainer(playfieldLayer, game.player4);  // Create player sprite set
                		playerViewContainerList.add(player4View);
                	}
        		}
    		}
    	}

		generateIslands(humanPlayers + aiPlayers);
		
    	game.resetBall();
    	game.resetVariables();
    	generateStaticVariables(playerViewContainerList);

    	
    	testImage = new Image(this.getClass().getResource("/img/ballBasic.png").toExternalForm()); // Create ball sprite
    	SpriteBase ballSprite = new SpriteBase(playfieldLayer, testImage, 100, 100);
    	
    	
    	
    	inputListener = new InputListener(gameScene); // Create a listener for the paddle and assign it to the scene
    	bgListener = new InputListener(gameScene); // Create a listener for the paddle and assign it to the scene
    	
    	Text t = new Text("Time Remaining");
    	t.setFont(arcadeFont);
    	VBox timeRemainingBox = new VBox();
    	timeRemainingBox.getChildren().add(t);		// Create the time remaining text and add it to the pane
    	timeRemainingBox.setAlignment(Pos.CENTER);
    	timeRemainingBox.setPrefWidth(gameDisplayWidth);
    	t.setStyle("-fx-font-size: 28px;");

    	textLayer.getChildren().add(timeRemainingBox);	
    	countDown = (60 * 4) - 1;  // 3 seconds	
    	gamePaused = false;
    	
    	
    	AnimationTimer gameLoop = new AnimationTimer() {
    		
			@Override
			public void handle(long now) {		// Start a timer at 60 updates per second
				
				
				
				t.setText(game.getTimeRemaining() / 60 + " remaining");
				ballSprite.setXPos(game.ball.getXPos()); 
				ballSprite.setYPos(game.ball.getYPos());
				ballSprite.updateUI();					// Set the ball sprite position to that of the ball in the game instance
				
				checkPaddleMovement(inputListener, game.player1);
				checkPaddleMovement(inputListener,game.player2);
				int i = 0;
				if (game.isFinished()) {
					gameRunEnabled = false;
					if (storyMode) {
						if (game.lostStoryMode()) {
							t.setText("YOU LOST");
							lostStoryMode = true;
						} else if (game.getWinner() == 5) {		// The text to be shown in story mode, draws count as a loss here
							t.setText("YOU DIDN'T WIN");
							lostStoryMode = true;
						} else {
							t.setText("YOU WIN");
							lostStoryMode = false;
						}
					} else {
						if (game.getWinner() < 5) {
							switch (game.getWinner()) {
								case 1:
									t.setText("TOP LEFT WINS");
								break;
								case 2:
									t.setText("BOTTOM RIGHT WINS");		// Standard victory text
								break;
								case 3:
									t.setText("TOP RIGHT WINS");
								break;
								case 4:
									t.setText("BOTTOM LEFT WINS");
								break;
							}
						} else {
							t.setText("GAME TIED");
						}	
					}
					gameFinishPrompt(lostStoryMode);		// Show the prompt at the end of the game, this will depend on the outcome of the story mode game if it is one
					stop();		// stop the loop
				} else {
					if (countDown > 0) {
						t.setText("Starting in: " + countDown / 60);		
						countDown --;
					} else if (gamePaused) {
						t.setText("GAME PAUSED");			// Basic logic for display text at the top
						gameRunEnabled = false;
						holdPause++;
					} else if (!gamePaused) {
						holdGame++;
						gameRunEnabled = true;
					}
					
					if (gameRunEnabled){
						game.tick();   // Tick the game instance
					}
					if ((inputListener.pressPause()) && (!gamePaused) && (holdGame >= 15 && quitEnabled == false)) {
						gamePaused = true;
						holdGame = 0;
					} else if ((inputListener.pressPause()) && gamePaused && (holdPause >= 15 && quitEnabled == false)) {		// Pause and un-pause when the key is hit
						gamePaused = false;
						gameRunEnabled = true;						// As the game updates quickly, the key must be 'held' for a fraction of a second before it registers to stop rapid pausing/un-pausing
						holdPause = 0;
					}
					if (inputListener.pressEscape() && (!gamePaused) && (holdGame >= 15 && countDown < 1)) {
						gamePaused = true;
						quitEnabled = true;
						holdGame = 0;
						quitDialogue(quitEnabled);
					} else if ((inputListener.pressEscape()) && gamePaused && (holdPause >=15 && countDown < 1)) {
						gamePaused = false;
						gameRunEnabled = true;		// Pause the game when we hit escape, but also show a special dialogue 
						quitEnabled = false;
						holdPause = 0;
						quitDialogue(quitEnabled);
					}
					if ((inputListener.pressPageDown()) && countDown < 1) {		// page down takes us to the endo of the time
						game.setTimeRemaining(0);
					}
				}
				for (PlayerViewContainer temp : playerViewContainerList) {
					for (int j = 0; j < 16; j++) {
						temp.updateWallVisual(j);				// Update wall visuals for cracks etc
						temp.wall[j].updateUI();
						temp.wall[j].removeFromLayer(game.playerList.get(i).walls[j].isDestroyed());    // Remove the wall if it is destroyed
					}
					temp.updateWarlordVisual();
					temp.warlord.removeFromLayer(game.playerList.get(i).warlord.isDead());    // remove the Warlord if it is destroyed
					if (!game.playerList.get(i).warlord.isDead()) {
						temp.paddle.setXPos(game.playerList.get(i).paddle.getXPos()); 
						temp.paddle.setYPos(game.playerList.get(i).paddle.getYPos()); // Set the paddle sprite position to that of the ball in the game instance
						temp.paddle.updateUI();
					} else {
						temp.paddle.removeFromLayer(game.playerList.get(i).warlord.isDead());   // remove the paddle if it's associated warlord is destroyed
					}
					i++;
				}
			}
    	};
    	gameLoop.start();			// Start the loop
	}
	
	//This creates the buttons that will show when escape is pressed
	private void quitDialogue(boolean enabled) {	
		if (enabled) {
			optionsBox = new VBox(20);
	    	optionsBox.getStylesheets().add("main.css");
	    	optionsBox.setStyle("-fx-font-size: 22px;");
	    	optionsBox.setAlignment(Pos.CENTER);			//Create a vertical box and add it to the pane
	    	optionsBox.setPrefWidth(gameDisplayWidth);		
	    	optionsBox.setPrefHeight(gameDisplayHeight);
	    	
	    	textLayer.getStylesheets().add("main.css");
	    	
			Button resume = new Button("Resume");
			Button mainMenu = new Button("Main Menu");			//Create the buttons
			Button quit = new Button("Quit");
			
			optionsBox.getChildren().add(resume);
			optionsBox.getChildren().add(mainMenu);		// Add the buttons to the box
			optionsBox.getChildren().add(quit);
			
			resume.setMinWidth(250);
			mainMenu.setMinWidth(250);		// Uniform width
			quit.setMinWidth(250);
	
				
			resume.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	gamePaused = false;
					gameRunEnabled = true;
					holdPause = 0;
					textLayer.getChildren().remove(optionsBox);		// Resume reverses the effects of hitting escape
			    }
	    	});
			
			mainMenu.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	initalizeMainMenuScene(primaryStage);
					quitEnabled = false;
					holdPause = 0;
					game.setFinished(true);
			    	bgMusicPlayer.stop();				// Main menu stops the game and resets values so a clean game is created next time
			    	level--;
			    }
	    	});
	    	
			quit.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	System.exit(0);				// The quit box ends the application
			    }
	    	});
	    	
	    	textLayer.getChildren().add(optionsBox);
		}
		else {
			textLayer.getChildren().remove(optionsBox);			// When this function is called 'disabled' we remove the box
		}
    	
	}
	
	// This prompt shows when a game ends. The parameter allows different response if we lost a story game, otherwise it is ignored
	// It shows the buttons as well as scores over the players
	private void gameFinishPrompt(boolean lost) {;
    	VBox buttonBox = new VBox(20);
    	buttonBox.getStylesheets().add("main.css");
    	buttonBox.setStyle("-fx-font-size: 22px;");
    	buttonBox.setAlignment(Pos.CENTER);					// Create a vertical box for the buttons and add it to the pane
    	buttonBox.setPrefWidth(gameDisplayWidth);
    	buttonBox.setPrefHeight(gameDisplayHeight);
    	textLayer.getStylesheets().add("main.css");


    	VBox scoreBoxLeft = new VBox(gameDisplayHeight / 6);
    	VBox scoreBoxRight = new VBox(gameDisplayHeight / 6);		// Create 2 vertical boxes for the 4 scores
    	
    	scoreBoxLeft.setLayoutX(20);
    	scoreBoxLeft.setPrefHeight(gameDisplayHeight);
    	scoreBoxLeft.setAlignment(Pos.CENTER);					// Put the 2 boxes on either side of the screen
    	
    	scoreBoxRight.setLayoutX(gameDisplayWidth - 200);
    	scoreBoxRight.setPrefHeight(gameDisplayHeight);
    	scoreBoxRight.setAlignment(Pos.CENTER);
    	
		Label p1label = new Label();
		p1label.setText("Score: " + game.scoreList.get(0));
		scoreBoxLeft.getChildren().add(p1label);						// We always have a player 1 so  place that in the left box   
		
		p1label.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");
		p1label.setTextAlignment(TextAlignment.CENTER);
		
	    if (game.scoreList.size() > 2) {
	    	
			Label p3label = new Label("Score: " + game.scoreList.get(2));
			scoreBoxRight.getChildren().add(p3label);
			p3label.setTextAlignment(TextAlignment.CENTER);						// We next put the Player 3 on the right if we have more than 2 players
			p3label.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");
	    	
			Label p4label = new Label("Score: " + game.scoreList.get(1));
			scoreBoxRight.getChildren().add(p4label);									// Player 4 is placed in the left box, the spacing means these will be shown over the 2 islands
			p4label.setTextAlignment(TextAlignment.CENTER);
			p4label.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");	

			if (game.scoreList.size() > 3) {
				Label p2label = new Label("Score: " + game.scoreList.get(3));
				scoreBoxLeft.getChildren().add(p2label);
				p2label.setTextAlignment(TextAlignment.CENTER);
				p2label.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");
			}
    	} 																				// We then place the last player
	    else {
	    	Label p4label = new Label("Score: " + game.scoreList.get(1));
			scoreBoxRight.getChildren().add(p4label);
			p4label.setTextAlignment(TextAlignment.CENTER);
			p4label.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");
    	}
    	
    	
    	textLayer.getChildren().add(scoreBoxLeft);
    	textLayer.getChildren().add(scoreBoxRight);
    	textLayer.getChildren().add(buttonBox);
 
    	if (storyMode) {
    		Button nextStory = new Button("Next");
    		nextStory.setMinWidth(250);
    		buttonBox.getChildren().add(nextStory);
    		if (!lost) {
    			level ++;
    		}								// In the case of story mode, we change the level depending on the game outcome
    		else {
    			level = -1;
    		}
    			
    		nextStory.setOnAction(new EventHandler<ActionEvent>() {
    		    @Override public void handle(ActionEvent e) {
    		    	initalizeStoryScene(primaryStage, level);			// The next button starts the next menu screen
    		    	bgMusicPlayer.stop();
    		    }
        	});
    	}
    	
    	Button buttonMenu = new Button("Back to Menu");
    	buttonMenu.setMinWidth(250);
    	buttonBox.getChildren().add(buttonMenu);					// The menu button shows in either mode and shows the main menu screen
    	buttonMenu.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	initalizeMainMenuScene(primaryStage);
		    	quitEnabled = false;
		    	bgMusicPlayer.stop();
		    }
    	});	
	}
	
	// We update the visuals of the non dynamic objects (The walls and warlords)
	private void generateStaticVariables(List<PlayerViewContainer> playerViewContainerList) {
		int i = 0;
		for (PlayerViewContainer temp : playerViewContainerList) {
			if (game.playerList.get(i).getPlayerType() != playerType.PLAYER_NONE) {
				temp.warlord.setXPos(game.playerList.get(i).warlord.getXPos());
				temp.warlord.setYPos(game.playerList.get(i).warlord.getYPos());
				for (int j = 0; j < 16; j++) {
					temp.wall[j].setXPos(game.playerList.get(i).walls[j].getXPos());
					temp.wall[j].setYPos(game.playerList.get(i).walls[j].getYPos());
					temp.wall[j].updateUI();
				}
				temp.warlord.updateUI();
				i++;
			}
		}
	}	 
	
	// This will generate the BG island sprites, depending on the number of players
	private void generateIslands(int players) {
		if (players > 4) {
			return;
		}
		
		int islandHeight = 272;
		int islandWidth = 352;
		
		islandImage = new Image(this.getClass().getResource("/img/island.png").toExternalForm()); // Create island image
		for (int i = 0; i < players; i++) {
			
			 switch (i) {
				case 0: // Top left
					SpriteBase islandSprite1 = new SpriteBase(islandLayer, islandImage, 100, 100);
					islandSprite1.setXPos(0);
					islandSprite1.setYPos(0);
					islandSprite1.rotate(0);		
					islandSprite1.updateUI();	
				break;
				case 1:  // Bottom right 
					SpriteBase islandSprite2 = new SpriteBase(islandLayer, islandImage, 100, 100);
					islandSprite2.setXPos(gameDisplayWidth - islandWidth);
					islandSprite2.setYPos(gameDisplayHeight - islandHeight);							// Place the sprites in the correct corner by flipping them as needed
					islandSprite2.flipX();
					islandSprite2.flipY();
					islandSprite2.updateUI();
				break;
				case 2: // top right
					SpriteBase islandSprite3 = new SpriteBase(islandLayer, islandImage, 100, 100);
					islandSprite3.setXPos(gameDisplayWidth - islandWidth);
					islandSprite3.setYPos(0);
					islandSprite3.flipX();
					islandSprite3.updateUI();	
				break;
				case 3: // bottom left
					SpriteBase islandSprite4 = new SpriteBase(islandLayer, islandImage, 100, 100);
					islandSprite4.setXPos(0);
					islandSprite4.setYPos(gameDisplayHeight - islandHeight);
					islandSprite4.flipY();
					islandSprite4.updateUI();	
				break;
			 }
		}
		
	}

	// This checks to see what inputs are detected by a listener and changes the paddle position accordingly
	public void checkPaddleMovement(InputListener listener, PlayerContainer player) {
		boolean up, down, right, left; 
		if (player == game.player1) {
			up = listener.moveUp();
			down = listener.moveDown();
			right = listener.moveRight();
			left = listener.moveLeft();
		} else {									// controls are dependent on which human player
			up = listener.p2moveUp();
			down = listener.p2moveDown();
			right = listener.p2moveRight();
			left = listener.p2moveLeft();
		}
		if (!player.warlord.isDead()) {
			if (up) {
				game.movePaddle(player, 1);			// We call the game movePaddle function based on the direction we are moving
			}
			if (down) {
				game.movePaddle(player, -1);
			}
		} else if (game.isGhosting()){
			if (up) {
				for (Ball temp : game.ballList) {
					temp.setYPos(temp.getYPos() + GHOST_STRENGTH);
				}
			}
			if (down) {
				for (Ball temp : game.ballList) {
					temp.setYPos(temp.getYPos() - GHOST_STRENGTH);			// The ghosting options allow the player to influence the ball on death
				}
			}
			if (right) {
				for (Ball temp : game.ballList) {
					temp.setXPos(temp.getXPos() + GHOST_STRENGTH);
				}
			}
			if (left) {
				for (Ball temp : game.ballList) {
					temp.setXPos(temp.getXPos() - GHOST_STRENGTH);
				}
			}
		}
		
	}
	
	public static void main(String[] args)
	{
		launch();	
	}

	
}
