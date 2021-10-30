import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class PesticideTile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PesticideTileBorder extends MapElement
{
    /**
     * Act - do whatever the PesticideTileBorder wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    private boolean isClicked = false;
    
    public PesticideTileBorder() {
    }
    
    public void act() 
    {
        setMouseClicked(); //check if this actor is clicked
    }    
    private void setMouseClicked() {
        if (Greenfoot.mouseClicked(this)) {
            isClicked = true;
        }
        else{
            isClicked = false;
        }
    }
    public boolean isClicked() {
        return isClicked;
    }
}
