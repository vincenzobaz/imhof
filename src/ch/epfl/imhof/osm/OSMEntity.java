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
     * Constructeur
     * 
     * @param id
     *            identificateur unique d'une entité dans la base de données de
     *            OpenStreetMaps
     * @param attributes
     *            les attributs de l'entité
     */
    public OSMEntity(long id, Attributes attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    /**
     * Accesseur pour l'id de l'entité
     * 
     * @return l'identifiant unique de l'identité
     */
    public long id() {
        return id;
    }

    /**
     * Accesseur pour les attributs de l'entité
     * 
     * @return les attributs de l'entité
     */
    public Attributes attributes() {
        return attributes;
    }

    /**
     * Vérifie si l'ensemble des attributs de l'entité contient un attribut
     * 
     * @param key
     * @return True si l'attribut est dans la liste, False dans le cas contraire
     */
    public boolean hasAttribute(String key) {
        return attributes.contains(key);
    }

    /**
     * Retourne la valeur de l'attribut donné
     * 
     * @param key
     *            la clé de l'attribut dont on cherche la valeur
     * @return la valeur associée à la clé, ou null si elle n'existe pas
     */
    public String attributeValue(String key) {
        return attributes.get(key);
    }

    /**
     * Bâtisseur pour les objets OSMEntity
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public abstract static class Builder {
        private final long id;
        private boolean isIncomplete;
        private Attributes.Builder attributesInProgress;

        /**
         * Constructeur du bâtisseur. Cet objet s'instancie à partir d'un
         * identifiant unique
         * 
         * @param id
         *            l'identifiant unique de l'entité
         */
        public Builder(long id) {
            this.id = id;
            attributesInProgress = new Attributes.Builder();
        }

        /**
         * Ajoute une association clé/valeur à l'attribut de l'entité en
         * construction
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
         * Déclare que l'instance du bâtisseur n'est pas prête pour appeler
         * build() et créer une instance de OSMEntity
         */
        public void setIncomplete() {
            isIncomplete = true;
        }

        /**
         * Permet de savoir si le bâtisseur peut instancier une OSMEntity
         * 
         * @return True si OSMEntity ne peut pas être instanciée, False dans le
         *         cas contraire
         */
        public boolean isIncomplete() {
            return isIncomplete;
        }

        /**
         * Cette méthode est nécessaire pour que les classes héritant de
         * celle-ci puissent accéder à la liste d'attribut.
         * 
         * @return les attributs de l'entité, un Attributes
         */
        protected Attributes attributesBuild() {
            return attributesInProgress.build();
        }

        /**
         * Cette méthode est nécessaire pour que les classes héritant de
         * celle-ci puissent accéder à l'identifiant unique de l'entité
         * 
         * @return l'identifiant unique de l'entité, un long
         */
        protected long idBuild() {
            return id;
        }
    }
}
