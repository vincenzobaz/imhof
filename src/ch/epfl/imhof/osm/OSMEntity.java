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
     * Constructeur.
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
     * accesseur pour l'id de l'entité
     * 
     * @return ll'identifiant unique de l'identité
     */
    public long id() {
        return id;
    }

    /**
     * accesseur pour les attributes de l'entité
     * 
     * @return les attributs de l'entité
     */
    public Attributes attributes() {
        return attributes;
    }

    /**
     * vérifie si l'ensemble des attributs de l'entité contient un attribut
     * 
     * @param key
     * @return True si l'attribut est dans la liste, False dans le cas
     *         contraires
     */
    public boolean hasAttribute(String key) {
        return attributes.contains(key);
    }

    /**
     * permet d'acceder à un attribut à partir d'une clé
     * 
     * @param key
     *            la clé de l'attribut rechercé
     * @return La chaîne de charactères de l'attribut correspondant à la clé
     */
    public String attributeValue(String key) {
        return attributes.get(key);
    }

<<<<<<< HEAD
    /**
     * Bâtisseur pour les objets OSMEntity
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public abstract static class Builder {
=======
    public static abstract class Builder {
>>>>>>> 985d06695e5e6060d9f58fdf438b377cf5fa577a
        private final long id;
        private boolean isIncomplete;
        private Attributes.Builder attributesInProgress;

        /**
         * constructeur du bâtisseur. Cet objet s'instancie à partir d'un
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
         * ajoute un attribut à la liste des attributs
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
         * Permet de savoir si le bâtisseur per instancier une OSMEntity
         * 
         * @return True si OSMEntity ne peut pas être instanciée, False dans le
         *         cas contraire
         */
        public boolean isIncomplete() {
            return isIncomplete;
        }

        /**
         * Cette méthode est nécéssaire pour que les classes héritant de
         * celle-ci puissent accéder à la liste d'attribut.
         * 
         * @return Attributes les attributs de l'entité
         */
        protected Attributes attributesBuild() {
            return attributesInProgress.build();
        }

        /**
         * Cette méthode est nécéssaire pour que les classes héritant de
         * celle-ci puissent accéder à l'identifiant unique de l'entité
         * 
         * @return long id l'identifiant unique de l'entité
         */
        protected long idBuild() {
            return id;
        }
    }
}
