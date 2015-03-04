package ch.epfl.imhof.osm;

import static org.junit.Assert.*;
import ch.epfl.imhof.*;

import org.junit.Test;

import java.util.*;

public final class OSMNodeTest {
    @Test
    public void positionReturnsRightValues(){
        OSMNode prova = new OSMNode(123456789, new PointGeo(Math.PI/3, Math.PI/3), genereAttribuz("il cane", "il gatto", "il topo", "l'elefante", "nicolas", "maxime" ));
       assertTrue(Math.PI/3 == prova.position().latitude()); 
       assertTrue(Math.PI/3 == prova.position().longitude());
    }

    public Attributes genereAttribuz(String... args){    
        HashMap<String, String> newHashMap = new HashMap<>();
            for (int i = 0; i < args.length - 1; ++i) {
                newHashMap.put(args[i], args[i + 1]);
            }
            return new Attributes(newHashMap);
        }
}
