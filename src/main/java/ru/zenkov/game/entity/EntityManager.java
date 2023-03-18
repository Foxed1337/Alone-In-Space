package ru.zenkov.game.entity;

import ru.zenkov.game.GameMap;
import ru.zenkov.phisics.Vector2D;

import java.util.concurrent.ThreadLocalRandom;

public class EntityManager {

    private static GameMap gameMap;

    public static void setGameMap(GameMap gm) {
        gameMap = gm;
    }

    public static Entity getNew(EntityType entityType) {
        //TODO Сделать кастомный эксепшен
        if (gameMap == null) throw new IllegalArgumentException("Game map is not set");
        switch (entityType) {
            case ROCK:
                return getRock();
            case PLAYER:
                return getPlayer();
        }
        return null;
    }


    private static Player getPlayer() {
        return new Player((gameMap.getWidth() + 2 * gameMap.getLeftBorder()) / 2,
                (gameMap.getHeight() + 2 * gameMap.getTopBorder()) / 2,
                25,
                10,
                10,
                20,
                Vector2D.createVector(0, 0));
    }

    private static Rock getRock() {
        int minSize = 10;
        int maxSize = 110;
        int size = ThreadLocalRandom.current().nextInt(minSize, maxSize + 1);

        int minX = size + gameMap.getLeftBorder();
        int maxX = gameMap.getRightBorder() - size;
        int minY = size + gameMap.getTopBorder();
        int maxY = gameMap.getBottomBorder() - size;

        int x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
        int y = ThreadLocalRandom.current().nextInt(minY, maxY + 1);

        int minMass = size + 5;
        int maxMass = size + 50;
        int mass = ThreadLocalRandom.current().nextInt(minMass, maxMass + 1);

        double forceAngle = ThreadLocalRandom.current().nextDouble();

        int minForceMod = minSize + 10;
        int maxForceMod = maxSize + 30;
        int forceMod = ThreadLocalRandom.current().nextInt(minForceMod, maxForceMod + 1);

        double forceX = forceMod * Math.cos(forceAngle);
        double forceY = forceMod * Math.sin(forceAngle);

        return new Rock(x, y, mass, size, size, 0, Vector2D.createVector(forceX, forceY));
    }
}
