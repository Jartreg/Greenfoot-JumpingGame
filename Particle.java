import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

public class Particle extends GravityActor {
    private final int radius;

    public Particle(GreenfootImage of) {
        int maxSize = Math.min(Math.min(of.getWidth(), of.getHeight()), 16);
        int size = Math.min((Greenfoot.getRandomNumber(maxSize) / 2) + 6, maxSize);
        radius = size / 2;

        GreenfootImage image = new GreenfootImage(size, size);
        image.drawImage(
                of,
                -Greenfoot.getRandomNumber(of.getWidth() - size + 1),
                -Greenfoot.getRandomNumber(of.getHeight() - size + 1)
        );
        setImage(image);
    }

    public Particle(double vx, double vy, GreenfootImage image) {
        this(image);
        vX = vx;
        vY = vy;
    }

    @Override
    public void gameAct() {
        super.gameAct();

        if (getY() > getWorld().getHeight() + radius)
            getWorld().removeObject(this);
    }

    @Override
    public boolean shouldCollideWith(RelativeActor actor) {
        return false;
    }
}
