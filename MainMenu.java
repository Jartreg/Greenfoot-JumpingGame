import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainMenu extends Menu {
    private final Level currentLevel;

    public MainMenu(GameWorld world) {
        super(createMenuItems(world));
        this.currentLevel = world.getCurrentLevel();
    }

    private static String[] createMenuItems(GameWorld world) {
        ArrayList<String> items = new ArrayList<>();

        if (world.getCurrentLevel().isCompleted() && world.getNextLevel() != null)
            items.add("Nächstes Level");

        items.addAll(Arrays.asList(
                "Neu Starten",
                "Level Wählen",
                "Screenshot",
                "Zurück"
        ));

        return items.toArray(new String[0]);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible && getWorld().getCurrentLevel() != currentLevel)
            back();
        else
            super.setVisible(visible);
    }

    @Override
    protected void itemSelected(String item) {
        switch (item) {
            case "Nächstes Level":
                getWorld().setCurrentLevel(getWorld().getNextLevel());
                back();
                break;
            case "Neu Starten":
                getWorld().resetLevel();
                back();
                break;
            case "Level Wählen":
                pushMenu(new LevelSelectionMenu(getWorld().getLevels()));
                break;
            case "Screenshot":
                takeScreenShot();
                break;
            case "Zurück":
                back();
                break;
        }
    }

    private void takeScreenShot() {
        File file = GameWorld.selectScreenshotFile();
        if (file != null) {
            GameWorld.saveScreenshot(getWorld().createScreenshot(), file);
            back();
        }
    }
}
