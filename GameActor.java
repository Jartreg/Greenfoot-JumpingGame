import greenfoot.Actor;
import greenfoot.World;

/**
 * Basisklasse f�r alles, das auf die {@link GameWorld} zugreifen muss
 * oder vom Spielzustand beeinflusst wird
 */
public class GameActor extends Actor {
    private GameWorld world;
    private boolean gameRunning;

    public void act() {
        if (gameRunning) // Nur beim laufenden Spiel
            gameAct();
    }

    /**
     * Wie {@link Actor#act()}, wird aber nur bei laufendem Spiel aufgerufen.
     */
    public void gameAct() {

    }

    /**
     * Wird von {@link GameWorld} aufgerufen, wenn sich der Spielzustand ge�ndert hat.
     * @param gameRunning ob das Spiel aktuell l�uft
     */
    public void updateGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    @Override
    protected void addedToWorld(World world) {
        this.world = (GameWorld) world;
    }

    // Erm�glicht es, in Unterklassen mit getWorld() direkt auf die GameWorld zuzugreifen
    @Override
    public GameWorld getWorld() {
        return world;
    }
}
