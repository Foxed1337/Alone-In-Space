package ru.zenkov.phisics.rayCasting;

import ru.zenkov.phisics.Vector2D;

public class ReflectingLine {
    private final Vector2D pt1;
    private final Vector2D pt2;

    private final Object curObj;

    private ReflectingLine(Vector2D pt1, Vector2D pt2, Object curObj) {
        this.pt1 = pt1;
        this.pt2 = pt2;
        this.curObj = curObj;
    }

    public static ReflectingLine getReflectingLine(double x1, double y1, double x2, double y2, Object curObj) {
        return new ReflectingLine(Vector2D.newVector(x1, y1), Vector2D.newVector(x2, y2), curObj);
    }

    public Vector2D getPt1() {
        return pt1;
    }

    public Vector2D getPt2() {
        return pt2;
    }

    public Object getCurObj() {
        return curObj;
    }
}
