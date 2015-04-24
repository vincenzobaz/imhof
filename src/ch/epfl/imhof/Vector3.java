package ch.epfl.imhof;

public final class Vector3 {
    private double x;
    private double y;
    private double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3 normalized() {
        double norm = norm();
        return new Vector3(x / norm, y / norm, z / norm);
    }

    public double scalarProduct(Vector3 that) {
        return x * that.x() + y * that.y() + z * that.z();
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

}
