package ch.epfl.imhof.painting;
import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.CH1903Projection;

public final class FiltersTest {
    private Map rolex(){
	OSMMap newMap = null;
	try {
	    newMap = OSMMapReader.readOSMFile("data/lc.osm", false);
	} catch(Exception e){
	    e.printStackTrace();
	}
        OSMToGeoTransformer convertisseur = new OSMToGeoTransformer(new CH1903Projection());
        return convertisseur.transform(newMap);
    }
    
    @Test
    public void booleanTagged(){
        Map map = rolex();
        boolean containsTag  = false;
        for (Attributed<PolyLine> line : map.polyLines()){
            containsTag = containsTag || Filters.tagged("layer").test(line);
        }
        for (Attributed<Polygon> polygon : map.polygons()){
            containsTag = containsTag || Filters.tagged("layer").test(polygon);
        }
        assertTrue(containsTag);
    }
    
    @Test
    public void taggedReturnsString(){
        Map map = rolex();
        boolean containsTagANDValue = false;
        for (Attributed<Polygon> polygon : map.polygons()){
           containsTagANDValue = containsTagANDValue || Filters.tagged("building", "yes").test(polygon); 
        }
        assertTrue(containsTagANDValue);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void isOnLayerTHROWS (){
        boolean exceptionBORDELE = Filters.isOnLayer(-7).test(rolex().polygons().get(1));
    }
    
    @Test
    public void isOnLayerRETURNSRIGHT(){
        Map map = rolex();
        for (Attributed<PolyLine> line : map.polyLines()){
            if (Filters.tagged("layer").test(line)){
                assertTrue(Filters.isOnLayer(1).test(line));
            } else 
                assertTrue(Filters.isOnLayer(0).test(line));
        }
        
        assertTrue(Filters.isOnLayer(0).test(map.polygons().get(0)));
    }
}
