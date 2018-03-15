import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.GreenfootImage;
import greenfoot.World;

/**
 * Der Hintergrund der Welt
 */
public class WorldBackground extends Actor {
    private static final int PATTERN_SIZE = (int) (GameWorld.CELL_SIZE * 5);

    private int offsetX;
    private int offsetY;

    private int worldX;
    private int worldY;

    @Override
    protected void addedToWorld(World world) {
        // Das Bild muss die Welt nach außen um zwei Mustergrößen abdecken
        GreenfootImage bg = new GreenfootImage(
                world.getWidth() + PATTERN_SIZE * 2,
                world.getHeight() + PATTERN_SIZE * 2
        );
        Wall.paintWall(
                bg,
                0,
                0,
                (int) Math.ceil(world.getWidth() / (float) PATTERN_SIZE) + 2,
                (int) Math.ceil(world.getHeight() / (float) PATTERN_SIZE) + 2,
                new Color(220, 220, 220),
                PATTERN_SIZE
        );
        setImage(bg);

        // Der Mittelpunkt der Welt ist die Standardposition
        offsetX = world.getWidth() / 2;
        offsetY = world.getHeight() / 2;
        updateLocation();
    }

    /**
     * Wird von {@link GameWorld} aufgerufen, wenn sich die Position des sichtbaren Bereichs geändert hat.
     * @param worldX die X-Koordinate des sichtbaren Bereichs
     * @param worldY die Y-Koordinate des sichtbaren Bereichs
     */
    void updateWorldLocation(double worldX, double worldY) {
        this.worldX = (int) worldX;
        this.worldY = (int) worldY;
        updateLocation();
    }

    private void updateLocation() {
        setLocation(
                offsetX - (worldX / 2) % PATTERN_SIZE,
                offsetY - (worldY / 2) % PATTERN_SIZE
        );
    }
}
