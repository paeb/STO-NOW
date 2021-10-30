import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.List;

/**
 * Write a description of class Farm here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Farm extends World
{
    
    //farmer actor variables
    protected int farmerX = 282; //x-coordinate of farmer's starting position
    protected int farmerY = 217; //y-coordinate of farmer's starting position
    
    //pickaxe variables
    protected int angle = 0; //angle of the pickaxe
    protected boolean pickaxeRotatingLow = true; //if the pickaxe is currently rotating down
    protected boolean pickaxeRotatingHigh = false; //if the pickaxe is currently rotating up
    protected final int leftLowAngleBound = -70;
    protected final int leftHighAngleBound = -10;
    protected final int rightLowAngleBound = 170;
    protected final int rightHighAngleBound = 110;
    protected int angleChangeRate = 8;
    protected boolean pickaxeEquipped = false;
    protected boolean wasJustEquipped = false; //if the pickaxe was just equipped
    protected int pickaxeDifference;
    protected int iterations = 0;
    protected boolean pickaxeClicked = false;
    
    protected Shop shop = null;
    protected String lastPos = null;
    protected GreenfootImage rightImage = null;
    protected boolean flipped = false;
    protected boolean wasJustClicked = false;
    protected boolean onFirstIteration = true;
    protected String prevPos = null;

    protected Pickaxe pickaxe = null;
    protected GreenfootImage pickaxeImg = null;
    protected int lastX = 0;
    protected int lastY = 0;
    protected boolean hasAddedPickaxe = false;
    protected Farmer farmer = null;
    protected boolean justClicked2 = false;
    protected long startTime;
    protected boolean hasResetTimer = false;
    protected boolean goToNextIteration = false;
    
    protected long displayTime;
    protected boolean onRoundOne = true;
    protected boolean timeReset = false;
    protected Timer timer = null;
    protected GreenfootImage shopText = null;
    protected TopHeader topHeader = null;
    protected BottomHeader bottomHeader = null;
    protected TimeText timeText = new TimeText();
    protected GreenfootImage img = null;
    protected boolean hasMovedLeft = false;
    
    //the different boxes/slots for the inventory
    protected Slot slot1 = new Slot();
    protected Slot slot2 = new Slot();
    protected Slot slot3 = new Slot();
    protected Slot slot4 = new Slot();
    protected Slot slot5 = new Slot();
    protected Slot slot6 = new Slot();
    protected Slot slot7 = new Slot();
    protected Slot[] slotArray = {slot1,slot2,slot3,slot4,slot5,slot6,slot7};
    
    protected GreenfootImage[] slotB_img = new GreenfootImage[7];
    protected GreenfootImage[] slotG_img = new GreenfootImage[7];
    
    //info button
    protected ShopButton shopButton = null;
    protected boolean shopShown = false;
    protected int shopTransparency = 0;
    protected boolean showShop = false;
    protected int round = 1; 
    protected int currencyThreshold1 = 300;
    
    //timer
    protected long totalSecondsElapsed;
    protected long secondsElapsed;
    protected long minutesElapsed;
    protected boolean onInitPhase = true;
    protected boolean onCritPhase = false;

    protected int roundNumber = 1;

    //make an array of the Tiles (size of maximum Tiles) and pass in the Tiles
    protected List<Tile> infertileTileList = new ArrayList<Tile>();
    
    protected long timeDelay1_start = 0;
    protected boolean hasSetTimeDelay1 = false;
    
    protected boolean onCritPhaseFirstIteration = true;
    protected Insect insect1 = null;
    protected Insect insect2 = null;
    protected Insect insect3 = null;
    protected Insect insect4 = null;
    protected Insect insect5 = null;
    protected Insect insect6 = null;
    
    
    //pesticide fields
    protected Pesticide pesticide = null;
    protected GreenfootImage pesticideImg = null;
    protected boolean pesticideWasJustEquipped = false;
    protected boolean pesticideEquipped = false;
    protected GreenfootImage pesticideImgLeft = null;
    protected GreenfootImage pesticideImgRight = null;
    protected boolean justClicked3 = false;
    
    //drone fields
    protected Drone drone = null;
    protected GreenfootImage droneImg = null;
    protected boolean droneWasJustEquipped = false;
    protected boolean droneEquipped = false;
    protected boolean droneEnroute = false;
    protected Actor nearestCrop = null;
    protected int nearestCropX;
    protected int nearestCropY;
    protected double newDroneX;
    protected double newDroneY;
    protected double droneSpeed = 60.0; //the higher, the slower it goes
    protected double xIncrement;
    protected double yIncrement;
    protected boolean returningToCenter = false;
    protected Tile nearestTile = null;
    
    //from r2 to r3
    protected long timeDelay2_start = 0;
    protected boolean hasSetTimeDelay2 = false;
    
    //r3 variables
    protected ImprovedDrone improvedDrone = null;
    protected GreenfootImage improvedDroneImg = null;
    protected boolean improvedDroneWasJustEquipped = false;
    protected boolean improvedDroneEquipped = false;

    protected boolean improvedDroneEnroute = false;
    protected double newImprovedDroneX;
    protected double newImprovedDroneY;
    protected double droneSpeed2 = 60.0; //the higher, the slower it goes
    protected int nearestTileX;
    protected int nearestTileY;
    protected double xIncrement2;
    protected double yIncrement2;
    protected boolean returningToCenter2 = false;
    protected Tile nearestTile2 = null;
    
    protected Robot robot = null;
    protected GreenfootImage robotImg = null;
    protected boolean robotWasJustEquipped = false;
    protected boolean robotEquipped = false;
    
    protected boolean robotEnroute = false;
    protected int robotNearestTileX;
    protected int robotNearestTileY;
    protected String robotTravel;
    protected int robotXTarget;
    protected int robotYTarget;
    protected int robotXThreshold = 2;
    protected int robotYThreshold = 2;
    protected double newRobotX;
    protected double newRobotY;
    protected double robotXIncrement;
    protected double robotYIncrement;
    protected boolean robotReturningToCenter = false;
    protected Wheat robotNearestWheat = null;
    protected Tile robotNearestTile = null;
    protected double robotSpeed = 60.0; //lowest possible is 1 (teleports there)
    protected boolean robotTurning = true;
    protected int robotAngle;
    protected int targetAngle;
    protected String robotTravelNext;
    
    protected Controller controller = null;
    protected GreenfootImage controllerImg = null;
    protected boolean controllerEquipped = false;
    protected boolean controllerWasJustEquipped = false;
    
    protected Slider sliderDroneSpeed;
    protected Slider sliderDroneResource;
    protected Slider sliderImprovedDroneSpeed;
    protected Slider sliderImprovedDroneResource;
    protected Slider sliderRobotSpeed;
    protected Slider sliderRobotResource;
    
    protected YesButton yesButtonDrone;
    protected YesButton yesButtonImprovedDrone;
    protected YesButton yesButtonRobot;
    
    protected NoButton noButtonDrone;
    protected NoButton noButtonImprovedDrone;
    protected NoButton noButtonRobot;
    
    protected GreenfootImage sliderDroneSpeedImg;
    protected GreenfootImage sliderDroneResourceImg;
    protected GreenfootImage sliderImprovedDroneSpeedImg;
    protected GreenfootImage sliderImprovedDroneResourceImg;
    protected GreenfootImage sliderRobotSpeedImg;
    protected GreenfootImage sliderRobotResourceImg;
    protected GreenfootImage yesButtonDroneImg;
    protected GreenfootImage yesButtonImprovedDroneImg;
    protected GreenfootImage yesButtonRobotImg;
    protected GreenfootImage noButtonDroneImg;
    protected GreenfootImage noButtonImprovedDroneImg;
    protected GreenfootImage noButtonRobotImg;
    
    protected int droneSpeedCost = 0;
    protected int lastPosSliderDroneSpeed = 125;
    protected int newPosSliderDroneSpeed;
    protected int droneResourceCost = 0;
    protected int newPosSliderDroneResource;
    protected ShopText droneCostText = new ShopText();
    protected GreenfootImage droneCostImg = null;
    protected boolean excessivePesticideSold = false;
    
    protected int improvedDroneSpeedCost = 0;
    protected int lastPosSliderImprovedDroneSpeed = 219;
    protected int newPosSliderImprovedDroneSpeed;
    protected int improvedDroneResourceCost = 0;
    protected int newPosSliderImprovedDroneResource;
    protected ShopText improvedDroneCostText = new ShopText();
    protected GreenfootImage improvedDroneCostImg = null;
    protected boolean excessiveFertilizerSold = false;
    
    protected int robotSpeedCost = 0;
    protected int lastPosSliderRobotSpeed = 306;
    protected int newPosSliderRobotSpeed;
    protected int robotResourceCost = 0;
    protected int newPosSliderRobotResource;
    protected ShopText robotCostText = new ShopText();
    protected GreenfootImage robotCostImg = null;
    protected boolean excessiveWheatSeedSold = false;
    
    protected ShopText bankText = new ShopText();
    protected GreenfootImage bankImg = null;
    
    
    //wheat seed animation fields
    protected boolean wheatSeedEquipped = false;
    protected boolean wheatSeedWasJustEquipped = false;
    protected WheatSeed2 wheatSeed = null;
    protected GreenfootImage wheatSeedImg = null;
    
    protected long timeDelay3_start = 0;
    protected boolean hasSetTimeDelay3 = false;
    
    public Farm()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(565, 435, 1); 
    }
}
