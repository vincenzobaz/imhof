package ch.epfl.imhof.painting;

/**
 * Classe regroupant tous les paramètres de style utiles au dessin d'une ligne.
 * Elle est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class LineStyle {
    private final float width;
    private final Color color;
    private final LineCap cap;
    private final LineJoin join;
    private final float[] dashingPattern;

    /**
     * Construit un style de ligne à partir des cinq paramètres de style donnés.
     * 
     * @param width
     *            la largeur du trait
     * @param color
     *            la couleur du trait
     * @param cap
     *            le type de terminaison des lignes
     * @param join
     *            le type de jointure des segments
     * @param dashingPattern
     *            l'alternance des sections opaques et transparentes pour le
     *            dessin en traitillés
     * @throws IllegalArgumentException
     *             lève une exception si la largeur du trait est négative ou si
     *             l'un des éléments de la séquence d'alternance des segments
     *             est négatif ou nul
     */
    public LineStyle(float width, Color color, LineCap cap, LineJoin join,
            float... dashingPattern) throws IllegalArgumentException {
        if (width < 0) {
            throw new IllegalArgumentException(
                    "La largeur de ligne doit être positive.");
        }

        this.width = width;
        this.color = color;
        this.cap = cap;
        this.join = join;

        // Test de validité des éléments du tableau passé en argument et copie
        // de celui-ci
        if (dashingPattern == null) {
            this.dashingPattern = dashingPattern;
        } else {
            float[] temp = new float[dashingPattern.length];
            for (int i = 0; i < dashingPattern.length; i++) {
                if (dashingPattern[i] <= 0) {
                    throw new IllegalArgumentException(
                            "La longueur d'un segment de ligne doit être strictement positive.");
                } else {
                    temp[i] = dashingPattern[i];
                }
            }
            this.dashingPattern = temp;
        }
    }

    /**
     * Constructeur secondaire prenant seulement en arguments la largeur et la
     * couleur du trait, et utilisant des valeurs par défaut pour les autres
     * paramètres. Les valeurs par défaut sont: butt pour la terminaison des
     * lignes, miter pour la jointure des segments, et un trait continu.
     * 
     * @param width
     *            la largeur du trait
     * @param color
     *            la couleur du trait
     */
    public LineStyle(float width, Color color) throws IllegalArgumentException {
        this(width, color, LineCap.BUTT, LineJoin.MITER, new float[0]);
    }

    /**
     * Retourne la largeur du trait.
     * 
     * @return la largeur
     */
    public float width() {
        return width;
    }

    /**
     * Retourne la couleur du trait.
     * 
     * @return la couleur, sous forme de
     *         <code>ch.epfl.imhof.painting.Color</code>
     */
    public Color color() {
        return color;
    }

    /**
     * Retourne le type de terminaison de la ligne.
     * 
     * @return la terminaison
     */
    public LineCap cap() {
        return cap;
    }

    /**
     * Retourne le type de jointure des segments.
     * 
     * @return le type de jointure
     */
    public LineJoin join() {
        return join;
    }

    /**
     * Retourne la séquence d'alternance des sections opaques et transparentes
     * pour le dessin en traitillés.
     * 
     * @return l'alternance des segments, un tableau de <code>float</code>.
     */
    public float[] dashingPattern() {
        // La classe étant immuable et les tableaux statiques java ne pouvant
        // pas être rendus immuables, on retourne une copie défensive du
        // tableau.
        return dashingPattern == null ? null : dashingPattern.clone();
    }

    /**
     * Retourne un nouveau style de ligne différant de <code>this</code> par la
     * largeur du trait.
     * 
     * @param width
     *            la nouvelle largeur du trait
     * @return un nouveau <code>LineStyle</code>
     */
    public LineStyle withWidth(float width) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    /**
     * Retourne un nouveau style de ligne différant de <code>this</code> par la
     * couleur du trait.
     * 
     * @param color
     *            la nouvelle couleur du trait
     * @return un nouveau <code>LineStyle</code>
     */
    public LineStyle withColor(Color color) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    /**
     * Retourne un nouveau style de ligne différant de <code>this</code> par le
     * type de terminaison de ligne.
     * 
     * @param cap
     *            le nouveau type de terminaison de ligne
     * @return un nouveau <code>LineStyle</code>
     */
    public LineStyle withCap(LineCap cap) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    /**
     * Retourne un nouveau style de ligne différant de <code>this</code> par le
     * type de jointure de segments.
     * 
     * @param join
     *            le nouveau type de jointure de segments
     * @return un nouveau <code>LineStyle</code>
     */
    public LineStyle withJoin(LineJoin join) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    /**
     * Retourne un nouveau style de ligne différant de <code>this</code> par
     * l'alternance des segments opaques et transparents.
     * 
     * @param dashingPattern
     *            le nouveau schéma d'alternance de segments opaques et
     *            transparents
     * @return un nouveau <code>LineStyle</code>
     */
    public LineStyle withDashingPattern(float... dashingPattern) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    /**
     * Énumération des types de terminaison d'une ligne.
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static enum LineCap {
        BUTT, ROUND, SQUARE
    }

    /**
     * Énumération des types de jointure de segments de ligne.
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static enum LineJoin {
        BEVEL, MITER, ROUND
    }
}
