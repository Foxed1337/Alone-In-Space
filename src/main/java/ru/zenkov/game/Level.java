package ru.zenkov.game;


import ru.zenkov.IO.Input;
import ru.zenkov.game.entity.Entity;
import ru.zenkov.game.entity.EntityType;
import ru.zenkov.game.entity.Player;
import ru.zenkov.game.entity.Rock;
import ru.zenkov.phisics.rayCasting.RayCasting;
import ru.zenkov.phisics.Vector2D;
import ru.zenkov.collision.Collision;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class Level {

    private final RayCasting rayCasting;
    private final GameMap gameMap;
    private final List<Entity> entities;

    public Level(int screenWidth, int screenHeight) {
        entities = new ArrayList<>();
        Player player = new Player(screenWidth / 2, screenHeight / 2, 25, 32, 32, 20, Vector2D.getVector(0, 0));
        entities.add(player);
        entities.add(new Rock(400, 400, 50, 45, 45, 0, Vector2D.getVector(0, 0)));
        entities.add(new Rock(100, 100, 100, 60, 60, 10, Vector2D.getVector(0, 0)));
        entities.add(new Rock(250, 349, 75, 70, 70, 10, Vector2D.getVector(0, 0)));
        entities.add(new Rock(400, 400, 50, 45, 45, 10, Vector2D.getVector(0, 0)));
        entities.add(new Rock(100, 100, 100, 60, 60, 10, Vector2D.getVector(0, 0)));
        entities.add(new Rock(250, 349, 75, 70, 70, 10, Vector2D.getVector(0, 0)));


        rayCasting = new RayCasting(1, player);

        this.gameMap = GameMap.getMap(2000, 2000, screenWidth, screenHeight);
        Camera.init(screenWidth, screenHeight, 10, 10, player);


    }

    public void checkCollision() {

        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                if (Collision.areIntersectedCircle(entities.get(i), entities.get(j))) {
                    Collision.interact(entities.get(i), entities.get(j));
                }
            }
        }
    }


    public void update(Input input, Point mousePosition) {

        entities.forEach(gameObject -> {
            gameObject.update(input, mousePosition);
            if (!Collision.areIntersectedRect(
                    gameMap.getLeftBorder() + gameObject.getWidth(),
                    gameMap.getRightBorder() - gameObject.getWidth(),
                    gameMap.getTopBorder() + gameObject.getHeight(),
                    gameMap.getBottomBorder() - gameObject.getHeight(),
                    gameObject)) {

                if (gameObject.getLeft() <= gameMap.getLeftBorder()) {
                    gameObject.setX(gameMap.getLeftBorder() + (gameObject.getWidth() / 2));
                    gameObject.setResultantForce(Vector2D.getReflection(GameMap.LEFT_NORMAL, gameObject.getResultantForce()));
                }
                if (gameObject.getRight() >= gameMap.getRightBorder()) {
                    gameObject.setX(gameMap.getRightBorder() - (gameObject.getWidth() / 2));
                    gameObject.setResultantForce(Vector2D.getReflection(GameMap.RIGHT_NORMAL, gameObject.getResultantForce()));
                }
                if (gameObject.getTop() <= gameMap.getTopBorder()) {
                    gameObject.setY(gameMap.getTopBorder() + (gameObject.getHeight() / 2));
                    gameObject.setResultantForce(Vector2D.getReflection(GameMap.TOP_NORMAL, gameObject.getResultantForce()));
                }
                if (gameObject.getBottom() >= gameMap.getBottomBorder()) {
                    gameObject.setY(gameMap.getBottomBorder() - (gameObject.getHeight() / 2));
                    gameObject.setResultantForce(Vector2D.getReflection(GameMap.BOTTOM_NORMAL, gameObject.getResultantForce()));
                }

            }
        });

        Camera.update(entities, gameMap);
    }

    public void render(Graphics2D g) {
        g.setStroke(new BasicStroke(0.5f));
        entities.forEach(gameObject -> gameObject.render(g));
        g.setStroke(new BasicStroke(0.3f));
        rayCasting.render(g);
        g.setStroke(new BasicStroke(20));
        g.setColor(new Color(0x800080));
        Camera.render(g);
        g.drawRect(gameMap.getLeftBorder(), gameMap.getTopBorder(), gameMap.getWidth(), gameMap.getHeight());
    }

    public void rayCast(Point mousePosition) {

        rayCasting.castRays(entities
                .stream()
                .filter(go -> go.type == EntityType.ROCK)
                .map(Entity::getReflectingLines)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()), mousePosition);
    }
}

