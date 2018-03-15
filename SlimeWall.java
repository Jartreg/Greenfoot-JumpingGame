import greenfoot.Color;
import greenfoot.GreenfootImage;

/**
 * Eine Schleimwand, von der der Spieler abprallt.
 * Beim Laufen ist der Spieler langsamer.
 */
public class SlimeWall extends Wall {
    public static final Color COLOR = new Color(107, 225, 107);
    public static final double FRICTION = DEFAULT_FRICTION * 1.5;

    public SlimeWall(int width, int height) {
        super(width, height);
    }

    @Override
    protected boolean performTargetCollision(GravityActor source, boolean horizontalCollision, boolean verticalCollision) {
        // Den Spieler abprallen lassen

        if (horizontalCollision) {
            double vy = -source.vY;

            if (vy < 2 && vy > 2) {
                vy = 0;
            } else if (vy < 0) {
                vy += 2;
            } else {
                vy -= 2;
            }

            source.vY = vy;
        }

        if (verticalCollision) {
            double vx = -source.vX;

            if (vx < 2 && vx > 2) {
                vx = 0;
            } else if (vx < 0) {
                vx += 2;
            } else {
                vx -= 2;
            }

            source.vX = vx;
        }

        return false;
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
