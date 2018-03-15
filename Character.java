import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

/**
 * Die Spielfigur
 */
public class Character extends GravityActor {
    private static final int MAX_EDGE_OFFSET_X = 200;
    private static final int MAX_EDGE_OFFSET_Y = 100;
    private static final double AIR_ACCELERATION = 0.15;
    private static final int JUMP_ACCELERATION = 15;
    private static final double MAX_SPEED = 5;

    private boolean damaged = false;
    private int easterEggCount = 0;

    private int successJump = 0;

    public Character() {
        GreenfootImage bg = new GreenfootImage(GameWorld.CELL_SIZE, GameWorld.CELL_SIZE);
        Wall.paintWall(bg, 0, 0, 1, 1, Color.ORANGE);
        setImage(bg);
    }

    @Override
    public void gameAct() {
        super.gameAct();
        GameWorld world = getWorld();

        // Aus der Welt gefallen? -> verloren
        if (getY() > getWorld().getHeight() + GameWorld.CELL_SIZE) {
            world.levelCompleted(false);
        }

        // Wenn der Spieler beschädigt wurde, ist keine Steuerung mehr aktiv und er fällt einfach aus der Welt
        if (damaged) {
            return;
        }

        // Bewegen der Welt in X-Richtung
        int x = getX();
        if (x < MAX_EDGE_OFFSET_X) {
            world.setWorldX(world.getWorldX() - (MAX_EDGE_OFFSET_X - x));
        } else {
            int rightEdge = world.getWidth() - MAX_EDGE_OFFSET_X;
            if (x > rightEdge) {
                world.setWorldX(world.getWorldX() + (x - rightEdge));
            }
        }

        // Bewegen der Welt in Y-Richtung
        int y = getY();
        if (y < MAX_EDGE_OFFSET_Y) {
            world.setWorldY(world.getWorldY() - (MAX_EDGE_OFFSET_Y - y));
        } else {
            int bottomEdge = world.getHeight() - MAX_EDGE_OFFSET_Y;
            if (y > bottomEdge) {
                world.setWorldY(world.getWorldY() + (y - bottomEdge));
            }
        }

        if (successJump == 0) { // Tastensteuerung
            boolean a = Greenfoot.isKeyDown("a");
            boolean d = Greenfoot.isKeyDown("d");

            // Durch die Steuerung wird die Geschwindigkeit bis auf ein Maximum beschleunigt

            if (isOnGround()) {
                RelativeActor ground = getCollidingGround();
                double friction = ground == null ? DEFAULT_FRICTION : ground.getFriction();
                double acceleration = (friction > 1 ? 1 / friction : friction) * 1.2;

                if (a && vX > -MAX_SPEED)
                    vX -= 2 * acceleration;

                if (d && vX < MAX_SPEED)
                    vX += 2 * acceleration;

                if (Greenfoot.isKeyDown("space")) // Springen
                    vY = JUMP_ACCELERATION;
            } else { // In der Luft wird auch Steuerung Zugelassen
                if (a && vX > -MAX_SPEED)
                    vX -= AIR_ACCELERATION;

                if (d && vX < MAX_SPEED)
                    vX += AIR_ACCELERATION;
            }
        } else if (isOnGround()) { // Siegesanimation
            vX = 0;
            vY = 15;
        }
    }

    @Override
    protected void onGroundChanged() {
        if (isOnGround()) {
            // Bei Fall aus großer Höhe: Erschütterung
            if (!(getCollidingGround() instanceof SlimeWall) && (getFallDistance() > 200))
                getWorld().shake(7, (int) (getFallDistance() / 200 * 15));

            // Siegesanimation
            if (successJump != 0) {
                if (successJump == 1) { // Ende der Siegesanimation
                    getWorld().levelCompleted(true);
                } else {
                    successJump--;
                }
            }
        }
    }

    @Override
    public boolean shouldCollideWith(RelativeActor actor) {
        return !damaged; // Nach Beschädigung wird einfach runtergefallen
    }

    /**
     * Wird von {@link Spikes} aufgerufen, wenn der Spieler beschädigt wurde
     */
    public void spikeDamage() {
        damaged = true;
        vX = 0;
        vY = 10;

        GameWorld world = getWorld();
        world.shake(20, 50);
        world.completedScreenshot = world.createScreenshot(); // Screenshot
    }

    /**
     * Startet die Siegesanimation und beendet das Level danach erfolgreich
     */
    public void startSuccessAnimation() {
        if (successJump > 0)
            return;

        successJump = 3;

        GameWorld world = getWorld();
        world.completedScreenshot = world.createScreenshot(); // Screenshot
    }

    /**
     * Wird von {@link EasterEgg} aufgerufen, wenn es gesammelt wurde
     */
    public void easterEggCollected() {
        spawnActionText("+1");
        easterEggCount++;
        getWorld().getEggCounter().setEggCount(easterEggCount);
    }

    /**
     * Gibt die Anzahl der gesammelten Eiern zurück
     * @return die Anzahl der gesammelten Eier
     */
    public int getEasterEggCount() {
        return easterEggCount;
    }
}
