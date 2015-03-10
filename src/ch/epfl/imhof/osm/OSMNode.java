jpackage ch.epfl.imhof.osm;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Attributes;

/**
 * Classe qui représente un noeud OSM, héritant de OSMEntity. Elle est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class OSMNode extends OSMEntity {
    private final PointGeo position;

    /**
     * Construit un noeud OSM avec l'identifiant, la position et les attributs
     * donnés.
     * 
     * @param id
     *            l'identifiant du noeud, un long
     * @param position
     *            la position du noeud, un PointGeo
     * @param attributes
     *            les attributs du noeud, un Attributes
     */
    public OSMNode(long id, PointGeo position, Attributes attributes) {
        super(id, attributes);
        this.position = position;
    }

    /**
     * Accesseur de la position du noeud
     * 
     * @return l'attribut position
     */
    public PointGeo position() {
        return position;
    }

    /**
     * Bâtisseur de la classe OSMNode, héritant du bâtisseur de OSMEntity
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static final class Builder extends OSMEntity.Builder {
        private final PointGeo position;

        /**
         * Constructeur du bâtisseur
         * 
         * @param id
         *            l'identifiant du noeud à bâtir, un long
         * @param position
         *            la position du noeud à bâtir, un PointGeo
         */
        public Builder(long id, PointGeo position) {
            super(id);
            this.position = position;
        }

        /**
         * Construit un nouveau noeud OSM avec l'identifiant et la position
         * passés au constructeur, et les attributs ajoutés par le bâtisseur
         * 
         * @return le noeud, un OSMNode
         * @throws IllegalStateException
         *             lève une exception si le noeud est incomplet (si la
         *             méthode setIncomplete a été appelée sur le bâtisseur)
         */
        public OSMNode build() throws IllegalStateException {
            if (isIncomplete()) {
                throw new IllegalStateException(
                        "Le nœud en cours de construction est incomplet.");
            }
            return new OSMNode(idBuild(), position, attributesBuild());
        }
    }
}
