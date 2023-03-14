package ru.zenkov.phisics;

import ru.zenkov.game.entity.Entity;

public class Interacting {
    public static void interact(Entity go1, Entity go2) {

        Vector2D dirActForceObj1 = Vector2D.getUnitVector(Vector2D.getForceDirection(go1.getX(), go1.getY(), go2.getX(), go2.getY()));
        Vector2D dirActForceObj2 = dirActForceObj1.invert();

        solveCollision(go1, go2, dirActForceObj1, dirActForceObj2);

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

    private static void solveCollision(Entity go1, Entity go2, Vector2D dirActForceObj1, Vector2D dirActForceObj2) {
        double dist = ((go1.getWidth() + go2.getWidth()) / 2f - Vector2D.getAbs(go1.getX() - go2.getX(), go1.getY() - go2.getY()));

        Vector2D dist1 = Vector2D.multiplyConst(dirActForceObj1, dist);
        Vector2D dist2 = Vector2D.multiplyConst(dirActForceObj2, dist);

        go1.setX((int) (go1.getX() + dist1.getX()));
        go1.setY((int) (go1.getY() + dist1.getY()));
        go2.setX((int) (go2.getX() + dist2.getX()));
        go2.setY((int) (go2.getY() + dist2.getY()));
    }
}
