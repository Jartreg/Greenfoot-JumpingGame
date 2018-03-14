import java.util.ArrayList;
import java.util.HashMap;

public class LevelSelectionMenu extends Menu {
    private final HashMap<String, Level> levels = new HashMap<>();

    protected LevelSelectionMenu(Level[] levels) {
        super(createMenuItems(levels));

        for (Level level : levels) {
            this.levels.put("Level " + level.getLevelNumber(), level);
        }
    }

    private static String[] createMenuItems(Level[] levels) {
        ArrayList<String> items = new ArrayList<>();

        for (Level level : levels) { // Für jedes Level
            items.add("Level " + level.getLevelNumber());
        }

        items.add("Abbrechen"); // und zum Abbrechen
        return items.toArray(new String[0]);
    }

    @Override
    protected void itemSelected(String item) {
        if (item.equals("Abbrechen")) {
            back();
            return;
        }

        Level level = levels.get(item);
        if (level != null) {
            getWorld().setCurrentLevel(level);
            back();
        }
    }
}
