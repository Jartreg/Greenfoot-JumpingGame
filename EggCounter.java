import greenfoot.Color;
import greenfoot.GreenfootImage;
import greenfoot.World;

public class EggCounter extends GameActor {
    private int eggCount;

    @Override
    protected void addedToWorld(World world) {
        super.addedToWorld(world);
        update();
    }

    private void update() {
        if (getWorld().getCurrentLevel() == null) {
            setImage((GreenfootImage) null);
            return;
        }

        int min = getWorld().getCurrentLevel().getMinEasterEggCount();

        String text = Integer.toString(eggCount);
        if (min == 0) {
            text += eggCount == 1 ? " Osterei" : " Ostereier";
        } else {
            text += "/" + min + (min == 1 ? " Osterei" : " Ostereiern");
        }

        GreenfootImage image = new GreenfootImage(
                text,
                20,
                Color.WHITE,
                new Color(0, 0, 0, 0),
                Color.DARK_GRAY
        );

        setLocation(
                8 + image.getWidth() / 2,
                8 + image.getHeight() / 2
        );
        setImage(image);
    }

    public int getEggCount() {
        return eggCount;
    }

    public void setEggCount(int eggCount) {
        this.eggCount = eggCount;
        update();
    }
}
