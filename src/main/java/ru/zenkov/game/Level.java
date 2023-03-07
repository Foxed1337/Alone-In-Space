package ru.zenkov.game;


import ru.zenkov.IO.Input;
import ru.zenkov.game.entity.*;
import ru.zenkov.phisics.Interacting;
import ru.zenkov.phisics.rayCasting.RayCasting;
import ru.zenkov.phisics.Vector2D;
import ru.zenkov.collision.Collision;
import ru.zenkov.phisics.rayCasting.ReflectingLine;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Level {

    private final GameMap gameMap;
    private final List<Entity> entities;
    private final Camera camera;
    private final RayCasting rayCasting;


    public Level(int screenWidth, int screenHeight) {
        entities = new ArrayList<>();
        this.gameMap = GameMap.newGameMap(10000, 10000, screenWidth, screenHeight);
        //TODO getEntities
        EntityManager.setGameMap(gameMap);
        Entity player = EntityManager.getNew(EntityType.PLAYER);
        entities.add(player);

        for (int i = 0; i < 2000; i++) {
            entities.add(EntityManager.getNew(EntityType.ROCK));
        }
        rayCasting = RayCasting.newRayCasting(player, 300);
        camera = Camera.newCamera(screenWidth, screenHeight, 10, 10, (Player) player);
    }


    public void update(Input input, Point mousePosition) {
        checkCollision();
        checkOutOfBorder();
        entities.forEach(entity -> entity.update(input, mousePosition));
        rayCast();
        camera.update(entities, gameMap);
    }

    public void render(Graphics2D g) {

        g.setStroke(new BasicStroke(0.5f));
        entities.forEach(gameObject -> gameObject.render(g));
        g.setStroke(new BasicStroke(0.3f));
        rayCasting.render(g);
        camera.render(g);
    }

    private void checkCollision() {
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                if (Collision.areIntersectedCircle(entities.get(i), entities.get(j))) {
                    Interacting.interact(entities.get(i), entities.get(j));
                }
            }
        }
    }

    private void checkOutOfBorder() {
        entities.forEach(entity -> {
            if (!Collision.areIntersectedRect(
                    gameMap.getLeftBorder() + entity.getWidth(),
                    gameMap.getRightBorder() - entity.getWidth(),
                    gameMap.getTopBorder() + entity.getHeight(),
                    gameMap.getBottomBorder() - entity.getHeight(),
                    entity)) {

                if (entity.getLeft() <= gameMap.getLeftBorder()) {
                    entity.setX(gameMap.getLeftBorder() + (entity.getWidth() / 2));
                    entity.setResultantForce(Vector2D.getReflection(GameMap.LEFT_NORMAL, entity.getResultantForce()));
                }
                if (entity.getRight() >= gameMap.getRightBorder()) {
                    entity.setX(gameMap.getRightBorder() - (entity.getWidth() / 2));
                    entity.setResultantForce(Vector2D.getReflection(GameMap.RIGHT_NORMAL, entity.getResultantForce()));
                }
                if (entity.getTop() <= gameMap.getTopBorder()) {
                    entity.setY(gameMap.getTopBorder() + (entity.getHeight() / 2));
                    entity.setResultantForce(Vector2D.getReflection(GameMap.TOP_NORMAL, entity.getResultantForce()));
                }
                if (entity.getBottom() >= gameMap.getBottomBorder()) {
                    entity.setY(gameMap.getBottomBorder() - (entity.getHeight() / 2));
                    entity.setResultantForce(Vector2D.getReflection(GameMap.BOTTOM_NORMAL, entity.getResultantForce()));
                }
            }
        });
    }

    private void rayCast() {
        List<ReflectingLine> refLines = Stream.concat(entities
                                .stream()
                                .filter(go -> go.type != EntityType.PLAYER)
                                .map(Entity::getReflectingLines)
                                .flatMap(Collection::stream)
                        , gameMap.getReflectingLines().stream())
                .collect(Collectors.toList());
        rayCasting.castRays(refLines);
    }
}

