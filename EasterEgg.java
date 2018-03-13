import greenfoot.Color;
import greenfoot.GreenfootImage;

public class EasterEgg extends RelativeActor {
    public EasterEgg() {
        int width = GameWorld.CELL_SIZE;
        int height = (int) (GameWorld.CELL_SIZE * 1.2);

        GreenfootImage image = new GreenfootImage(width, height);
        image.setColor(Color.RED);
        image.fillOval(0, 0, width, height);
        setImage(image);
    }

    @Override
    public boolean shouldCollideWith(RelativeActor actor) {
        return actor instanceof Character;
    }

    @Override
    protected boolean performTargetCollision(GravityActor source, boolean horizontalCollision, boolean verticalCollision) {
        getWorld().removeObject(this);
        ((Character) source).easterEggCollected();
        return true;
    }
}
