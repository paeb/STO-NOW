import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Header here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Header extends MapElement
{
    /**
     * Act - do whatever the Header wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private GreenfootImage image = null;
    private int xLength = 1;
    private int xPos = 106;
    private boolean completed = false;

    public Header() {
        image = getImage();
        image.setTransparency(100);
        image.scale(1, 100);
    }

    public void act() 
    {
        if (xPos <= 320) {
            xLength += 8;
            xPos += 4;
        }
        else {
            completed = true;
        }

        image.scale(xLength, 100);
        this.setLocation(xPos, 76);
    }    
    public boolean isFinished() {
        return completed;
    }
}
