package ch.epfl.imhof;

/**
 * Classe répresentant un vecteur tridimensonnel.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class Vector3D {
    private double x;
    private double y;
    private double z;

    /**
     * Construit un vecteur tridimensionnel à partir des ses trois composantes
     * x,y,z
     * 
     * @param x
     * @param y
     * @param z
     */
    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Calcule la norme du vecteur
     * 
     * @return la norme du vecteur
     */
    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Permet d'obtenir la version normalisée du vecteur
     * 
     * @return un vecteur parallèle à <code> this </code> mais de longueur
     *         unitaire
     */
    public Vector3D normalized() {
        double norm = norm();
        return new Vector3D(x / norm, y / norm, z / norm);
    }

    /**
     * Calcule le produit scalaire entre <code> this </code> et
     * <code>that</code>
     * 
     * @param that
     *            le deuxième terme pour le calcul du produit scalaire
     * @return le produit scalaire entre les deux vecteurs.
     */
    public double scalarProduct(Vector3D that) {
        return x * that.x + y * that.y + z * that.z;
    }
}
