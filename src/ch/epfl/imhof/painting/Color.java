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

    private Color(double redRatio, double greenRatio, double blueRatio)
            throws IllegalArgumentException {
        if (ratioIsInvalid(redRatio)) {
            throw new IllegalArgumentException(
                    "La composante rouge est invalide.");
        }
        if (ratioIsInvalid(greenRatio)) {
            throw new IllegalArgumentException(
                    "La composante verte est invalide.");
        }
        if (ratioIsInvalid(blueRatio)) {
            throw new IllegalArgumentException(
                    "La composante bleue est invalide.");
        }

        this.redRatio = redRatio;
        this.greenRatio = greenRatio;
        this.blueRatio = blueRatio;
    }

    public static Color gray(double ratio) throws IllegalArgumentException {
        try {
            return new Color(ratio, ratio, ratio);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public static Color rgb(double redRatio, double greenRatio, double blueRatio)
            throws IllegalArgumentException {
        try {
            return new Color(redRatio, greenRatio, blueRatio);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public static Color rgb(int binaryContainer)
            throws IllegalArgumentException {

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

    public Color multiplyWith(Color c) {
        return new Color(redRatio * c.redRatio(), greenRatio * c.greenRatio(),
                blueRatio * c.blueRatio());
    }

    public java.awt.Color convert() {
        return new java.awt.Color((float) redRatio, (float) greenRatio,
                (float) blueRatio);
    }

    private boolean ratioIsInvalid(double ratio) {
        return ratio < 0 || ratio > 1;
    }
}
