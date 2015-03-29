package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * Classe définisssant l'objet Polyligne ouvert. On fournit un constructeur
 * public. La classe est immuable.
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
     * Retourne faux, la polyligne est ouverte.
     */
    @Override
    public boolean isClosed() {
        return false;
    }
}
