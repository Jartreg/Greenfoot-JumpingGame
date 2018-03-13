import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

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

        if (getY() > getWorld().getHeight() + GameWorld.CELL_SIZE) {
            world.levelCompleted(false);
        }

        if (damaged) {
            return;
        }

        // Bewegen der Welt
        int x = getX();
        if (x < MAX_EDGE_OFFSET_X) {
            world.setWorldX(world.getWorldX() - (MAX_EDGE_OFFSET_X - x));
        } else {
            int rightEdge = world.getWidth() - MAX_EDGE_OFFSET_X;
            if (x > rightEdge) {
                world.setWorldX(world.getWorldX() + (x - rightEdge));
            }
        }

        int y = getY();
        if (y < MAX_EDGE_OFFSET_Y) {
            world.setWorldY(world.getWorldY() - (MAX_EDGE_OFFSET_Y - y));
        } else {
            int bottomEdge = world.getHeight() - MAX_EDGE_OFFSET_Y;
            if (y > bottomEdge) {
                world.setWorldY(world.getWorldY() + (y - bottomEdge));
            }
        }

        if (successJump == 0) {
            // Tastensteuerung
            boolean a = Greenfoot.isKeyDown("a");
            boolean d = Greenfoot.isKeyDown("d");

            if (isOnGround()) {
                RelativeActor ground = getCollidingGround();
                double friction = ground == null ? DEFAULT_FRICTION : ground.getFriction();
                double acceleration = (friction > 1 ? 1 / friction : friction) * 1.2;

                if (a && vX > -MAX_SPEED) vX -= 2 * acceleration;
                if (d && vX < MAX_SPEED) vX += 2 * acceleration;

                if (Greenfoot.isKeyDown("space"))
                    vY = JUMP_ACCELERATION;
            } else {
                if (a && vX > -MAX_SPEED) vX -= AIR_ACCELERATION;
                if (d && vX < MAX_SPEED) vX += AIR_ACCELERATION;
            }
        } else if (isOnGround()) {
            vX = 0;
            vY = 15;
        }
    }

    @Override
    protected void onGroundChanged() {
        if (isOnGround()) {
            if (!(getCollidingGround() instanceof SlimeWall) && (getFallDistance() > 200))
                getWorld().shake(7, (int) (getFallDistance() / 200 * 15));

            if (successJump != 0) {
                if (successJump == 1) {
                    getWorld().levelCompleted(true);
                } else {
                    successJump--;
                }
            }
        }
    }

    @Override
    public boolean shouldCollideWith(RelativeActor actor) {
        return !damaged;
    }

    public void spikeDamage() {
        damaged = true;
        vX = 0;
        vY = 10;

        GameWorld world = getWorld();
        world.shake(20, 50);
        world.completedScreenshot = world.createScreenshot();
    }

    public void startSuccessAnimation() {
        if (successJump > 0)
            return;

        successJump = 3;

        GameWorld world = getWorld();
        world.completedScreenshot = world.createScreenshot();
    }

    public void easterEggCollected() {
        spawnActionText("+1");
        easterEggCount++;
        getWorld().getEggCounter().setEggCount(easterEggCount);
    }

    public int getEasterEggCount() {
        return easterEggCount;
    }
}
