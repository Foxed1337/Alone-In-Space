package ru.zenkov.display;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class Display {

    public static final DisplayParams DEFAULT_PARAMS = DisplayParams.build();

    private static boolean created = false;
    private static JFrame window;
    private static Canvas content;
    private static BufferedImage screenBuffer;
    private static int[] screenBufferData;
    private static Graphics2D screenGraphics;
    private static BufferStrategy bufferStrategy;
    private static int clearColor;
    private static Dimension size;


    public static void create(DisplayParams dp) {
        if (created) {
            try {
                throw new IllegalStateException("Display is already created. Please dispose of the current window prior to creating a new one.");
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return;
            }
        }


        window = new JFrame(dp.title);
        window.setUndecorated(dp.decorated);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    dp.onCloseRoutine.call();
                    System.exit(0);
                } catch (Exception e1) {
                    System.err.println("An error occurred  during 'on close routine'.");
                }
            }
        });
        window.setResizable(dp.resizable);
        size = new Dimension(dp.width, dp.height);
        content = new Canvas();
        content.setPreferredSize(size);
        window.getContentPane().add(content);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        screenBuffer = new BufferedImage(content.getWidth(), content.getHeight(), BufferedImage.TYPE_INT_ARGB);
        screenBufferData = ((DataBufferInt) screenBuffer.getRaster().getDataBuffer()).getData();
        content.createBufferStrategy(dp.numBuffers);
        bufferStrategy = content.getBufferStrategy();
        screenGraphics = (Graphics2D) screenBuffer.getGraphics();
        clearColor = dp.clearColor;

        if (dp.withAntialiasing) {
            if ((dp.antialiasing & DisplayParams.ANTIALIAS) > 0) {
                screenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            if ((dp.antialiasing & DisplayParams.TEXT_ANTIALIAS) > 0) {
                screenGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }
        }

        if (dp.input != null) {
            window.add(dp.input);
        }

        created = true;
    }

    public static void clear() {
        if (!created) {
            try {
                throw new IllegalStateException("Display is not created");
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return;
            }
        }
        Arrays.fill(screenBufferData, clearColor);
    }

    public static void swapBuffers() {
        if (!created) {
            try {
                throw new IllegalStateException("Display is not created.");
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return;
            }
        }
        Graphics g = bufferStrategy.getDrawGraphics();
        g.drawImage(screenBuffer, 0, 0, null);
        bufferStrategy.show();
    }

    public static int[] getScreenBufferData() {
        if (!created) {
            try {
                throw new IllegalStateException("Display is not created.");
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return null;
            }
        }
        return screenBufferData;
    }

    public static Graphics2D getGraphics() {
        if (!created) {
            try {
                throw new IllegalStateException("Display is not created.");
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return null;
            }
        }
        return screenGraphics;
    }

    public static void dispose() {
        if (!created) {
            try {
                throw new IllegalStateException("Display is not created.");
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return;
            }
        }
        window.dispose();
        created = false;
    }

    public static void setTitle(String title) {
        if (!created) {
            try {
                throw new IllegalStateException("Display is not created.");
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return;
            }
        }
        window.setTitle(title);
    }

    public static Point getMousePosition() {
        return content.getMousePosition();
    }

    public static Dimension getSize() {
        return size;
    }
}
