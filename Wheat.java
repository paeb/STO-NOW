import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Wheat here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wheat extends Crop
{
    /**
     * Act - do whatever the Wheat wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    //private boolean infected = false;
    
    public Wheat() {
        GreenfootImage image = getImage();
        image.scale(25, (int)(25 * 1.39));
        setImage(image);
    }    
    public void act() {
    }    
    public Tile getTile() {
        return (Tile)getOneIntersectingObject(Tile.class);
    }
}
