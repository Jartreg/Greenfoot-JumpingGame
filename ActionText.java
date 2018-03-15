import greenfoot.Color;
import greenfoot.GreenfootImage;

/**
 * Text, der langsam nach oben schwebt und blasser wird
 */
public class ActionText extends RelativeActor {
    private String text;
    private int timeToLive = 200;

    public ActionText(String text) {
        this.text = text;
        updateImage();
    }

    private void updateImage() {
        GreenfootImage image = new GreenfootImage(text, 20, Color.WHITE, new Color(0, 0, 0, 0), Color.DARK_GRAY);
        setImage(image);
        updateTransparency();
    }

    private void updateTransparency() {
        // Ab timeToLive=50 wird ausgeblendet
        getImage().setTransparency(Math.min(255, (int) (timeToLive / 50.0 * 200.0)));
    }

    /**
     * Gibt den angezeigten Text zurück.
     * @return der Text
     */
    public String getText() {
        return text;
    }

    /**
     * Legt den anzuzeigenden Text fest.
     * @param text der Text
     */
    public void setText(String text) {
        this.text = text;
        updateImage();
    }

    @Override
    public void gameAct() {
        timeToLive--;
        if (timeToLive == 0) { // Entfernen
            getWorld().removeObject(this);
            return;
        }

        // Nach oben bewegen
        setLocation(getRelativeX(), getRelativeY() - 0.2);

        // Transparenz neu berechnen
        updateTransparency();
    }
}
