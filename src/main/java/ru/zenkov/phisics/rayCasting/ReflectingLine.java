package ru.zenkov.phisics.rayCasting;

import ru.zenkov.phisics.Vector2D;

public class ReflectingLine {
    private final Vector2D pt1;
    private final Vector2D pt2;

    private ReflectingLine(Vector2D pt1, Vector2D pt2) {
        this.pt1 = pt1;
        this.pt2 = pt2;
    }

    public static ReflectingLine getReflectingLine(double x1, double y1, double x2, double y2) {
        return new ReflectingLine(Vector2D.getVector(x1, y1), Vector2D.getVector(x2, y2));
    }

    public Vector2D getPt1() {
        return pt1;
    }

    public Vector2D getPt2() {
        return pt2;
    }
}
