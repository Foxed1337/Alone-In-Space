package ru.zenkov.phisics.rayCasting;

import ru.zenkov.game.entity.Player;
import ru.zenkov.phisics.Vector2D;

import java.awt.*;

public class Ray {
    private Vector2D direction;
    private double x1;
    private double y1;
    private double x2;
    private double y2;

    public Ray(Vector2D dir) {
        this.direction = dir;
    }

    public Vector2D cast(Player player, ReflectingLine reflectingLine) {
        double x1 = reflectingLine.getPt1().getX();
        double y1 = reflectingLine.getPt1().getY();
        double x2 = reflectingLine.getPt2().getX();
        double y2 = reflectingLine.getPt2().getY();

        double x3 = player.getX();
        double y3 = player.getY();
        double x4 = player.getX() + direction.getX();
        double y4 = player.getY() + direction.getY();

        double den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (den == 0) {
            return null;
        }

        double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / den;
        double u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / den;

        if (t > 0 && t < 1 && u > 0) {
            double x = x1 + t * (x2 - x1);
            double y = y1 + t * (y2 - y1);
            return Vector2D.getVector(x, y);
        }
        return null;
    }

    public void render(Graphics2D g) {

        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    public Vector2D getDirection() {
        return direction;
    }

    public void setDirection(Vector2D direction) {
        this.direction = direction;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = (int) x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = (int) y1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = (int) x2;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = (int) y2;
    }
}
