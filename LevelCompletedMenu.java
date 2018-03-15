import greenfoot.Actor;
import greenfoot.GreenfootImage;
import greenfoot.World;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Das Men�, welches angezeigt wird, wenn man ein Level verloren oder gewonnen hat
 */
public class LevelCompletedMenu extends Menu {
    private final GameWorld world;
    private final GreenfootImage screenshot;
    private final ScreenshotPreview preview;

    public LevelCompletedMenu(GameWorld world) {
        super(createMenuItems(world));
        this.world = world;

        // Ein schon gemachter oder ein neuer Screenshot
        screenshot = world.completedScreenshot != null ? world.completedScreenshot : world.createScreenshot();
        preview = new ScreenshotPreview(world);
    }

    private static String[] createMenuItems(GameWorld world) {
        ArrayList<String> menuItems = new ArrayList<>();
        if (world.getCurrentLevel().isCompleted()) { // Wenn der Spieler erfolgreich war oder das Level schonmal gespielt hat

            if (world.getNextLevel() != null) // Nur wenn man auch weitergehen kann
                menuItems.add("N�chstes Level");

            menuItems.add("Wiederholen");
        } else { // Wenn der Spieler das Level zum ersten Mal spielt und verloren hat
            menuItems.add("Nochmal Versuchen");
        }

        // Standardoptionen
        menuItems.addAll(Arrays.asList(
                "Screenshot speichern",
                "Level W�hlen"
        ));

        return menuItems.toArray(new String[0]);
    }

    @Override
    protected void itemSelected(String item) {
        if (item.equals("N�chstes Level")) { // Weiter
            getWorld().setCurrentLevel(getWorld().getNextLevel());
            super.back();
        } else if (item.equals("Wiederholen") || item.equals("Nochmal Versuchen")) { // Zur�cksetzen
            getWorld().resetLevel();
            super.back();
        } else if (item.equals("Level W�hlen")) {
            pushMenu(new LevelSelectionMenu(getWorld().getLevels()));
        } else if (item.equals("Screenshot speichern")) {
            File file = GameWorld.selectScreenshotFile();
            if (file != null)
                GameWorld.saveScreenshot(screenshot, file);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible && !world.isLevelCompleted()) {
            super.back();
            return;
        }

        // Die Vorschau f�r den Screenshot muss auch ein- und ausgeblendet werden
        if (visible) {
            GreenfootImage image = preview.getImage();
            world.addObject(preview, image.getWidth() / 10 * 8, image.getHeight() / 10 * 8);
        } else {
            world.removeObject(preview);
        }

        super.setVisible(visible);
    }

    @Override
    public void back() {
        // man kann nicht zur�ck, da das Level neu gestartet werden muss
    }

    /**
     * Eine Vorschau f�r den genachten Screenshot.
     * Zeigt eine runterskalierte Version des Bildes.
     */
    class ScreenshotPreview extends Actor {
        private ScreenshotPreview(World world) {
            GreenfootImage scaled = new GreenfootImage(screenshot);
            scaled.scale(world.getWidth() / 4, world.getHeight() / 4);
            setImage(scaled);
            setRotation(360 - 10);
        }
    }
}
