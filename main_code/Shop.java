import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @Brandon Pae
 * @8.9.2020
 */
public class Shop extends World
{

    /**
     * Constructor for objects of class Shop.
     * 
     */

    protected int cropAmt; 
    protected int moneyAmt;
    
    protected Farmer farmer = null;
    //cost in dollars
    protected double pickaxeCost = 100;
    protected double wheatSeedCost = 20;
    protected double wheatCost = 50;
    protected BuyButton buyButtonPickaxe = null;
    protected BuyButton buyButtonWheat = null;
    protected BuyButton buyButtonWheatSeed = null;
    protected SellButton sellButtonPickaxe = null;
    protected SellButton sellButtonWheatSeed = null;
    protected SellButton sellButtonWheat = null;
    protected Timer timer = null;
    protected Farm farm = null;
    protected int round = 1; //starts on round 1
    
    //text
    protected ShopText currencyText = new ShopText();
    protected GreenfootImage currencyImg = null;
    
    protected ShopText pickaxeText = new ShopText();
    protected GreenfootImage pickaxeImg = null;
    
    protected ShopText pickaxeDescriptionText = new ShopText();
    protected GreenfootImage pickaxeDescriptionImg = null;
    
    protected ShopText pickaxeInventoryText = new ShopText();
    protected GreenfootImage pickaxeInventoryImg = null;
    
    protected ShopText pickaxeCostText = new ShopText();
    protected GreenfootImage pickaxeCostImg = null;
    
    protected ShopText wheatSeedText = new ShopText();
    protected GreenfootImage wheatSeedImg = null;
    
    protected ShopText wheatSeedDescriptionText = new ShopText();
    protected GreenfootImage wheatSeedDescriptionImg = null;
    
    protected ShopText wheatSeedInventoryText = new ShopText();
    protected GreenfootImage wheatSeedInventoryImg = null;
    
    protected ShopText wheatSeedCostText = new ShopText();
    protected GreenfootImage wheatSeedCostImg = null;
    
    protected ShopText wheatText = new ShopText();
    protected GreenfootImage wheatImg = null;
    
    protected ShopText wheatDescriptionText = new ShopText();
    protected GreenfootImage wheatDescriptionImg = null;
    
    protected ShopText wheatInventoryText = new ShopText();
    protected GreenfootImage wheatInventoryImg = null;
    
    protected ShopText wheatCostText = new ShopText();
    protected GreenfootImage wheatCostImg = null;
    
    protected FarmButton farmButton = null;
    
    //shop2 page 2 fields
    
    protected BuyButton buyButtonInfectedWheat = null;
    protected BuyButton buyButtonPesticide = null;
    protected BuyButton buyButtonDrone = null;
    
    protected SellButton sellButtonInfectedWheat = null;
    protected SellButton sellButtonPesticide = null;
    protected SellButton sellButtonDrone = null;
    
    protected ShopText infectedWheatText = new ShopText();
    protected GreenfootImage infectedWheatImg = null;
    
    protected ShopText infectedWheatDescriptionText = new ShopText();
    protected GreenfootImage infectedWheatDescriptionImg = null;
    
    protected ShopText infectedWheatInventoryText = new ShopText();
    protected GreenfootImage infectedWheatInventoryImg = null;
    
    protected ShopText infectedWheatCostText = new ShopText();
    protected GreenfootImage infectedWheatCostImg = null;
    
    protected ShopText pesticideText = new ShopText();
    protected GreenfootImage pesticideImg = null;
    
    protected ShopText pesticideDescriptionText = new ShopText();
    protected GreenfootImage pesticideDescriptionImg = null;
    
    protected ShopText pesticideInventoryText = new ShopText();
    protected GreenfootImage pesticideInventoryImg = null;
    
    protected ShopText pesticideCostText = new ShopText();
    protected GreenfootImage pesticideCostImg = null;
    
    protected ShopText droneText = new ShopText();
    protected GreenfootImage droneImg = null;
    
    protected ShopText droneDescriptionText = new ShopText();
    protected GreenfootImage droneDescriptionImg = null;
    
    protected ShopText droneInventoryText = new ShopText();
    protected GreenfootImage droneInventoryImg = null;
    
    protected ShopText droneCostText = new ShopText();
    protected GreenfootImage droneCostImg = null;
    
    protected LeftArrow leftArrow = null;
    protected RightArrow rightArrow = null;
    
    protected PageNumber pageNumBox = null;
    
    protected double infectedWheatCost = 25;
    protected double pesticideCost = 5;
    protected double droneCost = 150;
    
    //shop 3 page 3 fields
    
    protected BuyButton buyButtonImprovedDrone = null;
    protected BuyButton buyButtonRobot = null;
    protected BuyButton buyButtonController = null;
    
    protected SellButton sellButtonImprovedDrone = null;
    protected SellButton sellButtonRobot = null;
    protected SellButton sellButtonController = null;
    
    protected ShopText improvedDroneText = new ShopText();
    protected GreenfootImage improvedDroneImg = null;
    
    protected ShopText improvedDroneDescriptionText = new ShopText();
    protected GreenfootImage improvedDroneDescriptionImg = null;
    
    protected ShopText improvedDroneInventoryText = new ShopText();
    protected GreenfootImage improvedDroneInventoryImg = null;
    
    protected ShopText improvedDroneCostText = new ShopText();
    protected GreenfootImage improvedDroneCostImg = null;
    
    protected ShopText robotText = new ShopText();
    protected GreenfootImage robotImg = null;
    
    protected ShopText robotDescriptionText = new ShopText();
    protected GreenfootImage robotDescriptionImg = null;
    
    protected ShopText robotInventoryText = new ShopText();
    protected GreenfootImage robotInventoryImg = null;
    
    protected ShopText robotCostText = new ShopText();
    protected GreenfootImage robotCostImg = null;
    
    protected ShopText controllerText = new ShopText();
    protected GreenfootImage controllerImg = null;
    
    protected ShopText controllerDescriptionText = new ShopText();
    protected GreenfootImage controllerDescriptionImg = null;
    
    protected ShopText controllerInventoryText = new ShopText();
    protected GreenfootImage controllerInventoryImg = null;
    
    protected ShopText controllerCostText = new ShopText();
    protected GreenfootImage controllerCostImg = null;
    
    protected double improvedDroneCost = 150;
    protected double robotCost = 200;
    protected double controllerCost = 100;
    
    public Shop()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(565, 435, 1); //same size as the farm to maintain screen size
    }
}
