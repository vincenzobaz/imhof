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
        if (coordinateIsInvalid(longitude, 1.0)) {
            throw new IllegalArgumentException(
                    "La longitude doit être comprise entre -Π et Π");
        }
        if (coordinateIsInvalid(latitude, 2.0)) {
            throw new IllegalArgumentException(
                    "La latitude doit être comprise entre -Π/2 et Π/2");
        }
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Accesseur de la longitude
     * 
     * @return l'attribut longitude du point
     */
    public double longitude() {
        return longitude;
    }

    /**
     * Accesseur de la latitude
     * 
     * @return l'attribut latitude du point
     */
    public double latitude() {
        return latitude;
    }

    /**
     * Teste si la coordonnée fournie est valide
     * 
     * @param coordinate
     *            coordonnée à tester, en radians
     * @param parameter
     *            modificateur d'intervalle, 1.0 pour la longitude, 2.0 pour la
     *            latitude
     * @return true si la coordonée est invalide, false sinon
     */
    private boolean coordinateIsInvalid(double coordinate, double parameter) {
        return (coordinate < -Math.PI / parameter || coordinate > Math.PI
                / parameter);
    }
}
