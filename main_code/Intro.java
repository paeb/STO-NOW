import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Intro here.
 * 
 * @Brandon Pae
 * @9/21/20
 */
public class Intro extends World
{

    private boolean buttonMade = false;
    private int i = 2;
    private GreenfootImage image = null;
    private GreenfootImage text = null;
    private Logo logo = null;
    private Header header = null;
    private int transparency = 0;
    private boolean textDisplayed = false;
    private StartButton startButton = null;
    private int angle = 0;
    private int buttonTransparency = 0;
    private ShopText authorText = null;
    private GreenfootImage authorImg = null;

    public Intro()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(565, 435, 1);
        image = getBackground();
        image.scale(565, 435);

        text = new GreenfootImage(500, 100);
        text.setColor(Color.RED);
        text.setFont(new Font("Nunito", false, false , 64));
        text.drawString("AGBOTICS", 0, 50);

        prepare();
    }

    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    public void act() {
        if (header.isFinished()) {
            if (transparency <= 252) {
                transparency += 5;
            }
            else {
                textDisplayed = true;
            }
            text.setTransparency(transparency);
            getBackground().drawImage(text, 170, 50);
        }

        if (textDisplayed) {
            if (!buttonMade) {
                startButton = new StartButton();
                addObject(startButton, 290, 380);
                startButton.getImage().setTransparency(buttonTransparency); //starts at 0
                buttonMade = true;
            }
            if (Greenfoot.mouseClicked(startButton)) {
                Greenfoot.setWorld(new Round1());
            }
        }

        if (buttonMade && buttonTransparency <= 250) {
            authorText.getImage().setTransparency(255);
            startButton.getImage().setTransparency(buttonTransparency);
            buttonTransparency += 10;
        }

        logo.setRotation(angle);
        angle += 4;
    }    

    public void createText(ShopText text, GreenfootImage image, String phrase, Color color, int size, boolean bold, boolean italic) {
        image = new GreenfootImage(250,30);
        image.setColor(color);
        //image.fill();
        image.setFont(new Font("Arial", bold, italic, size));
        image.drawString(phrase, 0, 25);
        image.setTransparency(255);
        text.setImage(image);
    }

    private void prepare()
    {
        header = new Header();
        addObject(header,106,76);
        logo = new Logo();
        addObject(logo,86,60);
        logo.setLocation(90,76);
        authorText = new ShopText();
        createText(authorText,authorImg,"By Brandon Pae",Color.CYAN,25,true,true);
        this.addObject(authorText,318,145);
        authorText.getImage().setTransparency(0);
    }
}
