import java.util.HashMap;
import java.util.stream.Stream;

public class LevelSelectionMenu extends Menu {
    private final HashMap<String, Level> levels = new HashMap<>();

    protected LevelSelectionMenu(Level[] levels) {
        super(createMenuItems(levels));

        for (Level level : levels) {
            this.levels.put("Level " + level.getLevelNumber(), level);
        }
    }

    private static String[] createMenuItems(Level[] levels) {
        return Stream.concat(
                Stream.of(levels)
                        .map(l -> "Level " + l.getLevelNumber()),
                Stream.of("Abbrechen")
        ).toArray(String[]::new);
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
