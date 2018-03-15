import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

/**
 * Ein Partikel mit zufälliger Größe und einem Ausschnitt des Bildes des Ursprungs
 */
public class Particle extends GravityActor {
    private final int radius;

    /**
     * Erstellt einen Partikel
     * @param of das Bild des Ursprungs
     */
    public Particle(GreenfootImage of) {
        // Größe bestimmen
        int maxSize = Math.min(Math.min(of.getWidth(), of.getHeight()), 16);
        int size = Math.min((Greenfoot.getRandomNumber(maxSize) / 2) + 6, maxSize);
        radius = size / 2;

        // Einen Ausschnitt des Bildes kopieren
        GreenfootImage image = new GreenfootImage(size, size);
        image.drawImage(
                of,
                -Greenfoot.getRandomNumber(of.getWidth() - size + 1),
                -Greenfoot.getRandomNumber(of.getHeight() - size + 1)
        );
        setImage(image);
    }

    /**
     * Erstellt einen Partikel mit einer Bewegung
     * @param vx die horizontale Bewegung
     * @param vy die vertikale Bewegung
     * @param image das Bild des Ursprungs
     */
    public Particle(double vx, double vy, GreenfootImage image) {
        this(image);
        vX = vx;
        vY = vy;
    }

    @Override
    public void gameAct() {
        super.gameAct();

        // Aßerhalb der Welt entfernen
        if (getY() > getWorld().getHeight() + radius)
            getWorld().removeObject(this);
    }

    @Override
    public boolean shouldCollideWith(RelativeActor actor) {
        return false; // Keine Kollisionen
    }
}
