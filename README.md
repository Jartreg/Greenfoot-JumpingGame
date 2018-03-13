# Greenfoot Jumping Game
Ein Jump n' Run in Greenfoot
 
## In der Welt vorhandene Elemente

### Der Spieler (`Character`)
Die Spielfigur, welche mit den Tasten A und D nach links und rechts bewegt werden kann.
Zum Springen benutzt man die Leertaste.

Wenn der Spieler tief fällt, gibt es eine Erschütterung

### Wand/Stein (`Wall`)
Eine einfache Plattform, auf der der Spieler laufen kann.

#### Eis (`IceWall`)
Eine Boden, auf der der Spieler wenig halt hat, also langsamer bremst und beschleunigt.

#### Schleim (`SlimeWall`)
Schleim lässt den Spieler abprallen.

Eis und Schleim sind von den Eis- und Schleimblöcken von Minecraft inspiriert.

### Stacheln (`Spikes`)
Fügen dem Spieler Schaden zu. Der Spieler hat bei berührung das Level verloren.

### Ostereier (`EasterEgg`)
Da das Spiel etwas mit Ostern zu tun haben soll, muss man Ostereier sammeln

### Das Ziel (`Exit`)
Sobald der Spieler das Ziel erreicht hat und alle Aufgaben des Levels erfüllt hat,
hat er das Level gewonnen.

## Zusätzliche Funktionen

### Eine verschiebbare Welt
Objekte der Klasse `RelativeActor` haben eine Feste Position und werden "verschoben",
wenn sich der Anzeigebereich der Welt ändert.

### Ein Hintergrund mit Parallax-Effekt
`WorldBackground` hat ein sich wiederholendes Muster als Hintergrund und bewegt sich beim
bewegen der Welt so, dass es aussieht, als wäre der Hintergrund weiter weg.

### Partikel (`Particle`)
Partikel nehmen sich einen zufälligen Teil des Bildes des `Actor`s, von dem sie Stammen
und fallen ohne Kollisionen aus der Welt.

### Erschütterungen
Mit `GameWorld.shakle(int time, int amount)` erzeugt man eine Erschütterung der Stärke `amount` für `time` Ticks.
Bei einer Erschütterung wird die Welt durchgeschüttelt und Partikel angezeigt.

### Ein Menü
Beim Drücken der `Esc`-Taste oder am Ende eines Levels wird ein Menü mit Optionen angezeigt.
Während ein Menü angezeigt wird, wird das eigentliche Spiel pausiert, indem die zum Spiel gehörenden
Objekt ihre `gameAct()`-Methode nicht mehr aufrufen (siehe `GameActor`).

Das Menü funktionert wie ein Stapel. Wenn man aus einem Menü in ein anderes navigiert und zurückgeht,
wird das vorherige Menü angezeigt.

### Screenshots!! 📸
Jeder wollte doch schonmal ein Bild seiner Welt speichern, oder?
Auf den Screenshots erscheinen nur die zum Spiel gehörenden Objekte.
Man kann Screenshots aus dem Pause-Menü (`Esc`-Taste) und dem Menü am Ende eines Levels machen.
Sobald das Level verloren oder gewonnen ist, wird ein Screenshot zum richtigen Zeitpunkt gemacht, damit Animationen
das Bild nicht verfälschen.

## Bekannte Probleme
* Die Kollisionsmechanik funktioniert zwar für dieses Szenario, müsste aber angepasst werden,
  sobald es mehrere bewegliche Objekte gibt.