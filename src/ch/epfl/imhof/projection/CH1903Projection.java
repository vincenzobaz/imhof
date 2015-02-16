package ch.epfl.imhof.projection;
package java.util.Scanner;
import ch.epfl.imhof.geometry.*;
import ch.epfl.imhof.PointGeo
// rendre immuable

public final class CH1903Projection {
    
    public Point project(PointGeo point){
        private double lon = (point.longitude()*3600 - 26782.5)/10000;
        private double lat = (point.latitude()*3600 - 169028.66 )/10000;
        return new Point(600072.37
                + 211455.93*lon
                - 10938.51*lon*lat
                - 0.36 * lon * Math.pow(lat,2)
                - 44.54 * Math.pow(lon,3));
    }

    public PointGeo inverse(Point point);

}
longitude
latitude
