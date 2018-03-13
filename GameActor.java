import greenfoot.Actor;
import greenfoot.World;

public class GameActor extends Actor {
    private GameWorld world;
    private boolean gameRunning;

    public void act() {
        if (gameRunning)
            gameAct();
    }

    public void gameAct() {

    }

    public void updateGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    @Override
    protected void addedToWorld(World world) {
        this.world = (GameWorld) world;
    }

    @Override
    public GameWorld getWorld() {
        return world;
    }
}
