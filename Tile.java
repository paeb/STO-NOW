import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Write a description of class Tile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tile extends MapElement
{
    /**
     * Act - do whatever the Tile wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    //round to the nearest five or even number?

    private double[] r = {86, 116, 170, 140, 156, 92, 88, 20};
    private double[] g = {56, 64, 88, 118, 138, 132, 172, 166};
    private double[] b = {44, 44, 50, 66, 38, 48, 46, 50};

    //private double[] r = {85, 115, 169, 139, 156, 91, 88, 19};
    //private double[] g = {55, 63, 87, 117, 137, 132, 171, 166};
    //private double[] b = {43, 44, 49, 65, 37, 47, 45, 50};

    private double pickaxeHitDuration = 0;

    private double soilDegradationRate = 80; //higher the rate, faster it depletes
    //the lower the number, the faster

    private int i = 0; //i represents the current quality of the soil and the index in the color array

    //starting rgb values for the tile
    private double rVal = 86.0;
    private double gVal = 56.0;
    private double bVal = 44.0;

    private boolean hasPlantedWheat = false;
    private Wheat wheat = null;
    private WheatSeed wheatSeed = null;
    private Bar bar = null;
    private double cropHealth = 100;
    //private int time = 0;

    private boolean isTouchingPickaxe = false;
    private double rDif = 0;
    private double gDif = 0;
    private double bDif = 0;
    private double soilQualityMin = 1;
    private double soilQualityMax = 10;
    private double pickaxeEfficiency;
    private boolean wheatSpawned = false;
    private int time = 0;

    //indicates if the tile can be used or not
    private boolean fertile = true;
    private int pickaxeUse = 0;
    private int pickaxeUseThreshold;

    private boolean addedToList = false;

    //fields for pesticide application
    private boolean isClicked = false;
    private boolean pesticideSprayed = false;
    private PesticideTileBorder tileBorder = null;
    private GreenfootImage tileBorderImg = null;

    //private boolean firstPesticideSpray = true;
    private int pesticideEffectiveness = 255;
    private int pesticideI = 0;
    private int pesticideThreshold = 3;

    private boolean onRound2 = false;
    private boolean onRound3 = false;
    private Insect insect = null;
    private boolean wheatInfected = false;

    private boolean fertilizerSprayed = false;
    private boolean robotCollectingWheat = false;
    //private boolean wasJustTouchingPickaxe = false;
    //private boolean wasJustNotTouchingPickaxe = true;
    //private boolean changeInFarming = false;
    //85, 55, 43 (dark brown)
    //115, 63, 44 (lighter brown)
    //169, 87, 49 (brown)
    //139, 117, 65
    //156, 137, 37
    //91, 132, 47
    //88, 171. 45
    //19, 166, 50

    public Tile() {
        GreenfootImage image = getImage();
        image.scale(35, 35);

        getImage().setColor(new Color(85, 55, 43)); //starting brown color
        getImage().fill(); //fill the tile as brown

        this.pickaxeUseThreshold = Settings.pickaxeUseThreshold;

        //for the pesticide sprays
        tileBorder = new PesticideTileBorder();
        tileBorderImg = tileBorder.getImage();
        tileBorderImg.setTransparency(0);

        insect = new Insect();
        insect.getImage().scale(15,8);
        insect.setRotation(90);
    }

    public void act()
    {
        Pickaxe pickaxe = (Pickaxe)this.getOneIntersectingObject(Pickaxe.class); //get an intersecting pickaxe
        Farmer farmer = (Farmer)getWorld().getObjects(Farmer.class).get(0);
        tileFarming(); //handles a pickaxe touching

        setMouseClicked(); //see if this tile has been clicked (technically, it checks the border bc it's in the front)

        //now we need to change the rate of wheat spawn based on soil quality

        if (hasPlantedWheat) {
            if (time > 50 * (r.length - i) && !wheatSpawned && fertile) { 
                //only runs if we have not spawned the wheat yet and tile is fertile
                //the higher r is (more green), the smaller the difference 
                //and the less time it takes to spawn the wheat

                getWorld().removeObject(wheatSeed);
                wheat = new Wheat();
                getWorld().addObject(wheat, this.getX(), this.getY());
                bar = new Bar();
                getWorld().addObject(bar, this.getX(), this.getY() - 10);
                time = 0;
                wheatSpawned = true;
                wheatInfected = false;

            }
            time++;
        }

        if (wheatSpawned) { //if there is a wheat
            if (onRound2 || onRound3) {
                //should be in this block because the previous one only runs once
                boolean insectSpawned = this.isTouching(Insect.class);

                if (pesticideSprayed) {  //pesticide is active
                    if (insectSpawned) {
                        getWorld().removeObject(insect);
                    }
                    wheatInfected = false;
                }
                else { //pesticide is not active
                    if (!insectSpawned) {
                        getWorld().addObject(insect,this.getX()-5,this.getY()+5);
                    }
                    wheatInfected = true;
                }
            }

            if (cropHealth > 0) {
                cropHealth = Math.round(cropHealth - (r.length - i) / 5);
                bar.setPercentHealth(cropHealth);
            }
            else { //wheat dies
                getWorld().removeObject(bar);
                getWorld().removeObject(wheat);
                wheatSpawned = false;
                hasPlantedWheat = false;

                if (this.isTouching(Insect.class)) {
                    getWorld().removeObject(insect);
                }
            }
        }

        boolean wheatCollected = Greenfoot.mouseClicked(wheat) || robotCollectingWheat; //either farmer or robot

        if (wheat != null && wheatCollected) {

            double dist = 0;
            
            try {
                dist = distance(wheat.getX(), farmer.getX(), wheat.getY(), farmer.getY());
            }
            catch(Exception e) {
                wheatSpawned = false;
            }

            if (wheatSpawned && (dist < 50 || robotCollectingWheat)) { //to check that the farmer is close or within distance
                getWorld().removeObject(wheat);

                if ((onRound2 || onRound3) && wheatInfected) {
                    farmer.setNumInfectedWheat(farmer.getNumInfectedWheat() + 1);
                }
                else {
                    farmer.setNumWheat(farmer.getNumWheat() + 1);
                }

                getWorld().removeObject(bar);

                if (this.isTouching(Insect.class)) { //if there is an insect
                    getWorld().removeObject(insect);
                }

                wheatSpawned = false;
                hasPlantedWheat = false;
                robotCollectingWheat = false;
            }
        }

        /**
        if (wheat != null && Greenfoot.mouseClicked(wheat)) {

        double dist = distance(wheat.getX(), farmer.getX(), wheat.getY(), farmer.getY());

        if (wheatSpawned && dist < 50) { //to check that the farmer is close or within distance
        getWorld().removeObject(wheat);

        if (onRound2 && wheatInfected) {
        farmer.setNumInfectedWheat(farmer.getNumInfectedWheat() + 1);
        }
        else {
        farmer.setNumWheat(farmer.getNumWheat() + 1);
        }

        getWorld().removeObject(bar);

        if (this.isTouching(Insect.class)) { //if there is an insect
        getWorld().removeObject(insect);
        }

        wheatSpawned = false;
        hasPlantedWheat = false;
        }
        }
         */

        if (pesticideSprayed) { //if the user sprayed pesticide and it's still effective
            if (pesticideEffectiveness >= 0) {//transparency ranges from 0 to 255
                tileBorderImg.setTransparency(pesticideEffectiveness);
                pesticideI++;
                if (pesticideI == pesticideThreshold) { //so that we only reduce the effectiveness every couple iterations
                    pesticideEffectiveness--;
                    pesticideI = 0;
                }
            }
            else { //pesticide no longer effective
                pesticideSprayed = false;
                pesticideEffectiveness = 255; //reset the effectiveness for next time
            }

            //make the shop class next
            //add the option to buy pesticides
            //make shop1 and shop2 or maybe not?
            //add pesticide and different wheats - one for infected, one for regular infected wheat
        }

        checkPickaxeUse(); //check if the pickaxe has exhausted the tile
        //if so, set it to  infertile
    }

    public void tileFarming() {
        if (i == 0) { //no longer at maximum fertility, then we can reapply fertilizer
            fertilizerSprayed = false;
        }

        Pickaxe touchedPickaxe = (Pickaxe)this.getOneIntersectingObject(Pickaxe.class);

        if (touchedPickaxe != null && touchedPickaxe.isVisible()) { //the tool must be equipped
            //isTouchingPickaxe = true; (this is set from the Farm class)
            pickaxeEfficiency = touchedPickaxe.getEfficiency();
            //pickaxeEfficiency = 1 / pickaxeEfficiency; //higher the efficiency, the faster the tile changes color
        }
        else {
            isTouchingPickaxe = false;
        }

        if (isTouchingPickaxe) {
            pickaxeEfficiency = touchedPickaxe.getEfficiency();
            pickaxeUse++; //keeps track of how long pickaxe is used
            //this.getWorld().showText(pickaxeUse + "", 100, 217);
            //pickaxeEfficiency = 1 / pickaxeEfficiency;
        }

        //the reason it's not working has to do with how the code is implemented differently
        //the other one finds on intersecting tile. This one finds any touching pickaxe, so it can be
        //two tiles at once

        if (isTouchingPickaxe) {
            if (i < r.length - 1) {

                rDif = (r[i + 1] - r[i]) / pickaxeEfficiency;
                gDif = (g[i + 1] - g[i]) / pickaxeEfficiency;
                bDif = (b[i + 1] - b[i]) / pickaxeEfficiency;

                rVal += rDif;
                gVal += gDif;
                bVal += bDif;

                //IMPORTANT: Something to add later is perhaps if the rgb value is less than the one
                //it should be (for the current index), first add up to that value before adding more

                //in case the values exceed the next target value somehow

                boolean req1 = r[i] > r[i+1] && rVal < r[i+1]; //if current r value should be greater than the next
                boolean req2 = r[i] < r[i+1] && rVal > r[i+1];
                boolean req3 = g[i] > g[i+1] && gVal < g[i+1];
                boolean req4 = g[i] < g[i+1] && gVal > g[i+1];
                boolean req5 = b[i] > b[i+1] && bVal < b[i+1];
                boolean req6 = b[i] < b[i+1] && bVal > b[i+1];

                if (req1 || req2 || req3 || req4 || req5 || req6) {
                    rVal = r[i+1];
                    gVal = g[i+1];
                    bVal = b[i+1];
                }

                if (Math.round(rVal) == r[i+1] && Math.round(gVal) == g[i+1] && Math.round(bVal) == b[i+1]) {
                    this.incrementI();
                }
            }
        }
        else {//if not touching pickaxe, then the soil quality decreases
            //boolean worstSoilQuality = (Math.round(rVal) == r[0] && Math.round(gVal) == g[0] && Math.round(bVal) == b[0]);
            if (i > 0) { //if the tile has been hit already (starts at worstSoil)
                //we cannot say if i != 0, because i can still equal zero but have changed color (barely)

                //rDif = (r[i] - r[i - 1]) / (1/soilDegradationRate);
                //gDif = (g[i] - g[i - 1]) / (1/soilDegradationRate);
                //bDif = (b[i] - b[i - 1]) / (1/soilDegradationRate);

                //the main issue is that if the rVal is in between the r for the current index and the next,
                //this difference calculation will not be what it actually needs to be

                rDif = (r[i] - r[i - 1]) / soilDegradationRate;
                gDif = (g[i] - g[i - 1]) / soilDegradationRate;
                bDif = (b[i] - b[i - 1]) / soilDegradationRate;

                rVal -= rDif;
                gVal -= gDif;
                bVal -= bDif;

                //these stop and set the values immediately, or parameter error happens

                boolean req1 = r[i] > r[i-1] && rVal < r[i-1];
                boolean req2 = r[i] < r[i-1] && rVal > r[i-1];
                boolean req3 = g[i] > g[i-1] && gVal < g[i-1];
                boolean req4 = g[i] < g[i-1] && gVal > g[i-1];
                boolean req5 = b[i] > b[i-1] && bVal < b[i-1];
                boolean req6 = b[i] < b[i-1] && bVal > b[i-1];

                if (req1 || req2 || req3 || req4 || req5 || req6) {
                    rVal = r[i - 1];
                    gVal = g[i - 1];
                    bVal = b[i - 1];
                }

                if ((Math.round(rVal) == r[i-1] && Math.round(gVal) == g[i-1] && Math.round(bVal) == b[i-1])) {
                    i--;
                }
            }
        }
        //set the color
        //try {
        //this.getImage().setColor(new Color((int)Math.round(rVal), (int)Math.round(gVal), (int)Math.round(bVal)));
        //}

        if (fertile) { //tile color is only affected by pickaxe is fertile (otherwise it is red)
            this.getImage().setColor(new Color((int)(Math.round(rVal)), (int)Math.round(gVal), (int)Math.round(bVal)));
        }
        else {
            this.getImage().setColor(new Color(139, 0, 0));
        }

        /**
        catch (Exception e) {
        getWorld().showText((int)(rVal * 10) / 10.00 + ": r", 100, 217);
        getWorld().showText((int)(gVal * 10) / 10.00 + ": g", 200, 217);
        getWorld().showText((int)(bVal * 10) / 10.00 + ": b", 300, 217);
        getWorld().showText(i + ": i", 400, 217);
        }
         */
        this.getImage().fill();
    }

    public void checkPickaxeUse() {
        if (this.pickaxeUse >= this.pickaxeUseThreshold) { //tile used too much
            this.fertile = false;
        }
    }

    private void setMouseClicked() {
        if (tileBorder.isClicked()) {
            isClicked = true;
        }
        else{
            isClicked = false;
        }
    }

    public double distance (int x1, int x2, int y1, int y2) {
        double dis = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
        return dis;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }

    public void incrementI() {
        i++;
    }    

    public void decrementI() {
        i--;
    }

    public double getR() {
        return rVal;
    }    

    public double getG() {
        return gVal;
    }    

    public double getB() {
        return bVal;
    }    

    public int getXCoordinate() {
        return this.getX();
    }    

    public int getYCoordinate() {
        return this.getY();
    }    

    public void plantWheatSeed() { //spawns wheat (only if there is not already a wheat)
        wheatSeed = new WheatSeed();
        getWorld().addObject(wheatSeed, this.getX(), this.getY());
        hasPlantedWheat = true;
        //hasSpawnedCrop = true;
    }

    public boolean hasPlantedWheat() {
        return hasPlantedWheat;
    }

    public void setRGB(double rVal, double gVal, double bVal) {
        this.rVal = rVal;
        this.gVal = gVal;
        this.bVal = bVal;
    }

    public void isBeingFarmed() {
        // isBeingFarmed = true;
    }

    public int[] getRgb() {
        int[] rgb = {this.getImage().getColor().getRed(), this.getImage().getColor().getGreen(), this.getImage().getColor().getBlue()};
        return rgb;
    }    

    public void isTouchingPickaxe(boolean isTouching) {
        isTouchingPickaxe = isTouching;
    }

    public boolean getFertile() {
        return this.fertile;
    }

    public void setFertile(boolean fertile) {
        this.fertile = fertile;
    }

    public void setAddedToList(boolean added) {
        this.addedToList = added;
    }

    public boolean getAddedToList() {
        return this.addedToList;
    }

    public boolean hasSprayedPesticide() {
        return pesticideSprayed;
    }

    public void sprayPesticide() {
        pesticideSprayed = true;
    }

    public boolean hasSprayedFertilizer() {
        return fertilizerSprayed;
    }

    public void sprayFertilizer() {
        fertilizerSprayed = true;
        i = 7;
        rVal = 20;
        gVal = 166;
        bVal = 50;
    }

    public PesticideTileBorder getTileBorder() {
        return tileBorder;
    }

    public void setOnRound2(boolean onR2) {
        onRound2 = onR2;
    }

    public void setOnRound3(boolean onR3) {
        onRound3 = onR3;
    }

    public void collectWheat() {
        robotCollectingWheat = true;
    }
}
