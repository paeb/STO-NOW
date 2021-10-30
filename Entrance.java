import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Entrance here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Entrance extends MapElement
{
    /**
     * Act - do whatever the Entrance wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private GreenfootImage image = null;
    public Entrance() {
        image = getImage();
        image.scale(50, 75);
    }
    public void act() 
    {
        // Add your action code here.
    }    
}
