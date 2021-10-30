import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Propeller here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Propeller extends Advanced
{
    /**
     * Act - do whatever the Propeller wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    private int angle = 0;
    
    public void act() 
    {
        setRotation(angle);
        angle += 30;
    }    
}
