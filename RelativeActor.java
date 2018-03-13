import greenfoot.World;

public class RelativeActor extends GameActor {
    public static final double DEFAULT_FRICTION = 1.2;

    private double relativeX;
    private double relativeY;

    private int worldX;
    private int worldY;

    private void updateLocation() {
        super.setLocation((int) (relativeX - worldX), (int) (relativeY - worldY));
    }

    void updateWorldLocation(double worldX, double worldY) {
        this.worldX = (int) worldX;
        this.worldY = (int) worldY;
        updateLocation();
    }

    public void setLocation(double x, double y) {
        this.relativeX = x;
        this.relativeY = y;
        updateLocation();
    }

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

    @Override
    protected void addedToWorld(World world) {
        super.addedToWorld(world);

        worldX = (int) getWorld().getWorldX();
        worldY = (int) getWorld().getWorldY();
        updateLocation();
    }

    public double getRelativeX() {
        return relativeX;
    }

    public double getRelativeY() {
        return relativeY;
    }

    public boolean shouldCollideWith(RelativeActor actor) {
        return false;
    }

    protected boolean performTargetCollision(GravityActor source, boolean horizontalCollision, boolean verticalCollision) {
        if (horizontalCollision)
            source.vY = 0;

        if (verticalCollision)
            source.vX = 0;

        return false;
    }

    public double getFriction() {
        return DEFAULT_FRICTION;
    }

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

    public enum Alignment {
        BOTTOMLEFT,
        BOTTOMCENTER,
        TOPLEFT,
        CENTER
    }
}
