package ch.epfl.imhof.painting;

import java.util.Arrays;

/**
 * Classe regroupant tous les paramètres de style utiles au dessin d'une ligne
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
    private float[] dashingPattern;

    /**
     * Construit un style de ligne à partir des cinq paramètres de style
     * 
     * @param width
     *            largeur
     * @param color
     *            couleur
     * @param cap
     *            type de terminaison
     * @param join
     *            type de jointure
     * @param dashingPattern
     *            alternance des sections opaques et transparentes pour le
     *            dessin en traitillés
     * @throws IllegalArgumentException
     *             lancée si la largeur du trait est négative ou si l'un des
     *             éléments de la séquence d'alternance des segments est négatif
     *             ou nul
     */
    public LineStyle(float width, Color color, LineCap cap, LineJoin join,
            float[] dashingPattern) throws IllegalArgumentException {
        if (width < 0) {
            throw new IllegalArgumentException("negative line width");
        }
        
        this.width = width;
        this.color = color;
        this.cap = cap;
        this.join = join;
        this.dashingPattern = new float[dashingPattern.length];
        for (int i = 0; i < dashingPattern.length; i++) {
            if (dashingPattern[i] <= 0) {
                throw new IllegalArgumentException("negative line length");
            } else {
                this.dashingPattern[i] = dashingPattern[i];
            }
        }
    }

    /**
     * Constructeur secondaire prenant seulement deux arguments et qui appelle
     * le constructeur principal en lui fournissant des valeurs par défaut
     * 
     * @param width
     *            largeur
     * @param color
     *            couleur
     */
    public LineStyle(float width, Color color) {
        this(width, color, LineCap.BUTT, LineJoin.MITER, new float[0]);
    }

    /**
     * 
     * @return la largeur de la ligne
     */
    public float getWidth() {
        return width;
    }

    /**
     * 
     * @return la couleur de la ligne, un objet de type
     *         <code> ch.epfl.imhof.painting.Color </code>
     */
    public Color getColor() {
        return color;
    }

    /**
     * 
     * @return le type de terminaison de la ligne.
     */
    public LineCap getCap() {
        return cap;
    }

    /**
     * 
     * @return le type de jointure de la ligne
     */
    public LineJoin getJoin() {
        return join;
    }

    /**
     * 
     * @return l'alternance des segments opaques et transparents, un tableau de
     *         <code> int </code>.
     * */
    public float[] getDashingPattern() {
        // la classe étant immuable et les tableaux statiques java ne pouvant
        // pas être rendus imuables, on retourne une copie défensive du tableau.
        /*
         * float[] defensiveCopy = new float[dashingPattern.length];
         * System.arraycopy(dashingPattern, 0, defensiveCopy, 0,
         * dashingPattern.length); return defensiveCopy;
         */
        return Arrays.copyOf(dashingPattern, dashingPattern.length);
    }

    /**
     * Permet d'obtenir un nouveau style de ligne à partir de
     * <code> this </code> différant seulement pour la largeur du trait.
     * 
     * @param width
     *            largeur de la ligne
     * @return un nouveau <code> LineStyle </code>
     */
    public LineStyle withWidth(float width) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    /**
     * Permet d'obtenur un nouveau style de ligne à partir de <code> this</code>
     * différant du premier seulement pour la couleur du trait
     * 
     * @param color
     *            la couleur du trait
     * @return un nouveau <code> LineStyle </code>
     */
    public LineStyle withColor(Color color) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    /**
     * Permet d'obtenur un nouveau style de ligne à partir de <code> this</code>
     * différant du premier seulement pour le type de terminaison de ligne.
     * 
     * @param cap
     *            le type de terminaison de ligne
     * @return un nouveau <code> LineStyle </code>
     */
    public LineStyle withCap(LineCap cap) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    /**
     * Permet d'obtenur un nouveau style de ligne à partir de <code> this</code>
     * différant du premier seulement pour le type de jointure de segment
     * 
     * @param join
     *            le type de jointure du segment
     * @return un nouveau <code> LineStyle </code>
     */
    public LineStyle withJoin(LineJoin join) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    /**
     * Permet d'obtenur un nouveau style de ligne à partir de <code> this</code>
     * différant du premier seulement pour l'alternance des segments opaques et
     * transparents
     * 
     * @param dashingPattern
     *            l'alternance de segments opaques et transparents
     * @return un nouveau <code> LineStyle </code>
     */
    public LineStyle withDashingPattern(float[] dashingPattern) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    /**
     * Énumération des type de terminaison de la ligne.
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static enum LineCap {
        BUTT, ROUND, SQUARE
    }

    /**
     * Énumération des type de jointure de la ligne.
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static enum LineJoin {
        BEVEL, MITER, ROUND
    }
}
