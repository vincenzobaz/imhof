package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

/**
 * Classe abstraite définissant les entité OpenStreetMaps
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public abstract class OSMEntity {
    private final long id;
    private final Attributes attributes;

    /**
     * Construit une entité OSM dotée de l'identifiant unique et des attributs
     * donnés.
     * 
     * @param id
     *            identifiant unique de l'entité dans la base de données de
     *            OpenStreetMaps
     * @param attributes
     *            les attributs de l'entité
     */
    public OSMEntity(long id, Attributes attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    /**
     * Retourne l'identifiant unique de l'entité.
     * 
     * @return l'identifiant de l'identité, un long
     */
    public long id() {
        return id;
    }

    /**
     * Retourne les attributs de l'entité.
     * 
     * @return les attributs de l'entité
     */
    public Attributes attributes() {
        return attributes;
    }

    /**
     * Retourne vrai si et seulement si l'entité possède l'attribut passé en
     * argument.
     * 
     * @param key
     *            l'attribut dont on teste l'appartenance à l'entité
     * @return vrai si l'entité possède l'attribut donné, faux dans le cas
     *         contraire
     */
    public boolean hasAttribute(String key) {
        return attributes.contains(key);
    }

    /**
     * Retourne la valeur de l'attribut donné, ou null si celui-ci n'existe pas.
     * 
     * @param key
     *            l'attribut dont on cherche la valeur
     * @return la valeur associée à l'attribut si elle existe, null sinon
     */
    public String attributeValue(String key) {
        return attributes.get(key);
    }

    /**
     * Bâtisseur de la classe OSMEntity.
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public abstract static class Builder {
        /*
         * Utilisation du protected pour que les sous-classes puissent accéder
         * aux deux attributs id et attributesInProgress.
         */
        protected final long id;
        private boolean isIncomplete;
        protected final Attributes.Builder attributesInProgress;

        /**
         * Construit un bâtisseur pour une entité OSM identifiée par l'entier
         * donné.
         * 
         * @param id
         *            l'identifiant unique de l'entité
         */
        public Builder(long id) {
            this.id = id;
            attributesInProgress = new Attributes.Builder();
        }

        /**
         * Ajoute l'association clé/valeur donnée à lensemble d'attributs de
         * l'entité en construction. Remplace l'attribut par la nouvelle valeur
         * s'il était déjà présent.
         * 
         * @param key
         *            la clé de l'attribut à ajouter
         * @param value
         *            la valeur de l'attribut
         */
        public void setAttribute(String key, String value) {
            attributesInProgress.put(key, value);
        }

        /**
         * Déclare que l'entité en cours de construction est incomplète.
         */
        public void setIncomplete() {
            isIncomplete = true;
        }

        /**
         * Retourne vrai si et seulement si l'entité en cours de construction
         * est incomplète.
         * 
         * @return vrai si l'entité en construction ne peut pas être instanciée,
         *         faux dans le cas contraire
         */
        public boolean isIncomplete() {
            return isIncomplete;
        }
    }
}
