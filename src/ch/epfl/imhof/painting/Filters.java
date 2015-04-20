package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

/**
 * Classe non instanciable offrant trois méthodes statiques fournissant des
 * prédicats pour tester les attributs d'instances attribuées. Toutes les
 * méthode redéfinissent de façon anonyme la méthode <code>test(T t)</code> de
 * l'interface fonctionnelle <code>java.util.function.Predicate</code>.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class Filters {
    /**
     * La classe est non instanciable, ainsi le constructeur est privé.
     */
    private Filters() {
    }

    /**
     * Retourne un <code>Predicate</code> qui est vrai ssi la valeur attribuée à
     * laquelle on l'applique possède l'attribut ayant le nom de l'argument.
     * 
     * @param attributeName
     *            le nom de l'attribut dont on veut vérifier l'appartenance à la
     *            valeur attribuée
     * @return un filtre (<code>Predicate</code>)
     */
    public static Predicate<Attributed<?>> tagged(String attributeName) {
        return x -> x.hasAttribute(attributeName);
        // return Attributed<?>::hasAttribute;
    }

    /**
     * Retourne un <code>Predicate</code> qui est vrai ssi la valeur attribuée à
     * laquelle on l'applique possède l'attribut ayant le nom de l'argument et
     * si de plus la valeur associée à cet attribut fait partie de celles
     * données.
     * 
     * @param attributeName
     *            le nom de l'attribut dont on veut vérifier l'appartenance à la
     *            valeur attribuée
     * @param attributeValues
     *            tableau de valeurs dont on veut vérifier l'association avec le
     *            paramètre attributeName. L'ellipse permet à l'utilisateur de
     *            fournir à la méthode un nombre variable de valeurs sans devoir
     *            définir une <code>Collection</code>.
     * 
     * @return un filtre (<code>Predicate</code>)
     */
    public static Predicate<Attributed<?>> tagged(String attributeName,
            String... attributeValues) {
        // nombre supérieur à 1?
        return x -> {
            // On ne vérifie l'association attributeName-attributeValue que si
            // attributeName est le nom d'un attribut possédé par la valeur
            // attribuée
            /*if (tagged(attributeName).test(x)) {
                for (String s : attributeValues) {
                    if (s.equals(x.attributeValue(attributeName)))
                        return true;
                }
            }*/
            
            for (String s : attributeValues) {
                if (s.equals(x.attributeValue(attributeName, "defaultString"))) {
                    return true;
                }
            }
            return false;
        };
    }

    /**
     * Retourne un <code>Predicate</code> qui est vrai ssi la valeur attribuée à
     * laquelle on l'applique possède l'attribut <code>layer</code> associé à la
     * valeur entière en argument.
     * 
     * @param layer
     *            entier identifiant la couche que nous voulons tester pour
     *            savoir si elle contient la valeur attribuée
     * @return un filtre (<code>Predicate</code>)
     * @throws IllegalArgumentException
     *             lève une exception si le paramètre identifiant la couche est
     *             un entier inférieur à -5 ou supérieur à 5
     */
    public static Predicate<Attributed<?>> onLayer(int layer)
            throws IllegalArgumentException {
        // ignorer ce truc?
        if (layer < -5 || layer > 5) {
            throw new IllegalArgumentException(
                    "the value of the attribute layer is an integer between -5 and 5");
        }
        return x -> layer == x.attributeValue("layer", 0);
    }
}
