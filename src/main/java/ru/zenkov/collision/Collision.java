package ru.zenkov.collision;

import ru.zenkov.game.entity.Entity;
import ru.zenkov.phisics.Vector2D;

public class Collision {
    public static void interact(Entity go1, Entity go2) {

        Vector2D dirActForceObj1 = Vector2D.getUnitVector(Vector2D.getForceDirection(go1.getX(), go1.getY(), go2.getX(), go2.getY()));
        Vector2D dirActForceObj2 = dirActForceObj1.invert();

        double dist = ((go1.getWidth() + go2.getWidth()) / 2f - Vector2D.getAbs(go1.getX() - go2.getX(), go1.getY() - go2.getY()));

        Vector2D dist1 = Vector2D.multiplyConst(dirActForceObj1, dist);
        Vector2D dist2 = Vector2D.multiplyConst(dirActForceObj2, dist);

        go1.setX((int) Math.round(go1.getX() + dist1.getX()));
        go1.setY((int) Math.round(go1.getY() + dist1.getY()));
        go2.setX((int) Math.round(go2.getX() + dist2.getX()));
        go2.setY((int) Math.round(go2.getY() + dist2.getY()));

        double projectionActForceObj2ToObj1 = Vector2D.getProjection(go2.getResultantForce(), dirActForceObj1);
        double projectionActForceObj1ToObj2 = Vector2D.getProjection(go1.getResultantForce(), dirActForceObj2);

        Vector2D actForceObj1 = Vector2D.multiplyConst(dirActForceObj1, projectionActForceObj2ToObj1);
        Vector2D actForceObj2 = Vector2D.multiplyConst(dirActForceObj2, projectionActForceObj1ToObj2);

        Vector2D invertResForceBeforeInteract1 = Vector2D.getUnitVector(Vector2D.getReflection(dirActForceObj1, go1.getResultantForce()));
        Vector2D invertResForceBeforeInteract2 = Vector2D.getUnitVector(Vector2D.getReflection(dirActForceObj2, go2.getResultantForce()));

        go1.setResultantForce(Vector2D.add(go1.getResultantForce(), actForceObj1));
        go1.setResultantForce(Vector2D.add(go1.getResultantForce(), actForceObj2.invert()));
        go1.setResultantForce(Vector2D.add(go1.getResultantForce(), invertResForceBeforeInteract1));

        go2.setResultantForce(Vector2D.add(go2.getResultantForce(), actForceObj2));
        go2.setResultantForce(Vector2D.add(go2.getResultantForce(), actForceObj1.invert()));
        go2.setResultantForce(Vector2D.add(go2.getResultantForce(), invertResForceBeforeInteract2));

    }

    public static boolean areIntersectedCircle(Entity go1, Entity go2) {
        double d = Vector2D.getAbs(go1.getX() - go2.getX(), go1.getY() - go2.getY());
        return (go1.getWidth() + go2.getWidth()) / 2f >= d;
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
