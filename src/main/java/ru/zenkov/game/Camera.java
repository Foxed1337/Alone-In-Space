package ru.zenkov.game;

import ru.zenkov.game.entity.GameObject;
import ru.zenkov.game.entity.Player;
import ru.zenkov.phisics.Vector2D;

import java.awt.*;
import java.util.List;

public class Camera {
    private static int xRelativelyScreen;
    private static int yRelativelyScreen;
    private static int width;
    private static int height;
    private static int speed;
    private static int distanceForStartMoving;
    private static Player player;


    public static void init(int screenWidth, int screenHeight, int distanceForStartMoving, int speed, Player player) {

        width = screenWidth;
        height = screenHeight;
        xRelativelyScreen = screenWidth / 2;
        yRelativelyScreen = screenHeight / 2;

        Camera.speed = speed;
        Camera.player = player;
        Camera.distanceForStartMoving = distanceForStartMoving;
    }


    public static void update(List<GameObject> gameObjects, GameMap gameMap) {

        int dx = player.getX() - xRelativelyScreen;
        int dy = player.getY() - yRelativelyScreen;

        boolean canChangeX = false;
        boolean canChangeY = false;

        double s = Vector2D.getMod(dx, dy);
        Vector2D dir = Vector2D.getUnitVector(dx, dy);

        int dx2 = (int) (s / speed * dir.getX());
        int dy2 = (int) (s / speed * dir.getY());

        if (Math.abs(dx) >= distanceForStartMoving) {
            int newLeftBorder = (gameMap.getLeftBorder() - dx2);
            int newRightBorder = (gameMap.getRightBorder() - dx2);
            if (newLeftBorder <= 0 && newRightBorder >= width) {
                gameMap.setLeftBorder(newLeftBorder);
                gameMap.setRightBorder(newRightBorder);
                canChangeX = true;
            }

        }
        if (Math.abs(dy) >= distanceForStartMoving) {
            int newTopBorder = (gameMap.getTopBorder() - dy2);
            int newBottomBorder = (gameMap.getBottomBorder() - dy2);
            if (newTopBorder <= 0 && newBottomBorder >= height) {
                gameMap.setTopBorder(newTopBorder);
                gameMap.setBottomBorder(newBottomBorder);
                canChangeY = true;
            }
        }

        for (GameObject go : gameObjects) {
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

    public static void render(Graphics2D g) {
        g.fillOval(xRelativelyScreen, yRelativelyScreen, 5, 5);
    }
}
