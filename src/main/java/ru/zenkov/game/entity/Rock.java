package ru.zenkov.game.entity;

import ru.zenkov.IO.Input;
import ru.zenkov.phisics.Vector2D;
import ru.zenkov.phisics.rayCasting.ReflectingLine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class Rock extends Entity {

    List<Vector2D> dirs;

    protected Rock(int x, int y, double mass, int width, int height, double speed, Vector2D resultantForce) {
        super(x, y, mass, width, height, speed, resultantForce);
        type = EntityType.ROCK;
        dirs = new ArrayList<>();
        int sidesCount = ThreadLocalRandom.current().nextInt(4, 9);
        for (int i = 45; i <= 405; i += 360 / sidesCount) {
            double dirX = Math.cos(Math.toRadians(i));
            double dirY = Math.sin(Math.toRadians(i));
            dirs.add(Vector2D.newVector(dirX, dirY));
        }
    }

    @Override
    public void update(Input input, Point mousePosition) {
        move();
    }

    @Override
    public void render(Graphics2D g) {
        //g.drawRect(x - width / 2, y - height / 2, width, height);
    }

    @Override
    public List<ReflectingLine> getReflectingLines() {

        List<ReflectingLine> reflectingLines = new ArrayList<>();
        List<Vector2D> points = new ArrayList<>();
        for (Vector2D dir : dirs) {
            points.add(Vector2D.newVector(x + dir.getX() * width / 2, y + dir.getY() * height / 2));
        }

        for (int i = 1; i <= points.size(); i++) {
            reflectingLines.add(ReflectingLine.getReflectingLine(
                    points.get((i - 1) % points.size()).getX(),
                    points.get((i - 1) % points.size()).getY(),
                    points.get(i % points.size()).getX(),
                    points.get(i % points.size()).getY(),
                    this));
        }

        return reflectingLines;
    }
}
