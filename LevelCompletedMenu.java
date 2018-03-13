import greenfoot.Actor;
import greenfoot.GreenfootImage;
import greenfoot.World;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class LevelCompletedMenu extends Menu {
    private final GameWorld world;
    private final GreenfootImage screenshot;
    private final ScreenshotPreview preview;

    public LevelCompletedMenu(GameWorld world) {
        super(createMenuItems(world));
        this.world = world;
        screenshot = world.completedScreenshot != null ? world.completedScreenshot : world.createScreenshot();
        preview = new ScreenshotPreview(world);
    }

    private static String[] createMenuItems(GameWorld world) {
        if (world.getCurrentLevel().isCompleted()) {
            ArrayList<String> menuItems = new ArrayList<>();

            if (world.getNextLevel() != null)
                menuItems.add("Nächstes Level");

            menuItems.addAll(Arrays.asList(
                    "Wiederholen",
                    "Screenshot speichern",
                    "Level Wählen"
            ));

            return menuItems.toArray(new String[0]);
        } else {
            return new String[]{
                    "Nochmal Versuchen",
                    "Screenshot speichern",
                    "Level Wählen"
            };
        }
    }

    @Override
    protected void itemSelected(String item) {
        if (item.equals("Nächstes Level")) {
            getWorld().setCurrentLevel(getWorld().getNextLevel());
            super.back();
        } else if (item.equals("Wiederholen") || item.equals("Nochmal Versuchen")) {
            getWorld().resetLevel();
            super.back();
        } else if (item.equals("Level Wählen")) {
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

    }

    class ScreenshotPreview extends Actor {
        private ScreenshotPreview(World world) {
            GreenfootImage scaled = new GreenfootImage(screenshot);
            scaled.scale(world.getWidth() / 4, world.getHeight() / 4);
            setImage(scaled);
            setRotation(360 - 10);
        }
    }
}
