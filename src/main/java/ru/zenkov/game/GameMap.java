package ru.zenkov.game;

import ru.zenkov.phisics.Vector2D;
import ru.zenkov.phisics.rayCasting.ReflectingLine;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    public static final Vector2D LEFT_NORMAL = Vector2D.getVector(-1, 0);
    public static final Vector2D RIGHT_NORMAL = Vector2D.getVector(1, 0);
    public static final Vector2D TOP_NORMAL = Vector2D.getVector(0, 1);
    public static final Vector2D BOTTOM_NORMAL = Vector2D.getVector(0, -1);
    private final int width;
    private final int height;
    private int leftBorder;
    private int rightBorder;
    private int topBorder;
    private int bottomBorder;


    private GameMap(int width, int height, int leftBorder, int rightBorder, int topBorder, int bottomBorder) {
        this.width = width;
        this.height = height;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.topBorder = topBorder;
        this.bottomBorder = bottomBorder;
    }

    public static GameMap getMap(int width, int height, int screenWidth, int screenHeight) {
        if (width < screenWidth || height < screenHeight)
            throw new IllegalArgumentException("Map size can't be less than screen size");

        int borderX = Math.abs(screenWidth - width) / 2;
        int borderY = Math.abs(screenHeight - height) / 2;
        int leftBorder = -borderX;
        int rightBorder = screenWidth + borderX;
        int topBorder = -borderY;
        int bottomBorder = screenHeight + borderY;

        System.out.printf("LEFT BORDER - %s\n", leftBorder);
        System.out.printf("RIGHT BORDER - %s\n", rightBorder);
        System.out.printf("TOP BORDER - %s\n", topBorder);
        System.out.printf("BOTTOM BORDER - %s\n", bottomBorder);

        return new GameMap(width, height, leftBorder, rightBorder, topBorder, bottomBorder);
    }

    public List<ReflectingLine> getReflectingLines() {
        List<ReflectingLine> res = new ArrayList<>();
        res.add(ReflectingLine.getReflectingLine(getLeftBorder() + 10, getTopBorder() + 10, getRightBorder() - 10, getTopBorder() + 10, this));
        res.add(ReflectingLine.getReflectingLine(getLeftBorder() + 10, getBottomBorder() - 10, getRightBorder() - 10, getBottomBorder() - 10, this));
        res.add(ReflectingLine.getReflectingLine(getLeftBorder() + 10, getTopBorder() + 10, getLeftBorder() + 10, getBottomBorder() - 10, this));
        res.add(ReflectingLine.getReflectingLine(getRightBorder() - 10, getTopBorder() + 10, getRightBorder() - 10, getBottomBorder() - 10, this));

        return res;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLeftBorder() {
        return leftBorder;
    }

    public void setLeftBorder(int leftBorder) {
        this.leftBorder = leftBorder;
    }

    public int getRightBorder() {
        return rightBorder;
    }

    public void setRightBorder(int rightBorder) {
        this.rightBorder = rightBorder;
    }

    public int getTopBorder() {
        return topBorder;
    }

    public void setTopBorder(int topBorder) {
        this.topBorder = topBorder;
    }

    public int getBottomBorder() {
        return bottomBorder;
    }

    public void setBottomBorder(int bottomBorder) {
        this.bottomBorder = bottomBorder;
    }
}
