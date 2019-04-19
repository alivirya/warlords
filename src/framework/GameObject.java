package framework;

import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

/**
 *  Abstract class that is inherited by all game objects
 */
abstract public class GameObject {
	
	//initialize variables related to all objects
	public int positionX;
	public int positionY;
	public int height;
	public int width;
	public int previousX;
	public int previousY;
	
	public Rectangle rectangle;
	

	
	//set up a Constructor which sets up the the values of the variables
	public GameObject(int width, int height, int positionX, int positionY) {
		this.width = width;
		this.height = height;
		this.positionX = positionX;
		this.positionY = positionY;
		previousX = positionX;
		previousY = positionY;
		rectangle = new Rectangle(positionX, positionY, width, height);	
	}

	
    /***
     *  Set the horizontal position of the object to the given value.
     * @param x
     */
    public void setXPos(int x) {
    	previousX = positionX;
    	positionX = x;
    	rectangle.setX(positionX);
    }
    /***
     *  Set the vertical position of the object to the given value.
     * @param y
     */
    public void setYPos(int y) {
    	previousY = positionY;
    	positionY = y;
    	rectangle.setY(positionY);
    }
    
    /***
     * @return the x position of the object.
     */
    public int getXPos() {
    	return positionX;
    }
    
    /***
     * @return the Y position of the object.
     */
    public int getYPos() {
    	return positionY;
    }
    /***
     *  Set the vertical height of the object to the given value.
     * @param height
     */
    public void setHeight(int newHeight) {
    	 height = newHeight;
    	 rectangle.setHeight(height);
    }
    
    /***
     *  Set the width of the object to the given value.
     * @param width
     */
    public void setWidth(int newWidth) {
    	width = newWidth;
    	rectangle.setWidth(width);
    }
    
    /***
     * @return the height of the object.
     */
    public int getHeight() {
    	return height;
    }
    
    /***
     * @return the Width of the object.
     */
    public int getWidth() {
    	return width;
    }
    
    /***
     * @return the Bounds of the object.
     */
    public Bounds getBounds() {
    	return (rectangle.getBoundsInParent());
    }
    
    

}