import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.GreenfootImage;
import greenfoot.World;

public class WorldBackground extends Actor {
    private static final int PATTERN_SIZE = (int) (GameWorld.CELL_SIZE * 5);

    private int offsetX;
    private int offsetY;

    private int worldX;
    private int worldY;

    @Override
    protected void addedToWorld(World world) {
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

        offsetX = world.getWidth() / 2;
        offsetY = world.getHeight() / 2;
        updateLocation();
    }

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
