package ru.zenkov.phisics.rayCasting;

import ru.zenkov.game.entity.Player;
import ru.zenkov.game.entity.Wall;
import ru.zenkov.phisics.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RayCasting {
    private final List<Ray> rays;
    private List<Ray> rotatedRays;
    private final Player player;


    public RayCasting(int rayCount, Player player) {
        this.player = player;
        rays = new ArrayList<>();
        for (int i = 0 ; i <= 359; i += 1) {
            double x = Math.cos(Math.toRadians(i));
            double y = Math.sin(Math.toRadians(i));
            rays.add(new Ray(Vector2D.getVector(x, y)));
        }
        rotatedRays = List.copyOf(rays);
    }

    public void castRays(List<Wall> walls, Point mousePosition) {

        double maxRange = 220;

        if (mousePosition != null)
            rotatedRays = rotate(mousePosition);

        for (Ray ray : rays) {
            Vector2D closest = null;
            double record = Double.POSITIVE_INFINITY;
            ray.setX1(player.getX());
            ray.setY1(player.getY());
            for (Wall wall : walls) {
                Vector2D pt = ray.cast(player, wall);
                if (pt != null) {
                    double d = Vector2D.getMod(pt.getX() - ray.getX1(), pt.getY() - ray.getY1());
                    if (d < record) {
                        record = d;
                        closest = pt;
                    }
                }
            }
            if (closest != null && (Vector2D.getMod(closest.getX() - ray.getX1(), closest.getY() - ray.getY1()) <= maxRange)) {
                ray.setX2(closest.getX());
                ray.setY2(closest.getY());
            } else {
                ray.setX2(player.getX() + ray.getDirection().getX() * maxRange);
                ray.setY2(player.getY() + ray.getDirection().getY() * maxRange);
            }
        }
    }


    public void render(Graphics2D g) {
//        rotatedRays.forEach(ray -> ray.render(g));
        rays.forEach(ray -> ray.render(g));
//        for (int i = 1; i < rotatedRays.size(); i++) {
//            Ray prev = rotatedRays.get(i - 1);
//            Ray cur = rotatedRays.get(i);
//            prev.render(g);
//            g.drawLine((int) prev.getX2(), (int) prev.getY2(), (int) cur.getX2(), (int) cur.getY2());
//        }

//        Polygon pol = new Polygon();
//        pol.addPoint(player.getX(), player.getY());
//        for (Ray ray : rays) {
//            pol.addPoint((int) ray.getX2(), (int) ray.getY2());
//        }
//        g.setColor(new Color(255, 255, 255, 64));
//        g.fillPolygon(pol);
    }

    private List<Ray> rotate(Point mousePosition) {
        Vector2D dir = Vector2D.getForceDirection(mousePosition.getX(), mousePosition.getY(), player.getX(), player.getX());
        double rotationRequired = Math.atan2(dir.getY(), dir.getX()) + (Math.PI / 2);
        return rays.stream().map(ray -> new Ray(Vector2D.rotate(ray.getDirection(), normalize360(rotationRequired)))).collect(Collectors.toList());
    }

    public static double normalize360(double angle) {
        return (angle > 0 ? angle : (2 * Math.PI + angle)) * 360 / (2 * Math.PI);
    }

    public List<Ray> getRays() {
        return rays;
    }
}
