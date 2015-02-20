package ch.epfl.imhof;

/**
 * Classe définissant l'objet point en coordonnées sphériques. Celui-ci est
 * caractérisé par une latitude et par une longitude. On fournit deux méthodes
 * d'accès aux attributs. La classe est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class PointGeo {
    private final double longitude;
    private final double latitude;

    /**
     * Construit un point avec les coordonnées données
     * 
     * @param longitude
     *            la longitude du point, en radians
     * @param latitude
     *            la latitude du point, en radians
     * @throws IllegalArgumentException
     *             lève une exception si la latitude ou la longitude fournies
     *             sont hors des intervalles autorisés
     */
    public PointGeo(double longitude, double latitude)
            throws IllegalArgumentException {
        try {
            if (testCoordonnee(longitude, 1.0)) {
                throw new IllegalArgumentException(
                        "La longitude doit être comprise entre -Π et Π");
            }
            if (testCoordonnee(latitude, 2.0)) {
                throw new IllegalArgumentException(
                        "La latitude doit être comprise entre -Π/2 et Π/2");
            }
            this.longitude = longitude;
            this.latitude = latitude;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Getter pour la longitude
     * 
     * @return l'attribut longitude du point
     */
    public double longitude() {
        return longitude;
    }

    /**
     * Getter pour la latitude
     * 
     * @return l'attribut latitude du point
     */
    public double latitude() {
        return latitude;
    }

    /**
     * Teste si la coordonnée est valide
     * 
     * @param coordonnee
     *            coordonnée à tester, en radians
     * @param parametre
     *            modificateur d'intervalle, 1.0 pour la longitude, 2.0 pour la
     *            latitude
     * @return false si la coordonée est valide, true sinon
     */
    private boolean testCoordonnee(double coordonnee, double parametre) {
        return (coordonnee < -Math.PI / parametre || coordonnee > Math.PI
                / parametre);
    }
}
