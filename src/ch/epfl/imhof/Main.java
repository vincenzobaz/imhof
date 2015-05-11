import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

public final class Main {

    public static void main(String[] args) {

        PointGeo topRight = new PointGeo(Math.toRadians(Double
                .parseDouble(args[4])), Math.toRadians(Double
                .parseDouble(args[5])));
        PointGeo bottomLeft = new PointGeo(Math.toRadians(Double
                .parseDouble(args[2])), Math.toRadians(Double
                .parseDouble(args[3])));
        int resolutionPixelPerMeter = (int) Math.round(Integer.parseInt(args[6])
                * (5000d / 127d));
        int height = (int) Math.round(resolutionPixelPerMeter
                * (1 / 25000d)
                * Math.toRadians(Double.parseDouble(args[5])
                        - Double.parseDouble(args[3])) * Earth.RADIUS);

        Projection ch1903 = new CH1903Projection();
        Point projectedTopRight = ch1903.project(topRight);
        Point projectedBottomLeft = ch1903.project(bottomLeft);

        int width = (int) Math
                .round((projectedTopRight.x() - projectedBottomLeft.x())
                        / (projectedTopRight.y() - projectedBottomLeft.y())
                        * height);
        int shadingRadiusPixels = (int) Math.round((1.7 * resolutionPixelPerMeter) / 1000d);
        
        

    }
    
    private BufferedImage combine(BufferedImage shaderRelief, BufferedImage flatMap){
        
    }
    
    
}
