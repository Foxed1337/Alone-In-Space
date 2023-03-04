package ru.zenkov.game.entity;

import ru.zenkov.IO.Input;
import ru.zenkov.phisics.Vector2D;
import ru.zenkov.phisics.rayCasting.ReflectingLine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Rock extends Entity {

    public Rock(int x, int y, double mass, int width, int height, double speed, Vector2D resultantForce) {
        super(x, y, mass, width, height, speed, resultantForce);
        type = EntityType.ROCK;
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
        reflectingLines.add(ReflectingLine.getReflectingLine(getLeft(), getTop(), getRight(), getTop()));
        reflectingLines.add(ReflectingLine.getReflectingLine(getRight(), getTop(), getRight(), getBottom()));
        reflectingLines.add(ReflectingLine.getReflectingLine(getLeft(), getBottom(), getRight(), getBottom()));
        reflectingLines.add(ReflectingLine.getReflectingLine(getLeft(), getTop(), getLeft(), getBottom()));
        return reflectingLines;
    }
}
