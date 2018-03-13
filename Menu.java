import greenfoot.Color;
import greenfoot.GreenfootImage;
import greenfoot.World;

public abstract class Menu extends GameActor {
    private final String[] menuItems;
    private int selectedItem;

    private boolean visible = false;

    public Menu(String[] menuItems) {
        if (menuItems.length == 0)
            throw new IllegalArgumentException("menuItems kann nicht leer sein");

        this.menuItems = menuItems;
    }

    protected abstract void itemSelected(String item);

    @Override
    protected void addedToWorld(World world) {
        super.addedToWorld(world);
        setLocation(world.getWidth() / 2, world.getHeight() / 2);
    }

    public void setVisible(boolean visible) {
        if (this.visible == visible)
            return;

        this.visible = visible;

        if (visible)
            updateMenu();
        else
            setImage((GreenfootImage) null);
    }

    public boolean isVisible() {
        return visible;
    }

    public void close() {
        getWorld().removeObject(this);
    }

    public void back() {
        getWorld().popMenu();
    }

    public void pushMenu(Menu menu) {
        getWorld().pushMenu(menu);
    }

    private void updateMenu() {
        if (!visible)
            return;

        GreenfootImage[] buttons = new GreenfootImage[menuItems.length];
        int width = 0;
        int height = 0;

        for (int i = 0; i < menuItems.length; i++) {
            GreenfootImage button = new GreenfootImage(
                    menuItems[i],
                    32,
                    new Color(255, 255, 255, selectedItem == i ? 255 : 150),
                    new Color(0, 0, 0, 0)
            );

            buttons[i] = button;
            height += button.getHeight();
            if (button.getWidth() > width)
                width = button.getWidth();
        }

        GreenfootImage image = new GreenfootImage(width, (buttons.length - 1) * 16 + height);
        int center = width / 2;
        int y = 0;

        for (int i = 0; i < buttons.length; i++) {
            GreenfootImage button = buttons[i];
            image.drawImage(button, center - button.getWidth() / 2, y);
            y += 16 + button.getHeight();
        }

        setImage(image);
    }

    public void keyPressed(String key) {
        if (key.equals("enter")) {
            itemSelected(menuItems[selectedItem]);
            return;
        }

        boolean down = key.equals("down");
        boolean up = key.equals("up");

        if (down) {
            selectedItem++;
        }

        if (up) {
            selectedItem--;
        }

        if (down || up) {
            if (selectedItem >= menuItems.length) {
                selectedItem = 0;
            } else if (selectedItem < 0) {
                selectedItem = menuItems.length - 1;
            }

            updateMenu();
        }
    }
}
