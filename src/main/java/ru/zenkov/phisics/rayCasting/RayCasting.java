package ru.zenkov.phisics.rayCasting;

import ru.zenkov.game.entity.Entity;
import ru.zenkov.phisics.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RayCasting {
    private final List<Ray> rays;
    private final Entity entity;
    private final int rayRange;
    private List<ReflectingLine> reflectingLines;


    public static RayCasting newRayCasting(Entity entity, int rayRange) {
        List<Ray> rays = new ArrayList<>();
        for (int i = 0; i <= 359; i += 2) {
            double x = Math.cos(Math.toRadians(i));
            double y = Math.sin(Math.toRadians(i));
            rays.add(new Ray(Vector2D.createVector(x, y)));
        }

        return new RayCasting(entity, rays, rayRange);
    }

    private RayCasting(Entity entity, List<Ray> rays, int rayRange) {
        this.entity = entity;
        this.rays = rays;
        this.rayRange = rayRange;

    }


    public void castRays() {

        for (Ray ray : rays) {
            Vector2D closest = null;
            ReflectingLine line = null;
            double record = Double.POSITIVE_INFINITY;
            ray.setX1(entity.getX());
            ray.setY1(entity.getY());

            for (ReflectingLine reflectingLine : reflectingLines) {
                Vector2D pt = ray.cast(entity, reflectingLine);
                if (pt != null) {
                    double d = Vector2D.getAbs(pt.getX() - ray.getX1(), pt.getY() - ray.getY1());
                    if (d < record) {
                        record = d;
                        closest = pt;
                        line = reflectingLine;
                    }
                }
            }
            if (closest != null && (Vector2D.getAbs(closest.getX() - ray.getX1(), closest.getY() - ray.getY1()) < rayRange)) {
                ray.setX2(closest.getX());
                ray.setY2(closest.getY());
                ray.setCurObj(line.getCurObj());
            } else {
                ray.setX2(entity.getX() + ray.getDirection().getX() * rayRange);
                ray.setY2(entity.getY() + ray.getDirection().getY() * rayRange);
                ray.setCurObj(null);
            }
        }
    }

    public void render(Graphics2D g) {
//        int r = ThreadLocalRandom.current().nextInt(0, 256);
//        int gr = ThreadLocalRandom.current().nextInt(0, 256);
//        int b = ThreadLocalRandom.current().nextInt(0, 256);
//
//        g.setColor(new Color(r, gr, b));
        g.setStroke(new BasicStroke(0.1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        g.setColor(Color.WHITE);
        rays.forEach(ray -> ray.render(g));
        g.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        renderReflectingLines(g);
    }

    private void renderReflectingLines(Graphics2D g) {

        double ray1Length;
        double ray2Length;
        double k = 255f / rayRange;
        for (int i = 1; i <= rays.size(); i++) {
            Ray ray1 = rays.get((i - 1) % rays.size());
            Ray ray2 = rays.get(i % rays.size());
            ray1Length = Vector2D.getAbs(ray1.getX1() - ray1.getX2(), ray1.getY1() - ray1.getY2());
            ray2Length = Vector2D.getAbs(ray2.getX1() - ray2.getX2(), ray2.getY1() - ray2.getY2());
            if (ray1Length < rayRange - 1
                    && ray2Length < rayRange - 1
                    && ray1.getCurObj() != null
                    && ray2.getCurObj() != null
                    && ray1.getCurObj() == ray2.getCurObj()
            ) {

                g.setColor(new Color(255, 255, 255, (int) ((rayRange - ray1Length) * k)));
                g.drawLine((int) ray1.getX2(), (int) ray1.getY2(), (int) ray2.getX2(), (int) ray2.getY2());
            }
        }
    }

    public void setReflectingLines(List<ReflectingLine> reflectingLines) {
        this.reflectingLines = reflectingLines;
    }

    public int getLeft() {
        return entity.getX() - rayRange;
    }

    public int getRight() {
        return entity.getX() + rayRange;
    }

    public int getTop() {
        return entity.getY() - rayRange;
    }

    public int getBottom() {
        return entity.getY() + rayRange;
    }

}
