import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Settings here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Settings extends Actor
{
    //for key variables that are referenced by multiple classes or are quickly changed
    
    public static final int currencyThreshold1 = 300;
    public static final int currencyThreshold2 = 600;
    public static final int currencyThreshold3 = 900;
    
    //in seconds
    public static final int initialPhaseTime1 = 30;
    public static final int criticalPhaseTime1 = 60;
    public static final int initialPhaseTime2 = 30;
    public static final int criticalPhaseTime2 = 60;
    public static final int initialPhaseTime3 = 30;
    public static final int criticalPhaseTime3 = 60;
    public static final int pickaxeUseThreshold = 300;
    
    public static final long timeDelay1_threshold = 3; //delay from Round 1 to Round 2 transition
    public static final long timeDelay2_threshold = 3; //delay from Round 2 to Round 3 transition
    public static final long timeDelay3_threshold = 3; //delay from Round 3 to Intro transition
    
    public void act() 
    {
        // Add your action code here.
    }    
}
