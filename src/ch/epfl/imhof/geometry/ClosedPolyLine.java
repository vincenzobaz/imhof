package ch.epfl.imhof.geometry;

import java.util.List;

public final class ClosedPolyLine extends PolyLine {
    public ClosedPolyLine(List<Point> points) throws IllegalArgumentException {
        super(points);
        try {
            if (!samePoint(this.firstPoint(),
                    this.points().get(this.points().size() - 1))) {
                throw new IllegalArgumentException(
                        "Le 1er point est différent du dernier");
            }
            if (this.points().size() == 2) {
                throw new IllegalArgumentException("Un segment n'est pas un polygone");
            }
            for (int i = 0; i < this.points().size() - 1; ++i) {
                for (int j = i + 1; j < this.points().size(); ++j) {
                    if (samePoint(this.points().get(i), this.points().get(j))) {
                        throw new IllegalArgumentException("Un polygone ne peut pas avoir 2 fois le même sommet");
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public boolean isClosed() {
        return true;
    }

    public double area() {
        Point origine = new Point(0, 0);
        double area = 0;
        for (int i = 0; i < points().size(); i++) {
            area += signedTriangleArea(origine, getVertex(i), getVertex(i + 1));
        }
        return Math.abs(area);
    }

    private boolean samePoint(Point point1, Point point2) {
        return (point1.x() == point2.x() && point1.y() == point2.y());
    }

    private boolean isOnTheLeft(Point p, Point a, Point b) {
        return (signedTriangleArea(p, a, b) > 0);
    }

    private double signedTriangleArea(Point a, Point b, Point c) {
        return 0.5 * ((b.x() - a.x()) * (c.y() - a.y()) - (c.x() - a.x())
                * (b.y() - a.y()));
    }

    private Point getVertex(int n) {
        return points().get(Math.floorMod(n, points().size()));
    }

    public boolean containsPoint(Point p) {
        int index = 0;
        for (int i = 0; i <= points().size(); i++) {
            if (getVertex(i).y() <= p.y()) {
                if (getVertex(i + 1).y() > p.y()
                        && isOnTheLeft(p, getVertex(i), getVertex(i + 1))) {
                    index++;
                }
            } else if (getVertex(i + 1).y() <= p.y()
                    && isOnTheLeft(p, getVertex(i + 1), getVertex(i))) {
                index--;
            }
        }
        return index != 0;
    }
}
