import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Write a description of class Pickaxe here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Pickaxe extends Simple
{

    private GreenfootImage image;
    private boolean isTouching = false;
    private boolean isVisible = false;

    private double pickaxeHitDuration = 0;

    private double[] r = {86, 116, 170, 140, 156, 92, 88, 20};
    private double[] g = {56, 64, 88, 118, 138, 132, 172, 166};
    private double[] b = {44, 44, 50, 66, 38, 48, 46, 50};
    
    //private double[] r = {85, 115, 169, 139, 156, 91, 88, 19};
    //private double[] g = {55, 63, 87, 117, 137, 132, 171, 166};
    //private double[] b = {43, 44, 49, 65, 37, 47, 45, 50};
    private int i = 0;

    private double rVal = r[0];
    private double gVal = g[0];
    private double bVal = b[0];

    private int timer = 0;
    private boolean goToNextIteration = false;

    private double rDif = 0;
    private double gDif = 0;
    private double bDif = 0;
    private int iterations = 0;
    
    //the lower the efficiency value, the more effective it is
    //since the rgb values are all even numbers, min difference is 2
    //so these efficiency values need to divide 2 evenly (so that 2 / efficiency is a finite decimal)
    //possible values: 1, 2, 4, 8, 16, 20, 25, 32, 40, 50
    private double efficiency = 25;
    private boolean hasSpawnedCrop = false;
    public boolean tileSaysVisible = false;

    public Pickaxe() {
        image = getImage();
        image.scale(25, 25);
        setImage(image);
    }    

    public void act() 
    {
        if (this.getImage().getTransparency() == 255) {
            isVisible = true;
        }    
        else {
            isVisible = false;
        }
    }    

    public void farmingTile() {
        Tile touchedTile = (Tile)this.getOneIntersectingObject(Tile.class);

        if (touchedTile != null && this.isVisible) { //the tool must be equipped for it to count
            isTouching = true;
            
            i = touchedTile.getI();
            rVal = touchedTile.getR();
            gVal = touchedTile.getG();
            bVal = touchedTile.getB();
        }
        else {
            isTouching = false;
        }    

        if (isTouching) {
            if (i < r.length - 1) {
                
                rVal += rDif;
                gVal += gDif;
                bVal += bDif;
                
                touchedTile.setRGB(rVal, gVal, bVal);

                if (Math.round(rVal) == r[i+1] && Math.round(gVal) == g[i+1] && Math.round(bVal) == b[i+1]) {
                    touchedTile.incrementI();
                }
            }
            
            touchedTile.getImage().setColor(new Color((int)Math.round(rVal), (int)Math.round(gVal), (int)Math.round(bVal)));
            touchedTile.getImage().fill();

        }
    }
    public boolean isVisible() {
        return isVisible;
    }    
    public void setEfficiency(double e) {
        efficiency = e;
    }
    public double getEfficiency() {
        return this.efficiency;
    }
    public java.util.List<Tile> getIntersectingTiles() {
        return this.getIntersectingObjects(Tile.class);
    }
}
