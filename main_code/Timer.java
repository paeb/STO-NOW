
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Timer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Timer extends Actor
{
    /**
     * Act - do whatever the Timer wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private int timer = 0;
    private long startTime;
    private boolean hasResetTimer = false;
    private boolean goToNextIteration = false;
    private boolean onInitPhase1 = true;
    private boolean onCritPhase1 = false;
    private boolean onInitPhase2 = true;
    private boolean onCritPhase2 = false;
    private boolean onInitPhase3 = true;
    private boolean onCritPhase3 = false;
    private int initialPhaseTime1; //seconds
    private int criticalPhaseTime1;
    private int initialPhaseTime2; //seconds
    private int criticalPhaseTime2;
    private int initialPhaseTime3; //seconds
    private int criticalPhaseTime3;
    private long displayTime;
    private boolean onRound1 = true;
    private long totalSecondsElapsed;
    private long minutesElapsed;
    private long secondsElapsed;
    private long phaseTime;
    private Farmer farmer = null;
    private boolean timeReset = false;
    private int currencyThreshold1;
    private boolean finishedRound1 = false;
    private int currencyThreshold2;
    private boolean finishedRound2 = false;
    private boolean finishedRound3 = false;
    private int currencyThreshold3;

    //keeps track of the time and rounds
    public Timer() {
        startTime = System.currentTimeMillis(); //set the current time
        currencyThreshold1 = Settings.currencyThreshold1;
        currencyThreshold2 = Settings.currencyThreshold2;
        currencyThreshold3 = Settings.currencyThreshold3;
        initialPhaseTime1 = Settings.initialPhaseTime1;
        criticalPhaseTime1 = Settings.criticalPhaseTime1;
        initialPhaseTime2 = Settings.initialPhaseTime2;
        criticalPhaseTime2 = Settings.criticalPhaseTime2;
        initialPhaseTime3 = Settings.initialPhaseTime3;
        criticalPhaseTime3 = Settings.criticalPhaseTime3;
        //this.farmer = farmer;
    }
    
    public Timer(int val) {
        startTime = System.currentTimeMillis(); //set the current time
        currencyThreshold1 = Settings.currencyThreshold1;
        currencyThreshold2 = Settings.currencyThreshold2;
        currencyThreshold3 = Settings.currencyThreshold3;
        initialPhaseTime1 = Settings.initialPhaseTime1;
        criticalPhaseTime1 = Settings.criticalPhaseTime1;
        initialPhaseTime2 = Settings.initialPhaseTime2;
        criticalPhaseTime2 = Settings.criticalPhaseTime2;
        initialPhaseTime3 = Settings.initialPhaseTime3;
        criticalPhaseTime3 = Settings.criticalPhaseTime3;
        
        if (val == 2) { //on round 2
            finishedRound1 = true;
            onInitPhase1 = false;
            onCritPhase1 = false;
            onInitPhase2 = true;
            onCritPhase2 = false;
        }
        else if (val == 3) { //on round 3
            finishedRound1 = true;
            finishedRound2 = true;
            onInitPhase1 = false;
            onCritPhase1 = false;
            onInitPhase2 = false;
            onCritPhase2 = false;
            onInitPhase3 = true;
            onCritPhase3 = false;
        }
    }

    public void act()
    {
        totalSecondsElapsed = (System.currentTimeMillis() - startTime) / 1000; //total seconds that have passed
        //since the last start time
        
        countdownTimer();

        displayTime = phaseTime - totalSecondsElapsed; //for display purposes, to show
        //the time as a countdown from the total time

        minutesElapsed = displayTime / 60; //minutes to display
        secondsElapsed = displayTime % 60; //seconds to display

    }

    private void countdownTimer() {
        if (onInitPhase1) {
            this.setPhaseTime(initialPhaseTime1);
        }
        else if (onCritPhase1) {
            this.setPhaseTime(criticalPhaseTime1);
        }
        else if (onInitPhase2) {
            this.setPhaseTime(initialPhaseTime2);
        }
        else if (onCritPhase2) {
            this.setPhaseTime(criticalPhaseTime2);
        }
        else if (onInitPhase3) {
            this.setPhaseTime(initialPhaseTime3);
        }
        else if (onCritPhase3) {
            this.setPhaseTime(criticalPhaseTime3);
        }

        if (!finishedRound1) { //on Round 1
            if (onInitPhase1 && totalSecondsElapsed > initialPhaseTime1 && !timeReset) { //after enough seconds have passed
                onInitPhase1 = false;
                onCritPhase1 = true;
                this.setStartTime(System.currentTimeMillis());
                timeReset = true;

                totalSecondsElapsed = (System.currentTimeMillis() - startTime) / 1000; //need to reset total seconds elapsed
            }
            else if (onCritPhase1 && totalSecondsElapsed > criticalPhaseTime1) { //if the user's total time is over
                onCritPhase1 = false;
                finishedRound1 = true;
                timeReset = false;
            }
        }
        else if (!finishedRound2) { //on Round 2
            if (onInitPhase2 && totalSecondsElapsed > initialPhaseTime2 && !timeReset) { //after enough seconds have passed
                onInitPhase2 = false;
                onCritPhase2 = true;
                this.setStartTime(System.currentTimeMillis());
                timeReset = true;

                totalSecondsElapsed = (System.currentTimeMillis() - startTime) / 1000; //need to reset total seconds elapsed
            }
            else if (onCritPhase2 && totalSecondsElapsed > criticalPhaseTime2) { //if the user's total time is over
                onCritPhase2 = false;
                finishedRound2 = true;
                timeReset = false;
            }
        }
        else if (!finishedRound3) { //on Round 3
            if (onInitPhase3 && totalSecondsElapsed > initialPhaseTime3 && !timeReset) { //after enough seconds have passed
                onInitPhase3 = false;
                onCritPhase3 = true;
                this.setStartTime(System.currentTimeMillis());
                timeReset = true;

                totalSecondsElapsed = (System.currentTimeMillis() - startTime) / 1000; //need to reset total seconds elapsed
            }
            else if (onCritPhase3 && totalSecondsElapsed > criticalPhaseTime3) { //if the user's total time is over
                onCritPhase3 = false;
                finishedRound3 = true;
                timeReset = false;
            }
        }
    }

    public long getMinutes() {
        return minutesElapsed;
    }

    public long getSeconds() {
        return secondsElapsed;
    }

    public void setStartTime(long time) {
        this.startTime = time;
    }

    public void setTotalSeconds(long time) {
        this.totalSecondsElapsed = time;
    }

    public long getTotalSeconds() {
        return totalSecondsElapsed;
    }

    public void setPhaseTime(long time) {
        this.phaseTime = time;
    }

    public void setOnInitPhase1(boolean onInit) {
        this.onInitPhase1 = onInit;
    }

    public void setOnCritPhase1(boolean onCrit) {
        this.onCritPhase1 = onCrit;
    }

    public void setOnInitPhase2(boolean onInit) {
        this.onInitPhase2 = onInit;
    }

    public void setOnCritPhase2(boolean onCrit) {
        this.onCritPhase2 = onCrit;
    }
    
    public void setOnInitPhase3(boolean onInit) {
        this.onInitPhase3 = onInit;
    }

    public void setOnCritPhase3(boolean onCrit) {
        this.onCritPhase3 = onCrit;
    }

    public boolean getOnInitPhase1() {
        return onInitPhase1;
    }

    public boolean getOnCritPhase1() {
        return onCritPhase1;
    }

    public boolean getOnInitPhase2() {
        return onInitPhase2;
    }

    public boolean getOnCritPhase2() {
        return onCritPhase2;
    }
    
    public boolean getOnInitPhase3() {
        return onInitPhase3;
    }

    public boolean getOnCritPhase3() {
        return onCritPhase3;
    }

    public int getCurrencyThreshold1() {
        return currencyThreshold1;
    }

    public int getCurrencyThreshold2() {
        return currencyThreshold2;
    }
    
    public int getCurrencyThreshold3() {
        return currencyThreshold3;
    }

    public boolean finishedRound1() {
        return finishedRound1;
    }

    public boolean finishedRound2() {
        return finishedRound2;
    }
    
    public boolean finishedRound3() {
        return finishedRound3;
    }
    
    public long getAbsoluteSeconds() {
        return System.currentTimeMillis() / 1000;
    }
}
