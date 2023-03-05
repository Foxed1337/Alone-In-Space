package ru.zenkov.game;

import ru.zenkov.display.Display;
import ru.zenkov.IO.Input;
import ru.zenkov.display.DisplayParams;
import ru.zenkov.utils.Time;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.Callable;


public class Game implements Runnable, Callable<Void> {
    private static final float UPDATE_RATE = 60.0f;
    public static final float UPDATE_INTERVAL = Time.SECOND / UPDATE_RATE;
    public static final long IDLE_TIME = 1; //1ms
    public static final String TITLE = "AloneInSpace";
    public static final String TEXTURE_ATLAS_NAME = "test.png";

    private boolean running;
    private Thread gameThread;
    private Callable<Void> onCloseRoutine;
    private final Graphics2D displayGraphics;
    private final Input input;
    private Level level;
    private boolean isLevelUpdate = false;

    public Game() {
        input = Input.getInstance();
        DisplayParams dp = DisplayParams.build()
                .withTitle(TITLE)
                .withInput(input)
                .withCustomOnCloseOperation(this)
                .withWidth(800)
                .withHeight(600)
                .makeWithAntialiasing()
                .withAntialiasing((byte) 1)
                .withMultiBuffering(3);
        Display.create(dp);
        displayGraphics = Display.getGraphics();
        level = new Level(Display.getSize().width, Display.getSize().height);
    }

    private void recreateLevel() {
        level = new Level(Display.getSize().width, Display.getSize().height);
    }

    public synchronized void start() {
        if (running)
            return;

        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cleanUp();
    }

    @Override
    public Void call() throws Exception {
        stop();
        return null;
    }

    //расчеты
    private void update() {
        if (input.getKey(KeyEvent.VK_SPACE) && !isLevelUpdate) {
            recreateLevel();
            isLevelUpdate = true;
        } else if (!input.getKey(KeyEvent.VK_SPACE)) {
            isLevelUpdate = false;
        }

        level.checkCollision();
        level.update(input, Display.getMousePosition());
        level.rayCast(Display.getMousePosition());
    }

    //рисуем полсе render
    private void render() {
        Display.clear();
        level.render(displayGraphics);
        Display.swapBuffers();
    }

    @Override
    public void run() {


        int fps = 0;
        int upd = 0;
        int updl = 0;

        long count = 0;

        //определяет количество update
        float delta = 0;

        long lastTime = Time.get();
        while (running) {

            long now = Time.get();
            long elapsedTime = now - lastTime;
            lastTime = now;

            count += elapsedTime;

            boolean render = false;
            delta += (elapsedTime / UPDATE_INTERVAL);

            while (delta > 1) {
                update();
                upd++;
                delta--;
                if (render) {
                    updl++;
                } else {
                    render = true;
                }
            }

            if (render) {
                render();
                fps++;
            } else {
                try {
                    Thread.sleep(IDLE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (count >= Time.SECOND) {
                Display.setTitle(TITLE + " || Fps: " + fps + " | Upd: " + upd + " | Updl: " + updl);
                upd = 0;
                fps = 0;
                updl = 0;
                count = 0;
            }
        }
    }

    private void cleanUp() {
        Display.dispose();
    }
}
