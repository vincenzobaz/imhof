package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

/**
 * Classe non instanciable offrant trois méthodes statiques fournissant des
 * prédicats pour tester les attributs de valeurs attribuées. Toutes les
 * méthodes redéfinissent de façon anonyme la méthode <code>test(T t)</code> de
 * l'interface fonctionnelle <code>java.util.function.Predicate</code>.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class Filters {
    /**
     * La classe est non instanciable, le constructeur est privé.
     */
    private Filters() {
    }

    /**
     * Retourne un prédicat qui est vrai ssi la valeur attribuée à laquelle on
     * l'applique possède l'attribut ayant le nom donné.
     * 
     * @param attributeName
     *            le nom de l'attribut dont on veut vérifier l'appartenance à la
     *            valeur attribuée
     * @return un filtre (<code>Predicate</code>)
     */
    public static Predicate<Attributed<?>> tagged(String attributeName) {
        return x -> x.hasAttribute(attributeName);
    }

    /**
     * Retourne un prédicat qui est vrai ssi la valeur attribuée à laquelle on
     * l'applique possède l'attribut ayant le nom donné et si de plus la valeur
     * associée à cet attribut fait partie de celles données.
     * 
     * @param attributeName
     *            le nom de l'attribut dont on veut vérifier l'appartenance à la
     *            valeur attribuée
     * @param attributeValues
     *            tableau de valeurs dont on veut vérifier l'association avec le
     *            paramètre attributeName. L'ellipse permet à l'utilisateur de
     *            fournir à la méthode un nombre variable de valeurs sans devoir
     *            définir une <code>Collection</code>
     * 
     * @return un filtre (<code>Predicate</code>)
     * @throws IllegalArgumentException
     *             lève une exception si on ne reçoit pas de valeurs d'attribut
     *             à tester
     */
    public static Predicate<Attributed<?>> tagged(String attributeName,
            String... attributeValues) throws IllegalArgumentException {
        if (attributeValues == null || attributeValues.length == 0) {
            throw new IllegalArgumentException(
                    "On doit avoir au moins une valeur d'attribut.");
        }

        return x -> {
            if (x.hasAttribute(attributeName)) {
                for (String s : attributeValues) {
                    if (s.equals(x.attributeValue(attributeName))) {
                        return true;
                    }
                }
            }
            return false;
        };
    }

    /**
     * Retourne un prédicat qui est vrai ssi la valeur attribuée à laquelle on
     * l'applique possède l'attribut <code>layer</code> associé à la valeur
     * entière donnée.
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
        if (layer < -5 || layer > 5) {
            throw new IllegalArgumentException(
                    "Le numéro de couche doit être compris entre -5 et 5.");
        }
        return x -> layer == x.attributeValue("layer", 0);
    }
}
