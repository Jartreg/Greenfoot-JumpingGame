import greenfoot.GreenfootImage;

/**
 * Das erste Level des Spiels
 */
public class Level1 extends Level {
    private boolean firstRun = true;

    public Level1(int levelNumber) {
        super(levelNumber);
    }

    @Override
    public void buildWorld(GameWorld world) {
        if (firstRun) { // Beim ersten Mal wird Hilfe zum Steuern gegeben
            firstRun = false;
            world.addObject(new HelpActor(), 0, 0);
        }

        int bottom = world.getHeight();
        int cells = GameWorld.CELL_SIZE;
        RelativeActor.Alignment bottomleft = RelativeActor.Alignment.BOTTOMLEFT;

        world.setMinX(0);

        // Startplattform
        world.addRelativeActor(new Wall(7, 2), 5 * cells, bottom, bottomleft);
        world.addRelativeActor(new Character(), 7 * cells, bottom - 2 * cells, bottomleft);

        // Plattformen nach oben
        int x = 18 * cells;
        int y = bottom - 3 * cells;
        for (int i = 0; i < 6; i++) {
            world.addRelativeActor(new Wall(4, 2), x, y, bottomleft);
            world.addRelativeActor(new EasterEgg(), x + 2 * cells, y - 2 * cells, RelativeActor.Alignment.BOTTOMCENTER);
            x += 10 * cells;
            y -= 3 * cells;
        }

        // Eisplattform
        world.addRelativeActor(new IceWall(4, 2), x, y, bottomleft);
        world.addRelativeActor(new IceWall(11, 1), x, y + cells, bottomleft);
        x += 4 * cells;
        world.addRelativeActor(new Spikes(Spikes.Direction.TOP, 3), x, y, bottomleft);
        x += 3 * cells;
        world.addRelativeActor(new IceWall(4, 2), x, y, bottomleft);

        // Schleimboden
        x += (4 + 7) * cells;
        world.addRelativeActor(new SlimeWall(7, 1), x, bottom, bottomleft);

        // Ziel
        x += (7 + 7) * cells;
        y += 7 * cells;
        world.addRelativeActor(new Wall(10, 2), x, y, bottomleft);
        world.addRelativeActor(new Exit(), x + 7.5 * cells, y - 2 * cells, RelativeActor.Alignment.BOTTOMCENTER);
    }

    @Override
    public boolean checkLevelCompleted(GameWorld world) {
        return true;
    }

    @Override
    public int getMinEasterEggCount() {
        return 0;
    }

    /**
     * Zeigt über dem Spieler Tipps zur Steuerung an
     */
    private class HelpActor extends GameActor {
        private final String[] usageHints = {
                "A und D zum Bewegen",
                "Leertaste zum springen"
        };

        private int i = 0;
        private int time = 0;

        public HelpActor() {
            setImage((GreenfootImage) null);
        }

        @Override
        public void gameAct() {
            Character character = getWorld().getCharacter();

            // Wenn der Spieler schon losgelaufen ist, sind Tipps unnötig
            if (character.getRelativeX() > 18 * GameWorld.CELL_SIZE) {
                getWorld().removeObject(this);
            }

            if (time == 0) {
                character.spawnActionText(usageHints[i]);
                i++;
                if (i == usageHints.length)
                    getWorld().removeObject(this);
            }

            time++;
            if (time == 200) {
                time = 0;
            }
        }
    }
}
