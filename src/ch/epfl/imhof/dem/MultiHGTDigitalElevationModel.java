package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;

/**
 * Classe représentant un modèle numérique de terrain stocké dans deux fichiers
 * au format GT.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */

public class MultiHGTDigitalElevationModel implements DigitalElevationModel {
    private final HGTDigitalElevationModel firstDEM;
    private final HGTDigitalElevationModel secondDEM;

    /**
     * Construit un objet MultiHGTDigitalElevationModel à partir de deux objets
     * HGTDigitalElevationModel
     * 
     * @param firstDEM
     * @param secondDEM
     */
    public MultiHGTDigitalElevationModel(HGTDigitalElevationModel firstDEM,
            HGTDigitalElevationModel secondDEM) {
        this.firstDEM = firstDEM;
        this.secondDEM = secondDEM;
    }

    @Override
    public void close() throws Exception {
        firstDEM.close();
        secondDEM.close();
    }

    @Override
    public Vector3D normalAt(PointGeo point) throws IllegalArgumentException {
        return firstDEMContainsPoint(point) ? firstDEM.normalAt(point)
                : secondDEM.normalAt(point);
    }

    @Override
    public double latitudeSW() {
        return firstDEM.latitudeSW();
    }

    @Override
    public double longitudeSW() {
        return firstDEM.longitudeSW();
    }

    private boolean firstDEMContainsPoint(PointGeo point) {
        return (Math.toDegrees(point.latitude()) >= firstDEM.latitudeSW()
                && Math.toDegrees(point.latitude()) < firstDEM.latitudeSW() + 1
                && Math.toDegrees(point.longitude()) >= firstDEM.longitudeSW() && Math
                .toDegrees(point.longitude()) < firstDEM.longitudeSW() + 1);
    }
}