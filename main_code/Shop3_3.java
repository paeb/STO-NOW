import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @Brandon Pae
 * @8.9.2020
 */
public class Shop3_3 extends Shop
{
    /**
     * This constructor is never called
     * 
     */
    public Shop3_3()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(); //same size as the farm to maintain screen size
        prepare();
    }
    
    /**
     * Called when switching between shop page 1 and page 2
     */
    public Shop3_3(Farm farm) { //pass in the farm data, including the farmer
        super();
        this.farm = farm;
        this.farmer = farm.farmer;
        this.timer = farm.timer;
        addObject(timer, 100, 217);
        prepare();
    }
    
    public void act() {
        writeText(); //with the greenfootImage, it occupies a lot of space so may interfere with other buttons
        double currency = farmer.getCurrency();
        
        if (Greenfoot.mouseClicked(buyButtonImprovedDrone)) {
            if (currency >= improvedDroneCost) {
                farmer.setNumImprovedDrone(farmer.getNumImprovedDrone() + 1); //buy one pickaxe
                farmer.setCurrency(farmer.getCurrency() - improvedDroneCost); //deduct 100 from their currency
            }
        }
        else if (Greenfoot.mouseClicked(sellButtonImprovedDrone)) {
            if (farmer.getNumImprovedDrone() > 0) {
                farmer.setCurrency(farmer.getCurrency() + improvedDroneCost);
                farmer.setNumImprovedDrone(farmer.getNumImprovedDrone() - 1);
            }
        }
        else if (Greenfoot.mouseClicked(buyButtonRobot)) {
            if (currency >= robotCost) {
                farmer.setNumRobot(farmer.getNumRobot() + 1); //buy one wheat seed
                farmer.setCurrency(farmer.getCurrency() - robotCost);
            }
        }
        else if (Greenfoot.mouseClicked(sellButtonRobot)) {
            if (farmer.getNumRobot() > 0) {
                farmer.setCurrency(farmer.getCurrency() + robotCost);
                farmer.setNumRobot(farmer.getNumRobot() - 1);
            }
        }
        else if (Greenfoot.mouseClicked(buyButtonController)) {
            if (currency >= controllerCost) {
                farmer.setNumController(farmer.getNumController() + 1); //buy one wheat seed
                farmer.setCurrency(farmer.getCurrency() - controllerCost);
            }
        }
        else if (Greenfoot.mouseClicked(sellButtonController)) { //sell wheat
            if (farmer.getNumController() > 0) {
                farmer.setCurrency(farmer.getCurrency() + controllerCost);
                farmer.setNumController(farmer.getNumController() - 1);
            }
        }
        
        if (Greenfoot.mouseClicked(farmButton)) {
            Greenfoot.setWorld(new Round3((Round3)farm));
        }
        
        //from 1st to 2nd page
        if (Greenfoot.mouseClicked(leftArrow)) {
            Greenfoot.setWorld(new Shop3_2(farm));
        }
    }    
    private void writeText() {
        createCurrencyText(currencyText, currencyImg, "Currency: $" + farmer.getCurrency(), 25, false, false);
        this.addObject(currencyText, 325, 35);
        
        //infected wheat heading
        createText(improvedDroneText, improvedDroneImg, "Improved Drone", 24, true, false);
        this.addObject(improvedDroneText, 102, 80);
        
        //infected wheat description
        createText(improvedDroneDescriptionText, improvedDroneDescriptionImg, "Apply Fertilizer", 20, false, true);
        this.addObject(improvedDroneDescriptionText, 128, 105);
       
        //infected wheat inventory
        createText(improvedDroneInventoryText, improvedDroneInventoryImg, "Inventory: " + farmer.getNumImprovedDrone(), 20, false, false);
        this.addObject(improvedDroneInventoryText, 145, 230);
        
        //infected wheat cost
        createText(improvedDroneCostText, improvedDroneCostImg, "Cost: $" + improvedDroneCost, 20, false, false);
        this.addObject(improvedDroneCostText, 145, 255);
        
        //pesticide heading
        createText(robotText, robotImg, "Robot", 25, true, false);
        this.addObject(robotText, 345, 80);
        
        //wheat seed description
        createText(robotDescriptionText, robotDescriptionImg, "Plant & Collect", 20, false, true);
        this.addObject(robotDescriptionText, 315, 105);
        
        //wheat seed inventory
        createText(robotInventoryText, robotInventoryImg, "Inventory: " + farmer.getNumRobot(), 20, false, false);
        this.addObject(robotInventoryText, 325, 230);
        
        //wheat seed cost
        createText(robotCostText, robotCostImg, "Cost: $" + robotCost, 20, false, false);
        this.addObject(robotCostText, 325, 255);
        
        //wheat heading
        createText(controllerText, controllerImg, "Controller", 25, true, false);
        this.addObject(controllerText, 520, 80);
        
        //wheat description
        createText(controllerDescriptionText, controllerDescriptionImg, "Control Machines", 20, false, true);
        this.addObject(controllerDescriptionText, 500, 105);
        
        //wheat inventory
        createText(controllerInventoryText, controllerInventoryImg, "Inventory: " + farmer.getNumController(), 20, false, false);
        this.addObject(controllerInventoryText, 530, 230);
        
        //wheat cost
        createText(controllerCostText, controllerCostImg, "Cost: $" + controllerCost, 20, false, false);
        this.addObject(controllerCostText, 530, 255);
    }
    
    public void createText(ShopText text, GreenfootImage image, String phrase, int size, boolean bold, boolean italic) {
        image = new GreenfootImage(200,50);
        image.setColor(Color.BLUE);
        image.setFont(new Font("Arial", bold, italic, size));
        image.drawString(phrase, 0, 25);
        image.setTransparency(255);
        text.setImage(image);
    }
    
    public void createCurrencyText(ShopText text, GreenfootImage image, String phrase, int size, boolean bold, boolean italic) {
        image = new GreenfootImage(250,50);
        image.setColor(Color.BLUE);
        image.setFont(new Font("Arial", bold, italic, size));
        image.drawString(phrase, 0, 25);
        image.setTransparency(255);
        text.setImage(image);
    }
    
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        ShopBorder shopBorder = new ShopBorder();
        addObject(shopBorder,283,203);
        
        LargeImprovedDroneImage droneImage = new LargeImprovedDroneImage();
        addObject(droneImage,95,160);
        
        LargeRobotImage robotImage = new LargeRobotImage();
        addObject(robotImage,282,162);

        LargeControllerImage controllerImage = new LargeControllerImage();
        addObject(controllerImage,482,166);

        buyButtonImprovedDrone = new BuyButton();
        addObject(buyButtonImprovedDrone, 67,310);

        sellButtonImprovedDrone = new SellButton();
        addObject(sellButtonImprovedDrone, 127, 310);

        buyButtonRobot = new BuyButton();
        addObject(buyButtonRobot, 245,310);

        sellButtonRobot = new SellButton();
        addObject(sellButtonRobot, 305, 310);

        buyButtonController = new BuyButton();
        addObject(buyButtonController, 450,310);

        sellButtonController = new SellButton();
        addObject(sellButtonController, 510, 310);
        
        farmButton = new FarmButton();
        addObject(farmButton,60,398);

        leftArrow = new LeftArrow();
        addObject(leftArrow,388,400);
        
        rightArrow = new RightArrow();
        rightArrow.getImage().setTransparency(100);
        addObject(rightArrow,535,401);
        
        pageNumBox = new PageNumber();
        GreenfootImage pageImage = new GreenfootImage("Page3.png");
        pageNumBox.setImage(pageImage);
        addObject(pageNumBox,459,401);
        
    }
}
