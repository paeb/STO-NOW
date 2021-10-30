import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ImprovedDrone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ImprovedDrone extends Advanced
{
    /**
     * Act - do whatever the ImprovedDrone wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    private Propeller propeller1;
    private Propeller propeller2;
    private Propeller propeller3;
    private Propeller propeller4;
    private Propeller propeller5;
    private Propeller propeller6;
    private int droneX = 300;
    private int droneY = 217;
    private boolean added = false;
    
    public ImprovedDrone() {
        propeller1 = new Propeller();
        propeller2 = new Propeller();
        propeller3 = new Propeller();
        propeller4 = new Propeller();
        propeller5 = new Propeller();
        propeller6 = new Propeller();
    }
    
    public void act() 
    {
        droneX = this.getX();
        droneY = this.getY();
        
        if (!added) {
            getWorld().addObject(propeller1,droneX + 12,droneY - 20);
            getWorld().addObject(propeller2,droneX + 23,droneY - 1);
            getWorld().addObject(propeller3,droneX + 12,droneY + 20);
            getWorld().addObject(propeller4,droneX - 12,droneY + 20);
            getWorld().addObject(propeller5,droneX - 23,droneY - 2);
            getWorld().addObject(propeller6,droneX - 12,droneY - 21);
            added = true;
        }

        propeller1.setLocation(droneX + 12,droneY - 20);
        propeller2.setLocation(droneX + 23,droneY - 1);
        propeller3.setLocation(droneX + 12,droneY + 20);
        propeller4.setLocation(droneX - 12,droneY + 20);
        propeller5.setLocation(droneX - 23,droneY - 2);
        propeller6.setLocation(droneX - 12,droneY - 21);
    }    
    
    public void show() {
        getImage().setTransparency(255);
        propeller1.getImage().setTransparency(255);
        propeller2.getImage().setTransparency(255);
        propeller3.getImage().setTransparency(255);
        propeller4.getImage().setTransparency(255);
        propeller5.getImage().setTransparency(255);
        propeller6.getImage().setTransparency(255);
    }
    
    public void hide() {
        getImage().setTransparency(0);
        propeller1.getImage().setTransparency(0);
        propeller2.getImage().setTransparency(0);
        propeller3.getImage().setTransparency(0);
        propeller4.getImage().setTransparency(0);
        propeller5.getImage().setTransparency(0);
        propeller6.getImage().setTransparency(0);
    }
}
