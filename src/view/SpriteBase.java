package view;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/*
 *  This class is a visual 'sprite' or moveable image that represents and object from the game. It will be tracked to the position of a game object via the view.
 */
public class SpriteBase {
	//Set up the variables of the sprite
	public int height;
	public int width;
	public int positionX;
	public int positionY;
	Pane layer;
	Image image;
	ImageView imageView, decalImage;

	public SpriteBase(Pane layer, Image image, int positionX, int positionY) {
		this.positionX = positionX;				// Set the position to that provided
		this.positionY = positionY;
		this.width = 10;
		this.height = 10;			 // default values, these should be updated to the side of the object being represented
		this.layer = layer;
		this.image = image;
		this.imageView = new ImageView(image);   // Create the visual image from the path provided
		this.decalImage = null;
		
		addToLayer();
	}
	
	//Set the width of the sprite
    public void setWidth(int w) {
    	width = w;
    }
    
    //set the height of the sprite
    public void setHeight(int h) {
    	height = h;
    }
    
    //add the sprite to the layer
    public void addToLayer() {
        this.layer.getChildren().add(this.imageView);
    }

    //remove the sprite from the layer
    public void removeFromLayer(boolean remove) {
    	if (remove) {
    		this.layer.getChildren().remove(this.imageView);
        	if(decalImage != null) {
        		this.layer.getChildren().remove(this.decalImage);
        	}
    	}
    	
    }

    //get the layer of the sprite
    public Pane getLayer() {
        return layer;
    }

    //set the layer of the sprite
    public void setLayer(Pane layer) {
        this.layer = layer;
    }
	
    //Set the x location of the sprite
    public void setXPos(int x) {
    	positionX = x;
    }
    
    //Set the y position of the sprite
    public void setYPos(int y) {
    	positionY = y;
    }
    
    //retrieve the X position of the sprite
    public int getXPos() {
    	return positionX;
    }
    
    //retrieve the y position of the sprite
    public int getYPos() {
    	return positionY;
    }
    
    //set a decal image to the sprite. A decal is an extra image layered on top for the purposes of decoration
    public void setDecalImage(Image image) {
    	removeDecal();
    	this.decalImage = new ImageView(image);
    	addDecalToLayer();
    	updateUI();
    }
    
    //add the decal image to the layer
    public void addDecalToLayer() {
    	this.layer.getChildren().add(this.decalImage);
    }
    
    //remove the decal image from the layer
    public void removeDecal() {
    	if(decalImage != null) {
    		this.layer.getChildren().remove(this.decalImage);
    	}
    }
    
    //rotate the sprite
    public void rotate(int rotation) {
    	imageView.setRotate(rotation);
    }
    
    //flip the spirte across the x axis
    public void flipX() {
    	imageView.setScaleX(-1);
    }
    
    //flip the sprite across the y axis
    public void flipY() {
    	imageView.setScaleY(-1);
    }

    //update the sprite location
    public void updateUI() {
        imageView.relocate(positionX, positionY);
    	if(decalImage != null) {
    		decalImage.relocate(positionX, positionY);
    	}
    }


}
