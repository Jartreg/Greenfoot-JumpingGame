import greenfoot.Color;
import greenfoot.GreenfootImage;

/**
 * Stacheln, bei denen der Spieler, wenn er sie berührt, das Level verloren hat.
 */
public class Spikes extends RelativeActor {
    /**
     * Erstellt eine bestimmte Anzahl an Stacheln in eine bestimmte Richtung
     * @param direction die Richtung
     * @param cells die Anzahl der Stacheln
     */
    public Spikes(Direction direction, int cells) {
        this(direction, cells, Color.LIGHT_GRAY);
    }

    /**
     * Erstellt eine bestimmte Anzahl an Stacheln in eine bestimmte Richtung
     * @param direction die Richtung
     * @param cells die Anzahl der Stacheln
     * @param color die Farbe der Stacheln
     */
    public Spikes(Direction direction, int cells, Color color) {
        int length = GameWorld.CELL_SIZE * cells;
        int thickness = GameWorld.CELL_SIZE / 2;

        // Ein Bild, auf dem die Stacheln alle nach oben zeigen
        GreenfootImage spikes = new GreenfootImage(length, length);
        // bei links unt unten muss das Licht anders gezeichnet werden
        boolean invertLight = direction == Direction.LEFT || direction == Direction.BOTTOM;
        paintSpikes(spikes, cells, color, invertLight); // zeichnen

        // Das eigentliche Bild
        GreenfootImage image;
        switch (direction) {
            case TOP:
            case BOTTOM:
                image = new GreenfootImage(length, thickness); // Horizontal
                break;
            default: // LEFT || RIGHT
                image = new GreenfootImage(thickness, length); // Vertikal
                break;
        }

        // Das erste Bild wird gedreht, um zur Richtung zu passen
        switch (direction) {
            case TOP:
                image.drawImage(spikes, 0, 0);
                break;
            case LEFT:
                spikes.rotate(-90);
                image.drawImage(spikes, 0, 0);
                break;
            case RIGHT:
                spikes.rotate(90);
                image.drawImage(spikes, thickness - length, 0);
                break;
            case BOTTOM:
                spikes.rotate(180);
                image.drawImage(spikes, 0, thickness - length);
                break;
        }

        setImage(image);
    }

    /**
     * Zeichnet die Stacheln nach oben zeigend
     * @param image das Bild, auf das die Stacheln gezeichnet werden sollen
     * @param length die Anzahl an Stacheln
     * @param color die Farbe der Stacheln
     * @param invertLight ob die schattigen und beleuchteten Kanten vertauscht werden sollen (für's Drehen)
     */
    private void paintSpikes(GreenfootImage image, int length, Color color, boolean invertLight) {
        int cellSize = GameWorld.CELL_SIZE;
        int height = cellSize / 2;

        Color risingEdge = invertLight ? color.darker() : color.brighter();
        Color fallingEdge = invertLight ? color.brighter() : color.darker();

        for (int i = 0; i < length; i++) {
            int x = i * cellSize;

            int[] xPoints = {
                    x,
                    x + cellSize / 2,
                    x + cellSize
            };

            int[] yPoints = {
                    height,
                    0,
                    height
            };

            // Hintergrund
            image.setColor(color);
            image.fillPolygon(xPoints, yPoints, 3);

            // Beleuchtete Kante
            image.setColor(risingEdge);
            image.drawLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);

            // Schattige Kante
            image.setColor(fallingEdge);
            image.drawLine(xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
        }
    }

    @Override
    public boolean shouldCollideWith(RelativeActor actor) {
        return actor instanceof Character;
    }

    @Override
    protected boolean performTargetCollision(GravityActor source, boolean horizontalCollision, boolean verticalCollision) {
        ((Character) source).spikeDamage(); // Den Spieler benachrichtigen
        return true;
    }

    /**
     * Die Richtungen, in die Stacheln zeigen können
     */
    public enum Direction {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT
    }
}
