package ch.epfl.imhof;

import java.util.Collections;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

/**
 * Classe représentant un ensemble d'attributs et leurs valeurs associées.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class Attributes {
    private final Map<String, String> attributes;

    /**
     * Construit un nouvel ensemble d'attributs à partir de la table passée en
     * paramètre
     * 
     * @param attributes
     *            les attributs, sous forme de Map de deux String
     */
    public Attributes(Map<String, String> attributes) {
        //Map<String, String> temp = new HashMap<>(attributes);
        //this.attributes = Collections.unmodifiableMap(temp);
        this.attributes = Collections.unmodifiableMap(new HashMap<>(attributes));
    }

    /**
     * Retourne vrai si et seulement si l'ensemble d'attributs est vide.
     * 
     * @return true si l'ensemble d'attributs est vide, false sinon
     */
    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    /**
     * Retourne vrai si l'ensemble d'attributs contient la clef donnée.
     * 
     * @param key
     *            la clé dont on cherche l'appartenance à l'ensemble
     *            d'attributs, sous forme de String
     * @return true si la clé appartient à la liste d'attributs, false sinon
     */
    public boolean contains(String key) {
        return attributes.containsKey(key);
    }

    /**
     * Retourne la valeur associée à la clef donnée, ou null si la clef n'existe
     * pas.
     * 
     * @param key
     *            la clé dont on cherche la valeur associée, sous forme de
     *            String
     * @return la valeur associée à la clé passée en paramètre si elle existe,
     *         null sinon
     */
    public String get(String key) {
        return attributes.get(key);
    }

    /**
     * Retourne la valeur associée à la clef donnée, ou la valeur par défaut
     * donnée si aucune valeur ne lui est associée.
     * 
     * @param key
     *            la clé dont on cherche la valeur associée, sous forme de
     *            String
     * @param defaultValue
     *            la valeur par défaut, une String
     * @return la valeur associée à la clé passée en paramètre, la valeur par
     *         défaut si aucune valeur ne lui est associé
     */
    public String get(String key, String defaultValue) {
        return attributes.getOrDefault(key, defaultValue);
    }

    /**
     * Retourne l'entier associé à la clef donnée, ou la valeur par défaut
     * donnée si aucune valeur ne lui est associée, ou si cette valeur n'est pas
     * un entier valide.
     * 
     * @param key
     *            la clé dont on cherche l'entier associé, sous forme de String
     * @param defaultValue
     *            la valeur par défaut, un int
     * @return l'entier associé à la clé passée en paramètre s'il existe, la
     *         valeur par défaut sinon
     */
    public int get(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Retourne une version filtrée des attributs ne contenant que ceux dont le
     * nom figure dans l'ensemble passé.
     * 
     * @param keysToKeep
     *            les valeurs qu'on veut garder, sous forme de Set
     * @return un Attributes, filtré
     */
    public Attributes keepOnlyKeys(Set<String> keysToKeep) {
        Builder filteredAttributes = new Builder();
        for (String s : keysToKeep) {
            if (contains(s)) {
                filteredAttributes.put(s, get(s));
            }
        }
        return filteredAttributes.build();
    }

    /**
     * Bâtisseur de la classe Attributes
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static final class Builder {
        private HashMap<String, String> attributes;

        /**
         * Ajoute le couple clé, valeur à l'Attributes en construction
         * 
         * @param key
         *            la clé, une String
         * @param value
         *            la valeur, une String
         */
        public void put(String key, String value) {
            attributes.put(key, value);
        }

        /**
         * Construit un ensemble d'attributs contenant les associations
         * clef/valeur ajoutées jusqu'à présent.
         * 
         * @return l'Attributes en construction
         */
        public Attributes build() {
            return new Attributes(attributes);
        }
    }
}
