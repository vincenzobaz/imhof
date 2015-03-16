package ch.epfl.imhof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public final class Map {

    List<Attributed<PolyLine>> polyLines;
    List<Attributed<Polygon>> polygons;

    public Map(List<Attributed<PolyLine>> polyLines,
            List<Attributed<Polygon>> polygons) {
        this.polyLines = Collections
                .unmodifiableList(new ArrayList<Attributed<PolyLine>>(polyLines));
        this.polygons = Collections
                .unmodifiableList(new ArrayList<Attributed<Polygon>>(polygons));
    }

    public List<Attributed<PolyLine>> polyLines() {
        return polyLines;
    }

    public List<Attributed<Polygon>> polygons() {
        return polygons;
    }

    public static class Builder {
        List<Attributed<PolyLine>> polyLines;
        List<Attributed<Polygon>> polygons;

        public Builder(){
            polyLines = new ArrayList<>();
            polygons = new ArrayList<>();
        }
        
        public void addPolyLine(Attributed<PolyLine> newPolyLine){
            polyLines.add(newPolyLine);
        }
        
        public void addPolygon(Attributed<Polygon> newPolygon){
            polygons.add(newPolygon);
        }
        
        public Map build(){
            return new Map(polyLines, polygons);
        }
    }

}
