# Greenfoot Jumping Game
Ein Jump n' Run in Greenfoot
 
## In der Welt vorhandene Elemente

### Der Spieler (`Character`)
Die Spielfigur, welche mit den Tasten A und D nach links und rechts bewegt werden kann.
Zum Springen benutzt man die Leertaste.

Wenn der Spieler tief f�llt, gibt es eine Ersch�tterung

### Wand/Stein (`Wall`)
Eine einfache Plattform, auf der der Spieler laufen kann.

#### Eis (`IceWall`)
Eine Boden, auf der der Spieler wenig halt hat, also langsamer bremst und beschleunigt.

#### Schleim (`SlimeWall`)
Schleim l�sst den Spieler abprallen.

Eis und Schleim sind von den Eis- und Schleimbl�cken von Minecraft inspiriert.

### Stacheln (`Spikes`)
F�gen dem Spieler Schaden zu. Der Spieler hat bei ber�hrung das Level verloren.

### Ostereier (`EasterEgg`)
Da das Spiel etwas mit Ostern zu tun haben soll, muss man Ostereier sammeln

### Das Ziel (`Exit`)
Sobald der Spieler das Ziel erreicht hat und alle Aufgaben des Levels erf�llt hat,
hat er das Level gewonnen.

## Zus�tzliche Funktionen

### Eine verschiebbare Welt
Objekte der Klasse `RelativeActor` haben eine Feste Position und werden "verschoben",
wenn sich der Anzeigebereich der Welt �ndert.

### Ein Hintergrund mit Parallax-Effekt
`WorldBackground` hat ein sich wiederholendes Muster als Hintergrund und bewegt sich beim
bewegen der Welt so, dass es aussieht, als w�re der Hintergrund weiter weg.

### Partikel (`Particle`)
Partikel nehmen sich einen zuf�lligen Teil des Bildes des `Actor`s, von dem sie Stammen
und fallen ohne Kollisionen aus der Welt.

### Ersch�tterungen
Mit `GameWorld.shakle(int time, int amount)` erzeugt man eine Ersch�tterung der St�rke `amount` f�r `time` Ticks.
Bei einer Ersch�tterung wird die Welt durchgesch�ttelt und Partikel angezeigt.

### Ein Men�
Beim Dr�cken der `Esc`-Taste oder am Ende eines Levels wird ein Men� mit Optionen angezeigt.
W�hrend ein Men� angezeigt wird, wird das eigentliche Spiel pausiert, indem die zum Spiel geh�renden
Objekt ihre `gameAct()`-Methode nicht mehr aufrufen (siehe `GameActor`).

Das Men� funktionert wie ein Stapel. Wenn man aus einem Men� in ein anderes navigiert und zur�ckgeht,
wird das vorherige Men� angezeigt.

### Screenshots!! ?
Jeder wollte doch schonmal ein Bild seiner Welt speichern, oder?
Auf den Screenshots erscheinen nur die zum Spiel geh�renden Objekte.
Man kann Screenshots aus dem Pause-Men� (`Esc`-Taste) und dem Men� am Ende eines Levels machen.
Sobald das Level verloren oder gewonnen ist, wird ein Screenshot zum richtigen Zeitpunkt gemacht, damit Animationen
das Bild nicht verf�lschen.

## Bekannte Probleme
* Die Kollisionsmechanik funktioniert zwar f�r dieses Szenario, m�sste aber angepasst werden,
  sobald es mehrere bewegliche Objekte gibt.