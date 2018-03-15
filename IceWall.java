import greenfoot.Color;
import greenfoot.GreenfootImage;

/**
 * Eine Wand aus Eis, auf der der Spieler wenig Halt hat und ruscht.
 */
public class IceWall extends Wall {
    public static final Color COLOR = new Color(137, 198, 225);
    public static final double FRICTION = DEFAULT_FRICTION * 0.02;

    public IceWall(int width, int height) {
        super(width, height);
    }

    @Override
    public Color getWallColor() {
        return COLOR;
    }

    @Override
    public double getFriction() {
        return FRICTION;
    }
}
