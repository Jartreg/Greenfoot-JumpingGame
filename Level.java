public abstract class Level {
    private final int levelNumber;
    private boolean completed;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public abstract void buildWorld(GameWorld world);

    public abstract boolean checkLevelCompleted(GameWorld world);

    public abstract int getMinEasterEggCount();

    public void reset() {

    }
}
