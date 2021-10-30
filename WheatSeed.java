import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @Brandon Pae
 * @8.9.2020
 */
public class WheatSeed extends Crop
{
    /**
     * Act - do whatever the WheatSeed wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public WheatSeed() {
        GreenfootImage image = getImage();
        image.scale(15, 8);
        setImage(image);
    }    
    public void act() 
    {
        // Add your action code here.
    }    
    public Tile getTile() {
        return (Tile)getOneIntersectingObject(Tile.class);
    }
}
