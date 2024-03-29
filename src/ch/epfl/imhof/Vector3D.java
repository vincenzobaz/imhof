package ch.epfl.imhof;

/**
 * Classe représentant un vecteur tridimensionnel. Elle est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class Vector3D {
    private final double x;
    private final double y;
    private final double z;

    /**
     * Construit un vecteur tridimensionnel à partir des trois composantes
     * données.
     * 
     * @param x
     *            la première composante du vecteur
     * @param y
     *            la deuxième composante du vecteur
     * @param z
     *            la troisième composante du vecteur
     */
    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Retourne la composante x du vecteur.
     * 
     * @return la valeur de la composante x du vecteur
     */
    public double x() {
        return x;
    }

    /**
     * Retourne la composante y du vecteur.
     * 
     * @return la valeur de la composante y du vecteur
     */
    public double y() {
        return y;
    }

    /**
     * Retourne la composante z du vecteur.
     * 
     * @return la valeur de la composante z du vecteur
     */
    public double z() {
        return z;
    }

    /**
     * Calcule et retourne la norme du vecteur.
     * 
     * @return la norme du vecteur
     */
    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Retourne la version normalisée du vecteur.
     * 
     * @return un vecteur de même direction et de même sens que
     *         <code>this</code>, mais de longueur unitaire
     */
    public Vector3D normalized() {
        double norm = norm();
        return new Vector3D(x / norm, y / norm, z / norm);
    }

    /**
     * Calcule et retourne le produit scalaire entre <code>this</code> et le
     * vecteur donné.
     * 
     * @param that
     *            le vecteur avec lequel effectuer le produit scalaire
     * @return le produit scalaire des deux vecteurs
     */
    public double scalarProduct(Vector3D that) {
        return x * that.x + y * that.y + z * that.z;
    }
}
