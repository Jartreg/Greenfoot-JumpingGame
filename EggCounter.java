import greenfoot.Color;
import greenfoot.GreenfootImage;
import greenfoot.World;

/**
 * Eine Anzeige für die Anzahl der gesammelten Eier
 */
public class EggCounter extends GameActor {
    private int eggCount;

    @Override
    protected void addedToWorld(World world) {
        super.addedToWorld(world);
        update();
    }

    private void update() {
        if (getWorld().getCurrentLevel() == null) { // Kein Level - kein Bild
            setImage((GreenfootImage) null);
            return;
        }

        int min = getWorld().getCurrentLevel().getMinEasterEggCount();

        String text = Integer.toString(eggCount);
        if (min == 0) { // Ohne Minimalzahl
            text += eggCount == 1 ? " Osterei" : " Ostereier";
        } else { // Mit Minimalzahl
            text += "/" + min + (min == 1 ? " Osterei" : " Ostereiern");
        }

        GreenfootImage image = new GreenfootImage(
                text,
                20,
                Color.WHITE,
                new Color(0, 0, 0, 0),
                Color.DARK_GRAY
        );

        // Immer 8 pixel von oben und links
        setLocation(
                8 + image.getWidth() / 2,
                8 + image.getHeight() / 2
        );
        setImage(image);
    }

    /**
     * Gibt die angezeigte Anzahl an gesammelten Eiern zurück.
     * @return die Anzahl der gesammelten Eier
     */
    public int getEggCount() {
        return eggCount;
    }

    /**
     * Legt die anzuzeigende Anzahl an gesammelten Eiern fest.
     * @param eggCount die Anzahl der gesammelten Eier
     */
    public void setEggCount(int eggCount) {
        this.eggCount = eggCount;
        update();
    }
}
