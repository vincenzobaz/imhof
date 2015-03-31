package ch.epfl.imhof;

import java.util.Collections;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

/**
 * Classe représentant un ensemble d'attributs, chacun étant un couple
 * clé/valeur. Elle est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class Attributes {
    private final Map<String, String> attributes;

    /**
     * Construit un ensemble d'attributs avec les paires clef/valeur présentes
     * dans la table associative donnée.
     * 
     * @param attributes
     *            la table associative contenant les paires clé/valeur
     *            constituant les attributs
     */
    public Attributes(Map<String, String> attributes) {
        this.attributes = Collections
                .unmodifiableMap(new HashMap<>(attributes));
    }

    /**
     * Retourne vrai si et seulement si l'ensemble d'attributs est vide.
     * 
     * @return <code>true</code> si l'ensemble d'attributs est vide,
     *         <code>false</code> dans le cas contraire.
     */
    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    /**
     * Retourne vrai si l'ensemble d'attributs contient la clef donnée.
     * 
     * @param key
     *            la clé dont on cherche l'appartenance à l'ensemble
     *            d'attributs, sous forme de <code>String</code>
     * @return <code>true</code> si la clé appartient à la liste d'attributs,
     *         <code>false</code> dans le cas contraire
     * 
     */
    public boolean contains(String key) {
        return attributes.containsKey(key);
    }

    /**
     * Retourne la valeur associée à la clef donnée, si elle existe. Retourne
     * <code>null</code> si la clef n'est pas présente dans la table
     * associative.
     * 
     * @param key
     *            la clé dont on cherche la valeur associée, sous forme de
     *            <code>String</code>
     * @return la valeur associée à la clé passée en paramètre si elle existe,
     *         <code>null</code> sinon
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
     *            <code>String</code>
     * @param defaultValue
     *            la valeur par défaut, une <code>String</code>
     * @return la valeur associée à la clé passée en paramètre, la valeur par
     *         défaut si aucune valeur ne lui est associée
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
     *            la clé dont on cherche l'entier associé, sous forme de
     *            <code>String</code>
     * @param defaultValue
     *            la valeur par défaut, un <code>int</code>
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
     *            les valeurs qu'on veut garder, sous forme de <code>Set</code>
     * @return un objet de type <code>Attributes</code> filtré
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
     * Bâtisseur de la classe <code>Attributes</code>.
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static final class Builder {
        private final Map<String, String> attributes;

        /**
         * Constructeur par défaut du bâtisseur.
         */
        public Builder() {
            attributes = new HashMap<>();
        }

        /**
         * Ajoute la paire clé, valeur donnée à l'ensemble d'attributs en cours
         * de construction. Remplace la valeur associée à la clé par la nouvelle
         * si la clé est déjà présente dans l'ensemble d'attributs.
         * 
         * @param key
         *            la clé, une <code>String</code>
         * @param value
         *            la valeur, un <code>String</code>
         */
        public void put(String key, String value) {
            attributes.put(key, value);
        }

        /**
         * Construit et retourne un ensemble d'attributs contenant les
         * associations clef/valeur ajoutées jusqu'à présent.
         * 
         * @return l'objet <code>Attributes</code> en construction
         */
        public Attributes build() {
            return new Attributes(attributes);
        }
    }
}
