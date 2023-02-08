package org.example.New.Components;

import org.example.New.Liseteners.MyListener;
import org.example.New.Models.Box;
import org.example.New.Models.empty;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;

public class Panel extends JPanel {

    JFrame frame;
    public int size = 150;
    int border = 30;
    org.example.New.Models.Box[][] boxes;
    int targetX, targetY;
    int playerX, playerY;
    Random r = new Random();
    int level = 1;
    int width = 600, height = 600;
    public volatile int xPixels;
    public volatile int yPixels;
    public org.example.New.Models.Box currentBox;
    JLabel levelLabel, restart;
    ArrayList<org.example.New.Models.Box[][]> allLevels = new ArrayList<>();

    public Panel(JFrame frame_) {
        frame = frame_;

        this.setLayout(null);

        levelLabel = createLabel("Level : " + level, 680, 260, 130, 40, 20,
                () -> BorderFactory.createStrokeBorder(new BasicStroke(4)),
                Cursor.CROSSHAIR_CURSOR, Color.white);
        restart = createLabel("Restart", 680, 180, 130, 40, 20,
                () -> BorderFactory.createStrokeBorder(new BasicStroke(4)),
                Cursor.HAND_CURSOR, Color.white);
        this.add(createLabel(" Maze Game ", 650, 100, 200, 30, 30,
                BorderFactory::createEmptyBorder, Cursor.DEFAULT_CURSOR, Color.GREEN));
        this.add(levelLabel);
        this.add(restart);
        this.repaint();

        frame.add(this);

        start();
    }

    public void restart() {
        currentBox.x = playerX;
        currentBox.y = playerY;
        repaint();
    }

    private JLabel createLabel(String name, int x, int y, int width_, int height_, int fontSize, empty BorderFuntion, int cursor, Color color) {
        JLabel label = new JLabel(name);
        label.setCursor(new Cursor(cursor));
        label.setBounds(x, y, width_, height_);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(BorderFuntion.apply());
        label.setFont(new Font("times new roman", Font.BOLD, fontSize));
        label.setBackground(new Color(255, 255, 255));
        label.setForeground(color);
        return label;
    }

    private void start() {
        if (boxes != null) {
            allLevels.add(boxes);
        }
        xPixels = width / size;
        yPixels = height / size;
        this.boxes = new org.example.New.Models.Box[xPixels][yPixels];
        for (int i = 0; i < xPixels; i++) {
            for (int j = 0; j < yPixels; j++) {
                boxes[i][j] = new org.example.New.Models.Box(i, j);
            }
        }

        for (int i = 0; i < yPixels; i++) {
            for (int j = 0; j < yPixels; j++) {
                boxes[i][j].addNeighbors(boxes);
            }
        }
        int posx = r.nextInt(xPixels);
        int posy = r.nextInt(yPixels);

        targetX = r.nextInt(xPixels);
        targetY = r.nextInt(yPixels);


        currentBox = boxes[posx][posy];
        playerX = posx;
        playerY = posy;
//        currentBox.visited = true;

        startVisiting(currentBox);
        currentBox.walls[0] = true;
        currentBox.walls[2] = true;
        currentBox.walls[1] = true;
        currentBox.walls[3] = true;
        listener = new MyListener(boxes, this);
        frame.addKeyListener(listener);
        restart.addMouseListener(listener);
        repaint();
    }

    MyListener listener;
    Deque<org.example.New.Models.Box> neighbor = new ArrayDeque<>();

    private void startVisiting(org.example.New.Models.Box current) {
        while (current.hasUnvisited()) {
            org.example.New.Models.Box nextMove = current.giveARandoOne();
            if (checkBorders(current, nextMove)) {
                if (current.x > nextMove.x && current.y == nextMove.y) {
                    current.walls[0] = true;
                    nextMove.walls[1] = true;
                    currentBox = nextMove;
                    repaint();
                }
                if (current.x == nextMove.x && current.y > nextMove.y) {
                    current.walls[2] = true;
                    nextMove.walls[3] = true;
                    currentBox = nextMove;
                    repaint();

                }
                if (current.x == nextMove.x && current.y < nextMove.y) {
                    current.walls[3] = true;
                    nextMove.walls[2] = true;
                    currentBox = nextMove;
                    repaint();

                }
                if (current.x < nextMove.x && current.y == nextMove.y) {
                    current.walls[1] = true;
                    nextMove.walls[0] = true;
                    currentBox = nextMove;
                    repaint();
                }
                repaint();
                startVisiting(nextMove);
            }
            repaint();
        }
    }

    private boolean checkBorders(org.example.New.Models.Box box, org.example.New.Models.Box nextMove) {
        if (nextMove == null && box.visited && box.x < 0 && box.y < 0 && box.x > xPixels + 10 && box.y > 10 + yPixels) {
            return false;
        }
        return true;
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        DrawRects(g);

    }

    private boolean checkCollisions() {
        if (targetY == currentBox.y && targetX == currentBox.x) {
            return true;
        }
        return false;
    }

    public void next() {
        frame.removeKeyListener(frame.getKeyListeners()[0]);
        restart.removeMouseListener(restart.getMouseListeners()[0]);
        start();
    }

    private void DrawRects(Graphics g) {
        if (checkCollisions()) {
            size -= 1;
            level += 1;
            levelLabel.setText(" Level : " + level);
            next();
        }

        //background
        g.setColor(Color.black);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        //border
        g.setColor(ColorUIResource.white);
        g.fillRect(10, 10, 10, frame.getHeight() - border);
        g.fillRect(frame.getWidth() - border, 10, 10, frame.getHeight() - 20);
        g.fillRect(10, 10, frame.getWidth() - border, 10);
        g.fillRect(10, frame.getHeight() - border, frame.getWidth() - border, 10);
        //lines
        g.setColor(Color.red);
        for (org.example.New.Models.Box[] box : boxes) {
            for (Box box2 : box) {
                int x = box2.x * size + border;
                int y = box2.y * size + border;
                for (int i = 0; i < box2.walls.length; i++) {
                    if (!box2.walls[i]) {
                        switch (i) {
                            case 1 -> {
                                g.drawLine(x + size, y, x + size, y + size);
                                break;
//                                9110356100
                            }
                            case 0 -> {
                                g.drawLine(x, y, x, y + size);
                                break;
                            }
                            case 2 -> {
                                g.drawLine(x, y, x + size, y);
                                break;
                            }
                            case 3 -> {
                                g.drawLine(x, y + size, x + size, y + size);
                                break;
                            }
                        }
                    }
                }
            }
        }

        g.setColor(Color.green);
        g.fillOval(currentBox.x * size + border + 2, currentBox.y * size + border + 2, size - 2, size - 2);

        //target
        g.setColor(Color.yellow);
        g.fillOval(targetX * size + border + 2, targetY * size + border + 2, size - 2, size - 2);

        //menu
        g.setColor(Color.white);
        g.setFont(new Font("times new roman", Font.BOLD, 20));

    }

}