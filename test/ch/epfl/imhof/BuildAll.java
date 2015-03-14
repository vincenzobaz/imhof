package ch.epfl.imhof;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.osm.OSMNode;
import ch.epfl.imhof.osm.OSMWay;

public final class BuildAll {
    private BuildAll() {
    }

    public static Map<String, String> newHashMap(String... args) {
        Map<String, String> newHashMap = new HashMap<>();
        for (int i = 0; i < args.length - 1; ++i) {
            newHashMap.put(args[i], args[i + 1]);
        }
        return newHashMap;
    }

    public static Attributes newAttributes(String... args) {
        return new Attributes(newHashMap(args));
    }

    public static OSMWay newWay(long id) {
        List<OSMNode> nodes = new ArrayList<>();
        nodes.add(new OSMNode(00, new PointGeo(0, 0), newAttributes("key00",
                "value00")));
        nodes.add(new OSMNode(01, new PointGeo(1, 1), newAttributes("key01",
                "value01")));
        nodes.add(new OSMNode(02, new PointGeo(-1, -1), newAttributes("key02",
                "value02")));

        return new OSMWay(id, nodes, newAttributes("key", "value"));
    }
}
