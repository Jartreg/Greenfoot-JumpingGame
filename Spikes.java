import greenfoot.Color;
import greenfoot.GreenfootImage;

public class Spikes extends RelativeActor {
    public Spikes(String direction, int cells) {
        this(Direction.valueOf(direction.toUpperCase()), cells);
    }

    public Spikes(Direction direction, int cells) {
        this(direction, cells, Color.LIGHT_GRAY);
    }

    public Spikes(Direction direction, int cells, Color color) {
        int length = GameWorld.CELL_SIZE * cells;
        int thickness = GameWorld.CELL_SIZE / 2;

        GreenfootImage spikes = new GreenfootImage(length, length);
        boolean invertLight = direction == Direction.LEFT || direction == Direction.BOTTOM;
        paintSpikes(spikes, cells, color, invertLight);

        GreenfootImage image;
        switch (direction) {
            case TOP:
            case BOTTOM:
                image = new GreenfootImage(length, thickness);
                break;
            default: // LEFT || RIGHT
                image = new GreenfootImage(thickness, length);
                break;
        }

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

    private void paintSpikes(GreenfootImage image, int length, Color color, boolean invertLight) {
        int cellSize = GameWorld.CELL_SIZE;
        int height = cellSize / 2;

        Color rigingEdge = invertLight ? color.darker() : color.brighter();
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

            image.setColor(color);
            image.fillPolygon(xPoints, yPoints, 3);

            image.setColor(rigingEdge);
            image.drawLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);

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
        ((Character) source).spikeDamage();
        return true;
    }

    public enum Direction {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT
    }
}
