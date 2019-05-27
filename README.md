# README #

### What is this repository for? ###

* Warlords

### To run the game ###

* If you know what to do, the main class is in GameStage.java in the view package, otheriwse see below:

* Clone this git repository
* Run Eclipse Java and set the workspace to the parent folder containing "warlords"
* Go file > import then under 'general' choose "Import existing project into workspace". 
* Under "Select root directory" choose the "warlords" folder and click OK, then Finish
* Expand the project on the explorer on the left hand side, then expand 'src'
* Expand the view Package and right click on the "GameStage" and click: 
		Run As > Run Configurations
* On the left under "Java Applicatiion" choose "GameStage". If it doesn't exist, just double click on "Java Application" at it should appear.
* Ensure it says "view.GameStage" in the "Main Class:" box
* Next select the JRE tab at the top of this window
* Select "Installed JREs"
* Click 'add' and select 'Standard VM'
* Click directory and navigate to /usr/lib/jvm/jdk1.8.0_91 and go OK
* Click finish and ensure the JDK is selected

* You can make sure Java FX is installed by going to the eclipse marketplace located in the 'help' menu and installing "e(fx)clipse"

* Finally, run the project by right clicking the 'view' package and going:
		run as > java application

### To run unit tests ###

* Right click the package 'warlordsTest' and select:
		Run As > Run Configurations
* On the left, under "JUnit" choose "warlordsTest". If it doesn't exist, just double click on "JUnit" at it should appear.
* Ensure JUnit 4 is selected as the "Test Runner"
* Click run, the results are shown on the left

### Navigating Menus ###

* You can use the mouse to click options, otherwise, use the arrow keys to interact and the space bar to select

### Main Menu Options ###

- CUSTOM MODE 

	* In this menu you can select the properties for a game

	* The first slider indicates the number of human players for the game. 0 means it will be entirely  AI. 1 means one human in the top left.
	* 2 means 2 human players, the second in the bottom right. See the "Controls" section of this document for more info.

	* The second slider controls the AI players that fill the places not used by human players in the order of : Bottom Right, Top Right, Bottom left.

	* There must be at least 2 players and at max 4. Any combo of player types is otherwise allowed.

	* Ball speed allows you to change the ball speed. A faster ball corresponds to a harder difficulty

	* "Ghosting" allows you to exert influence on the ball if you die (Human Players Only). See the "Controls" section of this document for more info.

- STORY MODE 

	* This is a single player experience controlled by one human player in the top left. Story chapters can be left and returned to from the main menu

- MORE OPTIONS 

	* This allows you to select a resolution (Up to 2560x1440) for the gameplay. This does not affect menus. The change will shown on the next match.
	* We reccomend a faster ball speed for a bigger resolution in custom games

- QUIT

	* Stops the game

### Game info ###

* The game is set in the late 17th century Caribbean, the player takes control of a pirate ship defending an island fort from a cannonball.  
* You can win by having the most walls left at the end of the time or by being the last fort left standing. (Draws do not count as victories in story mode)
* There are 4 Nations in the game: Pirates, Spain, Britain, and France.
* Some of these nations have bonuses. Pirates have a faster paddle, British warlords can take 2 hits, and some Spanish walls are reinforced. 
* By default your walls can take 2 hits, but watch out, your warlord can die after one hit!

### Controls ###

- DURING GAMEPLAY
	* "Page Down" Skips to the end of the game time
	* "Escape" allows you to go back to the menu, resume or quit
	* "P" pauses the game, pressing it again unpauses the game

- PLAYER 1
	* Situated in the top left. "Arrow key up" and "Arrow key down" move your ship in an L shape around the island.
	* If you die and Ghosting is enabled, the 4 arrow keys can be used to influence the ball speed.

- PLAYER 2
	* Situated in the bottom right. "W" and "S" move your ship in an L shape around the island.
	* If you die and Ghosting is enabled, the  "W, A, S, D" can be used to influence the ball speed.
