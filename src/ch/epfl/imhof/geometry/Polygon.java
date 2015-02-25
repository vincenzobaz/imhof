package ch.epfl.imhof.geometry;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe définissant un polygone à trous. La classe est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class Polygon {
    private ClosedPolyLine shell;
    private List<ClosedPolyLine> holes;

    /**
     * Construit un polygone avec trous
     * 
     * @param shell
     *            l'enveloppe du polygone, sous forme de polyligne fermée
     * @param holes
     *            l'ensemble des trous du polygone, sous forme de liste de
     *            polylignes fermées
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes) {
        ArrayList<ClosedPolyLine> temp = new ArrayList<>();
        temp.addAll(0, holes);
        // for (int i = 0; i < holes.size(); i++) {
        // temp.add(holes.get(i));
        // }
        this.shell = shell;
        this.holes = Collections.unmodifiableList(temp);
    }

    /**
     * Construit un polygone sans trous
     * 
     * @param shell
     *            l'enveloppe du polygone, sous forme de polyligne fermée
     */
    public Polygon(ClosedPolyLine shell) {
        this.shell = shell;
        this.holes = Collections.emptyList();
    }

    /**
     * Accesseur de l'enveloppe du polygone
     * 
     * @return l'attribut shell du Polygon
     */
    public PolyLine shell() {
        return shell;
    }

    /**
     * Accesseur de la liste des trous du polygone
     * 
     * @return l'attribut holes du Polygon
     */
    public List<ClosedPolyLine> holes() {
        return holes;
    }
}
