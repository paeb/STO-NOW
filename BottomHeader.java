import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class TopHeader here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BottomHeader extends MapElement
{
    /**
     * Act - do whatever the TopHeader wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private GreenfootImage image = null;
    //private int yLength = 1;
    //private int yPos = 435;
    private int yLength = 47;
    private int yPos = 412;
    private boolean completed = false;
    private boolean lowerBanner = false;
    private boolean raiseBanner = false;
    private boolean bannerLowered = false;
    private boolean bannerRaised = true; //starts up
    private boolean goToNextIteration = false;

    public BottomHeader() {
        image = getImage();
        image.setTransparency(127);
        image.scale(565, yLength);
    }

    public void act() 
    {
        if (raiseBanner) {
            bannerLowered = false;
            if (yPos >= 413) {
                yLength += 1;
                if (goToNextIteration) {
                    yPos -= 1;
                }
                goToNextIteration = !goToNextIteration;
            }
            else {
                bannerRaised = true;
            }
            image.scale(565, yLength);
            this.setLocation(282, yPos);
        }
        else if (lowerBanner) {
            bannerRaised = false;
            if (yPos <= 434) {
                yLength -= 1;
                if (goToNextIteration) {
                    yPos += 1;
                }
                goToNextIteration = !goToNextIteration;
            }
            else {
                bannerLowered = true;
            }
            image.scale(565, yLength);
            this.setLocation(282, yPos);
        }
        //getWorld().showText(yPos + "", 100, 210); //412
        //getWorld().showText(yLength + "", 200, 250); //47
    }

    public boolean isFinished() {
        return completed;
    }

    public int getEndY() {
        return (int)(yLength / 2 + yPos);
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
