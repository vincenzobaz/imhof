package ch.epfl.imhof;

public final class PointGeo {
    private final double longitude;
    private final double latitude;
    
    public PointGeo(double longitude, double latitude) {
        try {
            if (testLongitude(longitude)) {
                throw new IllegalArgumentException("La Longitude doit être comprise entre -Pi et Pi");
            }
            if (testLatitude(latitude)) {
                throw new IllegalArgumentException("La latitude doit être comprise entre -PI/2 et Pi/2");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.longitude = longitude;
        this.latitude = latitude;
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
