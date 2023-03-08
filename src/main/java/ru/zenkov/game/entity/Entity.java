package ru.zenkov.game.entity;

import ru.zenkov.IO.Input;
import ru.zenkov.phisics.Vector2D;
import ru.zenkov.phisics.rayCasting.ReflectingLine;
import java.util.List;

import java.awt.*;

public abstract class Entity {

    protected float RESISTANCE = 0.33f;

    protected int x;
    protected int y;
    protected double mass;
    protected int height;
    protected int width;
    protected double speed;
    protected Vector2D acceleration;
    protected Vector2D resultantForce;
    protected Vector2D resistanceForce;

    public EntityType type;


    protected Entity(int x, int y, double mass, int width, int height, double speed, Vector2D resultantForce) {
        this.x = x;
        this.y = y;
        this.mass = mass;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.resultantForce = resultantForce;
        this.acceleration = Vector2D.newVector(0, 0);
    }

    public abstract void update(Input input, Point mousePosition);

    public abstract void render(Graphics2D g);

    public abstract List<ReflectingLine> getReflectingLines();

    public void move() {

        int newX, newY;
        resistanceForce = Vector2D.multiplyConst(resultantForce, -RESISTANCE / mass);
        resultantForce = addResistAndResultForce(resistanceForce, resultantForce);
        resultantForce = Vector2D.add(resultantForce, resistanceForce);
        acceleration = Vector2D.divideConst(resultantForce, mass);

        newX = Math.round((float) (x + acceleration.getX()));
        newY = Math.round((float) (y + acceleration.getY()));

        x = newX;
        y = newY;

    }

    private Vector2D addResistAndResultForce(Vector2D resistanceForce, Vector2D resultantForce) {
        double newX;
        double newY;

        newX = (Math.abs(resistanceForce.getX()) >= Math.abs(resultantForce.getX()))
                ? 0
                : resultantForce.getX() + resistanceForce.getX();

        newY = (Math.abs(resistanceForce.getY()) >= Math.abs(resultantForce.getY()))
                ? 0
                : resultantForce.getY() + resistanceForce.getY();

        return Vector2D.newVector(newX, newY);
    }

    public int getLeft() {
        return x - width / 2;
    }

    public int getRight() {
        return x + width / 2;
    }

    public int getTop() {
        return y - height / 2;
    }

    public int getBottom() {
        return y + height / 2;
    }

    public Vector2D getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2D acceleration) {
        this.acceleration = acceleration;
    }

    public Vector2D getResultantForce() {
        return resultantForce;
    }

    public void setResultantForce(Vector2D resultantForce) {
        this.resultantForce = resultantForce;
    }

    public Vector2D getResistanceForce() {
        return resistanceForce;
    }

    public void setResistanceForce(Vector2D resistanceForce) {
        this.resistanceForce = resistanceForce;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getHeight() {
        return height;
    }

    public EntityType getType() {
        return type;
    }
}
