import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class GameWorld extends World {
    public static final int CELL_SIZE = 16;
    public static final int GRAVITY = 1;

    private final Level[] levels = new Level[]{
            new Level1(1),
            new Level2(2)
    };

    private final ArrayList<Actor> hudActors = new ArrayList<>();
    private final Stack<Menu> menuStack = new Stack<>();
    private Overlay overlay;
    private Level currentLevel;
    private boolean levelCompleted;
    private boolean gameRunning = true;

    private final WorldBackground background = new WorldBackground();
    private final EggCounter eggCounter = new EggCounter();
    private Character character;

    private double worldX;
    private double worldY;

    private double minX = Double.NEGATIVE_INFINITY;
    private double maxX = Double.POSITIVE_INFINITY;
    private double minY = Double.NEGATIVE_INFINITY;
    private double maxY = 0;

    private int shakeOffsetX;
    private int shakeOffsetY;
    private int shakeAmount;
    private int shakeTime = 0;

    public GreenfootImage completedScreenshot;

    public GameWorld(Level level) {
        super(600, 400, 1, false);
        setPaintOrder(
                Menu.class,
                LevelCompletedMenu.ScreenshotPreview.class,
                Overlay.class,

                EggCounter.class,

                Particle.class,

                Character.class,
                RelativeActor.class,

                WorldBackground.class
        );

        hudActors.add(eggCounter);
        addObject(eggCounter, 0, 0);
        addObject(background, 0, 0);

        setCurrentLevel(level == null ? getLevels()[0] : level);
        Greenfoot.start();
    }

    public GameWorld() {
        this(null);
    }

    @Override
    public void act() {
        String key = Greenfoot.getKey();
        if (key != null) {
            if (key.equals("escape")) {
                if (menuStack.isEmpty()) {
                    pushMenu(new MainMenu(this));
                } else {
                    menuStack.peek().back();
                }
            }

            if (!menuStack.isEmpty())
                menuStack.peek().keyPressed(key);
        }

        if ((gameRunning || levelCompleted) && shakeTime > 0) {
            shakeOffsetX = Greenfoot.getRandomNumber(shakeAmount) - shakeAmount / 2;
            shakeOffsetY = Greenfoot.getRandomNumber(shakeAmount) - shakeAmount / 2;

            shakeTime--;
            if (shakeTime == 0) {
                shakeOffsetX = 0;
                shakeOffsetY = 0;
                shakeAmount = 0;
            }

            updateRelativeActors();
        }
    }

    private void resetWorld() {
        for (Actor actor : getObjects(Actor.class)) {
            if (actor != background && !hudActors.contains(actor))
                removeObject(actor);
        }

        setWorldX(0);
        setWorldY(0);

        setMinX(Double.NEGATIVE_INFINITY);
        setMaxX(Double.POSITIVE_INFINITY);
        setMinY(Double.NEGATIVE_INFINITY);
        setMaxY(getHeight());
    }

    public Level[] getLevels() {
        return levels;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level newLevel) {
        if (currentLevel != null) {
            resetWorld();
        }

        currentLevel = newLevel;
        eggCounter.setEggCount(0);
        levelCompleted = false;
        completedScreenshot = null;

        if (currentLevel != null) {
            currentLevel.reset();
            currentLevel.buildWorld(this);
        }
    }

    public void resetLevel() {
        if (currentLevel == null)
            return;

        setCurrentLevel(currentLevel);
    }

    public Level getNextLevel() {
        Level currentLevel = getCurrentLevel();

        if (currentLevel == null)
            return null;

        for (int i = 0; i < levels.length; i++) {
            if (levels[i] == currentLevel && levels.length > i + 1)
                return levels[i + 1];
        }

        return null;
    }

    public void levelCompleted(boolean successfully) {
        if (levelCompleted)
            return;
        levelCompleted = true;

        if (successfully)
            getCurrentLevel().setCompleted(true);

        pushMenu(new LevelCompletedMenu(this));
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    private void updateGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;

        for (GameActor actor : getObjects(RelativeActor.class)) {
            actor.updateGameRunning(gameRunning);
        }
    }

    private void updateRelativeActors() {
        double x = worldX + shakeOffsetX;
        double y = worldY + shakeOffsetY;

        for (RelativeActor actor : getObjects(RelativeActor.class)) {
            actor.updateWorldLocation(x, y);
        }

        background.updateWorldLocation(x, y);
    }

    public double getWorldX() {
        return worldX;
    }

    public void setWorldX(double worldX) {
        worldX = Math.max(Math.min(worldX, maxX), minX);

        if (this.worldX != worldX) {
            this.worldX = worldX;
            updateRelativeActors();
        }
    }

    public double getWorldY() {
        return worldY;
    }

    public void setWorldY(double worldY) {
        worldY = Math.max(Math.min(worldY, maxY), minY);

        if (this.worldY != worldY) {
            this.worldY = worldY;
            updateRelativeActors();
        }
    }

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
        setWorldX(getWorldX());
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX - getWidth();
        setWorldX(getWorldX());
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
        setWorldY(getWorldY());
    }

    public double getMaxY() {
        return maxY + getHeight();
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY - getHeight();
        setWorldY(getWorldY());
    }

    public GameActor addRelativeActor(RelativeActor actor, double x, double y) {
        return addRelativeActor(actor, x, y, RelativeActor.Alignment.CENTER);
    }

    public GameActor addRelativeActor(RelativeActor actor, double x, double y, RelativeActor.Alignment alignment) {
        addObject(actor, 0, 0);
        actor.setLocation(x, y, alignment);
        return actor;
    }

    @Override
    public void addObject(Actor object, int x, int y) {
        if (object instanceof RelativeActor)
            ((RelativeActor) object).updateWorldLocation(worldX, worldY);

        if (object instanceof GameActor)
            ((GameActor) object).updateGameRunning(gameRunning);

        super.addObject(object, x, y);

        if (object instanceof Character) {
            if (character != null)
                removeObject(character);
            character = (Character) object;
        }
    }

    @Override
    public void removeObject(Actor object) {
        super.removeObject(object);

        if (object == character)
            character = null;

        hudActors.remove(object);
    }

    public void shake(int time, int amount) {
        shakeTime = Math.max(shakeTime, time);
        shakeAmount = Math.max(shakeAmount, amount);

        for (RelativeActor actor : getObjects(RelativeActor.class)) {
            if (actor instanceof Particle)
                continue;

            GreenfootImage image = actor.getImage();
            int particleCount = (image.getWidth() / CELL_SIZE) * (image.getHeight() / CELL_SIZE) / 2 + Greenfoot.getRandomNumber(4);
            double minx = actor.getRelativeX() - image.getWidth() / 2;
            double miny = actor.getRelativeY() - image.getHeight() / 2;

            for (int i = 0; i < particleCount; i++) {
                double x = minx + Greenfoot.getRandomNumber(image.getWidth());
                double y = miny + Greenfoot.getRandomNumber(image.getHeight());

                double vx = x - actor.getRelativeX();
                double vy = y - actor.getRelativeY();

                double hypot = Math.hypot(vx, vy);
                double factor = hypot == 0 ? 0 : 1 / hypot * 1.5;
                vx *= factor;
                vy = vy * factor + 3 + Greenfoot.getRandomNumber(5);

                addRelativeActor(new Particle(vx, vy, image), x, y);
            }
        }
    }

    public void adjustObjectsOnCells() {
        for (RelativeActor actor : getObjects(RelativeActor.class)) {
            int rx = actor.getImage().getWidth() / 2;
            int ry = actor.getImage().getHeight() / 2;

            actor.setLocation(
                    Math.round((actor.getRelativeX() - rx) / CELL_SIZE) * CELL_SIZE + rx,
                    Math.round((actor.getRelativeY() - ry) / CELL_SIZE) * CELL_SIZE + ry
            );
        }
    }

    public Character getCharacter() {
        return character;
    }

    public ArrayList<Actor> getHudActors() {
        return hudActors;
    }

    public void pushMenu(Menu menu) {
        if (menuStack.isEmpty()) {
            overlay = new Overlay();
            hudActors.add(overlay);
            addObject(overlay, 0, 0);
            updateGameRunning(false);
        } else {
            menuStack.peek().setVisible(false);
        }

        menuStack.push(menu);
        hudActors.add(menu);
        addObject(menu, 0, 0);
        menu.setVisible(true);
    }

    public void popMenu() {
        if (menuStack.isEmpty())
            return;

        Menu closedMenu = menuStack.pop();
        closedMenu.setVisible(false);
        closedMenu.close();

        if (menuStack.isEmpty() && overlay != null) {
            overlay.hide();
            overlay = null;
            updateGameRunning(true);
        } else {
            menuStack.peek().setVisible(true);
        }
    }

    public Menu getCurrentMenu() {
        return menuStack.isEmpty() ? null : menuStack.peek();
    }

    public EggCounter getEggCounter() {
        return eggCounter;
    }

    public GreenfootImage createScreenshot() {
        GreenfootImage image = new GreenfootImage(getBackground());

        for (Actor actor : getObjects(Actor.class)) {
            if (!getHudActors().contains(actor)) {
                GreenfootImage actorImage = actor.getImage();

                if (actorImage == null)
                    continue;

                image.drawImage(
                        actorImage,
                        actor.getX() - actorImage.getWidth() / 2,
                        actor.getY() - actorImage.getHeight() / 2
                );
            }
        }

        return image;
    }

    public static File selectScreenshotFile() {
        FileDialog dialog = new FileDialog((Frame) null, "Screenshot speichern", FileDialog.SAVE);
        dialog.setDirectory(System.getProperty("user.home") + "/Desktop");
        dialog.setFile("Screenshot.png");
        dialog.setFilenameFilter((dir, name) -> name.toLowerCase().endsWith(".png"));
        dialog.setVisible(true);

        File[] files = dialog.getFiles();
        return files.length == 1 ? files[0] : null;
    }

    public static void saveScreenshot(GreenfootImage screenshot, File file) {
        try {
            ImageIO.write(screenshot.getAwtImage(), "PNG", file);
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Der Screenshot konnte nicht gespeichert werden.", "Screenshot speichern", JOptionPane.ERROR_MESSAGE);
        }
    }
}