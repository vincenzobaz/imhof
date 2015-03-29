# imhof
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

	À faire:
- protected dans OSMEntity.Builder? Je pense que on peut le garder.
- [x] énumération dans OSMRelation!!

## [Étape 5](http://cs108.epfl.ch/p05_osm-reading.html)
- OSMGraph
	- [x] code
	- [x] doc
	- [ ] test
	- [x] Réparer constructeur (la copie n'est pas assez profonde)
- OSMMap
	- [x] code
	- [x] doc
	- [ ] test
- OSMMap.Builder
	- [x] code
	- [x] doc
	- [ ] test
- OSMMapReader
	- [x] code (trop moche!!!!!) (mais non)
	- [x] doc
	- [x] test
		Le test crée un fichier `Debugging.txt` dans le fichier principal du projet (pour moi `Eclipse-Workspace/imhof`)

## [Etape 6](http://cs108.epfl.ch/p06_osm-to-geo.html)
- Map & Map.Builder
	- [x] code
	- [x] doc
	- [ ] test
	- Déclarer le constructeur en final?
- OSMToGeoTransformer
	- [ ] code
	- [ ] doc
	- [ ] test
	- Trucs à vérifier: modification d'éléments immuables, validité des données/conditions, performances, ConcurrentModificationException
	- La creation des polygons ne marche pas. addEdge de Graphe lance une illegalargumentexception.
	- Avant le rendu: vérifier immuabilité de toutes les classes, notamment pour les getters, retourner des copies au lieu de l'attribut lui-même.
