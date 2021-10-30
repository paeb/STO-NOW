import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math.*;
import java.util.List;
/**
 * @Brandon Pae
 * @9/21/20
 */
public class Round2 extends Farm
{

    /**
     * Never Used
     */
    public Round2()
    {
        // Create a new world with 565x435 cells with a cell size of 1x1 pixels.
        super();
        timer = new Timer();
        prepare();
    }

    /**
     * Constructor for transitioning between Round 1 to Round 2
     * @param previous Round 1 instance
     */
    public Round2(Round1 farm) {
        super();
        timer = new Timer(2); //since we are now on round2, use a slightly different constructor
        updateData(farm);
        prepare();
    }

    /**
     * Constructor for transitioning between Round 2 and Shop
     * @param previous saved Round 2 instance
     */
    public Round2(Round2 farm) {
        super();
        updateData(farm);
        prepare();
    }

    public void act() {
        farmer = (Farmer)this.getObjects(Farmer.class).get(0); //get the farmer object in the game

        farmerX = farmer.getX(); //farmer's x position
        farmerY = farmer.getY(); //farmer's y position

        lastPos = farmer.getLastPos(); //get the last recorded sprite of the farmer (up/down, left/right)

        //shows pickaxe swings
        animatePickaxe();

        //shows pesticide equipping
        equipPesticide();

        //sprays pesticide to tiles
        sprayPesticide();

        //tile + pickaxe interaction
        pickaxeTile();

        //drone behaviors
        equipDrone();

        activateDrone();

        //press m to show/hide shop
        adjustShopTransparency();

        if (Greenfoot.mouseClicked(shopButton)) {
            Greenfoot.setWorld(new Shop2_1(this));
        }

        //handles the countdown timer
        countdownTimer();

        //handles planting seeds (press key 2)
        equipWheatSeed();
        
        plantWheatSeeds();

        //makes sure that the farmer cannot pass through the fence
        collisionControl();

        //update the list of tiles
        updateInfertileTiles();

        //changing the inventory colors based on whether the tools are equipped
        adjustSlotColors();

        //set the previous sprite position of farmer
        prevPos = farmer.getLastPos();
    }    

    private void countdownTimer() {
        totalSecondsElapsed = timer.getTotalSeconds();
        secondsElapsed = timer.getSeconds();
        minutesElapsed = timer.getMinutes();

        onInitPhase = timer.getOnInitPhase2();
        onCritPhase = timer.getOnCritPhase2();

        if (onInitPhase) {
            img = new GreenfootImage(500, 100);
            img.setColor(Color.CYAN);
            img.setFont(new Font("Arial", false, false , 30));
            img.setTransparency(255);

            if (secondsElapsed < 10) {//from :01 to:09
                img.drawString("ROUND 2" + "     " + "INIT PHASE-" + minutesElapsed + ":" + "0" + secondsElapsed, 0, 50);
            }
            else {//from :10+
                img.drawString("ROUND 2" + "     " + "INIT PHASE-" + minutesElapsed + ":" + secondsElapsed, 0, 50);
            }

            timeText.setImage(img);
            this.addObject(timeText, 335, 30);
        }

        if (onCritPhase) {
            initialInsects();
            if (topHeader.bannerLowered()) {
                img = new GreenfootImage(500, 100);
                img.setColor(Color.RED);
                img.setFont(new Font("Arial", false, false , 30));
                img.setTransparency(255);

                if (secondsElapsed < 10) {//from :01 to:09
                    img.drawString("ROUND 2" + "     " + "CRIT PHASE-" + minutesElapsed + ":" + "0" + secondsElapsed, 0, 50);
                }
                else {//from :10+
                    img.drawString("ROUND 2" + "     " + "CRIT PHASE-" + minutesElapsed + ":" + secondsElapsed, 0, 50);
                }

                timeText.setImage(img);
                this.addObject(timeText, 335, 30);
            }
            else {
                this.removeObject(timeText);
            }
        }

        if (timer.finishedRound2()) { //if the user's time is over
            if (topHeader.bannerLowered()) {
                img = new GreenfootImage(500, 100);
                //img.setColor(Color.CYAN);
                img.setFont(new Font("Arial", false, false , 30));
                img.setTransparency(255);

                if (farmer.getCurrency() >= timer.getCurrencyThreshold2()) {
                    img.setColor(Color.GREEN);
                    img.drawString("ROUND 2 PASSED", 75, 50);
                    roundNumber = 3; //change it to the next round
                    
                    if (!hasSetTimeDelay2) {
                        timeDelay2_start = timer.getAbsoluteSeconds();
                        hasSetTimeDelay2 = true;
                    }
                    
                    if (timer.getAbsoluteSeconds() - timeDelay2_start >= Settings.timeDelay2_threshold) { //enough time has passed
                        Greenfoot.setWorld(new Round3(this));
                    }
                    
                }
                else {
                    img.setColor(Color.RED);
                    img.drawString("ROUND 2 FAILED", 75, 50);
                    
                    if (!hasSetTimeDelay2) {
                        timeDelay2_start = timer.getAbsoluteSeconds();
                        hasSetTimeDelay2 = true;
                    }
                    
                    if (timer.getAbsoluteSeconds() - timeDelay2_start >= Settings.timeDelay2_threshold) { //enough time has passed
                        Greenfoot.setWorld(new Intro());
                    }
                }
                timeText.setImage(img);
                this.addObject(timeText, 335, 30);
            }
            else {
                this.removeObject(timeText);
            }
        }
    }

    private void animatePickaxe() {
        pickaxe.setRotation(angle);

        if (prevPos != null) {
            if (!prevPos.equals(lastPos)) { //register a change in position
                onFirstIteration = true; //first iteration in a certain direction (left, right, up, down)
            }
        }

        if (Greenfoot.isKeyDown("1") && !wasJustEquipped) {
            pickaxeEquipped = !pickaxeEquipped;
            wasJustEquipped = true;
        }

        if (!Greenfoot.isKeyDown("1")) {
            wasJustEquipped = false;
        }

        if (lastPos.equals("back") || lastPos.equals("front")) {
            pickaxeEquipped = false;
        }

        if (pickaxeEquipped && farmer.getNumPickaxe() > 0) { //if the player has equipped the pickaxe (must be facing left or right)
            pesticideEquipped = false;
            wheatSeedEquipped = false;
            pickaxeImg.setTransparency(255);

            if (lastPos.equals("left")) { //facing left
                if (onFirstIteration) { //so that it only occurs once
                    angle = leftHighAngleBound;
                    pickaxe.setLocation(farmerX - 22, farmerY - 1); //-1
                    onFirstIteration = false; //to prevent multiple setLocations which could override
                }
                if (pickaxeRotatingLow && pickaxeEquipped) {
                    if (angle > leftLowAngleBound) {
                        angle -= angleChangeRate;
                        //pickaxe.move(-2);
                        pickaxe.setLocation(farmerX - 22, pickaxe.getY() + 2);
                    }
                    else { //if it has reached the target angle
                        pickaxeRotatingLow = false;
                        pickaxeRotatingHigh = true;
                    }    
                }
                else if (pickaxeEquipped) { //if pickaxeRotatingHigh
                    if (angle < leftHighAngleBound) {
                        angle += angleChangeRate;
                        //pickaxe.move(2);
                        pickaxe.setLocation(farmerX - 22, pickaxe.getY() - 2);
                    }
                    else {
                        pickaxeRotatingLow = true;
                        pickaxeRotatingHigh = false;
                    }    
                }
            }
            else if (lastPos.equals("right")) { //facing right
                if (onFirstIteration) { //so that it only occurs once
                    angle = rightHighAngleBound;
                    pickaxe.setLocation(farmerX + 22, farmerY - 1); //-1
                    onFirstIteration = false; //to prevent multiple setLocations which could override
                }
                if (pickaxeRotatingLow && pickaxeEquipped) {
                    if (angle < rightLowAngleBound) { //which is 70
                        angle += angleChangeRate;
                        //pickaxe.move(-2);
                        pickaxe.setLocation(farmerX + 22, pickaxe.getY() + 2);
                    }
                    else { //if it has reached the target angle
                        pickaxeRotatingLow = false;
                        pickaxeRotatingHigh = true;
                    }    
                }
                else if (pickaxeEquipped) { //if pickaxeRotatingHigh
                    if (angle > rightHighAngleBound) { //greater than 10
                        angle -= angleChangeRate;
                        //pickaxe.move(2);
                        pickaxe.setLocation(farmerX + 22, pickaxe.getY() - 2);
                    }
                    else {
                        pickaxeRotatingLow = true;
                        pickaxeRotatingHigh = false;
                    }    
                }
            }           
        }
        else { //if it is not equipped, make it disappear
            pickaxeImg.setTransparency(0);  
            onFirstIteration = true;
        }
    }    
    
    public void equipWheatSeed() {
        if (Greenfoot.isKeyDown("2") && !wheatSeedWasJustEquipped) {
            wheatSeedEquipped = !wheatSeedEquipped;
            wheatSeedWasJustEquipped = true;
        }

        if (!Greenfoot.isKeyDown("2")) {
            wheatSeedWasJustEquipped = false;
        }

        if (lastPos.equals("back") || lastPos.equals("front")) { //facing forward or backward
            wheatSeedEquipped = false;
        }

        if (wheatSeedEquipped && farmer.getNumWheatSeeds() > 0) { //if the player has equipped the pickaxe (must be facing left or right)
            pickaxeEquipped = false;
            pesticideEquipped = false;
            wheatSeedImg.setTransparency(255);

            if (lastPos.equals("left")) { //facing left
                wheatSeed.setLocation(farmerX - 13, farmer.getY() + 7);
            }
            else if (lastPos.equals("right")) {
                wheatSeed.setLocation(farmerX + 13, farmer.getY() + 7);
            }
        }
        else { //if it is not equipped, make it disappear
            wheatSeedImg.setTransparency(0);  
        }
    }

    public void equipPesticide() { 
        if (Greenfoot.isKeyDown("3") && !pesticideWasJustEquipped) {
            pesticideEquipped = !pesticideEquipped;
            pesticideWasJustEquipped = true;
        }

        if (!Greenfoot.isKeyDown("3")) {
            pesticideWasJustEquipped = false;
        }

        if (lastPos.equals("back") || lastPos.equals("front")) { //facing forward or backward
            pesticideEquipped = false;
        }

        if (pesticideEquipped) { //if the player has equipped the pickaxe (must be facing left or right)
            pickaxeEquipped = false;
            wheatSeedEquipped = false;
            pesticideImg.setTransparency(255);

            if (lastPos.equals("left")) { //facing left
                pesticideImg = pesticideImgLeft;
                pesticide.setImage(pesticideImg);
                pesticide.setLocation(farmerX - 13, farmer.getY() + 2);
                pesticide.setRotation(345);
            }
            else if (lastPos.equals("right")) {
                pesticideImg = pesticideImgRight;
                pesticide.setImage(pesticideImg);
                pesticide.setLocation(farmerX + 13, farmer.getY() + 2);
                pesticide.setRotation(15);
            }
        }
        else { //if it is not equipped, make it disappear
            pesticideImg.setTransparency(0);  
        }
    }

    public void equipDrone() {
        if (Greenfoot.isKeyDown("4") && !droneWasJustEquipped) {
            droneEquipped = !droneEquipped; //use key 4 to equip/dequip
            droneWasJustEquipped = true;
        }

        if (!Greenfoot.isKeyDown("4")) {
            droneWasJustEquipped = false;
        }
        
        if (droneEquipped && farmer.getNumDrone() > 0) {
            droneImg.setTransparency(255);
        }
        else {
            droneImg.setTransparency(0);
            drone.setLocation(283,217);
        }
    }

    public void adjustShopTransparency() {
        if (Greenfoot.isKeyDown("m")) {
            if (shopShown) {
                showShop = false;
            }
            else {
                showShop = true;
            }
        }

        if (!showShop) {
            if (shopTransparency >= 15) {
                shopTransparency -= 15;
                shopButton.getImage().setTransparency(shopTransparency);
            }
            else {
                shopShown = false;
            }
        }
        else {
            if (shopTransparency <= 240) {
                shopTransparency += 15;
                shopButton.getImage().setTransparency(shopTransparency);
            }
            else {
                shopShown = true;
            }
        }
    }

    public void collisionControl() {
        if (!farmer.isTouchingTile()) {
            lastX = farmerX;
            lastY = farmerY;
        }    
        else {
            farmer.setLocation(lastX, lastY); //make it so they cannot go 
            //through a tile
        }   
    }   

    public double distance (int x1, int x2, int y1, int y2) {
        double dis = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
        return dis;
    }

    public void pickaxeTile() {
        if (farmer.getNumPickaxe() > 0) { //if the user has bought a pickaxe
            if (pickaxeEquipped) { //if it is equipped
                List<Tile> intersectingTiles = pickaxe.getIntersectingTiles(); //get all the touching tiles
                double minDist = 1000;
                Tile nearestTile = null;
                for (Tile tile: intersectingTiles) { //for all the tiles in the game
                    //calculate the distance from the farmer to the tile
                    double dist = distance(farmer.getX(), tile.getX(), farmer.getY() + 20, tile.getY());
                    if (dist < minDist) { //if the tile is closer than the min distance recorded
                        minDist = dist; //set this closest distance as the new min distance
                        nearestTile = tile; //set the tile as the nearest tile
                    }
                }
                if (minDist <= 50 && nearestTile != null) {
                    nearestTile.isTouchingPickaxe(true); //call the method in the nearest tile that changes
                    //its color
                }
            }
        }
    }

    public void plantWheatSeeds() {
        if (farmer.getNumWheatSeeds() > 0 && wheatSeedEquipped) { //choosing to plant a crop
            MouseInfo mouse = Greenfoot.getMouseInfo();
            int x = 0;
            int y = 0;
            boolean clicked = false;

            if (Greenfoot.mouseClicked(null)) { //clicked anywhere on the map
                clicked = true;
                if (mouse != null) {
                    x = mouse.getX();
                    y = mouse.getY();
                }
            }

            if (clicked) {
                double minDist = 1000;
                Tile nearestTile = null;
                double distFromFarmer;

                for (Tile tile: this.getObjects(Tile.class)) {
                    double dist = distance(x, tile.getX(), y, tile.getY());
                    if (dist < minDist) {
                        minDist = dist;
                        nearestTile = tile; //set the nearestTile
                    }
                }

                //after we find the closest tile, add the wheat and the health bar
                //but only if the tile is close enough

                if (nearestTile != null) {
                    distFromFarmer = distance(farmer.getX(), nearestTile.getX(), farmer.getY(), nearestTile.getY());
                    if (nearestTile.getFertile() && !nearestTile.hasPlantedWheat() && distFromFarmer < 50) { //if there is not already a wheat seed or crop
                        nearestTile.plantWheatSeed(); //spawn the wheat and health bar
                        farmer.setNumWheatSeeds(farmer.getNumWheatSeeds() - 1);
                    }
                }
                clicked = false;
            }
        }
    }

    public void sprayPesticide() {
        if (farmer.getNumPesticide() > 0 && pesticideEquipped) { //choosing to apply one pesticide spray
            MouseInfo mouse = Greenfoot.getMouseInfo();
            int x = 0;
            int y = 0;
            boolean clicked = false;

            if (Greenfoot.mouseClicked(null)) { //clicked anywhere on the map
                clicked = true;
                if (mouse != null) {
                    x = mouse.getX();
                    y = mouse.getY();
                }
            }

            if (clicked) {
                double minDist = 50;
                Tile nearestTile = null;
                double distFromFarmer;

                for (Tile tile: this.getObjects(Tile.class)) {
                    double dist = distance(x, tile.getX(), y, tile.getY());
                    if (dist < minDist) {
                        minDist = dist;
                        nearestTile = tile; //set the nearestTile
                    }
                }

                if (nearestTile != null) { //if user clicked on a tile
                    distFromFarmer = distance(farmer.getX(), nearestTile.getX(), farmer.getY(), nearestTile.getY());

                    if (!nearestTile.hasSprayedPesticide() && distFromFarmer < 50) { //if it is unsprayed and close enough
                        nearestTile.sprayPesticide(); //spawn the wheat and health bar
                        farmer.setNumPesticide(farmer.getNumPesticide() - 1);
                    }
                }
                clicked = false;
            }
        }
    }

    private void activateDrone() {
        if (farmer.getNumDrone() > 0 && droneEquipped) {

            if (droneEnroute) { //if the drone is already moving to a certain crop
                //then we increment distance and also check if it has arrived
                double dist = distance(drone.getX(), nearestCropX, drone.getY(), nearestCropY);

                if (!returningToCenter && dist < 15) { //if it has arrived, then simply apply pesticide and set enroute to false

                    if (!nearestTile.hasSprayedPesticide() && nearestTile.getFertile()) { //if it is unsprayed and close enough
                        nearestTile.sprayPesticide(); //spawn the wheat and health bar
                        farmer.setNumPesticide(farmer.getNumPesticide() - 1);
                    }
                    
                    droneEnroute = false;
                    nearestCrop = null; //need to find the nearest crop and tile again
                    nearestTile = null;
                }
                else if (returningToCenter && dist < 7) { //must be exact position in this case, and no spraying pesticide
                    droneEnroute = false;
                    returningToCenter = false;
                }
                else { //increment the distance again because we are still too far
                    newDroneX = newDroneX + xIncrement;
                    newDroneY = newDroneY + yIncrement;

                    drone.setLocation((int)(Math.round(newDroneX)), (int)(Math.round(newDroneY)));
                }
            }
            else { //if the drone is not enroute, it either has arrived and finished pesticide application or needs to find a target
                //either way, it needs to search for a crop

                double minDist = 1000;
                nearestTile = null;
                
                //also, make sure to check if that seed or wheat's tile already has pesticide or not

                for (WheatSeed seed: getObjects(WheatSeed.class)) {
                    double dist = distance(drone.getX(), seed.getX(), drone.getY(), seed.getY());
                    if (dist < minDist) {
                        Tile seedTile = seed.getTile(); //also need to check that pesticide is not already applied
                        if (seedTile != null && !seedTile.hasSprayedPesticide() && seedTile.getFertile()) {
                            minDist = dist;
                            nearestCrop = seed;
                            nearestTile = seedTile;
                        }
                    }
                }

                for (Wheat wheat: getObjects(Wheat.class)) {
                    double dist = distance(drone.getX(), wheat.getX(), drone.getY(), wheat.getY());
                    if (dist < minDist) {
                        Tile wheatTile = wheat.getTile();
                        if (wheatTile != null && !wheatTile.hasSprayedPesticide() && wheatTile.getFertile()) {
                            minDist = dist;
                            nearestCrop = wheat;
                            nearestTile = wheatTile;
                        }
                    }
                }

                if (nearestCrop != null && farmer.getNumPesticide() > 0) { //if there is a crop AND the farmer
                    //has at least one pesticide spray left
                    
                    nearestCropX = nearestCrop.getX();
                    nearestCropY = nearestCrop.getY();

                    int droneX = drone.getX();
                    int droneY = drone.getY();

                    int xDiff = nearestCropX - droneX;
                    int yDiff = nearestCropY - droneY;

                    xIncrement = xDiff / droneSpeed;
                    yIncrement = yDiff / droneSpeed;
                    
                    newDroneX = droneX + xIncrement;
                    newDroneY = droneY + yIncrement;

                    drone.setLocation((int)(Math.round(newDroneX)), (int)(Math.round(newDroneY)));

                    droneEnroute = true;
                }
                else { //go back to the center
                    nearestCropX = 283;
                    nearestCropY = 217;
                    
                    int droneX = drone.getX();
                    int droneY = drone.getY();

                    int xDiff = nearestCropX - droneX;
                    int yDiff = nearestCropY - droneY;

                    xIncrement = xDiff / droneSpeed;
                    yIncrement = yDiff / droneSpeed;
                    
                    newDroneX = droneX + xIncrement;
                    newDroneY = droneY + yIncrement;

                    drone.setLocation((int)(Math.round(newDroneX)), (int)(Math.round(newDroneY)));

                    droneEnroute = true;
                    returningToCenter = true;
                }
            }
        }
    }

    private void updateInfertileTiles() {
        for (Tile tile: this.getObjects(Tile.class)) {
            if (!tile.getFertile() && !tile.getAddedToList()) { //if it is not fertile and it hasn't already been added
                infertileTileList.add(tile);
                tile.setAddedToList(true);
            }
        }
    }

    private void adjustSlotColors() {
        GreenfootImage image = null;

        if (pickaxeEquipped) { //if tool 1 is equipped
            image = slotG_img[0]; //index 0 is the first element
        }
        else {
            image = slotB_img[0]; //index 0 is the first element
        }

        slot1.setImage(image);

        if (wheatSeedEquipped) { //if tool 2 is equipped
            image = slotG_img[1]; //index 0 is the first element
        }
        else {
            image = slotB_img[1]; //index 0 is the first element
        }

        slot2.setImage(image);

        if (pesticideEquipped) { //if tool 3 is equipped
            image = slotG_img[2]; //index 0 is the first element
        }
        else {
            image = slotB_img[2]; //index 0 is the first element
        }

        slot3.setImage(image);

        if (droneEquipped) { //if tool 4 is equipped
            image = slotG_img[3]; //index 0 is the first element
        }
        else {
            image = slotB_img[3]; //index 0 is the first element
        }

        slot4.setImage(image);
    }

    private void initialInsects() {
        if (onCritPhaseFirstIteration) { //when it starts, move the insects
            insect1.getImage().setTransparency(255);
            insect2.getImage().setTransparency(255);
            insect3.getImage().setTransparency(255);
            insect4.getImage().setTransparency(255);
            insect5.getImage().setTransparency(255);
            insect6.getImage().setTransparency(255);
            insect1.move(-10);
            insect2.move(-10);
            insect3.move(-10);
            insect4.move(-10);
            insect5.move(-10);
            insect6.move(-10);
        }
        if (insect6.isAtEdge()) {
            insect1.getImage().setTransparency(0);
            insect2.getImage().setTransparency(0);
            insect3.getImage().setTransparency(0);
            insect4.getImage().setTransparency(0);
            insect5.getImage().setTransparency(0);
            insect6.getImage().setTransparency(0);
            onCritPhaseFirstIteration = false;
        }
    }

    private void updateData(Round1 farm) { //from Round 1 to Round 2
        this.farmer = farm.farmer; //only need to save the farmer
    }

    private void updateData(Round2 farm) { //from Round 2 to Shop and vice-versa
        this.onInitPhase = farm.onInitPhase;
        this.onCritPhase = farm.onCritPhase;
        this.farmerX = farm.farmerX;
        this.farmerY = farm.farmerY;
        this.farmer = farm.farmer;
        this.timer = farm.timer;
        this.infertileTileList = farm.infertileTileList;
        this.onCritPhaseFirstIteration = farm.onCritPhaseFirstIteration;
    }

    private void prepare()
    {
        //prepare the farm background

        for (int j = 1; j < 5; j++) { //j index represents left and right tiles
            for (int i = 1; i < 11; i++) { //i index represent up and down tiles (capacity of 10)
                int initY; //inital was 19
                if (i < 5) {
                    initY = 19;
                }
                else {
                    initY = 10;
                }
                if (i == 1 || i == 7) { //add an extra fence for the ends, horizontal
                    Wood wood = new Wood();
                    GreenfootImage woodImage = wood.getImage();
                    woodImage.scale(40, 5); //slightly longer
                    wood.setImage(woodImage);
                    addObject(wood, 20 + j * 105, initY + i * 36 - 17); //to get the starting one
                }
                if (i != 5 && i != 6) {
                    Wood wood = new Wood(); //horizontal woods
                    GreenfootImage woodImage = wood.getImage();
                    woodImage.scale(35, 5);
                    wood.setImage(woodImage);

                    addObject(wood, 20 + j * 105, initY + i * 36 + 17);

                    Wood wood2 = new Wood(); //vertical, right
                    GreenfootImage woodImage2 = wood2.getImage();
                    woodImage2.scale(35, 5);
                    wood2.setRotation(90);
                    wood2.setImage(woodImage);
                    addObject(wood2, 20 + j * 105 + 17, initY + i * 36);

                    Wood wood3 = new Wood(); //vertical, left
                    GreenfootImage woodImage3 = wood3.getImage();
                    woodImage3.scale(35, 5);
                    wood3.setRotation(90);
                    wood3.setImage(woodImage);
                    addObject(wood3, 20 + j * 105 - 17, initY + i * 36);

                    //the tile position equals the position of the infertile tile in the list
                    //update tile infertility 

                    int tileX = 20 + j * 105;
                    int tileY = initY + i * 36;
                    Tile newTile = new Tile();
                    PesticideTileBorder newTileBorder = newTile.getTileBorder();
                    newTile.setOnRound2(true);

                    if (infertileTileList.size() != 0) { //if there are some infertile Tiles
                        for (int t = 0; t < infertileTileList.size(); t++) {
                            Tile tile = infertileTileList.get(t);
                            if (tile.getX() == tileX && tile.getY() == tileY) { //if there was an infertile tile in this location
                                newTile.setFertile(false);
                                newTile.setAddedToList(true);
                            }
                        }  
                    }

                    addObject(newTile, tileX, tileY); //maximum overlap
                    addObject(newTileBorder, tileX, tileY);
                    //addObject (new Tile(), 20 + j * 105, initY + i * 36); //maximum overlap
                }
            }   
        }

        pickaxe = new Pickaxe();
        addObject(pickaxe, 100, 217);
        pickaxeImg = pickaxe.getImage();
        pickaxeImg.setTransparency(0); //starts invisible
        
        wheatSeed = new WheatSeed2();
        addObject(wheatSeed, 50, 217);
        wheatSeedImg = wheatSeed.getImage();
        wheatSeedImg.setTransparency(0); //starts invisible

        pesticide = new Pesticide();
        addObject(pesticide, 200, 217);
        pesticideImg = pesticide.getImage();
        pesticideImg.setTransparency(0); //the tool starts invisible

        //how to make text
        /**
        GreenfootImage shopText = new GreenfootImage(500, 100);
        shopText.setColor(Color.BLACK);
        shopText.setFont(new Font("Arial", false, false , 40));
        shopText.drawString("SHOP", 0, 50);
        shopText.setTransparency(127);
        getBackground().drawImage(shopText, 70, 185);
         */

        topHeader = new TopHeader();
        //addObject(topHeader, 282, 0);
        addObject(topHeader, 282, 18);

        bottomHeader = new BottomHeader();
        //addObject(bottomHeader, 282, 435);
        addObject(bottomHeader, 282, 412);

        shopButton = new ShopButton();
        shopButton.getImage().setTransparency(255);
        addObject(shopButton, 30, 70);

        timer.getImage().setTransparency(0); //make the timer invisible
        addObject(timer, 210, 317);

        if (farmer == null) { //if this is the first load of the game
            farmer = new Farmer("front");
        }
        addObject(farmer,farmerX,farmerY); //otherwise, just add the farmer with data

        //int i = 1;
        //int xCoordinate = 45;
        int xCoordinate = 142;

        for (int s = 0; s < slotB_img.length; s++) { //set the blue and green image arrays
            //set the blue images
            GreenfootImage imageB = new GreenfootImage("Box" + (s + 1) + "B.png");
            imageB.scale(45,45);
            slotB_img[s] = imageB;

            GreenfootImage imageG = new GreenfootImage("Box" + (s + 1) + "G.png");
            imageG.scale(45,45);
            slotG_img[s] = imageG;
        }

        for (int s = 0; s < slotArray.length; s++) {
            Slot slot = slotArray[s];
            GreenfootImage image = slotB_img[s];
            slot.setImage(image);
            this.addObject(slot,xCoordinate,412);
            xCoordinate += 47;
        }

        //add Pickaxe image
        PickaxeImage pickaxeImage2 = new PickaxeImage();
        //pickaxeImage2.getImage().scale(35,35);
        pickaxeImage2.getImage().scale(25,25);
        //pickaxeImage2.getImage().rotate(180);
        this.addObject(pickaxeImage2, 142, 415);

        //add Wheat Seed image
        WheatSeedImage wheatSeedImage = new WheatSeedImage();
        wheatSeedImage.getImage().scale(25, 13);
        this.addObject(wheatSeedImage, 189, 412);

        //add the flying insects at the start
        insect1 = new Insect();
        addObject(insect1,560,100);
        insect1.getImage().setTransparency(0);
        insect2 = new Insect();
        addObject(insect2,560,140);
        insect2.getImage().setTransparency(0);
        insect3 = new Insect();
        addObject(insect3,560,180);
        insect3.getImage().setTransparency(0);
        insect4 = new Insect();
        addObject(insect4,560,220);
        insect4.getImage().setTransparency(0);
        insect5 = new Insect();
        addObject(insect5,560,260);
        insect5.getImage().setTransparency(0);
        insect6 = new Insect();
        addObject(insect6,560,300);
        insect6.getImage().setTransparency(0);

        SmallPesticideImage inventoryPesticideImg = new SmallPesticideImage();
        addObject(inventoryPesticideImg,236,412);

        pesticideImgLeft = new GreenfootImage("PesticideLeft.png");
        pesticideImgRight = new GreenfootImage("PesticideRight.png");

        SmallDroneImage smallDroneImage = new SmallDroneImage();
        addObject(smallDroneImage,283,413);

        drone = new Drone();
        addObject(drone, 283, 217);
        droneImg = drone.getImage();
        droneImg.setTransparency(0); //the tool starts invisible
    }
}
