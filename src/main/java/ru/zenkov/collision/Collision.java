package ru.zenkov.collision;

import ru.zenkov.game.entity.Entity;
import ru.zenkov.phisics.Vector2D;

public class Collision {

    public static boolean areIntersectedCircle(Entity go1, Entity go2) {
        return (go1.getWidth() + go2.getWidth()) / 2f
                >= Vector2D.getAbs(go1.getX() - go2.getX(), go1.getY() - go2.getY());
    }

    public static boolean areIntersectedRect(Entity go1, Entity go2) {
        double xIntersection = getIntersectionLength(go1.getLeft(), go1.getRight(), go2.getLeft(), go2.getRight());
        double yIntersection = getIntersectionLength(go1.getTop(), go1.getBottom(), go2.getTop(), go2.getBottom());

        return xIntersection >= 0 && yIntersection >= 0;
    }

    public static boolean areIntersectedRect(double left, double right, double top, double bottom, Entity go) {
        double xIntersection = getIntersectionLength(go.getLeft(), go.getRight(), left, right);
        double yIntersection = getIntersectionLength(go.getTop(), go.getBottom(), top, bottom);

        return xIntersection >= 0 && yIntersection >= 0;
    }

    public static boolean areIntersectedRect(double left1, double right1, double top1, double bottom1,
                                             double left2, double right2, double top2, double bottom2) {
        double xIntersection = getIntersectionLength(left2, right2, left1, right1);
        double yIntersection = getIntersectionLength(top2, bottom2, top1, bottom1);

        return xIntersection >= 0 && yIntersection >= 0;
    }

    private static double getIntersectionLength(double r1Left, double r1Right, double r2Left, double r2Right) {
        double left = Math.max(r1Left, r2Left);
        double right = Math.min(r1Right, r2Right);

        return right - left;
    }
}
