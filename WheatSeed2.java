import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @Brandon Pae
 * @8.9.2020
 */
public class WheatSeed2 extends Crop
{
    /**
     * For the farmer holding the wheatSeed ONLY
     * So that it doesn't mess up the other drone stuff
     */
    public WheatSeed2() {
        GreenfootImage image = getImage();
        image.scale(15, 8);
        setImage(image);
    }    
    public void act() 
    {
        // Add your action code here.
    }    
}
