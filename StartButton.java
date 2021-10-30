import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class StartButton here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StartButton extends MapElement
{
    /**
     * Act - do whatever the StartButton wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public StartButton() {
        GreenfootImage image = getImage();
        image.scale(175, 75);
        /**
        image.setFont(new Font("Times New Roman", true, false, 24)); //bold, italic
        image.drawString("Start", 75, 55);
        */
    }    
    public void act() 
    {
        // Add your action code here.
    }    
}
