package ch.epfl.imhof.geometry;

import java.util.List;
import java.util.Collections;

public final class Polygon {
    private ClosedPolyLine shell;
    private List<ClosedPolyLine> holes;
    
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes) {
        this.shell = shell;
        this.holes = Collections.unmodifiableList(holes);
    }
    
    public Polygon(ClosedPolyLine shell) {
        this.shell = shell;
    }
    
    public PolyLine shell() {
        return shell;
    }
    
    public List<ClosedPolyLine> holes() {
        return holes;
    }
}
