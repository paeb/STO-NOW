import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @Brandon Pae
 * @8.9.2020
 */
public class Shop3_1 extends Shop
{
    /**
     * This constructor is never called
     * 
     */
    public Shop3_1()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(); //same size as the farm to maintain screen size
        prepare();
    }
    
    /**
     * Always called when switching between shop and farm, and page 1 and 2
     */
    public Shop3_1(Farm farm) { //pass in the farm data, including the farmer
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
        
        if (Greenfoot.mouseClicked(buyButtonPickaxe)) {
            if (currency >= pickaxeCost) {
                farmer.setNumPickaxe(farmer.getNumPickaxe() + 1); //buy one pickaxe
                farmer.setCurrency(farmer.getCurrency() - pickaxeCost); //deduct 100 from their currency
            }
        }
        else if (Greenfoot.mouseClicked(sellButtonPickaxe)) {
            if (farmer.getNumPickaxe() > 0) {
                farmer.setCurrency(farmer.getCurrency() + pickaxeCost);
                farmer.setNumPickaxe(farmer.getNumPickaxe() - 1);
            }
        }
        else if (Greenfoot.mouseClicked(buyButtonWheatSeed)) {
            if (currency >= wheatSeedCost) {
                farmer.setNumWheatSeeds(farmer.getNumWheatSeeds() + 1); //buy one wheat seed
                farmer.setCurrency(farmer.getCurrency() - wheatSeedCost);
            }
        }
        else if (Greenfoot.mouseClicked(sellButtonWheatSeed)) {
            if (farmer.getNumWheatSeeds() > 0) {
                farmer.setCurrency(farmer.getCurrency() + wheatSeedCost);
                farmer.setNumWheatSeeds(farmer.getNumWheatSeeds() - 1);
            }
        }
        else if (Greenfoot.mouseClicked(buyButtonWheat)) {
            if (currency >= wheatCost) {
                farmer.setNumWheat(farmer.getNumWheat() + 1); //buy one wheat seed
                farmer.setCurrency(farmer.getCurrency() - wheatCost);
            }
        }
        else if (Greenfoot.mouseClicked(sellButtonWheat)) { //sell wheat
            if (farmer.getNumWheat() > 0) {
                farmer.setCurrency(farmer.getCurrency() + wheatCost);
                farmer.setNumWheat(farmer.getNumWheat() - 1);
            }
        }
        
        if (Greenfoot.mouseClicked(farmButton)) {
            Greenfoot.setWorld(new Round3((Round3)farm));
        }
        
        //from 1st to 2nd page
        if (Greenfoot.mouseClicked(rightArrow)) {
            Greenfoot.setWorld(new Shop3_2(farm));
        }
    }    
    private void writeText() {

        createCurrencyText(currencyText, currencyImg, "Currency: $" + farmer.getCurrency(), 25, false, false);
        this.addObject(currencyText, 325, 35);
        
        //pickaxe heading
        createText(pickaxeText, pickaxeImg, "Pickaxe", 25, true, false);
        this.addObject(pickaxeText, 150, 80);
        
        //pickaxe description
        createText(pickaxeDescriptionText, pickaxeDescriptionImg, "Dig 1 Tile", 20, false, true);
        this.addObject(pickaxeDescriptionText, 152, 105);
       
        //pickaxe inventory
        createText(pickaxeInventoryText, pickaxeInventoryImg, "Inventory: " + farmer.getNumPickaxe(), 20, false, false);
        this.addObject(pickaxeInventoryText, 145, 230);
        
        //pickaxe cost
        createText(pickaxeCostText, pickaxeCostImg, "Cost: $" + pickaxeCost, 20, false, false);
        this.addObject(pickaxeCostText, 145, 255);
        
        //wheat seed heading
        createText(wheatSeedText, wheatSeedImg, "Wheat Seed", 25, true, false);
        this.addObject(wheatSeedText, 310, 80);
        
        //wheat seed description
        createText(wheatSeedDescriptionText, wheatSeedDescriptionImg, "Produce Wheat", 20, false, true);
        this.addObject(wheatSeedDescriptionText, 310, 105);
        
        //wheat seed inventory
        createText(wheatSeedInventoryText, wheatSeedInventoryImg, "Inventory: " + farmer.getNumWheatSeeds(), 20, false, false);
        this.addObject(wheatSeedInventoryText, 325, 230);
        
        //wheat seed cost
        createText(wheatSeedCostText, wheatSeedCostImg, "Cost: $" + wheatSeedCost, 20, false, false);
        this.addObject(wheatSeedCostText, 325, 255);
        
        //wheat heading
        createText(wheatText, wheatImg, "Wheat", 25, true, false);
        this.addObject(wheatText, 540, 80);
        
        //wheat description
        createText(wheatDescriptionText, wheatDescriptionImg, "Sell for $50", 20, false, true);
        this.addObject(wheatDescriptionText, 530, 105);
        
        //wheat inventory
        createText(wheatInventoryText, wheatInventoryImg, "Inventory: " + farmer.getNumWheat(), 20, false, false);
        this.addObject(wheatInventoryText, 530, 230);
        
        //wheat cost
        createText(wheatCostText, wheatCostImg, "Cost: $" + wheatCost, 20, false, false);
        this.addObject(wheatCostText, 530, 255);
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
       
        PickaxeImage pickaxeImage = new PickaxeImage();
        addObject(pickaxeImage,95,160);

        WheatImage wheatImage = new WheatImage();
        addObject(wheatImage, 482, 166); //282, 162

        WheatSeedImage wheatSeedImage = new WheatSeedImage();
        addObject(wheatSeedImage,282,162); //464, 166

        buyButtonPickaxe = new BuyButton();
        addObject(buyButtonPickaxe, 67,310);

        sellButtonPickaxe = new SellButton();
        addObject(sellButtonPickaxe, 127, 310);

        buyButtonWheatSeed = new BuyButton();
        addObject(buyButtonWheatSeed, 245,310); //464, 315

        sellButtonWheatSeed = new SellButton();
        addObject(sellButtonWheatSeed, 305, 310);

        buyButtonWheat = new BuyButton();
        addObject(buyButtonWheat, 450,310); //464, 315

        sellButtonWheat = new SellButton();
        addObject(sellButtonWheat, 510, 310); //282, 315

        farmButton = new FarmButton();
        addObject(farmButton,60,398);

        leftArrow = new LeftArrow();
        leftArrow.getImage().setTransparency(100);
        addObject(leftArrow,388,400);

        rightArrow = new RightArrow();
        addObject(rightArrow,535,401);

        pageNumBox = new PageNumber();
        GreenfootImage pageImage = new GreenfootImage("Page1.png");
        pageNumBox.setImage(pageImage);
        addObject(pageNumBox,459,401);

    }
}
