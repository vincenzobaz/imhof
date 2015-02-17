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

    private boolean testCoordonnee(double coordonnee, double parametre) {
        return (coordonnee < -Math.PI / parametre || coordonnee > Math.PI / parametre);
    }
}
