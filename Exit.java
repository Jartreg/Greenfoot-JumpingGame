import greenfoot.Color;
import greenfoot.GreenfootImage;

/**
 * Das Ziel eines Levels.
 * Beim erreichen wird mit {@link Level#checkLevelCompleted(GameWorld)} überprüft,
 * ob der Spieler das Level gewonnen hat.
 */
public class Exit extends RelativeActor {
    public Exit() {
        GreenfootImage image = new GreenfootImage(GameWorld.CELL_SIZE, GameWorld.CELL_SIZE + 8);

        // Fahnenstange
        image.setColor(Color.DARK_GRAY);
        image.drawLine(8, 0, 8, 24);

        // Fahne
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
        if (world.getCurrentLevel().checkLevelCompleted(world)) { // Wenn gewonnen
            world.getCharacter().startSuccessAnimation(); // Animation und beenden
        }

        return true;
    }
}
