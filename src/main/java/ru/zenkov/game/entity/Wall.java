package ru.zenkov.game.entity;

import ru.zenkov.phisics.Vector2D;

public class Wall {
    private final Vector2D pt1;
    private final Vector2D pt2;

    private Wall(Vector2D pt1, Vector2D pt2) {
        this.pt1 = pt1;
        this.pt2 = pt2;
    }

    public static Wall getWall(double x1, double y1, double x2, double y2) {
        return new Wall(Vector2D.getVector(x1, y1), Vector2D.getVector(x2, y2));
    }

    public Vector2D getPt1() {
        return pt1;
    }

    public Vector2D getPt2() {
        return pt2;
    }
}
