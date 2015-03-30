package ch.epfl.imhof.osm;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Attributes;

/**
 * Classe représentant un noeud OSM, héritant de <code>OSMEntity</code>. Elle
 * est immuable.
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
     *            l'identifiant du noeud, un <code>long</code>
     * @param position
     *            la position du noeud, un <code>PointGeo</code>
     * @param attributes
     *            les attributs du noeud, un <code>Attributes</code>
     */
    public OSMNode(long id, PointGeo position, Attributes attributes) {
        super(id, attributes);
        this.position = position;
    }

    /**
     * Retourne la position du noeud.
     * 
     * @return la position du noeud, un <code>PointGeo</code>
     */
    public PointGeo position() {
        return position;
    }

    /**
     * Bâtisseur de la classe <code>OSMNode</code>. Il hérite du bâtisseur de la
     * classe <code>OSMEntity</code>.
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static final class Builder extends OSMEntity.Builder {
        private final PointGeo position;

        /**
         * Construit un bâtisseur pour un noeud ayant l'identifiant et la
         * position donnés.
         * 
         * @param id
         *            l'identifiant du noeud à bâtir, un <code>long</code>
         * @param position
         *            la position du noeud à bâtir, un <code>PointGeo</code>
         */
        public Builder(long id, PointGeo position) {
            super(id);
            this.position = position;
        }

        /**
         * Construit et retourne un noeud OSM avec l'identifiant et la position
         * passés au constructeur, et les attributs ajoutés par le bâtisseur.
         * 
         * @return le noeud, un <code>OSMNode</code>
         * @throws IllegalStateException
         *             lève une exception si le noeud est incomplet (si la
         *             méthode setIncomplete a été appelée sur le bâtisseur)
         */
        public OSMNode build() throws IllegalStateException {
            if (isIncomplete()) {
                throw new IllegalStateException(
                        "Le nœud en cours de construction est incomplet.");
            }
            return new OSMNode(this.id, position,
                    this.attributesBuilder.build());
        }
    }
}
