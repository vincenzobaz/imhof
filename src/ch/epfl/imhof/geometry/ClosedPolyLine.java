package ch.epfl.imhof.geometry;

import java.util.List;

public final class ClosedPolyLine extends PolyLine {
    public ClosedPolyLine(List<Point> points) throws IllegalArgumentException {
        super(points);
        try {
            if (samePoint(this.points().get(0), this.points().get(this.points().size() - 1)) {
                throw new IllegalArgumentException ("Le 1er point est différent du dernier");
            }
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    public boolean isClosed() {
        return true;
    }
    
    public double area() {
        
    }
    
    private boolean samePoint(Point point1, Point point2) {
        return (point1.x() == point2.x() && point1.y() == point2.y());
    }
    
    private boolean isOnTheLeft (Point p, Point a, Point b){
        return  (signedTriangleArea(p, a, b)>0);
    }
    
    private double signedTriangleArea(Point a, Point b, Point c){
        return 0.5*((b.x()- a.x())*(c.y()-a.y())-(c.x()-a.x())*(b.y()-a.y()));
    }
    
}
