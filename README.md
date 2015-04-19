# imhof
![Comment rédiger de la javadoc?](./Javadoc_Guidelines/javadocGuidelines.md)
## [Étape 1](http://cs108.epfl.ch/p01_points.html)
- [x] Rendu
	- PointGeo
	- Point
	- EquirectangularProjection
	- CH1903Projection
	- Projection

## [Étape 2](http://cs108.epfl.ch/p02_geometry.html)
- [x] Rendu
	- PolyLine
	- OpenPolyLine
	- ClosedPolyLine
	- PolyLine.Builder
	- Polygon

## [Étape 3](http://cs108.epfl.ch/p03_attributes.html)
- [x] Rendu
	- Attributes
	- Attributes.Builder
	- Attributed

## [Étape 4](http://cs108.epfl.ch/p04_osm-entities.html)
- [ ] OSMEntity
    - [x] code
    - [x] documentation
    - [x] test
- [ ] OSMNode
    - [x] code
    - [x] documentation
    - [x] test
- [ ] OSMWay
    - [x] code
    - [x] documentation
    - [x] test
- [ ] OSMRelation
    - [x] code
    - [x] documentation
    - [x] test
- [ ] OSMRelation.Member
    - [x] code
    - [x] documentation
    - [x] test

## [Étape 5](http://cs108.epfl.ch/p05_osm-reading.html)
- OSMGraph
	- [x] code
	- [x] doc
	- [x] test
	- [x] Réparer constructeur (la copie n'est pas assez profonde)
- OSMMap
	- [x] code
	- [x] doc
	- [x] test
- OSMMap.Builder
	- [x] code
	- [x] doc
	- [x] test
- OSMMapReader
	- [x] code (trop moche!!!!!) (mais non)
	- [x] doc
	- [x] test
		Le test crée un fichier `Debugging.txt` dans le fichier principal du projet (pour moi `Eclipse-Workspace/imhof`)

## [Etape 6](http://cs108.epfl.ch/p06_osm-to-geo.html)
- Map & Map.Builder
	- [x] code
	- [x] doc
	- [x] test
	- Déclarer le constructeur en final?
- OSMToGeoTransformer
	- [x] code
	- [x] doc
	- [x] test
	- Trucs à vérifier: modification d'éléments immuables, validité des données/conditions, performances, ConcurrentModificationException
	- La creation des polygons ne marche pas. addEdge de Graphe lance une illegalargumentexception.
	- Avant le rendu: vérifier immuabilité de toutes les classes, notamment pour les getters, retourner des copies au lieu de l'attribut lui-même.
	- Rajouter Collections.unmodifiableSet pour les sets statiques?

## [Etape 7](http://cs108.epfl.ch/p07_drawing-style.html)
- Color
	- [x] code
	- [x] doc
	- [ ] test
- LineStyle
	- [x] code
	- [x] doc
	- [ ] test
- Filters
	- [x] code
	- [x] doc
	- [x] test

- Point (classe étape 1, ajouter une méthode statique)
	- [x] code
	- [x] doc
	- [x] test

## [Étape 8](http://cs108.epfl.ch/p08_canvas-painters.html)
### Canvas
- [x] `interface Canvas`
    - [x] `drawPolyLine` dessine sur la toile un `PolyLine` à partir d'un `LineStyle`
    - [x] `drawPolygon` dessine sur la toile un `Polygon` à partir d'un `Color`
- [x] `class Java2DCanvas implements Canvas`
    - attributs (à fournir au constructeur):
        - coord bas-gauche + coord haut-droite
        - largeur et hauteur image en pixels
        - résolution points/pouce
        - couleur fond
        - changement de repère plan/image
    Dans le constructeur on utilise `alignedCoordinateChange` a stocker dans la variable ci-dessus pour simplifier l'écriture de `drawX` qui peuvent utiliser la fonction pour transformer les coordonnées des points des lignes ou polygones en coordonnées à passer aux méthodes de `java2d`
    - [x] `public BufferedImage image()`

### Painters
- `public interface Painter<E>`

    - `abstract drawMap(Map , Canvas)`

    - [x] `static Painter<Polygon> polygon (couleur de dessin)` retourne un peintre dessinant l'intérieur de tous les polygones de la carte qu'il reçoit avec cette couleur

    - [x] `static Painter<PolyLine> line (les paramètres de style d'une ligne)` retourne un peintre dessinant toutes les lignes de la carte qu'on lui fornit avec le style correspondant

    - [x] `static Painter<PoyLine> line(largeur trait, couleur)` meme chose que ci-dessus (en utilisant le deuxième constricuteur de `LineStyle`)

    - [x] `static Painter<Polygon> outline (cinq paramètres de style d'une ligne)` dessine les pourtours de l'enveloppe et des trous de tous les polygones de la carte qu'on lui fournit

    - [x] `static Painter<Polygon> outline (largeur trait, couleur)` meme chose que ci-dessus (en utilisant le deuxième constructeur de `LineStyle`)

    - [x] `default Painter<E> when (Predicate<Attributed<?>> pred)` retourne un peintre se comportant comme celui auquel on l'applique si ce n'est qu'il ne considère que les éléments de la carte satisfaisant le predicat

    - [x] `default Painter<E> above (Painter<E> p)` retourne un peintre dessinant d'abord la carte produite par ce second peintre puis, par dessus, la carte produite par le premier peintre

    - [x] `default Painter<E> layered()` retourne un peintre utilisant l'attribut layer attaché aux entités de la carte pour la dessiner par couches (en commençant de -5 jusqu'à + 5)

- Polygon (colore intérieure polygons)
- PolyLigne (peint les polylignes à partir de `LineStyle`s)
- Pourtour (peint les polylignes formant les enveloppes et les trous des polygons avec un `LineStyle` donné)
On doit pouvoir appliquer deux opérations de dérivation sur ces peintres. Je pense que la démarche à suivre est celle de créer une interface définissant un peintre et qui contient des méthodes pour:
- *Filtrer*: obtenir un nouveau peintre à partir d'un peintre existant en ne **fournissant à ce dernier qu'un sous-ensemble des entités de la carte à dessiner**
- *Empiler*: combiner deux peintres pour en obtenir un nouveau **dessinnant tout d'abord la carte du premier peintre puis, par dessus, la carte du second**


