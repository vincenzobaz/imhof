# imhof
## [Étape 1](http://cs108.epfl.ch/p01_points.html)
- [x] Rendu

## [Étape 2](http://cs108.epfl.ch/p02_geometry.html)
- [x] Rendu

## [Étape 3](http://cs108.epfl.ch/p03_attributes.html)
- [x] Rendu

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
	- [x] Réparer constructeur (la copie n'est pas asset profonde)
- OSMMap
	- [x] code
	- [x] doc
	- [ ] test
- OSMMap.Builder
	- [x] code
	- [x] doc
	- [ ] test
- OSMMapReader
	- [x] code (trop moche!!!!!)
	- [ ] doc
	- [ ] test
		- le fichier `test.ch.epfl.imhof.osm.ReaderTest.java` maintenant donne un fichier `.osm` à notre adoré reader qui produit une OSMMap. J'ai écrit un peu de code qui cherche à imprimer tout ce qui se trouve dans cette OSMMap. Actuellement j'arrive à imprimer les ways et les relations en affichant id, lat, lon, type, role mais je ne sais pas comment afficher les attributs qu'il n'est pas possible d'acceder aux elements d'un hashmap sans posseder la clé. Selon les slides (et internet) cela est possible en recourrant à un iterateur. Je pense de pouvoir y arriver, plus tard. En outre dans le fichier osm on a des `ref=""`pour nodes et members et je ne sais pas comment reproduire ce comportement dans mon test (je ne fais pas trop d'effort, c'est plus simple d'avoir plusieurs fois le même id).
		Le test crée un fichier `Debugging.txt` dans le fichier principal du projet (pour moi `Eclipse-Workspace/imhof`)
		- Si tu en as envie,  il faudrait écrire des tests pour l'étape 4 en utilisant le reader, vu que pour cette étape-là on n'a fait aucun test, et il serait bien d'en avoir pour la suite.
