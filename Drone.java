import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Drone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Drone extends Advanced
{
    /**
     * Act - do whatever the Drone wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private GreenfootImage image;
    private int angle = 0;
    
    public Drone() {
    }    
    
    public void act() {
        setRotation(angle);
        angle += 10;
    }    
    
    public boolean intersects(Actor object) {
        boolean intersects = this.intersects(object);
        return intersects;
    }
}
