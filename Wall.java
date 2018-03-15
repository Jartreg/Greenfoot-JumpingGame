import greenfoot.Color;
import greenfoot.GreenfootImage;

/**
 * Eine Wand
 */
public class Wall extends RelativeActor {
    public static final Color COLOR = Color.GRAY;

    public Wall(int width, int height) {
        GreenfootImage bg = new GreenfootImage(width * GameWorld.CELL_SIZE, height * GameWorld.CELL_SIZE);
        paintWall(bg, 0, 0, width, height, getWallColor());
        setImage(bg);
    }

    /**
     * Zeichnet eine Wand
     * @param image das Bild für die Wans
     * @param startX x des Startpunkts
     * @param startY y des Startpunkts
     * @param cellsX breite in Zellen
     * @param cellsY höhe in Zellen
     * @param fillColor Farbe der Wand
     */
    public static void paintWall(GreenfootImage image, int startX, int startY, int cellsX, int cellsY, Color fillColor) {
        paintWall(image, startX, startY, cellsX, cellsY, fillColor, GameWorld.CELL_SIZE);
    }

    /**
     * Zeichnet eine Wand mit einer bestimmten Zellengröße
     * @param image das Bild für die Wans
     * @param startX x des Startpunkts
     * @param startY y des Startpunkts
     * @param cellsX breite in Zellen
     * @param cellsY höhe in Zellen
     * @param fillColor Farbe der Wand
     * @param cellSize die Zellengröße
     */
    public static void paintWall(GreenfootImage image, int startX, int startY, int cellsX, int cellsY, Color fillColor, int cellSize) {
        int sizeOffset = cellSize - 1;
        Color darker = fillColor.darker();
        Color brighter = fillColor.brighter();

        for (int cellX = 0; cellX < cellsX; cellX++) {
            for (int cellY = 0; cellY < cellsY; cellY++) {
                int x = startX + cellX * cellSize;
                int y = startY + cellY * cellSize;

                // Hintergrund
                image.setColor(fillColor);
                image.fillRect(x, y, x + cellSize, y + cellSize);

                // Schattige Kante
                image.setColor(darker);
                image.drawLine(x, y + sizeOffset, x + sizeOffset, y + sizeOffset);
                image.drawLine(x + sizeOffset, y, x + sizeOffset, y + sizeOffset);

                // Beleuchtete Kante
                image.setColor(brighter);
                image.drawLine(x, y, x + sizeOffset - 1, y);
                image.drawLine(x, y, x, y + sizeOffset - 1);
            }
        }
    }

    /**
     * Gibt die Wandfarbe zurück
     * @return die Wandfarbe
     */
    public Color getWallColor() {
        return COLOR;
    }

    @Override
    public boolean shouldCollideWith(RelativeActor actor) {
        return true; // immer kollidieren
    }
}
