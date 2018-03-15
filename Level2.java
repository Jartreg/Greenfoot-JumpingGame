/**
 * Das zweite Level des Spiels
 */
public class Level2 extends Level {
    public Level2(int levelNumber) {
        super(levelNumber);
    }

    @Override
    public void buildWorld(GameWorld world) {
        int bottom = world.getHeight();
        int cells = GameWorld.CELL_SIZE;
        int floor = bottom - cells * 2;
        RelativeActor.Alignment bottomleft = RelativeActor.Alignment.BOTTOMLEFT;

        world.setMinX(0);
        world.setMaxX(67 * cells);

        // Boden
        world.addRelativeActor(new Wall(56, 2), cells, bottom, bottomleft);

        // Wand links und rechts
        world.addRelativeActor(new Wall(1, 40), 0, bottom, bottomleft);
        world.addRelativeActor(new Wall(1, 40), 67 * cells, bottom, bottomleft);

        // Erste Plattform
        world.addRelativeActor(new Wall(10, 1), 5 * cells, floor - 4 * cells, bottomleft);
        world.addRelativeActor(new SlimeWall(3, 3), 15 * cells, floor - 4 * cells, bottomleft);

        // Zweite Plattform
        world.addRelativeActor(new IceWall(8, 1), 20 * cells, floor - 11 * cells, bottomleft);

        // Dritte Plattform
        world.addRelativeActor(new Wall(3, 4), 4 * cells, floor - 14 * cells, bottomleft);
        world.addRelativeActor(new Wall(4, 1), 7 * cells, floor - 14 * cells, bottomleft);
        world.addRelativeActor(new EasterEgg(), 5.5 * cells, floor - 18 * cells, RelativeActor.Alignment.BOTTOMCENTER);

        // Eisweg und Schleimblock
        world.addRelativeActor(new IceWall(20, 1), 16 * cells, floor - 20 * cells, bottomleft);
        world.addRelativeActor(new SlimeWall(5, 5), 39 * cells, floor - 7 * cells, bottomleft);

        // Eisplattform vor dem Ziel
        world.addRelativeActor(new IceWall(10, 1), 47 * cells, floor - 15 * cells, bottomleft);
        world.addRelativeActor(new Spikes(Spikes.Direction.LEFT, 3), 56.5 * cells, floor - 16 * cells, bottomleft);

        // Hindernisse unter der zweiten Plattform
        world.addRelativeActor(new Spikes(Spikes.Direction.TOP, 1), cells * 28, floor, bottomleft);
        world.addRelativeActor(new Spikes(Spikes.Direction.TOP, 2), cells * 32, floor, bottomleft);

        // Osterei unter dem Ziel
        world.addRelativeActor(new Wall(8, 2), 49 * cells, floor, bottomleft);
        world.addRelativeActor(new Wall(5, 1), 52 * cells, floor - 2 * cells, bottomleft);
        world.addRelativeActor(new EasterEgg(), 55.5 * cells, floor - 3 * cells, RelativeActor.Alignment.BOTTOMCENTER);

        // Ziel
        world.addRelativeActor(new Wall(10, 21), 57 * cells, bottom, bottomleft);
        world.addRelativeActor(new Exit(), 62.5 * cells, bottom - 21 * cells, RelativeActor.Alignment.BOTTOMCENTER);

        world.addRelativeActor(new Character(), 20 * cells, floor, bottomleft);
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
