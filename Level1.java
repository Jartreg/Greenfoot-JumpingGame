import greenfoot.GreenfootImage;

public class Level1 extends Level {
    private boolean firstRun = true;

    protected Level1(int levelNumber) {
        super(levelNumber);
    }

    @Override
    public void buildWorld(GameWorld world) {
        if (firstRun) {
            firstRun = false;
            world.addObject(new HelpActor(), 0, 0);
        }

        int bottom = world.getHeight();
        int cells = GameWorld.CELL_SIZE;

        world.setMinX(0);

        world.addRelativeActor(new Wall(7, 2), 5 * cells, bottom, RelativeActor.Alignment.BOTTOMLEFT);
        world.addRelativeActor(new Character(), 7 * cells, bottom - 2 * cells, RelativeActor.Alignment.BOTTOMLEFT);

        int x = 18 * cells;
        int y = bottom - 3 * cells;
        for (int i = 0; i < 6; i++) {
            world.addRelativeActor(new Wall(4, 2), x, y, RelativeActor.Alignment.BOTTOMLEFT);
            world.addRelativeActor(new EasterEgg(), x + 2 * cells, y - 2 * cells, RelativeActor.Alignment.BOTTOMCENTER);
            x += 10 * cells;
            y -= 3 * cells;
        }

        world.addRelativeActor(new IceWall(4, 2), x, y, RelativeActor.Alignment.BOTTOMLEFT);
        world.addRelativeActor(new IceWall(11, 1), x, y + cells, RelativeActor.Alignment.BOTTOMLEFT);
        x += 4 * cells;
        world.addRelativeActor(new Spikes(Spikes.Direction.TOP, 3), x, y, RelativeActor.Alignment.BOTTOMLEFT);
        x += 3 * cells;
        world.addRelativeActor(new IceWall(4, 2), x, y, RelativeActor.Alignment.BOTTOMLEFT);
        x += (4 + 7) * cells;
        world.addRelativeActor(new SlimeWall(7, 1), x, bottom, RelativeActor.Alignment.BOTTOMLEFT);
        x += (7 + 7) * cells;
        y += 7 * cells;
        world.addRelativeActor(new Wall(10, 2), x, y, RelativeActor.Alignment.BOTTOMLEFT);
        world.addRelativeActor(new Exit(), x + 7 * cells, y - 2 * cells, RelativeActor.Alignment.BOTTOMCENTER);
    }

    @Override
    public boolean checkLevelCompleted(GameWorld world) {
        return true;
    }

    @Override
    public int getMinEasterEggCount() {
        return 0;
    }

    private class HelpActor extends GameActor {
        private String[] usageHints = {
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
