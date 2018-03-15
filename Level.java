/**
 * Die Basisklasse f�r Levels
 */
public abstract class Level {
    private final int levelNumber;
    private boolean completed;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    /**
     * Gibt die Nummer des Levels zur�ck
     * @return die Nummer des Levels
     */
    public int getLevelNumber() {
        return levelNumber;
    }

    /**
     * Gibt zur�ck, ob das Level schonmal vom Spieler gewonnen wurde
     * @return ob der Spieler das Level schonmal gewonnen hat
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Legt fest, ob das Level schonmal vom Spieler gewonnen wurde
     * @param completed ob der Spieler das Level schonmal gewonnen hat
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Baut das Level in der Welt auf.
     * @param world die Welt, in die das Level gebaut werden soll
     */
    public abstract void buildWorld(GameWorld world);

    /**
     * �berpr�ft, ob alle Aufgaben des Levels erf�llt wurden.
     * Wird beim Erreichen des Ziels ({@link Exit}) verfendet.
     * @param world die Welt, in der sich das Level befindet
     * @return ob der Spieler das Level gewonnen hat
     */
    public abstract boolean checkLevelCompleted(GameWorld world);

    /**
     * Gibt die minimale Anzahl an Ostereiern zur�ck, die der Spieler einsammeln soll.
     * Diese Zahl wird nur am Bildschirmrand im {@link EggCounter} angezeigt und hat keine Bedeutung f�r den Spielverlauf.
     * Wenn die Zahl 0 ist, wird sich nicht angezeigt.
     * @return die minimale Anzahl an Ostereiern, die der Spieler einsammeln soll
     */
    public abstract int getMinEasterEggCount();
}
