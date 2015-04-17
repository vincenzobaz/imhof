package ch.epfl.imhof.painting;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.Iterator;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public interface Painter<E> {
    void drawMap(Map map, Canvas canvas);
    
    public static Painter<Polygon> polygon (Color fillColor){
        return (map, canvas) ->{
            for (Attributed<Polygon> attributed : map.polygons()) {
                PolyLine shell = attributed.value().shell();
                Path2D shellPath = new Path2D.Double() ;
                shellPath.moveTo(shell.firstPoint().x(), shell.firstPoint().y() );
                Iterator<Point> iterator = shell.points().iterator();
                iterator.next();
                while (iterator.hasNext()){
                    Point point = iterator.next();
                    shellPath.lineTo(point.x(), point.y());
                }
                shellPath.closePath();
                Area area = new Area(shellPath);
            }
        };
    }
    
}