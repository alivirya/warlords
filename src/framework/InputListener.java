package framework;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/* 
 * Creates a listener for key inputs, this should be polled every game tick to check for inputs
 */
public class InputListener {
	 private KeyCode pageDown = KeyCode.PAGE_DOWN;
	 private KeyCode p = KeyCode.P;
	 private KeyCode escape = KeyCode.ESCAPE;
	 private KeyCode left = KeyCode.LEFT;
	 private KeyCode right = KeyCode.RIGHT;
	 private KeyCode up = KeyCode.UP;
	 private KeyCode down = KeyCode.DOWN;
	 private KeyCode w = KeyCode.W;
	 private KeyCode a = KeyCode.A;
	 private KeyCode s = KeyCode.S;
	 private KeyCode d = KeyCode.D;
	 private Boolean leftClicked, rightClicked, upClicked, downClicked, pageDownClicked;
	 private Boolean pClicked, escapeClicked, wClicked, aClicked, sClicked, dClicked;
	 Scene scene;
	 
	 public InputListener(Scene scene) { // Default constructor, set all inputs to false
		 this.scene = scene;
		 pageDownClicked = false;
		 pClicked = false;
		 escapeClicked = false;
		 leftClicked = false;
		 rightClicked = false;
		 upClicked = false;
		 downClicked = false;
		 wClicked = false;
		 aClicked = false;
		 sClicked = false;
		 dClicked = false;
		 addListeners();
	 }
	 
	 public void addListeners() {   // Enable the listener
		 scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressed);
		 scene.addEventFilter(KeyEvent.KEY_RELEASED, keyReleased);
	 }
	 
	 public void removeListeners() { // Disable the listener
		 scene.removeEventFilter(KeyEvent.KEY_PRESSED, keyPressed);
		 scene.removeEventFilter(KeyEvent.KEY_RELEASED, keyReleased);
	 }
	 
	 //check what key is pressed, and handle the event accordingly
	 private EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {
	 	@Override
	 	public void handle (KeyEvent event) {
	 		/** Check if the associated key is pressed, and if so, set the variable to be true 
	 		 * 
	 		 */
	 		
	 		if (event.getCode().equals(left)) {
 				leftClicked = true;
 			}

 			if (event.getCode().equals(right)) {
 				rightClicked = true;
 			}
 			
 			if (event.getCode().equals(up) || (event.getCode().equals(up) && (event.getCode().equals(w) || event.getCode().equals(s)))) {
 				upClicked = true;
 			} 

 			if (event.getCode().equals(down) || (event.getCode().equals(down) && (event.getCode().equals(w) || event.getCode().equals(s)))) {
 				downClicked = true;
 			}

 			if (event.getCode().equals(a)) {
 				aClicked = true;
 			}

 			if (event.getCode().equals(d)) {
 				dClicked = true;
 			}

 			if (event.getCode().equals(w) || (event.getCode().equals(w) && (event.getCode().equals(up) || event.getCode().equals(down)))) {
 				wClicked = true;
 			} 

 			if (event.getCode().equals(s) || (event.getCode().equals(s) && (event.getCode().equals(up) || event.getCode().equals(down)))) {
 				sClicked = true;
 			}
 			
 			
 			if (event.getCode().equals(pageDown)) {
 				pageDownClicked = true;
 			}
 			if (event.getCode().equals(p)) {
 				pClicked = true;
 			}
 			if (event.getCode().equals(escape)) {
 				escapeClicked = true;
 			}
		}
	 };
	 
	 //set all variables to false if keys are released
	 private EventHandler<KeyEvent> keyReleased = new EventHandler<KeyEvent>() {
	 	@Override
	 	public void handle (KeyEvent event) {
	 		leftClicked = false;
			rightClicked = false;
			upClicked = false;
			downClicked = false;
			aClicked = false;
			sClicked = false;
			wClicked = false;
			dClicked = false;
			pClicked = false;
			pageDownClicked = false;
			escapeClicked = false;
		}
	};
	
	//set up variables, so that the controller of the game knows which button has been clicked
	public boolean moveRight() {
		return rightClicked;
	}
	
	public boolean moveLeft() {
		return leftClicked;
	}
	
	public boolean moveUp() {
		return upClicked;
	}

	public boolean moveDown() {
		return downClicked;
	}
	
	public boolean p2moveRight() {
		return dClicked;
	}
	
	public boolean p2moveLeft() {
		return aClicked;
	}
	
	public boolean p2moveUp() {
		return wClicked;
	}

	public boolean p2moveDown() {
		return sClicked;
	}
	
	public boolean pressEscape() {
		return escapeClicked;
	}
	public boolean pressPause() {
		return pClicked;
	}
	public boolean pressPageDown() {
		return pageDownClicked;
	}



}