package ru.zenkov.game;

import ru.zenkov.game.entity.Entity;
import ru.zenkov.phisics.Vector2D;

import java.awt.*;
import java.util.List;

public class Camera {
    private final int xRelativelyScreen;
    private final int yRelativelyScreen;
    private final int width;
    private final int height;
    private final int speed;
    private final int distanceForStartMoving;
    private final Entity player;


    public static Camera newCamera(int screenWidth, int screenHeight, int distanceForStartMoving, int speed, Entity entity) {
        int xRelativelyScreen = screenWidth / 2;
        int yRelativelyScreen = screenHeight / 2;

        return new Camera(xRelativelyScreen, yRelativelyScreen, screenWidth, screenHeight, speed, distanceForStartMoving, entity);
    }

    public Camera(int xRelativelyScreen, int yRelativelyScreen, int width, int height, int speed, int distanceForStartMoving, Entity entity) {
        this.xRelativelyScreen = xRelativelyScreen;
        this.yRelativelyScreen = yRelativelyScreen;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.distanceForStartMoving = distanceForStartMoving;
        this.player = entity;
    }

    public void update(List<Entity> entities, GameMap gameMap) {

        int dx = player.getX() - xRelativelyScreen;
        int dy = player.getY() - yRelativelyScreen;

        boolean canChangeX = false;
        boolean canChangeY = false;

        double s = Vector2D.getAbs(dx, dy);
        Vector2D dir = Vector2D.getUnitVector(dx, dy);

        int dx2 = (int) (s / speed * dir.getX());
        int dy2 = (int) (s / speed * dir.getY());

        if (Math.abs(dx) >= distanceForStartMoving) {
            int newLeftBorder = pushBorder(gameMap.getLeftBorder(), dx2);
            int newRightBorder = pushBorder(gameMap.getRightBorder(), dx2);
            if (newLeftBorder <= 0 && newRightBorder >= width) {
                gameMap.setLeftBorder(newLeftBorder);
                gameMap.setRightBorder(newRightBorder);
                canChangeX = true;
            }

        }
        if (Math.abs(dy) >= distanceForStartMoving) {
            int newTopBorder = pushBorder(gameMap.getTopBorder(), dy2);
            int newBottomBorder = pushBorder(gameMap.getBottomBorder(), dy2);
            if (newTopBorder <= 0 && newBottomBorder >= height) {
                gameMap.setTopBorder(newTopBorder);
                gameMap.setBottomBorder(newBottomBorder);
                canChangeY = true;
            }
        }

        for (Entity go : entities) {
            if (canChangeX) {
                int newX = (go.getX() - dx2);
                go.setX(newX);
            }
            if (canChangeY) {
                int newY = (go.getY() - dy2);
                go.setY(newY);
            }
        }
    }

    private int pushBorder(int oldBorder, int offset) {
        return oldBorder - offset;
    }


    public void render(Graphics2D g) {
        //g.fillOval(xRelativelyScreen, yRelativelyScreen, 5, 5);
    }
}
