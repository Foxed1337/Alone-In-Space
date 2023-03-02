package ru.zenkov.phisics;


public class Vector2D {

    private static final double FRACTION_LENGTH = 1e3;

    private final double x;
    private final double y;

    private Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2D getVector(double x, double y) {
        return new Vector2D(x, y);
    }

    public static Vector2D getForceDirection(double startX, double startY, double endX, double endY) {
        double actingForceX = (startX - endX);
        double actingForceY = (startY - endY);
        return Vector2D.getUnitVector(actingForceX, actingForceY);
    }

    public static Vector2D getReflection(Vector2D normal, Vector2D fall) {
        return add(fall, multiplyConst(multiplyConst(normal, getProjection(fall, normal)), -2));
    }

    public static Vector2D add(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    public static Vector2D multiplyConst(Vector2D v, double c) {
        return getVector(v.getX() * c, v.getY() * c);
    }

    public static Vector2D divideConst(Vector2D v, double c) {
        return getVector(division(v.getX(), c), division(v.getY(), c));
    }

    public static Vector2D rotate(Vector2D v, double angle) {
        angle = Math.toRadians(angle);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double newX = (v.getX() * cos - v.getY() * sin);
        double newY = (v.getX() * sin + v.getY() * cos);
        return getVector(newX, newY);
    }

    public static double getProjection(Vector2D v1, Vector2D v2) {
        return getMod(v1) * Math.cos(getAngleBetween(v1, v2));
    }

    public static double getAngleBetween(Vector2D v1, Vector2D v2) {
        return Math.acos(division(getScalar(v1, v2), (getMod(v1) * getMod(v2))));
    }

    public static double getScalar(Vector2D v1, Vector2D v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY();
    }

    public static Vector2D getUnitVector(Vector2D v) {
        return divideConst(v, getMod(v));
    }

    public static Vector2D getUnitVector(double x, double y) {
        double mod = getMod(x, y);
        return divideConst(getVector(x, y), mod);
    }

    private static double division(double a, double b) {
        return b == 0 ? 0 : a / b;
    }

    public static double roundDown(double d) {
        return Math.floor(d * FRACTION_LENGTH) / FRACTION_LENGTH;
    }

    public static double getMod(Vector2D v) {
        return Math.sqrt(v.getX() * v.getX() + v.getY() * v.getY());
    }

    public static double getMod(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D invert() {
        return Vector2D.getVector(-this.getX(), -this.getY());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}