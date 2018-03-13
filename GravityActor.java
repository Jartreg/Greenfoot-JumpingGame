public class GravityActor extends RelativeActor {
    public double vX;
    public double vY;

    private RelativeActor collidingGround;

    private boolean onGround;
    private double fallDistance;

    @Override
    public void gameAct() {
        vY -= GameWorld.GRAVITY;

        // Bewegung
        double xOld = getRelativeX();
        double yOld = getRelativeY();
        setLocation(xOld + vX, yOld - vY);

        boolean collidesBottom = false;

        // Kollision mit Objekten
        if (vX != 0 || vY != 0) {
            double rx = getImage().getWidth() / 2;
            double ry = getImage().getHeight() / 2;

            /*// Kollision mit dem Boden
            if (getRelativeY() > 350) {
                collidesBottom = true;
                vY = 0;
                setLocation(getRelativeX(), 350);
            }*/

            for (RelativeActor actor : getIntersectingObjects(RelativeActor.class)) {
                if (!shouldCollideWith(actor) || !actor.shouldCollideWith(this))
                    continue;

                double dx = getRelativeX() - xOld;
                double dy = getRelativeY() - yOld;

                boolean fromBottom = yOld > getRelativeY();
                boolean fromRight = xOld > getRelativeX();

                double rxA = actor.getImage().getWidth() / 2;
                double ryA = actor.getImage().getHeight() / 2;

                double xMin = actor.getRelativeX() - rxA;
                double xMax = actor.getRelativeX() + rxA;
                double yMin = actor.getRelativeY() - ryA;
                double yMax = actor.getRelativeY() + ryA;

                double xEdge = fromRight ? xMax : xMin;
                double yEdge = fromBottom ? yMax : yMin;

                double offsetX = fromRight ? -rx : rx;
                double offsetY = fromBottom ? -ry : ry;

                double ax = (getRelativeX() + offsetX) - xEdge;
                double ay = (getRelativeY() + offsetY) - yEdge;

                boolean horizontalCollision, verticalCollision;
                if (dx == 0 /*|| (xOld < xMax && xOld > xMax)*/) {
                    horizontalCollision = true;
                    verticalCollision = false;
                } else if (dy == 0 /*|| (yOld < yMax && yOld > yMax)*/) {
                    horizontalCollision = false;
                    verticalCollision = true;
                } else {
                    horizontalCollision = fromBottom != fromRight ? (ay / ax >= dy / dx) : (ay / ax <= dy / dx);
                    verticalCollision = fromBottom != fromRight ? (ay / ax <= dy / dx) : (ay / ax >= dy / dx);
                }

                if (!actor.performTargetCollision(this, horizontalCollision, verticalCollision)) {
                    if (!fromBottom && !verticalCollision) {
                        collidesBottom = true;
                        collidingGround = actor;
                    }

                    if (verticalCollision)
                        setLocation(getRelativeX() - ax, getRelativeY());

                    if (horizontalCollision)
                        setLocation(getRelativeX(), getRelativeY() - ay);
                }
            }
        }

        // Boden
        if (collidesBottom != onGround) {
            onGround = collidesBottom;
            onGroundChanged();

            if (!onGround) collidingGround = null;
        }

        if (onGround) {
            // Reibung
            double friction = collidingGround == null ? DEFAULT_FRICTION : collidingGround.getFriction();
            if (vX < friction && vX > -friction) {
                vX = 0;
            } else {
                if (vX < 0)
                    vX += friction;
                else
                    vX -= friction;
            }
        }

        // Falldistanz
        if (vY < 0)
            fallDistance -= vY;
        else
            fallDistance = 0;
    }

    protected void onGroundChanged() {

    }

    public boolean isOnGround() {
        return onGround;
    }

    public double getFallDistance() {
        return fallDistance;
    }

    public RelativeActor getCollidingGround() {
        return collidingGround;
    }
}
