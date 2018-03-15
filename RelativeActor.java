import greenfoot.World;

/**
 * Ein Actor, der relativ zur eigentlichen Spielwelt platziert ist.
 * Wenn sich der sichtbare Ausschnitt der Welt ändert, wird sich der Actor mitbewegen.
 */
public class RelativeActor extends GameActor {
    public static final double DEFAULT_FRICTION = 1.2;

    private double relativeX;
    private double relativeY;

    private int worldX;
    private int worldY;

    private void updateLocation() {
        super.setLocation((int) (relativeX - worldX), (int) (relativeY - worldY));
    }

    /**
     * Wird von {@link GameWorld} aufgerufen, wenn sich die Position des sichtbaren Bereichs geändert hat.
     * @param worldX die X-Koordinate des sichtbaren Bereichs
     * @param worldY die Y-Koordinate des sichtbaren Bereichs
     */
    void updateWorldLocation(double worldX, double worldY) {
        this.worldX = (int) worldX;
        this.worldY = (int) worldY;
        updateLocation();
    }

    /**
     * Setzt die relative Position des Actors
     * @param x die X-Koordinate
     * @param y die Y-Koordinate
     */
    public void setLocation(double x, double y) {
        this.relativeX = x;
        this.relativeY = y;
        updateLocation();
    }

    /**
     * Setzt die relative Position eines bestimmten Punktes des Actors
     * @param x die X-Koordinate
     * @param y die Y-Koordinate
     * @param alignment woran sich der Actor ausrichten soll
     */
    public void setLocation(double x, double y, Alignment alignment) {
        switch (alignment) {
            case BOTTOMLEFT:
                x += getImage().getWidth() / 2;
            case BOTTOMCENTER:
                y -= getImage().getHeight() / 2;
                break;
            case TOPLEFT:
                x += getImage().getWidth() / 2;
                y += getImage().getHeight() / 2;
                break;
        }

        setLocation(x, y);
    }

    @Override
    public void setLocation(int x, int y) {
        setLocation((double) x + worldX, (double) y + worldY);
    }

    /**
     * Gibt die relative X-Koordinate des Actors zurück
     * @return die relative X-Koordinate
     */
    public double getRelativeX() {
        return relativeX;
    }

    /**
     * Gibt die relative Y-Koordinate des Actors zurück
     * @return die relative Y-Koordinate
     */
    public double getRelativeY() {
        return relativeY;
    }

    /**
     * Gibt zurück, ob dieses Objekt mit einem Actor kollidieren soll.
     * @param actor des Actor
     * @return ob eine Kollision stattfinden soll
     */
    public boolean shouldCollideWith(RelativeActor actor) {
        return false;
    }

    /**
     * Wird auf dem Actor aufgerufen, gegen den gestoßen wird.
     * @param source der auftreffende Actor
     * @param horizontalCollision ob der Actor von oben oder unten kommt
     * @param verticalCollision ob der Actor von links oder rechts kommt
     * @return true, wenn <code>source</code> nicht Aufgehalten wird und sich weiterbewegen kann.
     */
    protected boolean performTargetCollision(GravityActor source, boolean horizontalCollision, boolean verticalCollision) {
        if (horizontalCollision)
            source.vY = 0;

        if (verticalCollision)
            source.vX = 0;

        return false;
    }

    /**
     * Gibt die Reibung dieses Actors zurück
     * @return die Reibung
     */
    public double getFriction() {
        return DEFAULT_FRICTION;
    }

    /**
     * Zeigt über diesem Actor einen {@link ActionText} an.
     * @param text der anzuzeigende Text
     * @return der {@link ActionText}
     */
    public ActionText spawnActionText(String text) {
        ActionText actionText = new ActionText(text);
        getWorld().addRelativeActor(
                actionText,
                getRelativeX(),
                getRelativeY() - getImage().getHeight() / 2,
                Alignment.BOTTOMCENTER
        );

        return actionText;
    }

    /**
     * Woran sich {@link #setLocation(double, double, Alignment)} ausrichten kann
     */
    public enum Alignment {
        BOTTOMLEFT,
        BOTTOMCENTER,
        TOPLEFT,
        CENTER
    }
}
