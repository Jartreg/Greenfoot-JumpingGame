import greenfoot.Color;
import greenfoot.GreenfootImage;

public class Exit extends RelativeActor {
    public Exit() {
        GreenfootImage image = new GreenfootImage(GameWorld.CELL_SIZE, GameWorld.CELL_SIZE + 8);
        image.setColor(Color.DARK_GRAY);
        image.drawLine(8, 0, 8, 24);

        image.setColor(Color.RED);
        image.fillPolygon(
                new int[]{8, 0, 8},
                new int[]{0, 4, 8},
                3
        );

        setImage(image);
    }

    @Override
    public boolean shouldCollideWith(RelativeActor actor) {
        return actor instanceof Character && ((Character) actor).isOnGround();
    }

    @Override
    protected boolean performTargetCollision(GravityActor source, boolean horizontalCollision, boolean verticalCollision) {
        GameWorld world = getWorld();
        if (world.getCurrentLevel().checkLevelCompleted(world)) {
            world.getCharacter().startSuccessAnimation();
        }

        return true;
    }
}
