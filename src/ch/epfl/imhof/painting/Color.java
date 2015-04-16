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
    // Les couleurs rouge, vert et bleu pures
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

    // Est-ce qu'on peut faire du blanc/noir??
    public static Color gray(double ratio) throws IllegalArgumentException {
        if (ratio <= 0.0 || ratio >= 1.0) {
            throw new IllegalArgumentException("La composante n'est pas valide");
        }

        return new Color(ratio, ratio, ratio);
    }

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

    public static Color rgb(int binaryContainer)
            throws IllegalArgumentException {

    }

    /**
     * Retourne la composante rouge de la couleur
     * 
     * @return
     */
    public double redRatio() {
        return redRatio;
    }

    /**
     * Retourne la composante verte de la couleur
     * 
     * @return
     */
    public double greenRatio() {
        return greenRatio;
    }

    /**
     * Retourne la composante bleue de la couleur
     * 
     * @return
     */
    public double blueRatio() {
        return blueRatio;
    }

    // on multiplie deux couleurs ou this avec une autre couleur?
    /**
     * 
     * @param c
     * @return
     */
    public Color multiplyWith(Color c) {
        return new Color(redRatio * c.redRatio(), greenRatio * c.greenRatio(),
                blueRatio * c.blueRatio());
    }

    // idem que pour la multiplication
    /**
     * Convertit et retourne la couleur en <code>Color</code>
     * 
     * @return
     */
    public java.awt.Color convert() {
        return new java.awt.Color((float) redRatio, (float) greenRatio,
                (float) blueRatio);
    }
}
