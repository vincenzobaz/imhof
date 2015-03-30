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
     * Construit un point avec la longitude et la latitude données (en radians).
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
                    "La longitude doit être comprise entre -π et π");
        }
        if (coordinateIsInvalid(latitude, 2.0)) {
            throw new IllegalArgumentException(
                    "La latitude doit être comprise entre -π/2 et π/2");
        }
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Retourne la longitude du point.
     * 
     * @return la longitude du point, en radians
     */
    public double longitude() {
        return longitude;
    }

    /**
     * Retourne la latitude du point.
     * 
     * @return la latitude du point, en radians
     */
    public double latitude() {
        return latitude;
    }

    /**
     * Teste si la coordonnée fournie est valide.
     * 
     * @param coordinate
     *            coordonnée à tester, en radians
     * @param parameter
     *            modificateur d'intervalle, 1.0 pour la longitude, 2.0 pour la
     *            latitude
     * @return <code>true</code> si la coordonnée est invalide,
     *         <code>false</code> dans le cas contraire.
     */
    private boolean coordinateIsInvalid(double coordinate, double parameter) {
        return (coordinate < -Math.PI / parameter || coordinate > Math.PI
                / parameter);
    }
}
