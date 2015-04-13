package ch.epfl.imhof;

/**
 * Cette classe représente un objet de type T doté d'attributs.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 * @param <T>
 *            Le type d'objet auquel cette classe associe des attributs.
 */
public final class Attributed<T> {
    private final T value;
    private final Attributes attributes;

    /**
     * Construit une valeur attribuée dont la valeur et les attributs sont ceux
     * donnés.
     * 
     * @param value
     *            l'objet auquel on veut associer des attributs
     * @param attributes
     *            les attributs qu'on veut associer à l'objet
     */
    public Attributed(T value, Attributes attributes) {
        this.value = value;
        this.attributes = attributes;
    }

    /**
     * Retourne la valeur à laquelle les attributs sont attachés.
     * 
     * @return un objet générique attribué
     */
    public T value() {
        return value;
    }

    /**
     * Retourne les attributs attachés à la valeur.
     * 
     * @return les attributs de l'objet
     */
    public Attributes attributes() {
        return attributes;
    }

    /**
     * Retourne vrai si et seulement si les attributs incluent celui dont le nom
     * est passé en argument.
     * 
     * @param attributeName
     *            l'attribut dont on veut vérifier l'appartenance à l'ensemble
     *            d'attributs
     * @return <code>true </code> si l'attribut donné est présent,
     *         <code>false</code> dans le cas contraire
     */
    public boolean hasAttribute(String attributeName) {
        return attributes.contains(attributeName);
    }

    /**
     * Retourne la valeur associée à l'attribut donné, ou <code>null</code> si
     * celui-ci n'existe pas.
     * 
     * @param attributeName
     *            l'attribut dont on cherche la valeur
     * @return la valeur associée à l'attribut, ou <code>null</code> si celle-ci
     *         n'est pas présente
     */
    public String attributeValue(String attributeName) {
        return attributes.get(attributeName);
    }

    /**
     * Retourne la valeur associée à l'attribut donné, ou la valeur par défaut
     * donnée si celui-ci n'existe pas.
     * 
     * @param attributeName
     *            l'attribut dont on cherche la valeur
     * @param defaultValue
     *            la valeur par défaut
     * @return la valeur associée à l'attribut, la valeur par défaut si celui-ci
     *         n'existe pas
     */
    public String attributeValue(String attributeName, String defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }

    /**
     * Retourne la valeur entière associée à l'attribut donné, ou la valeur par
     * défaut si celui-ci n'existe pas ou si la valeur qui lui est associée
     * n'est pas un entier valide.
     * 
     * @param attributeName
     *            l'attribut dont on cherche la valeur
     * @param defaultValue
     *            la valeur par défaut
     * @return la valeur associée à l'attribut, la valeur par défaut dans les
     *         cas problématiques
     */
    public int attributeValue(String attributeName, int defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }
}
