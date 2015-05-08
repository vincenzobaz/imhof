package ch.epfl.imhof.painting;

/**
 * Classe représentant une couleur, définie par ses trois composantes rouge,
 * verte et bleue. Elle est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class Color {
    // Les couleurs rouge, vert et bleu purs
    public static final Color RED = Color.rgb(1d, 0d, 0d);
    public static final Color GREEN = Color.rgb(0d, 1d, 0d);
    public static final Color BLUE = Color.rgb(0d, 0d, 1d);
    // Les couleurs blanc et noir
    public static final Color WHITE = Color.rgb(1d, 1d, 1d);
    public static final Color BLACK = Color.rgb(0d, 0d, 0d);

    private final double redRatio;
    private final double greenRatio;
    private final double blueRatio;

    /**
     * Construit une nouvelle couleur. Ce constructeur est privé, la
     * construction d'une nouvelle couleur se fait uniquement à partir des
     * méthodes statiques de construction fournies.
     * 
     * @param redRatio
     *            la composante rouge de la couleur
     * @param greenRatio
     *            la composante verte de la couleur
     * @param blueRatio
     *            la composante bleue de la couleur
     */
    private Color(double redRatio, double greenRatio, double blueRatio)
            throws IllegalArgumentException {
        if (redRatio < 0d || redRatio > 1d) {
            throw new IllegalArgumentException(
                    "La composante rouge est invalide.");
        }
        if (greenRatio < 0d || greenRatio > 1d) {
            throw new IllegalArgumentException(
                    "La composante verte est invalide.");
        }
        if (blueRatio < 0d || blueRatio > 1d) {
            throw new IllegalArgumentException(
                    "La composante bleue est invalide.");
        }

        this.redRatio = redRatio;
        this.greenRatio = greenRatio;
        this.blueRatio = blueRatio;
    }

    /**
     * Construit et retourne la couleur grise dont les trois composantes sont
     * égales à la valeur donnée.
     * 
     * @param ratio
     *            le niveau de gris recherché
     * @return la couleur grise voulue
     * @throws IllegalArgumentException
     *             lève une exception si la valeur des composantes est hors des
     *             valeurs acceptées par le constructeur (intervalle 0 à 1)
     */
    public static Color gray(double ratio) throws IllegalArgumentException {
        return new Color(ratio, ratio, ratio);
    }

    /**
     * Construit et retourne la couleur correspondant aux valeurs données.
     * 
     * @param redRatio
     *            le taux de rouge de la couleur
     * @param greenRatio
     *            le taux de vert de la couleur
     * @param blueRatio
     *            le taux de bleu de la couleur
     * @return la couleur voulue
     * @throws IllegalArgumentException
     *             lève une exception si une des composantes n'est pas valide,
     *             c'est-à-dire si sa valeur n'est pas comprise entre 0 et 1
     */
    public static Color rgb(double redRatio, double greenRatio, double blueRatio)
            throws IllegalArgumentException {
        return new Color(redRatio, greenRatio, blueRatio);
    }

    /**
     * Construit et retourne la couleur correspondant aux valeurs empaquetées
     * dans l'entier donné.
     * 
     * @param binaryContainer
     *            l'entier contenant les taux des composantes
     * @return la couleur voulue
     * @throws IllegalArgumentException
     *             lève une exception si une des composantes n'est pas valide,
     *             c'est-à-dire si sa valeur n'est pas comprise entre 0 et 1
     */
    public static Color rgb(int binaryContainer)
            throws IllegalArgumentException {
        int mask = 0xFF;
        double blueRatio = (mask & binaryContainer) / 255d;
        double greenRatio = (mask & (binaryContainer >>> 8)) / 255d;
        double redRatio = (mask & (binaryContainer >>> 16)) / 255d;
        return new Color(redRatio, greenRatio, blueRatio);
    }

    /**
     * Retourne la composante rouge de la couleur.
     * 
     * @return le pourcentage de rouge de la couleur
     */
    public double redRatio() {
        return redRatio;
    }

    /**
     * Retourne la composante verte de la couleur.
     * 
     * @return le pourcentage de vert de la couleur
     */
    public double greenRatio() {
        return greenRatio;
    }

    /**
     * Retourne la composante bleue de la couleur.
     * 
     * @return le pourcentage de bleu de la couleur
     */
    public double blueRatio() {
        return blueRatio;
    }

    /**
     * Retourne le produit de l'instance de couleur en cours et de la couleur
     * passée en argument.
     * 
     * @param that
     *            la couleur à multiplier avec l'instance en cours
     * @return le produit, composante par composante, des deux couleurs
     */
    public Color multiplyWith(Color that) {
        return new Color(redRatio * that.redRatio,
                greenRatio * that.greenRatio, blueRatio * that.blueRatio);
    }

    /**
     * Convertit et retourne la couleur en couleur de l'API Java.
     * 
     * @return l'équivalent de la couleur en <code>java.awt.Color</code>
     */
    public java.awt.Color convert() {
        return new java.awt.Color((float) redRatio, (float) greenRatio,
                (float) blueRatio);
    }
}
