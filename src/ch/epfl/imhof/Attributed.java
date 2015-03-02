package ch.epfl.imhof;

/**
 * Cette classe représente une entité de type T dotée d'Attributes.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 * @param <T>
 *            Le type de l'entité à laquelle cette classe associe des attributs.
 */
public final class Attributed<T> {
    private final T value;
    private final Attributes attributes;

    /**
     * Construit une valeur attribuée.
     * 
     * @param value
     *            l'entité à laquelle on veut associer des attributs
     * @param attributes
     *            les attributs qu'on veut associer à notre entité
     */
    public Attributed(T value, Attributes attributes) {
        this.value = value;
        this.attributes = attributes;
    }

    /**
     * Accesseur
     * 
     * @return la valeur à laquelle les attributs sont attachés
     */
    public T value() {
        return value;
    }

    /**
     * Accesseur
     * 
     * @return les attributs attachés à la valeur
     */
    public Attributes attributes() {
        return attributes;
    }

    /**
     * Méthode qui teste si les attributes incluent une chaîne de caractères
     * 
     * @param attributeName
     *            l'attribut dont on veut vérifier l'appartenance à notre
     *            ensemble d'attributs
     * @return Vrai si et seulement si les attributs incluent l'argument
     */
    public boolean hasAttribute(String attributeName) {
        return attributes.contains(attributeName);
    }

    /**
     * Retourne la valeur associée à l'attribut donné, ou null si celui-ci
     * n'existe pas.
     * 
     * @param attributeName
     *            l'attribut dont on cherche la valeur
     * @return la valeur associée à l'attribut reçu en argument
     */
    public String attributeValue(String attributeName) {
        return attributes.get(attributeName);
    }

    /**
     * Retourne la valeur associée à l'attribut donné, ou la valeur par défaut.
     * 
     * @param attributeName
     * @param defaultValue
     * @return la valeur associée à l'attribut donné ou la valeur par défaut si
     *         celui-ci n'existe pas
     */
    public String attributeValue(String attributeName, String defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }

    /**
     * Retourne la valeur entière associée à l'attribut donné, ou la valeur par
     * défaut.
     * 
     * @param attributeName
     * @param defaultValue
     * @return la valeur associée à l'attribut donné ou la valeur par défaut si
     *         celui-ci n'existe pas ou si la valeur qui lui est associée n'est
     *         pas un entier valide
     */
    public int attributeValue(String attributeName, int defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }
}
