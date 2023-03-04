package ru.zenkov.game;


import ru.zenkov.IO.Input;
import ru.zenkov.game.entity.*;
import ru.zenkov.phisics.rayCasting.RayCasting;
import ru.zenkov.phisics.Vector2D;
import ru.zenkov.collision.Collision;
import ru.zenkov.phisics.rayCasting.ReflectingLine;

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
        this.gameMap = GameMap.getMap(2000, 2000, screenWidth, screenHeight);
        EntityManager.setGameMap(gameMap);
        Entity player = EntityManager.get(EntityType.PLAYER);
        entities.add(player);
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));
        entities.add(EntityManager.get(EntityType.ROCK));


        rayCasting = new RayCasting(1, (Player) player);


        Camera.init(screenWidth, screenHeight, 10, 10, (Player) player);


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
      //  g.drawRect(gameMap.getLeftBorder(), gameMap.getTopBorder(), gameMap.getWidth(), gameMap.getHeight());
    }

    public void rayCast(Point mousePosition) {
        var a = entities
                .stream()
                .filter(go -> go.type == EntityType.ROCK)
                .map(Entity::getReflectingLines)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        a.add(ReflectingLine.getReflectingLine(gameMap.getLeftBorder() + 10, gameMap.getTopBorder() + 10, gameMap.getRightBorder() - 10, gameMap.getTopBorder() + 10));
        a.add(ReflectingLine.getReflectingLine(gameMap.getLeftBorder() + 10, gameMap.getBottomBorder() - 10, gameMap.getRightBorder() - 10, gameMap.getBottomBorder() - 10));
        a.add(ReflectingLine.getReflectingLine(gameMap.getLeftBorder() + 10, gameMap.getTopBorder() + 10, gameMap.getLeftBorder() + 10, gameMap.getBottomBorder() - 10));
        a.add(ReflectingLine.getReflectingLine(gameMap.getRightBorder() - 10, gameMap.getTopBorder() + 10, gameMap.getRightBorder() - 10, gameMap.getBottomBorder() - 10));
        rayCasting.castRays(a, mousePosition);
    }
}

