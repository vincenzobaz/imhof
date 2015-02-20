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
     * Le contructeur de la polyligne ouverte
     * 
     * @param points
     *            la liste des points qui constituent la polyligne
     */
    public OpenPolyLine(List<Point> points) {
        super(points);
    }

    /**
     * la méthode renvoie toujours faux car une polyligne ouverte est par
     * définition non-fermée
     */
    public boolean isClosed() {
        return false;
    }
}
