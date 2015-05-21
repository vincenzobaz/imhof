package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;

/**
 * Interface représentant un modèle numérique de terrain (MNT, ou DEM en
 * anglais). Elle étend {@link java.lang.AutoCloseable AutoCloseable}.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public interface DigitalElevationModel extends AutoCloseable {
    /**
     * Retourne le vecteur normal à la Terre au point passé en argument.
     * 
     * @param point
     *            le point où l'on recherche le vecteur normal
     * @return le vecteur normal
     * @throws IllegalArgumentException
     *             lève une exception si le point pour lequel la normale est
     *             demandée ne fait pas partie de la zone couverte par le MNT
     */
    Vector3D normalAt(PointGeo point) throws IllegalArgumentException;
}
