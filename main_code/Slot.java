import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Box here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Slot extends MapElement
{
    /**
     * Act - do whatever the Box wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private int x = 0;
    private int y = 0;
    
    public void act() 
    {
    }   
    
    public void setXCoordinate(int x) {
        this.x = x;
    }
    
    public void setYCoordinate(int y) {
        this.y = y;
    }
    
    public int getXCoordinate() {
        return this.getX();
    }
    
    public int getYCoordinate() {
        return this.getY();
    }
}
