package ch.epfl.imhof;

/**
 * Classe définissant l'objet point en coordonnées sphériques. Celui-ci est
 * caractérisé par une latitude et par une longitude. On fournit deux méthodes
 * d'accès aux attributs. La classe est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733), Nicolas Phan Van (239293)
 *
 */
public final class PointGeo {
    private final double longitude;
    private final double latitude;

    public PointGeo(double longitude, double latitude)
            throws IllegalArgumentException {
        try {
            if (testLongitude(longitude)) {
                throw new IllegalArgumentException(
                        "La Longitude doit être comprise entre -Π et Π");
            }
            if (testLatitude(latitude)) {
                throw new IllegalArgumentException(
                        "La latitude doit être comprise entre -Π/2 et Π/2");
            }
            this.longitude = longitude;
            this.latitude = latitude;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public double longitude() {
        return longitude;
    }

    public double latitude() {
        return latitude;
    }

    private boolean testLongitude(double longitude) {
        return (longitude < -Math.PI || longitude > Math.PI);
    }

    private boolean testLatitude(double latitude) {
        return (latitude < -Math.PI / 2 || latitude > Math.PI / 2);
    }
}
