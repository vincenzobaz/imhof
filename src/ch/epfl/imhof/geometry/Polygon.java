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
    private final ClosedPolyLine shell;
    private final List<ClosedPolyLine> holes;

    /**
     * Construit un polygone avec l'enveloppe et les trous donnés.
     * 
     * @param shell
     *            l'enveloppe du polygone, une polyligne fermée
     * @param holes
     *            l'ensemble des trous du polygone, une liste de polylignes
     *            fermées
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes) {
        this.shell = shell;
        this.holes = Collections.unmodifiableList(new ArrayList<>(holes));
    }

    /**
     * Construit un polygone avec l'enveloppe donnée, sans trous.
     * 
     * @param shell
     *            l'enveloppe du polygone, une polyligne fermée
     */
    public Polygon(ClosedPolyLine shell) {
        this.shell = shell;
        this.holes = Collections.emptyList();
    }

    /**
     * Retourne l'enveloppe du polygone.
     * 
     * @return la polyligne constituant l'enveloppe du polygone
     */
    public ClosedPolyLine shell() {
        return shell;
    }

    /**
     * Retourne la liste des trous du polygone.
     * 
     * @return la liste de polylignes constituant les trous du polygone
     */
    public List<ClosedPolyLine> holes() {
        return holes;
    }
}
