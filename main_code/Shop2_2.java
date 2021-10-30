import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @Brandon Pae
 * @8.9.2020
 */
public class Shop2_2 extends Shop
{
    /**
     * This constructor is never called
     * 
     */
    public Shop2_2()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(); //same size as the farm to maintain screen size
        prepare();
    }
    
    /**
     * Called when switching between shop page 1 and page 2
     */
    public Shop2_2(Farm farm) { //pass in the farm data, including the farmer
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
        
        if (Greenfoot.mouseClicked(buyButtonInfectedWheat)) {
            if (currency >= infectedWheatCost) {
                farmer.setNumInfectedWheat(farmer.getNumInfectedWheat() + 1); //buy one pickaxe
                farmer.setCurrency(farmer.getCurrency() - infectedWheatCost); //deduct 100 from their currency
            }
        }
        else if (Greenfoot.mouseClicked(sellButtonInfectedWheat)) {
            if (farmer.getNumInfectedWheat() > 0) {
                farmer.setCurrency(farmer.getCurrency() + infectedWheatCost);
                farmer.setNumInfectedWheat(farmer.getNumInfectedWheat() - 1);
            }
        }
        else if (Greenfoot.mouseClicked(buyButtonPesticide)) {
            if (currency >= pesticideCost) {
                farmer.setNumPesticide(farmer.getNumPesticide() + 1); //buy one wheat seed
                farmer.setCurrency(farmer.getCurrency() - pesticideCost);
            }
        }
        else if (Greenfoot.mouseClicked(sellButtonPesticide)) {
            if (farmer.getNumPesticide() > 0) {
                farmer.setCurrency(farmer.getCurrency() + pesticideCost);
                farmer.setNumPesticide(farmer.getNumPesticide() - 1);
            }
        }
        else if (Greenfoot.mouseClicked(buyButtonDrone)) {
            if (currency >= droneCost) {
                farmer.setNumDrone(farmer.getNumDrone() + 1); //buy one wheat seed
                farmer.setCurrency(farmer.getCurrency() - droneCost);
            }
        }
        else if (Greenfoot.mouseClicked(sellButtonDrone)) { //sell wheat
            if (farmer.getNumDrone() > 0) {
                farmer.setCurrency(farmer.getCurrency() + droneCost);
                farmer.setNumDrone(farmer.getNumDrone() - 1);
            }
        }
        
        if (Greenfoot.mouseClicked(farmButton)) {
            Greenfoot.setWorld(new Round2((Round2)farm));
        }
        
        //from 1st to 2nd page
        if (Greenfoot.mouseClicked(leftArrow)) {
            Greenfoot.setWorld(new Shop2_1(farm));
        }
    }    
    private void writeText() {
        createCurrencyText(currencyText, currencyImg, "Currency: $" + farmer.getCurrency(), 25, false, false);
        this.addObject(currencyText, 325, 35);
        
        //infected wheat heading
        createText(infectedWheatText, infectedWheatImg, "Infected Wheat", 25, true, false);
        this.addObject(infectedWheatText, 102, 80);
        
        //infected wheat description
        createText(infectedWheatDescriptionText, infectedWheatDescriptionImg, "Sell for $25", 20, false, true);
        this.addObject(infectedWheatDescriptionText, 142, 105);
       
        //infected wheat inventory
        createText(infectedWheatInventoryText, infectedWheatInventoryImg, "Inventory: " + farmer.getNumInfectedWheat(), 20, false, false);
        this.addObject(infectedWheatInventoryText, 145, 230);
        
        //infected wheat cost
        createText(infectedWheatCostText, infectedWheatCostImg, "Cost: $" + infectedWheatCost, 20, false, false);
        this.addObject(infectedWheatCostText, 145, 255);
        
        //pesticide heading
        createText(pesticideText, pesticideImg, "Pesticide", 25, true, false);
        this.addObject(pesticideText, 325, 80);
        
        //wheat seed description
        createText(pesticideDescriptionText, pesticideDescriptionImg, "Remove Insects", 20, false, true);
        this.addObject(pesticideDescriptionText, 310, 105);
        
        //wheat seed inventory
        createText(pesticideInventoryText, pesticideInventoryImg, "Inventory: " + farmer.getNumPesticide(), 20, false, false);
        this.addObject(pesticideInventoryText, 325, 230);
        
        //wheat seed cost
        createText(pesticideCostText, pesticideCostImg, "Cost: $" + pesticideCost, 20, false, false);
        this.addObject(pesticideCostText, 325, 255);
        
        //wheat heading
        createText(droneText, droneImg, "Drone", 25, true, false);
        this.addObject(droneText, 540, 80);
        
        //wheat description
        createText(droneDescriptionText, droneDescriptionImg, "Apply Pesticide", 20, false, true);
        this.addObject(droneDescriptionText, 515, 105);
        
        //wheat inventory
        createText(droneInventoryText, droneInventoryImg, "Inventory: " + farmer.getNumDrone(), 20, false, false);
        this.addObject(droneInventoryText, 530, 230);
        
        //wheat cost
        createText(droneCostText, droneCostImg, "Cost: $" + droneCost, 20, false, false);
        this.addObject(droneCostText, 530, 255);
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
        
        WheatImage wheatImage = new WheatImage();
        addObject(wheatImage,95,160);
        
        Insect insectImage = new Insect();
        insectImage.setRotation(90);
        addObject(insectImage,75,175);
        
        LargePesticideImage pesticideImage = new LargePesticideImage();
        addObject(pesticideImage,282,162);

        DroneImage droneImage = new DroneImage();
        addObject(droneImage,482,166);

        buyButtonInfectedWheat = new BuyButton();
        addObject(buyButtonInfectedWheat, 67,310);

        sellButtonInfectedWheat = new SellButton();
        addObject(sellButtonInfectedWheat, 127, 310);

        buyButtonPesticide = new BuyButton();
        addObject(buyButtonPesticide, 245,310);

        sellButtonPesticide = new SellButton();
        addObject(sellButtonPesticide, 305, 310);

        buyButtonDrone = new BuyButton();
        addObject(buyButtonDrone, 450,310);

        sellButtonDrone = new SellButton();
        addObject(sellButtonDrone, 510, 310);
        
        farmButton = new FarmButton();
        addObject(farmButton,60,398);

        leftArrow = new LeftArrow();
        addObject(leftArrow,388,400);
        
        rightArrow = new RightArrow();
        rightArrow.getImage().setTransparency(100);
        addObject(rightArrow,535,401);
        
        pageNumBox = new PageNumber();
        GreenfootImage pageImage = new GreenfootImage("Page2.png");
        pageNumBox.setImage(pageImage);
        addObject(pageNumBox,459,401);
        
    }
}
