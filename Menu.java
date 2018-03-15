import greenfoot.Color;
import greenfoot.GreenfootImage;
import greenfoot.World;

/**
 * Die Basisklasse für Menüs
 */
public abstract class Menu extends GameActor {
    private final String[] menuItems;
    private int selectedItem;

    private boolean visible = false;

    /**
     * Erstellt ein Menü mit den angegebenen Optionen.
     * @param menuItems die Optionen
     */
    public Menu(String[] menuItems) {
        if (menuItems.length == 0)
            throw new IllegalArgumentException("menuItems kann nicht leer sein");

        this.menuItems = menuItems;
    }

    /**
     * Wird aufgerufen, wenn eine Option ausgewählt wurde.
     * @param item die ausgewählte Option
     */
    protected abstract void itemSelected(String item);

    @Override
    protected void addedToWorld(World world) {
        super.addedToWorld(world);
        setLocation(world.getWidth() / 2, world.getHeight() / 2); // Mittig
    }

    /**
     * Legt fest, ob das Menü sichtbar ist. Wird von {@link GameWorld} bei der Navigation verwendet.
     * @param visible ob das Menü sichtbar ist
     */
    public void setVisible(boolean visible) {
        if (this.visible == visible)
            return;

        this.visible = visible;

        if (visible) { // Anzeigen
            updateMenu();
        } else { // Ausblenden
            setImage((GreenfootImage) null);
        }
    }

    /**
     * Gibt zurück, ob das Menü sichtbar ist.
     * @return ob das Menü sichbar ist
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Schließt das Menü
     */
    public void close() {
        getWorld().removeObject(this);
    }

    /**
     * Geht einen Schritt in der Navigation zurück.
     * Wird u.a. von {@link GameWorld} aufgerufen, wenn die ESC-Taste edrückt wird.
     */
    public void back() {
        getWorld().popMenu();
    }

    /**
     * Navigiert zu einem anderen Menü.
     * @param menu das Menü, zu dem navigiert werden soll.
     */
    public void pushMenu(Menu menu) {
        getWorld().pushMenu(menu);
    }

    private void updateMenu() {
        if (!visible)
            return;

        GreenfootImage[] buttons = new GreenfootImage[menuItems.length];
        int width = 0;
        int height = 0;

        // Bilder wür die Optionen erstellen
        for (int i = 0; i < menuItems.length; i++) {
            GreenfootImage button = new GreenfootImage(
                    menuItems[i],
                    32,
                    new Color(255, 255, 255, selectedItem == i ? 255 : 150),
                    new Color(0, 0, 0, 0)
            );

            buttons[i] = button;
            height += button.getHeight(); // Höhe addieren
            width = Math.max(width, button.getWidth()); // Maximale Breite
        }

        GreenfootImage image = new GreenfootImage(width, (buttons.length - 1) * 16 + height);
        int center = width / 2;
        int y = 0;

        // Zu einem Bild zusammenfügen
        for (int i = 0; i < buttons.length; i++) {
            GreenfootImage button = buttons[i];
            image.drawImage(button, center - button.getWidth() / 2, y);
            y += 16 + button.getHeight(); // 16px Abstand
        }

        setImage(image);
    }

    /**
     * Wird von {@link GameWorld} aufgerufen, wenn eine Taste gedrückt wurde.
     * @param key die Taste
     */
    public void keyPressed(String key) {
        if (key.equals("enter")) { // Option auswählen
            itemSelected(menuItems[selectedItem]);
            return;
        }

        boolean down = key.equals("down");
        boolean up = key.equals("up");

        if (down)
            selectedItem++;

        if (up)
            selectedItem--;

        if (down || up) {
            // Am Ende oder am Anfang einfach zum anderen Ende springen
            if (selectedItem >= menuItems.length) {
                selectedItem = 0;
            } else if (selectedItem < 0) {
                selectedItem = menuItems.length - 1;
            }

            // Neu zeichnen
            updateMenu();
        }
    }
}
