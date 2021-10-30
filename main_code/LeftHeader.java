import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class LeftHeader here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LeftHeader extends MapElement
{
    /**
     * Act - do whatever the LeftHeader wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private GreenfootImage image = null;
    private int xLength = 1;
    private int xPos = 0;
    private boolean completed = false;
    private boolean lowerBanner = false;
    private boolean raiseBanner = false;
    private boolean bannerLowered = true;
    private boolean bannerRaised = false; //starts up
    private boolean goToNextIteration = false;
    private int yLength = 410;

    public LeftHeader() {
        image = getImage();
        image.setTransparency(127);
        image.scale(1, yLength);
    }

    public void act() 
    {
        if (raiseBanner) {
            bannerLowered = false;
            if (xPos <= 25) {
                xLength += 1;
                if (goToNextIteration) {
                    xPos += 1;
                }
                goToNextIteration = !goToNextIteration;
            }
            else {
                bannerRaised = true;
            }
            image.scale(xLength, yLength);
            this.setLocation(xPos, 218);
        }
        else if (lowerBanner) {
            bannerRaised = false;
            if (xPos > 0) {
                xLength -= 1;
                if (goToNextIteration) {
                    xPos -= 1;
                }
                goToNextIteration = !goToNextIteration;
            }
            else {
                bannerLowered = true;
            }
            image.scale(xLength, yLength);
            this.setLocation(xPos, 218);
        }
    }

    public boolean isFinished() {
        return completed;
    }

    public int getEndY() {
        return (int)(xLength / 2 + xPos);
    }

    public void lowerBanner() {
        lowerBanner = true;
        raiseBanner = false;
    }
    
    public void raiseBanner() {
        raiseBanner = true;
        lowerBanner = false;
    }
    
    public boolean bannerLowered() {
        return bannerLowered;
    }
    
    public boolean bannerRaised() {
        return bannerRaised;
    }
}
