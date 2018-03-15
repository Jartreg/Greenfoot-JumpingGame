import java.util.ArrayList;
import java.util.HashMap;

/**
 * Ein Men� zum ausw�hlen des Levels
 */
public class LevelSelectionMenu extends Menu {
    private final HashMap<String, Level> levels = new HashMap<>();

    protected LevelSelectionMenu(Level[] levels) {
        super(createMenuItems(levels));

        // Jedes Level seinem Men�eintrag zuordnen
        for (Level level : levels) {
            this.levels.put("Level " + level.getLevelNumber(), level);
        }
    }

    private static String[] createMenuItems(Level[] levels) {
        ArrayList<String> items = new ArrayList<>();

        for (Level level : levels) { // F�r jedes Level
            items.add("Level " + level.getLevelNumber());
        }

        items.add("Abbrechen"); // und zum Abbrechen
        return items.toArray(new String[0]);
    }

    @Override
    protected void itemSelected(String item) {
        if (item.equals("Abbrechen")) { // Einfach zur�ckgehen
            back();
            return;
        }

        Level level = levels.get(item);
        if (level != null) { // Level setzen und zur�ckgehen
            getWorld().setCurrentLevel(level);
            back();
        }
    }
}
