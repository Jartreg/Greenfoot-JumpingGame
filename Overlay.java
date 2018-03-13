import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.GreenfootImage;
import greenfoot.World;

public class Overlay extends Actor {
    private boolean visible = true;
    private double animationPos;

    @Override
    protected void addedToWorld(World world) {
        setLocation(world.getWidth() / 2, world.getHeight() / 2);

        GreenfootImage image = new GreenfootImage(world.getWidth(), world.getHeight());
        image.setColor(new Color(0, 0, 0, 100));
        image.fill();
        image.setTransparency(0);
        setImage(image);

        animationPos = 0;
    }

    @Override
    public void act() {
        if (animationPos < 1) {
            if (visible) {
                animationPos += 0.01;
                getImage().setTransparency((int) ((-1 * Math.pow(animationPos - 1, 4) + 1) * 255));
            } else {
                animationPos = Math.min(1, animationPos + 0.04);
                getImage().setTransparency((int) (Math.pow(animationPos - 1, 4) * 255));
                if (animationPos == 1)
                    getWorld().removeObject(this);
            }
        }
    }

    public void hide() {
        if (visible) {
            visible = false;
            animationPos = 0;
        }
    }
}
