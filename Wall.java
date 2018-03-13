import greenfoot.Color;
import greenfoot.GreenfootImage;

public class Wall extends RelativeActor {
    public static final Color COLOR = Color.GRAY;

    public Wall() {
        this(5, 5);
    }

    public Wall(int width, int height) {
        GreenfootImage bg = new GreenfootImage(width * GameWorld.CELL_SIZE, height * GameWorld.CELL_SIZE);
        paintWall(bg, 0, 0, width, height, getWallColor());

        setImage(bg);
    }

    public static void paintWall(GreenfootImage image, int startX, int startY, int cellsX, int cellsY, Color fillColor) {
        paintWall(image, startX, startY, cellsX, cellsY, fillColor, GameWorld.CELL_SIZE);
    }

    public static void paintWall(GreenfootImage image, int startX, int startY, int cellsX, int cellsY, Color fillColor, int cellSize) {
        int sizeOffset = cellSize - 1;
        Color darker = fillColor.darker();
        Color brighter = fillColor.brighter();

        for (int cellX = 0; cellX < cellsX; cellX++) {
            for (int cellY = 0; cellY < cellsY; cellY++) {
                int x = startX + cellX * cellSize;
                int y = startY + cellY * cellSize;

                image.setColor(fillColor);
                image.fillRect(x, y, x + cellSize, y + cellSize);

                image.setColor(darker);
                image.drawLine(x, y + sizeOffset, x + sizeOffset, y + sizeOffset);
                image.drawLine(x + sizeOffset, y, x + sizeOffset, y + sizeOffset);

                image.setColor(brighter);
                image.drawLine(x, y, x + sizeOffset - 1, y);
                image.drawLine(x, y, x, y + sizeOffset - 1);
            }
        }
    }

    public Color getWallColor() {
        return COLOR;
    }

    @Override
    public boolean shouldCollideWith(RelativeActor actor) {
        return true;
    }
}
