package ch.epfl.imhof.painting;

public final class Color {
    public final static Color RED = new Color(1, 0, 0);
    public final static Color GREEN = new Color(0, 1, 0);
    public final static Color BLUE = new Color(0, 0, 1);
    public final static Color WHITE = new Color(1, 1, 1);
    public final static Color BLACK = new Color(0, 0, 0);

    private final double redRatio;
    private final double greenRatio;
    private final double blueRatio;

    private Color(double red, double green, double blue)
            throws IllegalArgumentException {
        if (ratioIsInvalid(red) || ratioIsInvalid(green)
                || ratioIsInvalid(blue)) {
            throw new IllegalArgumentException(
                    "La valeur de la couleur doit �tre comprise entre 0 et 1.");
        }
        this.redRatio = red;
        this.greenRatio = green;
        this.blueRatio = blue;
    }

    public double redRatio() {
        return redRatio;
    }

    public double greenRatio() {
        return greenRatio;
    }

    public double blueRatio() {
        return blueRatio;
    }

    public static Color gray(double ratio) throws IllegalArgumentException {
        try {
            return new Color(ratio, ratio, ratio);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public static Color rgb(double red, double green, double blue)
            throws IllegalArgumentException {
        try {
            return new Color(red, green, blue);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public static Color rgb(int truc) throws IllegalArgumentException {

    }

    public Color multiply(Color c1, Color c2) {
        return new Color(c1.redRatio() * c2.redRatio(), c1.greenRatio()
                * c2.greenRatio(), c1.blueRatio() * c2.blueRatio());
    }

    public java.awt.Color convert(Color c) {
        return new java.awt.Color((float) c.redRatio(), (float) c.greenRatio(),
                (float) c.blueRatio());
    }

    private boolean ratioIsInvalid(double ratio) {
        return ratio < 0 || ratio > 1;
    }
}
