import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @Brandon Pae
 * @8.9.2020
 */
public class Bar extends Actor
{
    /**
     * Act - do whatever the Bar wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private GreenfootImage image = null;
    private final int barHeight = 3;
    private final int barLength = 30;
    private double percentHealth = 100;
    private int xFilled = 0;
    
    public Bar() {
        image = getImage();
        image.scale(barLength, barHeight);
        image.setColor(Color.GREEN);
        setImage(image);
    }
    
    public void act() 
    {
        int x = this.getX();
        int y = this.getY();
        xFilled = (int)((percentHealth / 100.0) * barLength);
        
        image.setColor(Color.GREEN);
        image.fillRect(0, 0, xFilled, barHeight);
        image.setColor(Color.RED);
        image.fillRect(xFilled, 0, barLength - xFilled, barHeight);
    }    
    public void setPercentHealth(double percent) {
        percentHealth = percent;
    }   
    public void pesticideBar() {
        
    }
}
