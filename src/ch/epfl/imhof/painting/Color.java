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
    public final static Color RED = new Color(1.0, 0.0, 0.0);
    public final static Color GREEN = new Color(0.0, 1.0, 0.0);
    public final static Color BLUE = new Color(0.0, 0.0, 1.0);
    // Les couleurs blanc et noir
    public final static Color WHITE = new Color(1.0, 1.0, 1.0);
    public final static Color BLACK = new Color(0.0, 0.0, 0.0);

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
    private Color(double redRatio, double greenRatio, double blueRatio) {
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
     *             valeurs acceptées par le constructeur (intervalle 0 à 1) ou
     *             si elle vaut 0 ou 1 (correspond au blanc et au noir)
     */
    public static Color gray(double ratio) throws IllegalArgumentException {
        // Est-ce qu'on peut faire du blanc/noir??
        if (ratio <= 0.0 || ratio >= 1.0) {
            throw new IllegalArgumentException(
                    "La composante n'est pas valide.");
        }
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
        if (redRatio < 0 || redRatio > 1) {
            throw new IllegalArgumentException(
                    "La composante rouge est invalide.");
        }
        if (greenRatio < 0 || greenRatio > 1) {
            throw new IllegalArgumentException(
                    "La composante verte est invalide.");
        }
        if (blueRatio < 0 || blueRatio > 1) {
            throw new IllegalArgumentException(
                    "La composante bleue est invalide.");
        }

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
        int mask = 0b1111111;
        double blueRatio = (binaryContainer & mask) / 255d;
        double greenRatio = ((binaryContainer & (mask << 8)) >>> 8) / 255d;
        double redRatio = ((binaryContainer & (mask << 16)) >>> 16) / 255d;
        if (redRatio < 0 || redRatio > 1) {
            throw new IllegalArgumentException(
                    "La composante rouge est invalide.");
        }
        if (greenRatio < 0 || greenRatio > 1) {
            throw new IllegalArgumentException(
                    "La composante verte est invalide.");
        }
        if (blueRatio < 0 || blueRatio > 1) {
            throw new IllegalArgumentException(
                    "La composante bleue est invalide.");
        }
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

    // on multiplie deux couleurs ou this avec une autre couleur?
    /**
     * Retourne le produit de l'instance de couleur en cours et de la couleur
     * passée en argument.
     * 
     * @param c
     *            la couleur à multiplier avec l'instance en cours
     * @return le produit, composante par composante, des deux couleurs
     */
    public Color multiplyWith(Color c) {
        return new Color(redRatio * c.redRatio(), greenRatio * c.greenRatio(),
                blueRatio * c.blueRatio());
    }

    // idem que pour la multiplication
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
