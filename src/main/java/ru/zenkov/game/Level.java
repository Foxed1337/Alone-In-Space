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
import java.util.HashMap;
import java.util.List;


public class Level {

    private final GameMap gameMap;
    private final List<Entity> entities;
    private final Camera camera;
    private final RayCasting rayCasting;

    public Level(int screenWidth, int screenHeight) {
        entities = new ArrayList<>();
        this.gameMap = GameMap.createGameMap(5000, 5000, screenWidth, screenHeight);
        EntityManager.setGameMap(gameMap);
        Entity player = EntityManager.getNew(EntityType.PLAYER);
        entities.add(player);

        for (int i = 0; i < 2000; i++) {
            entities.add(EntityManager.getNew(EntityType.ROCK));
        }
        rayCasting = RayCasting.newRayCasting(player, 300);
        camera = Camera.newCamera(screenWidth, screenHeight, 10, 10, player);
    }

    public void update(Input input, Point mousePosition) {
        entities.forEach(entity -> entity.update(input, mousePosition));
        checkCollision();
        rayCasting.castRays();
        camera.update(entities, gameMap);
    }

    public void render(Graphics2D g) {
        g.setStroke(new BasicStroke(0.5f));
        entities.forEach(gameObject -> gameObject.render(g));
        g.setStroke(new BasicStroke(0.3f));
        rayCasting.render(g);
    }

    private void checkCollision() {
        //Зависит от максимального размера entity
        int maxChunksInWhichEntityCanBe = 4;
        List<Vector2D> chunks = gameMap.getChunksCoordinates();
        HashMap<Vector2D, List<Entity>> entitiesInChunks = new HashMap<>();
        List<ReflectingLine> reflectingLines = new ArrayList<>();
        for (Entity entity : entities) {
            int chunksCount = 0;
            for (Vector2D chunk : chunks) {
                if (Collision.areIntersectedRect(
                        chunk.getX(), chunk.getX() + GameMap.CHUNK_SIZE, chunk.getY(), chunk.getY() + GameMap.CHUNK_SIZE, entity)) {
                    if (!entitiesInChunks.containsKey(chunk)) entitiesInChunks.put(chunk, new ArrayList<>());
                    entitiesInChunks.get(chunk).add(entity);
                    chunksCount++;
                }
                if (chunksCount >= maxChunksInWhichEntityCanBe) break;
            }
            if (Collision.areIntersectedRect(
                    rayCasting.getLeft(), rayCasting.getRight(), rayCasting.getTop(), rayCasting.getBottom(), entity)
                    && (entity.getType() != EntityType.PLAYER)) {
                reflectingLines.addAll(entity.getReflectingLines());
            }
            checkOutOfBorder(entity);
        }
        reflectingLines.addAll(gameMap.getReflectingLines());
        rayCasting.setReflectingLines(reflectingLines);
        for (Vector2D chunk : entitiesInChunks.keySet()) {
            List<Entity> ent = entitiesInChunks.get(chunk);
            for (int i = 0; i < ent.size(); i++) {
                for (int j = i + 1; j < ent.size(); j++) {
                    if (Collision.areIntersectedCircle(ent.get(i), ent.get(j))) {
                        Interacting.interact(ent.get(i), ent.get(j));
                    }
                }
            }
        }
//        List<ReflectingLine> reflectingLines = new ArrayList<>();
//        for (int i = 0; i < entities.size(); i++) {
//            if (Collision.areIntersectedRect(
//                    rayCasting.getLeft(), rayCasting.getRight(), rayCasting.getTop(), rayCasting.getBottom(), entities.get(i))
//                    && (entities.get(i).getType() != EntityType.PLAYER)) {
//                reflectingLines.addAll(entities.get(i).getReflectingLines());
//            }
//            for (int j = i + 1; j < entities.size(); j++) {
//                if (Collision.areIntersectedCircle(entities.get(i), entities.get(j))) {
//                    Interacting.interact(entities.get(i), entities.get(j));
//                }
//            }
//
//            checkOutOfBorder(entities.get(i));
//        }
//        reflectingLines.addAll(gameMap.getReflectingLines());
//        rayCasting.setReflectingLines(reflectingLines);
    }

    private void checkOutOfBorder(Entity entity) {
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
    }
}

