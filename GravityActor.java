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

            // Objekte überprüfen
            for (RelativeActor actor : getIntersectingObjects(RelativeActor.class)) {
                // Soll eine Kollision stattfinden?
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

                // Richtung der Kollision
                boolean horizontalCollision, verticalCollision;
                if (dx == 0) {
                    horizontalCollision = true;
                    verticalCollision = false;
                } else if (dy == 0) {
                    horizontalCollision = false;
                    verticalCollision = true;
                } else {
                    horizontalCollision = fromBottom != fromRight ? (ay / ax >= dy / dx) : (ay / ax <= dy / dx);
                    verticalCollision = fromBottom != fromRight ? (ay / ax <= dy / dx) : (ay / ax >= dy / dx);
                }

                // Kollision ausführen
                if (!actor.performTargetCollision(this, horizontalCollision, verticalCollision)) {
                    // Es findet eine Kollision statt und es kann sich nicht weiterbewegt werden.

                    // Liegt der Actor auf dem Boden?
                    if (!fromBottom && !verticalCollision) {
                        collidesBottom = true;
                        collidingGround = actor;
                    }

                    // Position anpassen

                    if (verticalCollision)
                        setLocation(getRelativeX() - ax, getRelativeY());

                    if (horizontalCollision)
                        setLocation(getRelativeX(), getRelativeY() - ay);
                }
            }
        }

        // Zustand, ob der Actor auf dem Boden liegt
        if (collidesBottom != onGround) {
            onGround = collidesBottom;
            onGroundChanged();

            if (!onGround) collidingGround = null;
        }

        if (onGround) {
            // Reibung -> Geschwindigkeit verringern
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

    /**
     * Wird aufgerufen, wenn sich der Zustand geändert hat, ob der Actor auf dem Boden liegt
     */
    protected void onGroundChanged() {

    }

    /**
     * Gibt zurück, ob sich der Actor auf dem Boden befindet.
     * @return ob sich der Actor auf dem Boden befindet
     */
    public boolean isOnGround() {
        return onGround;
    }

    /**
     * Gibt die Distanz zurück, die der Actor runtergefallen ist
     * @return wie weit der Actor gefallen ist
     */
    public double getFallDistance() {
        return fallDistance;
    }

    /**
     * Gibt den Actor zurück, auf dem dieser Actor zurzeit aufliegt oder <code>null</code>,
     * wenn er in der Luft ist.
     * @return der Boden
     */
    public RelativeActor getCollidingGround() {
        return collidingGround;
    }
}
