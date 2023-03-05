package ru.zenkov.game.entity;

import ru.zenkov.IO.Input;
import ru.zenkov.phisics.Vector2D;
import ru.zenkov.phisics.rayCasting.ReflectingLine;
import ru.zenkov.utils.ResourceLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

public class Player extends Entity {

    private final BufferedImage image;
    protected Vector2D movingDirection;

    protected Player(int x, int y, double mass, int width, int height, double speed, Vector2D resultantForce) {
        super(x, y, mass, width, height, speed, resultantForce);

        image = ResourceLoader.loadImage("test-ship.png");
        movingDirection = Vector2D.getVector(1, 1);
        type = EntityType.PLAYER;
    }

    @Override
    public void update(Input input, Point mousePosition) {

        if (mousePosition != null) {
            movingDirection = Vector2D.getForceDirection(mousePosition.getX(), mousePosition.getY(), x, y);
            if (input.getKey(KeyEvent.VK_W)) {
                double currentSpeed = Vector2D.getMod(acceleration);
                currentSpeed = speed - currentSpeed;
                Vector2D movingForce = Vector2D.multiplyConst(movingDirection, currentSpeed);
                resultantForce = Vector2D.add(resultantForce, movingForce);
            }
        }

        move();
    }

    @Override
    public void render(Graphics2D g) {

//        double rotationRequired = Math.atan2(movingDirection.getY(), movingDirection.getX()) + (Math.PI / 2);
//        AffineTransform tx = AffineTransform.getTranslateInstance(x - width / 2f, y - height / 2f);
//        tx.rotate(rotationRequired, width / 2f, height / 2f);
//        g.drawImage(image, tx, null);
//        g.setColor(Color.RED);
//        g.drawRect(x - width / 2, y - height / 2, width, height);
//        g.drawLine(x, y, (int) (x + resultantForce.getX() * 10), (int) (y + resultantForce.getY() * 10));
//        g.setColor(Color.WHITE);
    }

    @Override
    public List<ReflectingLine> getReflectingLines() {
        return null;
    }
}
