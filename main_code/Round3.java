import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math.*;
import java.util.List;
/**
 * @Brandon Pae
 * @9/21/20
 */
public class Round3 extends Farm
{

    /**
     * Never Used
     */
    public Round3()
    {
        // Create a new world with 565x435 cells with a cell size of 1x1 pixels.
        super();
        timer = new Timer();
        prepare();
    }

    /**
     * Constructor for transitioning between Round 2 to Round 3
     * @param previous Round 2 instance
     */
    public Round3(Round2 farm) {
        super();
        timer = new Timer(3); //since we are now on round 3, use a slightly different constructor
        updateData(farm);
        prepare();
    }

    /**
     * Constructor for transitioning between Round 3 and Shop
     * @param previous saved Round 3 instance
     */
    public Round3(Round3 farm) {
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

        //drone and improvedDrone behaviors
        equipDrone();

        activateDrone();

        equipImprovedDrone();

        activateImprovedDrone();

        //robot behaviors
        equipRobot();

        activateRobot();

        //controller behaviors
        equipController();

        activateController();

        //press m to show/hide shop
        adjustShopTransparency();

        if (Greenfoot.mouseClicked(shopButton)) {
            Greenfoot.setWorld(new Shop3_1(this));
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

        onInitPhase = timer.getOnInitPhase3();
        onCritPhase = timer.getOnCritPhase3();

        if (onInitPhase) {
            img = new GreenfootImage(500, 100);
            img.setColor(Color.CYAN);
            img.setFont(new Font("Arial", false, false , 30));
            img.setTransparency(255);

            if (secondsElapsed < 10) {//from :01 to:09
                img.drawString("ROUND 3" + "     " + "INIT PHASE-" + minutesElapsed + ":" + "0" + secondsElapsed, 0, 50);
            }
            else {//from :10+
                img.drawString("ROUND 3" + "     " + "INIT PHASE-" + minutesElapsed + ":" + secondsElapsed, 0, 50);
            }

            timeText.setImage(img);
            this.addObject(timeText, 335, 30);
        }

        if (onCritPhase) {
            if (topHeader.bannerLowered()) {
                img = new GreenfootImage(500, 100);
                img.setColor(Color.RED);
                img.setFont(new Font("Arial", false, false , 30));
                img.setTransparency(255);

                if (secondsElapsed < 10) {//from :01 to:09
                    img.drawString("ROUND 3" + "     " + "CRIT PHASE-" + minutesElapsed + ":" + "0" + secondsElapsed, 0, 50);
                }
                else {//from :10+
                    img.drawString("ROUND 3" + "     " + "CRIT PHASE-" + minutesElapsed + ":" + secondsElapsed, 0, 50);
                }

                timeText.setImage(img);
                this.addObject(timeText, 335, 30);
            }
            else {
                this.removeObject(timeText);
            }
        }

        if (timer.finishedRound3()) { //if the user's time is over
            if (topHeader.bannerLowered()) {
                img = new GreenfootImage(500, 100);
                //img.setColor(Color.CYAN);
                img.setFont(new Font("Arial", false, false , 30));
                img.setTransparency(255);

                if (farmer.getCurrency() >= timer.getCurrencyThreshold3()) {
                    img.setColor(Color.GREEN);
                    img.drawString("SIMULATION PASSED", 75, 50);
                    roundNumber = 3; //change it to the next round
                }
                else {
                    img.setColor(Color.RED);
                    img.drawString("ROUND 3 FAILED", 75, 50);
                    
                    if (!hasSetTimeDelay3) {
                        timeDelay3_start = timer.getAbsoluteSeconds();
                        hasSetTimeDelay3 = true;
                    }
                    
                    if (timer.getAbsoluteSeconds() - timeDelay3_start >= Settings.timeDelay3_threshold) { //enough time has passed
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

        if (pesticideEquipped && farmer.getNumPesticide() > 0) { //if the player has equipped the pickaxe (must be facing left or right)
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

    public void equipImprovedDrone() {
        if (Greenfoot.isKeyDown("5") && !improvedDroneWasJustEquipped) {
            improvedDroneEquipped = !improvedDroneEquipped; //use key 5 to equip/dequip
            improvedDroneWasJustEquipped = true;
        }

        if (!Greenfoot.isKeyDown("5")) {
            improvedDroneWasJustEquipped = false;
        }

        if (improvedDroneEquipped && farmer.getNumImprovedDrone() > 0) {
            improvedDrone.show();
        }
        else {
            improvedDrone.hide();
            improvedDrone.setLocation(300,217);
        }
    }

    public void equipRobot() {
        if (Greenfoot.isKeyDown("6") && !robotWasJustEquipped) {
            robotEquipped = !robotEquipped; //use key 5 to equip/dequip
            robotWasJustEquipped = true;
        }

        if (!Greenfoot.isKeyDown("6")) {
            robotWasJustEquipped = false;
        }

        if (robotEquipped && farmer.getNumRobot() > 0) {
            robotImg.setTransparency(255);
        }
        else {
            robotImg.setTransparency(0);
        }
    }

    public void equipController() {
        if (Greenfoot.isKeyDown("7") && !controllerWasJustEquipped) {
            controllerEquipped = !controllerEquipped; //use key 7 to equip/dequip
            controllerWasJustEquipped = true;
        }

        if (!Greenfoot.isKeyDown("7")) {
            controllerWasJustEquipped = false;
        }

        if (controllerEquipped && farmer.getNumController() > 0) {
            controllerImg.setTransparency(255); 
            sliderDroneSpeedImg.setTransparency(255);
            sliderDroneResourceImg.setTransparency(255);
            sliderImprovedDroneSpeedImg.setTransparency(255);
            sliderImprovedDroneResourceImg.setTransparency(255);
            sliderRobotSpeedImg.setTransparency(255);
            sliderRobotResourceImg.setTransparency(255);
            yesButtonDroneImg.setTransparency(255);
            yesButtonImprovedDroneImg.setTransparency(255);
            yesButtonRobotImg.setTransparency(255);
            noButtonDroneImg.setTransparency(255);
            noButtonImprovedDroneImg.setTransparency(255);
            noButtonRobotImg.setTransparency(255);

            if (droneCostText.getImage() != null) {
                droneCostText.getImage().setTransparency(255);
            }
            if (improvedDroneCostText.getImage() != null) {
                improvedDroneCostText.getImage().setTransparency(255);
            }
            if (robotCostText.getImage() != null) {
                robotCostText.getImage().setTransparency(255);
            }
            if (bankText.getImage() != null) {
                bankText.getImage().setTransparency(255);
            }
        }
        else {
            controllerImg.setTransparency(0); 
            sliderDroneSpeedImg.setTransparency(0);
            sliderDroneResourceImg.setTransparency(0);
            sliderImprovedDroneSpeedImg.setTransparency(0);
            sliderImprovedDroneResourceImg.setTransparency(0);
            sliderRobotSpeedImg.setTransparency(0);
            sliderRobotResourceImg.setTransparency(0);
            yesButtonDroneImg.setTransparency(0);
            yesButtonImprovedDroneImg.setTransparency(0);
            yesButtonRobotImg.setTransparency(0);
            noButtonDroneImg.setTransparency(0);
            noButtonImprovedDroneImg.setTransparency(0);
            noButtonRobotImg.setTransparency(0);

            if (droneCostText.getImage() != null) {
                droneCostText.getImage().setTransparency(0);
            }
            if (improvedDroneCostText.getImage() != null) {
                improvedDroneCostText.getImage().setTransparency(0);
            }
            if (robotCostText.getImage() != null) {
                robotCostText.getImage().setTransparency(0);
            }
            if (bankText.getImage() != null) {
                bankText.getImage().setTransparency(0);
            }
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

                if (!returningToCenter && dist < 5) { //if it has arrived, then simply apply pesticide and set enroute to false

                    if (!nearestTile.hasSprayedPesticide() && nearestTile.getFertile()) { //if it is unsprayed and close enough
                        nearestTile.sprayPesticide(); //spawn the wheat and health bar
                        farmer.setNumPesticide(farmer.getNumPesticide() - 1);
                    }

                    droneEnroute = false;
                    nearestCrop = null; //need to find the nearest crop and tile again
                    nearestTile = null;
                }
                else if (returningToCenter && dist < 5) { //must be exact position in this case, and no spraying pesticide
                    droneEnroute = false;
                    returningToCenter = false;
                }
                else {
                    newDroneX = newDroneX + xIncrement;
                    newDroneY = newDroneY + yIncrement;

                    drone.setLocation((int)(Math.round(newDroneX)), (int)(Math.round(newDroneY)));
                    //droneLastDist = dist; //save the last dist to target
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

    private void activateImprovedDrone() {
        if (farmer.getNumImprovedDrone() > 0 && improvedDroneEquipped) {

            if (improvedDroneEnroute) { //if the drone is already moving to a certain crop
                //then we increment distance and also check if it has arrived
                double dist = distance(improvedDrone.getX(), nearestTileX, improvedDrone.getY(), nearestTileY);
                //showText((int)dist+"",150,217);

                if (!returningToCenter2 && dist < 5) { //if it has arrived, then simply apply fertilizer and set enroute to false

                    if (!nearestTile2.hasSprayedFertilizer()) { //if it is unsprayed and close enough
                        nearestTile2.sprayFertilizer();
                        farmer.setNumFertilizer(farmer.getNumFertilizer() - 1);
                    }

                    improvedDroneEnroute = false;
                    nearestTile2 = null;
                }
                else if (returningToCenter2 && dist < 5) { //must be exact position in this case, and no spraying pesticide
                    improvedDroneEnroute = false;
                    returningToCenter2 = false;
                }
                else { 
                    newImprovedDroneX = newImprovedDroneX + xIncrement2;
                    newImprovedDroneY = newImprovedDroneY + yIncrement2;

                    improvedDrone.setLocation((int)(Math.round(newImprovedDroneX)), (int)(Math.round(newImprovedDroneY)));
                }
            }
            else { //if the drone is not enroute, it either has arrived and finished pesticide application or needs to find a target
                //either way, it needs to search for a crop

                double minDist = 1000;
                nearestTile2 = null;

                //also, make sure to check if that seed or wheat's tile already has pesticide or not

                //prioritize the wheat seeds before the tiles
                for (WheatSeed seed: getObjects(WheatSeed.class)) {
                    double dist = distance(improvedDrone.getX(), seed.getX(), improvedDrone.getY(), seed.getY());
                    if (dist < minDist) {
                        Tile seedTile = seed.getTile(); 
                        if (seedTile != null && seedTile.getI() == 0 && seedTile.getFertile()) {
                            minDist = dist;
                            nearestTile2 = seedTile;
                        }
                    }
                }

                if (nearestTile2 == null) { //if it cant find any wheat seeds, check the tiles

                    for (Tile tile: getObjects(Tile.class)) {
                        double dist = distance(improvedDrone.getX(), tile.getX(), improvedDrone.getY(), tile.getY());
                        if (dist < minDist) {
                            if (tile.getI() == 0 && tile.getFertile()) { //if the tile's soil is not fertilized and its able to be fertilized
                                minDist = dist;
                                nearestTile2 = tile;
                            }
                        }
                    }

                }

                if (nearestTile2 != null && farmer.getNumFertilizer() > 0) { //if there is a crop AND the farmer has enough fertilizer
                    //has at least one pesticide spray left

                    nearestTileX = nearestTile2.getX();
                    nearestTileY = nearestTile2.getY();

                    int droneX = improvedDrone.getX();
                    int droneY = improvedDrone.getY();

                    int xDiff = nearestTileX - droneX;
                    int yDiff = nearestTileY - droneY;

                    xIncrement2 = xDiff / droneSpeed2;
                    yIncrement2 = yDiff / droneSpeed2;

                    newImprovedDroneX = droneX + xIncrement2;
                    newImprovedDroneY = droneY + yIncrement2;

                    improvedDrone.setLocation((int)(Math.round(newImprovedDroneX)), (int)(Math.round(newImprovedDroneY)));

                    improvedDroneEnroute = true;
                }
                else { //go back to the center
                    nearestTileX = 300;
                    nearestTileY = 217;

                    int droneX = improvedDrone.getX();
                    int droneY = improvedDrone.getY();

                    int xDiff = nearestTileX - droneX;
                    int yDiff = nearestTileY - droneY;

                    xIncrement2 = xDiff / droneSpeed2;
                    yIncrement2 = yDiff / droneSpeed2;

                    newImprovedDroneX = droneX + xIncrement2;
                    newImprovedDroneY = droneY + yIncrement2;

                    improvedDrone.setLocation((int)(Math.round(newImprovedDroneX)), (int)(Math.round(newImprovedDroneY)));

                    improvedDroneEnroute = true;
                    returningToCenter2 = true;
                }
            }
        }
    }

    private void activateRobot() {
        if (farmer.getNumRobot() > 0 && robotEquipped) {
            if (robotEnroute) {
                int robotX = robot.getX();
                int robotY = robot.getY();
                boolean robotArrived = false;

                if (robotTravel.equals("horizontal")) { //travelling horizontally
                    if (Math.abs(robotX - robotXTarget) < robotXThreshold) { // close enough to the x-position of target
                        if (Math.abs(robotY - robotYTarget) < robotYThreshold) { // also close enough to the y-position of the target
                            robotArrived = true;
                        }
                        else {
                            robotTravel = "vertical";
                        }
                    }
                    else { //not close enough to the x-position of the target
                        newRobotX = newRobotX + robotXIncrement;
                        newRobotY = newRobotY;

                        robot.setLocation((int)(Math.round(newRobotX)), (int)(Math.round(newRobotY)));
                    }
                }
                else if (robotTravel.equals("vertical")) {
                    if (Math.abs(robotY - robotYTarget) < robotYThreshold) { // close enough to the y-position of target
                        if (Math.abs(robotX - robotXTarget) < robotXThreshold) { // also close enough to the x-position of the target
                            robotArrived = true;
                        }
                        else {
                            robotTravel = "horizontal";
                        }
                    } 
                    else { //not close enough to the y-position of the target
                        newRobotX = newRobotX;
                        newRobotY = newRobotY + robotYIncrement;

                        robot.setLocation((int)(Math.round(newRobotX)), (int)(Math.round(newRobotY)));
                    }
                }

                if (robotArrived && !robotReturningToCenter) {
                    if (robotNearestWheat != null) { //if we are collecting a wheat
                        robotNearestTile.collectWheat(); //collect wheat, which automatically updates farmer's inventory too
                    }
                    else { //planting a seed
                        if (!robotNearestTile.hasPlantedWheat()) { //there is not already a wheat there
                            robotNearestTile.plantWheatSeed();
                            farmer.setNumWheatSeeds(farmer.getNumWheatSeeds() - 1);
                        }
                    }

                    robotEnroute = false;
                    robotNearestTile = null;
                    robotNearestWheat = null;
                }
                else if (robotArrived && robotReturningToCenter) { //just going to center
                    robotEnroute = false;
                    robotReturningToCenter = false;
                }
                else { //robot hasn't arrived yet
                    robotEnroute = true;
                }
            }
            else { //if we are not enroute to a target, choose a new one or return to the center
                double minDist = 1000;
                robotNearestWheat = null;
                robotNearestTile = null;

                for (Wheat wheat: getObjects(Wheat.class)) {
                    double dist = distance(robot.getX(), wheat.getX(), robot.getY(), wheat.getY());
                    if (dist < minDist) {
                        Tile wheatTile = wheat.getTile();
                        minDist = dist;
                        robotNearestWheat = wheat;
                        robotNearestTile = wheatTile;
                    }
                }

                if (robotNearestWheat == null) { //if we can't go to a wheat
                    for (Tile tile: getObjects(Tile.class)) {
                        double dist = distance(robot.getX(), tile.getX(), robot.getY(), tile.getY());

                        if (dist < minDist) {
                            if (!tile.hasPlantedWheat()) { //if the tile doesn't already have a wheat seed
                                minDist = dist;
                                robotNearestTile = tile;
                            }
                        }
                    }
                }

                //if there is an available tile is the first priority

                boolean canCollectOrPlant = robotNearestWheat != null || (robotNearestTile != null && farmer.getNumWheatSeeds() > 0);

                if (canCollectOrPlant) {
                    robotNearestTileX = robotNearestTile.getX();
                    robotNearestTileY = robotNearestTile.getY();
                } 
                else { //cannot go to any tiles because they have a seed or they don't have a wheat or no seeds left
                    robotNearestTileX = 265;
                    robotNearestTileY = 217;
                    robotReturningToCenter = true;
                }

                int robotX = robot.getX();
                int robotY = robot.getY();

                if (!robotReturningToCenter) { //not going to the center
                    int tileX = robotNearestTileX;
                    if (tileX == 20) { //1st column of tiles
                        robotXTarget = 73;
                    }
                    else if (tileX == 125) {
                        if (Math.abs(robotX-73) < Math.abs(robotX-178)) { //closer to 73 pos than 178 pos
                            robotXTarget = 73;
                        }
                        else {
                            robotXTarget = 178;
                        }
                    }
                    else if (tileX == 230) {
                        if (Math.abs(robotX-178) < Math.abs(robotX-283)) {
                            robotXTarget = 178;
                        }
                        else {
                            robotXTarget = 283;
                        }
                    }
                    else if (tileX == 335) {
                        if (Math.abs(robotX-283) < Math.abs(robotX-388)) {
                            robotXTarget = 283;
                        }
                        else {
                            robotXTarget = 388;
                        }
                    }
                    else if (tileX == 440) {
                        if (Math.abs(robotX-388) < Math.abs(robotX-493)) {
                            robotXTarget = 388;
                        }
                        else {
                            robotXTarget = 493;
                        }
                    }
                    else if (tileX == 545) {
                        robotXTarget = 493;
                    }

                    robotYTarget = robotNearestTileY;
                }
                else { //returning to center
                    robotXTarget = robotNearestTileX;
                    robotYTarget = robotNearestTileY;
                }

                if (!robotReturningToCenter && Math.abs(robotY - 217) > robotYThreshold && Math.abs(robotXTarget - robotX) > 35){ 
                    robotNearestTileX = robotX;
                    robotNearestTileY = 217;
                    robotXTarget = robotNearestTileX;
                    robotYTarget = robotNearestTileY;
                    robotReturningToCenter = true;
                }

                int xDiff = robotXTarget - robotX;
                int yDiff = robotYTarget - robotY;

                robotXIncrement = xDiff / robotSpeed;
                robotYIncrement = yDiff / robotSpeed;

                if (Math.abs(robotY-217) < robotYThreshold) { //robot is close enough to the center height
                    //travel horizontally first
                    robotTravel = "horizontal";
                }
                else { //robot is not close enough to the center height
                    //travel vertically first
                    robotTravel = "vertical";
                }

                if (robotTravel.equals("horizontal")) {
                    newRobotX = robotX + robotXIncrement;
                    newRobotY = robotY;
                }
                else if (robotTravel.equals("vertical")) {
                    newRobotX = robotX;
                    newRobotY = robotY + robotYIncrement;
                }

                robot.setLocation((int)(Math.round(newRobotX)), (int)(Math.round(newRobotY)));

                robotEnroute = true;
            }
        }
    }

    private void activateController() {
        if (farmer.getNumController() > 0 && controllerEquipped) {
            droneControl();

            improvedDroneControl();

            robotControl();
            
            int bankAmt = (int)(farmer.getCurrency());
            createText(bankText, bankImg, "Bank: $"+bankAmt, Color.CYAN, 25, true, false);
            this.addObject(bankText,462,358);
        }
    }

    private void droneControl() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        int x = 0;
        int y = 0;

        if (mouse != null) {
            x = mouse.getX();
            y = mouse.getY();
        }

        int costFactor = 5;
        boolean excessiveCost = false;
        Color color = null;

        if (Greenfoot.mouseDragged(sliderDroneSpeed)) {

            int sliderX = sliderDroneSpeed.getX();
            int sliderY = constrict(99,153,y); //constrict the position of the slider

            sliderDroneSpeed.setLocation(sliderX, sliderY);
        }
        else if (Greenfoot.mouseDragEnded(sliderDroneSpeed)) { //was just dragging it then stopped

            newPosSliderDroneSpeed = sliderDroneSpeed.getY();
            int yDiff = newPosSliderDroneSpeed - lastPosSliderDroneSpeed; 
            //If we raise the slider, then the y coordinate decreases, so negative yDiff
            droneSpeedCost = yDiff * costFactor;
        }
        else if (Greenfoot.mouseDragged(sliderDroneResource)) {

            int sliderX = sliderDroneResource.getX();
            int sliderY = constrict(99,153,y); //constrict the position of the slider

            sliderDroneResource.setLocation(sliderX, sliderY);
        }

        //check new resource amt
        double sliderY = sliderDroneResource.getY();
        double lowerVal = 99;
        double higherVal = 153;
        double lowerAmt = 20; //most
        double higherAmt = -20; //least

        //use percentages to correlate the y Pos with speed val
        double percentage = (sliderY - lowerVal) / (higherVal - lowerVal);
        int newAmt = (int)(percentage * (higherAmt - lowerAmt) + lowerAmt);

        int pesticideAmt = farmer.getNumPesticide();
        int newPesticideAmt = pesticideAmt + newAmt;

        if (newPesticideAmt < 0) {
            excessivePesticideSold = true;
        }
        else {
            excessivePesticideSold = false;
        }

        newPosSliderDroneResource = (int)sliderY; //125 is the center
        int yDiff = newPosSliderDroneResource - 125; //if higher, then negative cost
        //if lower, then positive cost

        if (newAmt == 0) {
            yDiff = 0;
        }
        droneResourceCost = yDiff * costFactor;

        //calculate the total drone cost (from speed and resource)
        int droneCost = droneSpeedCost + droneResourceCost;

        //show the cost
        String sign = "";
        if (droneCost < 0) { //lose money
            sign = "-";
        }
        else if (droneCost > 0) { //gain money
            sign = "+";
        }

        //if the cost is negative and the magnitude is more than we currently have, show a red message

        if ((droneCost < 0 && Math.abs(droneCost) > farmer.getCurrency()) || excessivePesticideSold) { //we lose money and the cost is 
            //greater than the farmer's current currency
            excessiveCost = true;
            color = Color.RED;
        }
        else {
            excessiveCost = false;
            color = Color.BLUE;
        }

        createText(droneCostText, droneCostImg, sign+"$"+Math.abs(droneCost), color, 25, true, false);
        this.addObject(droneCostText, 495, 108);

        if (Greenfoot.mouseClicked(yesButtonDrone)) {
            if (!excessiveCost) { //only if they can afford it

                //ADJUST SPEED
                sliderY = sliderDroneSpeed.getY();
                lowerVal = 99;
                higherVal = 153;
                double lowerSpeed = 20; //fastest
                double higherSpeed = 180; //slowest

                //use percentages to correlate the y Pos with speed val
                percentage = (sliderY - lowerVal) / (higherVal - lowerVal);
                int newSpeed = (int)(percentage * (higherSpeed - lowerSpeed) + lowerSpeed);

                droneSpeed = newSpeed;

                lastPosSliderDroneSpeed = sliderDroneSpeed.getY(); //set the new pos as the most recent saved pos

                //ADJUST RESOURCE AMT (different because we are adding/subtracting amt, not setting the absolute)
                sliderY = sliderDroneResource.getY();
                lowerAmt = 20; //most
                higherAmt = -20; //least

                //use percentages to correlate the y Pos with speed val
                percentage = (sliderY - lowerVal) / (higherVal - lowerVal);
                newAmt = (int)(percentage * (higherAmt - lowerAmt) + lowerAmt);

                pesticideAmt = farmer.getNumPesticide();
                newPesticideAmt = pesticideAmt + newAmt;

                farmer.setNumPesticide(newPesticideAmt);

                int currency = (int)(farmer.getCurrency());
                currency += droneCost; //if it is negative, then deducts money
                //else, if positive, adds money
                farmer.setCurrency(currency);

                droneSpeedCost = 0; //reset the cost
                //but don't reset the resource cost
                excessivePesticideSold = false;
            }
        }
        else if (Greenfoot.mouseClicked(noButtonDrone)) {
            droneSpeedCost = 0; //reset the speed cost
            droneResourceCost = 0; //reset the resource cost
            excessivePesticideSold = false;

            //need to reset the position of the sliders
            sliderDroneSpeed.setLocation(sliderDroneSpeed.getX(), lastPosSliderDroneSpeed);
            sliderDroneResource.setLocation(sliderDroneResource.getX(), 125);
        }
    }

    private void improvedDroneControl() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        int x = 0;
        int y = 0;

        if (mouse != null) {
            x = mouse.getX();
            y = mouse.getY();
        }

        int costFactor = 5;
        boolean excessiveCost = false;
        Color color = null;

        if (Greenfoot.mouseDragged(sliderImprovedDroneSpeed)) {

            int sliderX = sliderImprovedDroneSpeed.getX();
            int sliderY = constrict(193,247,y); //constrict the position of the slider

            sliderImprovedDroneSpeed.setLocation(sliderX, sliderY);
        }
        else if (Greenfoot.mouseDragEnded(sliderImprovedDroneSpeed)) { //was just dragging it then stopped

            newPosSliderImprovedDroneSpeed = sliderImprovedDroneSpeed.getY();
            int yDiff = newPosSliderImprovedDroneSpeed - lastPosSliderImprovedDroneSpeed; 
            //If we raise the slider, then the y coordinate decreases, so negative yDiff
            improvedDroneSpeedCost = yDiff * costFactor;
        }
        else if (Greenfoot.mouseDragged(sliderImprovedDroneResource)) {

            int sliderX = sliderImprovedDroneResource.getX();
            int sliderY = constrict(193,247,y); //constrict the position of the slider

            sliderImprovedDroneResource.setLocation(sliderX, sliderY);
        }

        //check new resource amt
        double sliderY = sliderImprovedDroneResource.getY();
        double lowerVal = 193;
        double higherVal = 247;
        double lowerAmt = 20; //most
        double higherAmt = -20; //least

        //use percentages to correlate the y Pos with speed val
        double percentage = (sliderY - lowerVal) / (higherVal - lowerVal);
        int newAmt = (int)(percentage * (higherAmt - lowerAmt) + lowerAmt);

        int fertilizerAmt = farmer.getNumFertilizer();
        int newFertilizerAmt = fertilizerAmt + newAmt;

        if (newFertilizerAmt < 0) {
            excessiveFertilizerSold = true;
        }
        else {
            excessiveFertilizerSold = false;
        }

        newPosSliderImprovedDroneResource = (int)sliderY; //125 is the center
        int yDiff = newPosSliderImprovedDroneResource - 219; //if higher, then negative cost
        //if lower, then positive cost

        if (newAmt == 0) {
            yDiff = 0;
        }
        improvedDroneResourceCost = yDiff * costFactor;

        //calculate the total drone cost (from speed and resource)
        int improvedDroneCost = improvedDroneSpeedCost + improvedDroneResourceCost;

        //show the cost
        String sign = "";
        if (improvedDroneCost < 0) { //lose money
            sign = "-";
        }
        else if (improvedDroneCost > 0) { //gain money
            sign = "+";
        }

        //if the cost is negative and the magnitude is more than we currently have, show a red message

        if ((improvedDroneCost < 0 && Math.abs(improvedDroneCost) > farmer.getCurrency()) || excessiveFertilizerSold) { //we lose money and the cost is 
            //greater than the farmer's current currency
            excessiveCost = true;
            color = Color.RED;
        }
        else {
            excessiveCost = false;
            color = Color.BLUE;
        }

        createText(improvedDroneCostText, improvedDroneCostImg, sign+"$"+Math.abs(improvedDroneCost), color, 25, true, false);
        this.addObject(improvedDroneCostText, 495, 198);

        if (Greenfoot.mouseClicked(yesButtonImprovedDrone)) {
            if (!excessiveCost) { //only if they can afford it

                //ADJUST SPEED
                sliderY = sliderImprovedDroneSpeed.getY();
                lowerVal = 193;
                higherVal = 247;
                double lowerSpeed = 20; //fastest
                double higherSpeed = 180; //slowest

                //use percentages to correlate the y Pos with speed val
                percentage = (sliderY - lowerVal) / (higherVal - lowerVal); //maybe 1 minus this
                int newSpeed = (int)(percentage * (higherSpeed - lowerSpeed) + lowerSpeed);

                droneSpeed2 = newSpeed;

                lastPosSliderImprovedDroneSpeed = sliderImprovedDroneSpeed.getY(); //set the new pos as the most recent saved pos

                //ADJUST RESOURCE AMT (different because we are adding/subtracting amt, not setting the absolute
                sliderY = sliderImprovedDroneResource.getY();
                lowerAmt = 20; //most
                higherAmt = -20; //least

                //use percentages to correlate the y Pos with speed val
                percentage = (sliderY - lowerVal) / (higherVal - lowerVal);
                newAmt = (int)(percentage * (higherAmt - lowerAmt) + lowerAmt);

                fertilizerAmt = farmer.getNumFertilizer();
                newFertilizerAmt = fertilizerAmt + newAmt;

                farmer.setNumFertilizer(newFertilizerAmt);

                int currency = (int)(farmer.getCurrency());
                currency += improvedDroneCost; //if it is negative, then deducts money
                //else, if positive, adds money
                farmer.setCurrency(currency);

                improvedDroneSpeedCost = 0; //reset the cost
                //but don't reset the resource cost
                excessiveFertilizerSold = false;
            }
        }
        else if (Greenfoot.mouseClicked(noButtonImprovedDrone)) {
            improvedDroneSpeedCost = 0; //reset the speed cost
            improvedDroneResourceCost = 0; //reset the resource cost
            excessiveFertilizerSold = false;

            //need to reset the position of the sliders
            sliderImprovedDroneSpeed.setLocation(sliderImprovedDroneSpeed.getX(), lastPosSliderImprovedDroneSpeed);
            sliderImprovedDroneResource.setLocation(sliderImprovedDroneResource.getX(), 219);
        }
    }

    private void robotControl() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        int x = 0;
        int y = 0;

        if (mouse != null) {
            x = mouse.getX();
            y = mouse.getY();
        }

        int costFactor = 5;
        boolean excessiveCost = false;
        Color color = null;

        if (Greenfoot.mouseDragged(sliderRobotSpeed)) {

            int sliderX = sliderRobotSpeed.getX();
            int sliderY = constrict(280,334,y); //constrict the position of the slider

            sliderRobotSpeed.setLocation(sliderX, sliderY);
        }
        else if (Greenfoot.mouseDragEnded(sliderRobotSpeed)) { //was just dragging it then stopped

            newPosSliderRobotSpeed = sliderRobotSpeed.getY();
            int yDiff = newPosSliderRobotSpeed - lastPosSliderRobotSpeed; 
            //If we raise the slider, then the y coordinate decreases, so negative yDiff
            robotSpeedCost = yDiff * costFactor;
        }
        else if (Greenfoot.mouseDragged(sliderRobotResource)) {

            int sliderX = sliderRobotResource.getX();
            int sliderY = constrict(280,334,y); //constrict the position of the slider

            sliderRobotResource.setLocation(sliderX, sliderY);
        }

        //check new resource amt
        double sliderY = sliderRobotResource.getY();
        double lowerVal = 280;
        double higherVal = 334;
        double lowerAmt = 20; //most
        double higherAmt = -20; //least

        //use percentages to correlate the y Pos with speed val
        double percentage = (sliderY - lowerVal) / (higherVal - lowerVal);
        int newAmt = (int)(percentage * (higherAmt - lowerAmt) + lowerAmt);

        int wheatSeedAmt = farmer.getNumWheatSeeds();
        int newWheatSeedAmt = wheatSeedAmt + newAmt;

        if (newWheatSeedAmt < 0) {
            excessiveWheatSeedSold = true;
        }
        else {
            excessiveWheatSeedSold = false;
        }

        newPosSliderRobotResource = (int)sliderY; //125 is the center
        int yDiff = newPosSliderRobotResource - 306; //if higher, then negative cost
        //if lower, then positive cost

        if (newAmt == 0) {
            yDiff = 0;
        }
        robotResourceCost = yDiff * costFactor;

        //calculate the total drone cost (from speed and resource)
        int robotCost = robotSpeedCost + robotResourceCost;

        //show the cost
        String sign = "";
        if (robotCost < 0) { //lose money
            sign = "-";
        }
        else if (robotCost > 0) { //gain money
            sign = "+";
        }

        //if the cost is negative and the magnitude is more than we currently have, show a red message

        if ((robotCost < 0 && Math.abs(robotCost) > farmer.getCurrency()) || excessiveWheatSeedSold) { //we lose money and the cost is 
            //greater than the farmer's current currency
            excessiveCost = true;
            color = Color.RED;
        }
        else {
            excessiveCost = false;
            color = Color.BLUE;
        }

        createText(robotCostText, robotCostImg, sign+"$"+Math.abs(robotCost), color, 25, true, false);
        this.addObject(robotCostText, 495, 287);

        if (Greenfoot.mouseClicked(yesButtonRobot)) {
            if (!excessiveCost) { //only if they can afford it

                //ADJUST SPEED
                sliderY = sliderRobotSpeed.getY();
                lowerVal = 280;
                higherVal = 334;
                double lowerSpeed = 20; //fastest
                double higherSpeed = 180; //slowest

                //use percentages to correlate the y Pos with speed val
                percentage = (sliderY - lowerVal) / (higherVal - lowerVal);
                int newSpeed = (int)(percentage * (higherSpeed - lowerSpeed) + lowerSpeed);

                robotSpeed = newSpeed;

                lastPosSliderRobotSpeed = sliderRobotSpeed.getY(); //set the new pos as the most recent saved pos

                //ADJUST RESOURCE AMT (different because we are adding/subtracting amt, not setting the absolute
                sliderY = sliderRobotResource.getY();
                lowerAmt = 20; //most
                higherAmt = -20; //least

                //use percentages to correlate the y Pos with speed val
                percentage = (sliderY - lowerVal) / (higherVal - lowerVal);
                newAmt = (int)(percentage * (higherAmt - lowerAmt) + lowerAmt);

                wheatSeedAmt = farmer.getNumWheatSeeds();
                newWheatSeedAmt = wheatSeedAmt + newAmt;

                farmer.setNumWheatSeeds(newWheatSeedAmt);

                int currency = (int)(farmer.getCurrency());
                currency += robotCost; //if it is negative, then deducts money
                //else, if positive, adds money
                farmer.setCurrency(currency);

                robotSpeedCost = 0; //reset the cost
                //but don't reset the resource cost
                excessiveWheatSeedSold = false;
            }
        }
        else if (Greenfoot.mouseClicked(noButtonRobot)) {
            robotSpeedCost = 0; //reset the speed cost
            robotResourceCost = 0; //reset the resource cost
            excessiveWheatSeedSold = false;

            //need to reset the position of the sliders
            sliderRobotSpeed.setLocation(sliderRobotSpeed.getX(), lastPosSliderRobotSpeed);
            sliderRobotResource.setLocation(sliderRobotResource.getX(), 306);
        }
    }

    private int constrict(int min, int max, int val) {
        int value;

        if (val > max) {
            value = max;
        }
        else if (val < min) {
            value = min;
        }
        else {
            value = val;
        }

        return value;
    }

    public void createText(ShopText text, GreenfootImage image, String phrase, Color color, int size, boolean bold, boolean italic) {
        image = new GreenfootImage(200,30);
        image.setColor(color);
        image.setFont(new Font("Arial", bold, italic, size));
        image.drawString(phrase, 0, 25);
        image.setTransparency(255);
        text.setImage(image);
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

        if (improvedDroneEquipped) { //if tool 5 is equipped
            image = slotG_img[4]; //index 0 is the first element
        }
        else {
            image = slotB_img[4]; //index 0 is the first element
        }

        slot5.setImage(image);

        if (robotEquipped) { //if tool 5 is equipped
            image = slotG_img[5]; //index 0 is the first element
        }
        else {
            image = slotB_img[5]; //index 0 is the first element
        }

        slot6.setImage(image);

        if (controllerEquipped) { //if tool 5 is equipped
            image = slotG_img[6]; //index 0 is the first element
        }
        else {
            image = slotB_img[6]; //index 0 is the first element
        }

        slot7.setImage(image);
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

    private void updateData(Round2 farm) { //from Round 2 to Round 3
        this.farmer = farm.farmer; //only need to save the farmer
    }

    private void updateData(Round3 farm) { //from Round 3 to Shop and vice-versa
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

        for (int j = 0; j < 6; j++) { //j index represents left and right tiles
            for (int i = 1; i < 11; i++) { //i index represent up and down tiles (capacity of 10)
                int initY; //initial was 19
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
                    newTile.setOnRound3(true);

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

        timer.getImage().setTransparency(0); //make the timer invisible
        addObject(timer, 210, 317);

        robot = new Robot();
        addObject(robot,265,217);
        robotImg = robot.getImage();
        robotImg.setTransparency(0);

        if (farmer == null) { //if this is the first load of the game
            farmer = new Farmer("front");
        }
        addObject(farmer,farmerX,farmerY); //otherwise, just add the farmer with data

        //int i = 1;
        //int xCoordinate = 45;
        int xCoordinate = 142; //from 72 

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
            if (s == 4) { //on the fifth slot
                SmallImprovedDroneImage smallImprovedDroneImage = new SmallImprovedDroneImage();
                addObject(smallImprovedDroneImage,331,412);
            }
            else if (s == 5) {
                SmallRobotImage smallRobotImage = new SmallRobotImage();
                addObject(smallRobotImage,377,412);
            }
            else if (s == 6) {
                SmallControllerImage smallControllerImage = new SmallControllerImage();
                addObject(smallControllerImage,424,412);
            }
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

        improvedDrone = new ImprovedDrone();
        addObject(improvedDrone,300,217);
        improvedDroneImg = improvedDrone.getImage();
        improvedDroneImg.setTransparency(0);

        controller = new Controller();
        controllerImg = controller.getImage();
        controllerImg.setTransparency(0);
        addObject(controller,283,213); //283,198

        shopButton = new ShopButton();
        shopButton.getImage().scale(35,35);
        addObject(shopButton, 17, 17);

        int controllerTransparency = 0;

        sliderDroneSpeed = new Slider();
        sliderDroneSpeedImg = sliderDroneSpeed.getImage();
        sliderDroneSpeedImg.setTransparency(controllerTransparency);
        addObject(sliderDroneSpeed,179,125);

        sliderDroneResource = new Slider();
        sliderDroneResourceImg = sliderDroneResource.getImage();
        sliderDroneResourceImg.setTransparency(controllerTransparency);
        addObject(sliderDroneResource,293,125);

        sliderImprovedDroneSpeed = new Slider();
        sliderImprovedDroneSpeedImg = sliderImprovedDroneSpeed.getImage();
        sliderImprovedDroneSpeedImg.setTransparency(controllerTransparency);
        addObject(sliderImprovedDroneSpeed,179,219);

        sliderImprovedDroneResource = new Slider();
        sliderImprovedDroneResourceImg = sliderImprovedDroneResource.getImage();
        sliderImprovedDroneResourceImg.setTransparency(controllerTransparency);
        addObject(sliderImprovedDroneResource,293,219);

        sliderRobotSpeed = new Slider();
        sliderRobotSpeedImg = sliderRobotSpeed.getImage();
        sliderRobotSpeedImg.setTransparency(controllerTransparency);
        addObject(sliderRobotSpeed,179,307);

        sliderRobotResource = new Slider();
        sliderRobotResourceImg = sliderRobotResource.getImage();
        sliderRobotResourceImg.setTransparency(controllerTransparency);
        addObject(sliderRobotResource,293,306);

        yesButtonDrone = new YesButton();
        yesButtonDroneImg = yesButtonDrone.getImage();
        yesButtonDroneImg.setTransparency(controllerTransparency);
        addObject(yesButtonDrone,419,148); //467,128

        noButtonDrone = new NoButton();
        noButtonDroneImg = noButtonDrone.getImage();
        noButtonDroneImg.setTransparency(controllerTransparency);
        addObject(noButtonDrone,461,148);

        yesButtonImprovedDrone = new YesButton();
        yesButtonImprovedDroneImg = yesButtonImprovedDrone.getImage();
        yesButtonImprovedDroneImg.setTransparency(controllerTransparency);
        addObject(yesButtonImprovedDrone,419,238);

        noButtonImprovedDrone = new NoButton();
        noButtonImprovedDroneImg = noButtonImprovedDrone.getImage();
        noButtonImprovedDroneImg.setTransparency(controllerTransparency);
        addObject(noButtonImprovedDrone,461,238);

        yesButtonRobot = new YesButton();
        yesButtonRobotImg = yesButtonRobot.getImage();
        yesButtonRobotImg.setTransparency(controllerTransparency);
        addObject(yesButtonRobot,419,324);

        noButtonRobot = new NoButton();
        noButtonRobotImg = noButtonRobot.getImage();
        noButtonRobotImg.setTransparency(controllerTransparency);
        addObject(noButtonRobot,461,324);
    }
}
