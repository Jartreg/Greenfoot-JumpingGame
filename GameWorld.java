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

/**
 * Die Spielwelt
 */
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

    public GameWorld() {
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

        // EggCounter und Hintergrund hinzufügen
        hudActors.add(eggCounter);
        addObject(eggCounter, 0, 0);
        addObject(background, 0, 0);

        setCurrentLevel(getLevels()[0]);
        Greenfoot.start();
    }

    @Override
    public void act() {
        // Tasten für Menüs
        String key = Greenfoot.getKey();
        if (key != null) {
            if (key.equals("escape")) {
                if (menuStack.isEmpty()) { // Hauptmenü öffnen
                    pushMenu(new MainMenu(this));
                } else { // Eine Ebene zurück
                    menuStack.peek().back();
                }
            }

            if (!menuStack.isEmpty())
                menuStack.peek().keyPressed(key);
        }

        // Erschütterung
        if ((gameRunning || levelCompleted) && shakeTime > 0) {
            // Position berechnen und den RelativeActors mitteilen

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

    /**
     * Gibt die vorhandenen Levels zurück
     * @return  die vorhandenen Levels
     */
    public Level[] getLevels() {
        return levels;
    }

    /**
     * Gibt das aktuelle Level zurück
     * @return  das aktuelle Level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    private void resetWorld() {
        // Alles zurücksetzen

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

    /**
     * Setzt das aktuelle Level
     * @param newLevel das neue Level
     */
    public void setCurrentLevel(Level newLevel) {
        if (currentLevel != null) { // Zurücksetzen
            resetWorld();
        }

        // Level einrichten
        currentLevel = newLevel;
        eggCounter.setEggCount(0);
        levelCompleted = false;
        completedScreenshot = null;

        // Level bauen
        if (currentLevel != null)
            currentLevel.buildWorld(this);
    }

    /**
     * Setzt das aktuelle Level zurück
     */
    public void resetLevel() {
        if (currentLevel == null)
            return;

        setCurrentLevel(currentLevel);
    }

    /**
     * Gibt das nächste Level zurück oder <code>null</code> wenn dieses das letzte ist.
     * @return das nächste Level
     */
    public Level getNextLevel() {
        Level currentLevel = getCurrentLevel();

        // Kein aktuelles Level -> kein nächstes Level
        if (currentLevel == null)
            return null;

        // Aktuelles suchen und nächstes zurückgeben
        for (int i = 0; i < levels.length; i++) {
            if (levels[i] == currentLevel && levels.length > i + 1)
                return levels[i + 1];
        }

        return null;
    }

    /**
     * Beendet das aktuelle Level
     * @param successfully ob das Level gewonnen wurde
     */
    public void levelCompleted(boolean successfully) {
        if (levelCompleted)
            return;
        levelCompleted = true;

        if (successfully)
            getCurrentLevel().setCompleted(true);

        pushMenu(new LevelCompletedMenu(this));
    }

    /**
     * Gibt zurück, ob die aktuelle Runde in diesem Level gewonnen wurde.
     * @return ob die Runde gewonnen wurde
     */
    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    /**
     * Teilt den Akteuren und dem Hintergrund die Position des sichtbaren Bereichs mit
     */
    private void updateRelativeActors() {
        double x = worldX + shakeOffsetX;
        double y = worldY + shakeOffsetY;

        for (RelativeActor actor : getObjects(RelativeActor.class)) {
            actor.updateWorldLocation(x, y);
        }

        background.updateWorldLocation(x, y);
    }

    /**
     * Gibt die X-Koordinate des sichtbaren Bereichs zurück.
     * @return die X-Koordinate
     */
    public double getWorldX() {
        return worldX;
    }

    /**
     * Legt die X-Koordinate des sichtbaren Bereichs fest.
     * @param worldX die X-Koordinate
     */
    public void setWorldX(double worldX) {
        // Nicht weiter bewegen
        worldX = Math.max(Math.min(worldX, maxX), minX);

        if (this.worldX != worldX) {
            this.worldX = worldX;
            updateRelativeActors();
        }
    }

    /**
     * Gibt die Y-Koordinate des sichtbaren Bereichs zurück.
     * @return die Y-Koordinate
     */
    public double getWorldY() {
        return worldY;
    }

    /**
     * Legt die Y-Koordinate des sichtbaren Bereichs fest.
     * @param worldY die Y-Koordinate
     */
    public void setWorldY(double worldY) {
        // Nicht weiter bewegen
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
        setWorldX(getWorldX()); // Aktualisieren
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX - getWidth();
        setWorldX(getWorldX()); // Aktualisieren
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
        setWorldY(getWorldY()); // Aktualisieren
    }

    public double getMaxY() {
        return maxY + getHeight();
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY - getHeight();
        setWorldY(getWorldY()); // Aktualisieren
    }

    /**
     * Fügt einen {@link RelativeActor} zur Welt hinzu.
     * @param actor der Actor
     * @param x die X-Koordinate
     * @param y die Y-Koordinate
     */
    public void addRelativeActor(RelativeActor actor, double x, double y) {
        addRelativeActor(actor, x, y, RelativeActor.Alignment.CENTER);
    }

    /**
     * Fügt einen {@link RelativeActor} zur Welt hinzu und richtet seine Position aus.
     * @param actor der Actor
     * @param x die X-Koordinate
     * @param y die Y-Koordinate
     * @param alignment die Ausrichtung der Position
     */
    public void addRelativeActor(RelativeActor actor, double x, double y, RelativeActor.Alignment alignment) {
        addObject(actor, 0, 0);
        actor.setLocation(x, y, alignment);
    }

    @Override
    public void addObject(Actor object, int x, int y) {
        // Position aktualisieren
        if (object instanceof RelativeActor)
            ((RelativeActor) object).updateWorldLocation(worldX, worldY);

        // Status aktualisieren
        if (object instanceof GameActor)
            ((GameActor) object).updateGameRunning(gameRunning);

        super.addObject(object, x, y);

        // Nur ein Spieler pro Welt
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

        // Wenn es ein Display-Actor war
        hudActors.remove(object);
    }

    /**
     * Schüttelt die Welt
     * @param time die Zeit in Ticks
     * @param amount die Stärke
     */
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

    /**
     * Gibt die Spielfigur zurück
     * @return die Spielfigur
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * Gibt den Eierzähler zurück
     * @return der Eierzähler
     */
    public EggCounter getEggCounter() {
        return eggCounter;
    }

    /**
     * Gibt eine Liste für Actors zurück, die zwischen den Leveln nicht entfernt werden.
     * Diese Actors bilden das Head-Up-Display (Menü, Anzeigen).
     * @return
     */
    public ArrayList<Actor> getHudActors() {
        return hudActors;
    }

    private void updateGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;

        for (GameActor actor : getObjects(RelativeActor.class)) {
            actor.updateGameRunning(gameRunning);
        }
    }

    /**
     * Öffnet ein neues Menü
     * @param menu das Menü
     */
    public void pushMenu(Menu menu) {
        if (menuStack.isEmpty()) {
            // Overlay einblenden und Spiel pausieren
            overlay = new Overlay();
            hudActors.add(overlay);
            addObject(overlay, 0, 0);
            updateGameRunning(false);
        } else {
            // Letztes Menü ausblenden
            menuStack.peek().setVisible(false);
        }

        // Aktuelles Menü anzeigen
        menuStack.push(menu);
        hudActors.add(menu);
        addObject(menu, 0, 0);
        menu.setVisible(true);
    }

    /**
     * Geht zum letzten Menü zurück oder schließt das Menü, wenn es das erste war.
     */
    public void popMenu() {
        if (menuStack.isEmpty())
            return;

        // Menü ausblenden
        Menu closedMenu = menuStack.pop();
        closedMenu.setVisible(false);
        closedMenu.close();

        if (menuStack.isEmpty() && overlay != null) {
            // Overlay ausblenden und Spiel fortsetzen
            overlay.hide();
            overlay = null;
            updateGameRunning(true);
        } else {
            // Letztes Menü anzeigen
            menuStack.peek().setVisible(true);
        }
    }

    /**
     * Erstellt einem Screenshot der Welt
     * @return der Screenshot
     */
    public GreenfootImage createScreenshot() {
        // Hintergrund
        GreenfootImage image = new GreenfootImage(getBackground());

        // Actors
        for (Actor actor : getObjects(Actor.class)) {
            if (!getHudActors().contains(actor)) { // Nur die zeichnen, die zum Spiel gehören
                GreenfootImage actorImage = actor.getImage();

                if (actorImage == null)
                    continue;

                // Bild auf der Screenshot zeichnen
                image.drawImage(
                        actorImage,
                        actor.getX() - actorImage.getWidth() / 2,
                        actor.getY() - actorImage.getHeight() / 2
                );
            }
        }

        return image;
    }

    /**
     * Fragt den Benutzer, wo er einen Screenshot speichern will.
     * Als Dateiname wird <code>Screenshot.png</code> und als Speicherort der Desktop vorgschlagen.
     * @return der Speicherort oder <code>null</code> wenn der Benutzer abbricht
     */
    public static File selectScreenshotFile() {
        FileDialog dialog = new FileDialog((Frame) null, "Screenshot speichern", FileDialog.SAVE);
        dialog.setDirectory(System.getProperty("user.home") + "/Desktop");
        dialog.setFile("Screenshot.png");
        dialog.setFilenameFilter((dir, name) -> name.toLowerCase().endsWith(".png"));
        dialog.setVisible(true);

        File[] files = dialog.getFiles();
        return files.length == 1 ? files[0] : null;
    }

    /**
     * Speichert einen Screenshot in einer Datei
     * @param screenshot der Screenshot
     * @param file die Datei
     */
    public static void saveScreenshot(GreenfootImage screenshot, File file) {
        try {
            ImageIO.write(screenshot.getAwtImage(), "PNG", file);
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Der Screenshot konnte nicht gespeichert werden.", "Screenshot speichern", JOptionPane.ERROR_MESSAGE);
        }
    }
}