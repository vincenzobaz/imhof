package ch.epfl.imhof.painting;

public class LineStyle {
    private final float width;
    private final Color color;
    private final LineCap cap;
    private final LineJoin join;
    private float[] dashingPattern;

    public LineStyle(float width, Color color, LineCap cap, LineJoin join,
            float[] dashingPatter) throws IllegalArgumentException {
        if (width < 0)
            throw new IllegalArgumentException("negative line width");
        this.width = width;
        this.color = color;
        this.cap = cap;
        this.join = join;
        this.dashingPattern = new float[dashingPattern.length];
        for (int i = 0; i < dashingPatter.length; i++) {
            if (dashingPattern[i] <= 0)
                throw new IllegalArgumentException("negative line length");
            else
                this.dashingPattern[i] = dashingPattern[i];
        }
    }

    public LineStyle(float width, Color color) {
        this(width, color, LineCap.BUTT, LineJoin.MITER, new float[0]);
    }

    public float getWidth() {
        return width;
    }

    public Color getColor() {
        return color;
    }

    public LineCap getCap() {
        return cap;
    }

    public LineJoin getJoin() {
        return join;
    }

    public float[] getDashingPattern() {
        float[] defensiveCopy = new float[dashingPattern.length];
        System.arraycopy(dashingPattern, 0, defensiveCopy, 0,
                dashingPattern.length);
        return defensiveCopy;
    }

    public LineStyle withWidth(float width) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    public LineStyle withColor(Color color) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    public LineStyle withCap(LineCap cap) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    public LineStyle withJoin(LineJoin join) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    public LineStyle withDashingPattern(float[] dashingPattern) {
        return new LineStyle(width, color, cap, join, dashingPattern);
    }

    public static enum LineCap {
        BUTT, ROUND, SQUARE
    }

    public static enum LineJoin {
        BEVEL, MITER, ROUND
    }
}
