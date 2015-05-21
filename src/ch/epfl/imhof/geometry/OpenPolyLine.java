package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * Classe représentant une polyligne ouverte, héritant de
 * {@link ch.epfl.imhof.geometry.PolyLine PolyLine}. Elle est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 * 
 */
public final class OpenPolyLine extends PolyLine {

    /**
     * Construit une polyligne ouverte ayant les sommets donnés.
     * 
     * @param points
     *            la liste des points qui constituent la polyligne
     */
    public OpenPolyLine(List<Point> points) {
        super(points);
    }

    /**
     * Retourne <code>false</code>, la polyligne est ouverte.
     */
    @Override
    public boolean isClosed() {
        return false;
    }
}
