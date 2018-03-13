public class Level2 extends Level {
    protected Level2(int levelNumber) {
        super(levelNumber);
    }

    @Override
    public void buildWorld(GameWorld world) {
        int bottom = world.getHeight();
        int cells = GameWorld.CELL_SIZE;
        int floor = bottom - cells * 2;

        world.setMinX(0);
        world.setMaxX(1072);

        world.addRelativeActor(new Wall(56, 2), cells, bottom, RelativeActor.Alignment.BOTTOMLEFT);

        world.addRelativeActor(new Character(), 300, floor, RelativeActor.Alignment.BOTTOMCENTER);

        world.addRelativeActor(new Wall(1, 50), 8, 0);
        world.addRelativeActor(new Wall(1, 50), 1080, 0);

        world.addRelativeActor(new Wall(10, 1), 160, 296);
        world.addRelativeActor(new SlimeWall(3, 3), 264, 280);

        world.addRelativeActor(new IceWall(8, 1), 386, 184);

        world.addRelativeActor(new Spikes(Spikes.Direction.TOP, 1), cells * 28, floor, RelativeActor.Alignment.BOTTOMLEFT);
        world.addRelativeActor(new Spikes(Spikes.Direction.TOP, 2), cells * 32, floor, RelativeActor.Alignment.BOTTOMLEFT);

        world.addRelativeActor(new Wall(4, 1), 144, 136);
        world.addRelativeActor(new Wall(3, 4), 88, 112);

        world.addRelativeActor(new IceWall(20, 1), 416, 40);
        world.addRelativeActor(new SlimeWall(5, 5), 664, 216);
        world.addRelativeActor(new IceWall(10, 1), 832, 120);

        world.addRelativeActor(new Wall(8, 2), 848, 352);
        world.addRelativeActor(new Wall(5, 1), 872, 328);
        world.addRelativeActor(new Wall(10, 21), 912, bottom, RelativeActor.Alignment.BOTTOMLEFT);

        world.addRelativeActor(new Spikes(Spikes.Direction.LEFT, 3), 904, 112, RelativeActor.Alignment.BOTTOMLEFT);

        world.addRelativeActor(new EasterEgg(), 88, 80, RelativeActor.Alignment.BOTTOMCENTER);
        world.addRelativeActor(new EasterEgg(), cells * 55.5, floor - cells * 3, RelativeActor.Alignment.BOTTOMCENTER);

        world.addRelativeActor(new Exit(), 912 + cells * 5, bottom - cells * 21, RelativeActor.Alignment.BOTTOMCENTER);
    }

    @Override
    public boolean checkLevelCompleted(GameWorld world) {
        return world.getCharacter().getEasterEggCount() == 2;
    }

    @Override
    public int getMinEasterEggCount() {
        return 2;
    }
}
