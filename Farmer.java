import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @Brandon Pae
 * @8.9.2020
 */
public class Farmer extends Actor
{
    //arrays for the sprites
    private GreenfootImage[] leftArray = new GreenfootImage[3];
    private GreenfootImage[] rightArray = new GreenfootImage[3];
    private GreenfootImage[] frontArray = new GreenfootImage[3];
    private GreenfootImage[] backArray = new GreenfootImage[3];

    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean movingForward = false;
    private boolean movingBackward = false;

    private boolean hasUpdatedImage = true;

    private boolean goToNextIteration = true;

    private int farmerSpeed = 3;
    private boolean isVisible = true;
    private double scaleFactor = 1.82278;
    private String lastPos = "front";
    
    private double currency = 200; //start with 200 dollars
    private int numPickaxe = 1; //start with 1 pickaxe
    private int numWheat = 0; //start with 0 wheats
    private int numWheatSeeds = 5; //start with 5 wheat seeds

    private int numInfectedWheat = 0; //start with 0 infected wheats
    private int numPesticide = 5; //start with 5 pesticide sprays
    private int numDrone = 0; //start with 0 drones
    private int numFertilizer = 10; //start with 15 fertilizer sprays
    
    private int numImprovedDrone = 0;
    private int numRobot = 0;
    private int numController = 0;
    
    private int i = 0;
    
    private int x = 0;
    private int y = 0;

    public Farmer(String pos) { //starting position
        //we use loops to assign each index of the imageArray to a farmer image
        for (int i = 0; i < 3; i++) {
            this.rightArray[i] = new GreenfootImage("RCharacter_rpg-" + i + ".png");
            rightArray[i].scale(25, (int)(25 * scaleFactor));
        }
        for (int i = 0; i < 3; i++) {
            this.backArray[i] = new GreenfootImage("BCharacter_rpg-" + i + ".png");
            backArray[i].scale(25, (int)(25 * scaleFactor));
        }
        for (int i = 0; i < 3; i++) {
            this.frontArray[i] = new GreenfootImage("FCharacter_rpg-" + i + ".png");
            frontArray[i].scale(25, (int)(25 * scaleFactor));
        }
        for (int i = 0; i < 3; i++) {
            this.leftArray[i] = new GreenfootImage("LCharacter_rpg-" + i + ".png");
            leftArray[i].scale(25, (int)(25 * scaleFactor));
        }
        GreenfootImage image = null;
        switch (pos) {
            case "front":
            image = frontArray[0];
            break;
            case "back":
            image = backArray[0];
            break;
            case "left":
            image = leftArray[0];
            break;
            case "right":
            image = rightArray[0];
            break;
        }
        setImage(image);
    }

    public void act() 
    {
        controlMovement();
        animateSprites();
    }

    public void controlMovement()
    {
        movingLeft = false;
        movingRight = false;
        movingForward = false;
        movingBackward = false;

        if (Greenfoot.isKeyDown("right") || Greenfoot.isKeyDown("d")) {
            setLocation(getX()+farmerSpeed, getY()); //moves farmer to the right
            lastPos = "right";
            movingRight = true;
        }
        else if (Greenfoot.isKeyDown("left") || Greenfoot.isKeyDown("a")) {
            setLocation(getX()-farmerSpeed, getY()); //moves the farmer to the left
            lastPos = "left";
            movingLeft = true;
        }
        else if (Greenfoot.isKeyDown("down") || Greenfoot.isKeyDown("s")) {
            setLocation(getX(), getY()+farmerSpeed); //moves the farmer down 
            lastPos = "front";
            movingForward = true;
        }
        else if (Greenfoot.isKeyDown("up") || Greenfoot.isKeyDown("w")) {
            setLocation(getX(), getY()-farmerSpeed); //moves the farmer up
            lastPos = "back";
            movingBackward = true;
        }   
    }

    public void animateSprites() {
        if (movingRight) { //moving right
            if (hasUpdatedImage) {
                i++;
                if (i == 3) { //wrap around the index, from 3 to 0
                    i = 0;
                }
                setImage(rightArray[i]);
                hasUpdatedImage = false;
            }     
            if (getImage() == rightArray[i]) { //if the picture has been updated, then we can show the next
                hasUpdatedImage = true;
            }   
        }  
        else if (movingLeft) { //moving left
            if (hasUpdatedImage) {
                i++;
                if (i == 3) { //wrap around, from 3 to 0
                    i = 0;
                }
                setImage(leftArray[i]);
                hasUpdatedImage = false;
            }     
            if (getImage() == leftArray[i]) { //if the picture has been updated, then we can show the next
                hasUpdatedImage = true;
            }   
        }  
        else if (movingForward) { //moving forward
            if (hasUpdatedImage) {
                i++;
                if (i == 3) { //wrap around, from 3 to 0
                    i = 0;
                }
                setImage(frontArray[i]);
                hasUpdatedImage = false;
            }     
            if (getImage() == frontArray[i]) { //if the picture has been updated, then we can show the next
                hasUpdatedImage = true;
            }   
        }
        else if (movingBackward) { //moving backward
            if (hasUpdatedImage) {
                i++;
                if (i == 3) { //wrap around, from 3 to 0
                    i = 0;
                }
                setImage(backArray[i]);
                hasUpdatedImage = false;
            }     
            if (getImage() == backArray[i]) { //if the picture has been updated, then we can show the next
                hasUpdatedImage = true;
            }   
        }  
        else { //set the final image
            if (lastPos.equals("right")) {
                this.setImage(rightArray[1]);
            }    
            else if (lastPos.equals("left")) {
                this.setImage(leftArray[2]);
            }    
            else if (lastPos.equals("front")) {
                this.setImage(frontArray[0]);
            }
            else {
                this.setImage(backArray[0]);
            }   
        }
    }   

    public boolean isTouchingTile() {
        return isTouching(Tile.class);
    }   

    public String getLastPos() {
        return lastPos;
    }   

    public void setNumPickaxe(int num) {
        numPickaxe = num;
    }

    public int getNumPickaxe() {
        return numPickaxe;
    }

    public double getCurrency() {
        return currency;
    }

    public void setCurrency(double currency) {
        this.currency = currency;
    }

    public void setNumWheat(int num) {
        numWheat = num;
    }

    public int getNumWheat() {
        return numWheat;
    }

    public void setNumWheatSeeds(int num) {
        numWheatSeeds = num;
    }

    public int getNumWheatSeeds() {
        return numWheatSeeds;
    }
    
    public void setNumInfectedWheat(int num) {
        numInfectedWheat = num;
    }

    public int getNumInfectedWheat() {
        return numInfectedWheat;
    }
    
    public void setNumPesticide(int num) {
        numPesticide = num;
    }
    
    public int getNumPesticide() {
        return numPesticide;
    }
    
    public void setNumDrone(int num) {
        numDrone = num;
    }
    
    public int getNumDrone() {
        return numDrone;
    }
    
    public void setNumImprovedDrone(int num) {
        numImprovedDrone = num;
    }
    
    public int getNumImprovedDrone() {
        return numImprovedDrone;
    }
    
    public void setNumFertilizer(int num) {
        numFertilizer = num;
    }
    
    public int getNumFertilizer() {
        return numFertilizer;
    }
    
    public void setNumRobot(int num) {
        numRobot = num;
    }
    
    public int getNumRobot() {
        return numRobot;
    }
    
    public void setNumController(int num) {
        numController = num;
    }
    
    public int getNumController() {
        return numController;
    }
}
