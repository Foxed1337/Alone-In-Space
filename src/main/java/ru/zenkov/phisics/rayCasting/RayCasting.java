package ru.zenkov.phisics.rayCasting;

import ru.zenkov.game.entity.Entity;
import ru.zenkov.phisics.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RayCasting {
    private final List<Ray> rays;
    private final Entity entity;
    private final int maxRayRange = 300;


    public RayCasting(Entity entity) {
        this.entity = entity;
        rays = new ArrayList<>();
        for (int i = 0; i <= 359; i += 2) {
            double x = Math.cos(Math.toRadians(i));
            double y = Math.sin(Math.toRadians(i));
            rays.add(new Ray(Vector2D.getVector(x, y)));
        }
    }

    public void castRays(List<ReflectingLine> reflectingLines, Point mousePosition) {

        for (Ray ray : rays) {
            Vector2D closest = null;
            ReflectingLine line = null;
            double record = Double.POSITIVE_INFINITY;
            ray.setX1(entity.getX());
            ray.setY1(entity.getY());
            for (ReflectingLine reflectingLine : reflectingLines) {
                Vector2D pt = ray.cast(entity, reflectingLine);
                if (pt != null) {
                    double d = Vector2D.getMod(pt.getX() - ray.getX1(), pt.getY() - ray.getY1());
                    if (d < record) {
                        record = d;
                        closest = pt;
                        line = reflectingLine;
                    }
                }
            }
            if (closest != null && (Vector2D.getMod(closest.getX() - ray.getX1(), closest.getY() - ray.getY1()) < maxRayRange)) {
                ray.setX2(closest.getX());
                ray.setY2(closest.getY());
                ray.setCurObj(line.getCurObj());
            } else {
                ray.setX2(entity.getX() + ray.getDirection().getX() * maxRayRange);
                ray.setY2(entity.getY() + ray.getDirection().getY() * maxRayRange);
                ray.setCurObj(null);
            }
        }
    }


    public void render(Graphics2D g) {
        rays.forEach(ray -> ray.render(g));
        g.setStroke(new BasicStroke(1));

        for (int i = 1; i <= rays.size(); i++) {
            Ray ray1 = rays.get((i - 1) % rays.size());
            Ray ray2 = rays.get(i % rays.size());
            if ((Vector2D.getMod(ray1.getX1() - ray1.getX2(), ray1.getY1() - ray1.getY2()) < maxRayRange - 1)
                    && (Vector2D.getMod(ray2.getX1() - ray2.getX2(), ray2.getY1() - ray2.getY2()) < maxRayRange - 1)
                    && ray1.getCurObj() != null
                    && ray2.getCurObj() != null
                    && ray1.getCurObj() == ray2.getCurObj()
            ) {
                g.drawLine((int) ray1.getX2(), (int) ray1.getY2(), (int) ray2.getX2(), (int) ray2.getY2());
            }
        }
    }

    public List<Ray> getRays() {
        return rays;
    }
}
