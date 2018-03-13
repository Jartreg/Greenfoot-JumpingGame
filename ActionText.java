import greenfoot.Color;
import greenfoot.GreenfootImage;

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
        getImage().setTransparency(Math.min(255, (int) (timeToLive / 50.0 * 200.0)));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        updateImage();
    }

    @Override
    public void gameAct() {
        timeToLive--;
        if (timeToLive == 0) {
            getWorld().removeObject(this);
            return;
        }

        setLocation(getRelativeX(), getRelativeY() - 0.2);

        updateTransparency();
    }
}
