package ch.epfl.imhof;

public final class PointGeo {
    private final double longitude;
    private final double latitude;
    
    public PointGeo(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
    
    public double longitude() {
        return longitude;
    }
    
    public double latitude() {
        return latitude;
    }
}
