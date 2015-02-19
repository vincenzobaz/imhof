package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * Classe définisssant l'objet Polyligne ouvert. On fournit un constructeur
 * public. La classe est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733), Nicolas Phan Van (239293)
 * @author Nicolas Phan Van (239293)
 */
public final class OpenPolyLine extends PolyLine {
    public OpenPolyLine(List<Point> points) {
        super(points);
    }

    public boolean isClosed() {
        return false;
    }
}
