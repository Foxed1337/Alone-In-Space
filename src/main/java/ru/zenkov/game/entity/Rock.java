package ru.zenkov.game.entity;

import ru.zenkov.IO.Input;
import ru.zenkov.phisics.Vector2D;

import java.awt.*;

public class Rock extends GameObject {

    public Rock(int x, int y, double mass, int width, int height, double speed, Vector2D resultantForce) {

        super(x, y, mass, width, height, speed, resultantForce);
        type = GameObjectType.ROCK;
    }

    @Override
    public void update(Input input, Point mousePosition) {

        move();
    }

    @Override
    public void render(Graphics2D g) {
        g.drawRect(x - width / 2, y - height / 2, width, height);
    }
}
