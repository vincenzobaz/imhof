package ch.epfl.imhof.projection;

public interface Projection {
    Point project(PointGeo point);
    PointGeo inverse(Point point);
}
