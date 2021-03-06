import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Das Hauptmen�
 * kann durch dr�chen der ESC-Taste erreicht werden.
 */
public class MainMenu extends Menu {
    private final Level currentLevel;

    public MainMenu(GameWorld world) {
        super(createMenuItems(world));
        this.currentLevel = world.getCurrentLevel();
    }

    private static String[] createMenuItems(GameWorld world) {
        ArrayList<String> items = new ArrayList<>();

        // Nur wenn de Spieler dieses Level gewonnen hat und man auch weitergehen kann
        if (world.getCurrentLevel().isCompleted() && world.getNextLevel() != null)
            items.add("N�chstes Level");

        items.addAll(Arrays.asList(
                "Neu Starten",
                "Level W�hlen",
                "Screenshot",
                "Zur�ck"
        ));

        return items.toArray(new String[0]);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible && getWorld().getCurrentLevel() != currentLevel) {
            // Wenn das Level ge�ndert wurde (im Levelmen�) direkt zur�ckgehen
            back();
        } else {
            super.setVisible(visible);
        }
    }

    @Override
    protected void itemSelected(String item) {
        switch (item) {
            case "N�chstes Level":
                getWorld().setCurrentLevel(getWorld().getNextLevel());
                back();
                break;
            case "Neu Starten":
                getWorld().resetLevel();
                back();
                break;
            case "Level W�hlen":
                pushMenu(new LevelSelectionMenu(getWorld().getLevels()));
                break;
            case "Screenshot":
                takeScreenShot();
                break;
            case "Zur�ck":
                back();
                break;
        }
    }

    private void takeScreenShot() {
        // Screenshot speichern und zur�ckgehen
        File file = GameWorld.selectScreenshotFile();
        if (file != null) {
            GameWorld.saveScreenshot(getWorld().createScreenshot(), file);
            back();
        }
    }
}
