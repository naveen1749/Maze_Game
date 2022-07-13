package Maze_Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Window {


    JFrame frame;
    static MyPanel panel;
    int width = 600, height = 600;
    MyListener listener;


    int carX = 40, carY = 40;

    public Window() {
        frame = initailizeframe();
        panel = new MyPanel(frame);
        listener = new MyListener(panel.boxes, panel);
        frame.addKeyListener(listener);

    }

    private JFrame initailizeframe() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(width + 270, height + 70);

        frame.setResizable(false);
        frame.setVisible(true);

        return frame;
    }

    public class MyPanel extends JPanel {

        static ArrayList<int[][]> levels = new ArrayList<>();
        JFrame frame;
        int size = 150;
        int border = 30;
        int wallBlocks = 5;
        Box[][] boxes;
        int targetX, targetY;
        int playerX, playerY;

        Random r = new Random();
        int level = 1;
        int i = 0;

        volatile int xPixels, yPixels;
        BufferedImage wall, car;
        Box currentBox;
        JLabel levelLabel, restart;
        ArrayList<Box[][]> allLevels = new ArrayList<>();

        public MyPanel(JFrame frame_) {
            frame = frame_;

            this.setLayout(null);

            levelLabel = createLabel("Level : " + level, 700, 260, 100, 40, 20,
                    () -> BorderFactory.createStrokeBorder(new BasicStroke(4)),
                    Cursor.CROSSHAIR_CURSOR,Color.white);
            restart = createLabel("Restart" + level, 700, 180, 100, 40, 20,
                    () -> BorderFactory.createStrokeBorder(new BasicStroke(4)),
                    Cursor.HAND_CURSOR,Color.white);
            restart.addMouseListener(new MyListener(null, this));
            this.add(createLabel(" Maze Game ", 650, 100, 200, 30, 30,
                    BorderFactory::createEmptyBorder, Cursor.DEFAULT_CURSOR,Color.GREEN));
            this.add(levelLabel);
            this.add(restart);
            this.repaint();

            frame.add(this);

            try {
                wall = ImageIO.read(new File("C:\\Users\\naveen\\IdeaProjects\\forSwing\\src\\drawWalls\\Imgs\\wall.png"));
                car = ImageIO.read(new File("C:\\Users\\naveen\\IdeaProjects\\forSwing\\src\\drawWalls\\Imgs\\player-white.png"));


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            start();
        }

        protected void restart() {
            currentBox.x = playerX;
            currentBox.y = playerY;
            repaint();
        }

        private JLabel createLabel(String name, int x, int y, int width_, int height_, int fontSize, empty BorderFuntion, int cursor,Color color) {
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
            System.out.println("siZE IS " + allLevels.size());
            xPixels=width/size;
            yPixels=height/size;
            System.out.println(xPixels +" y : "+yPixels);
            boxes = new Box[xPixels][yPixels];
            for (int i = 0; i < xPixels; i++) {
                for (int j = 0; j < yPixels; j++) {
                    boxes[i][j] = new Box(i, j);
                }
            }

            for (int i = 0; i < xPixels; i++) {
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
            currentBox.visited = true;
            startVisiting(currentBox);
            repaint();
        }


        private void startVisiting(Box box) {
            while (box.hasUnvisited()) {
                Box nextMove = box.giveARandoOne();

                if (checkBorders(box, nextMove)) {
                    if (box.x > nextMove.x && box.y == nextMove.y) {

                        box.walls[3] = true;
                        nextMove.walls[1] = true;
                        currentBox = nextMove;
                        repaint();

                    }
                    if (box.x == nextMove.x && box.y > nextMove.y) {
                        box.walls[0] = true;
                        nextMove.walls[2] = true;
                        currentBox = nextMove;
                        repaint();

                    }
                    if (box.x == nextMove.x && box.y < nextMove.y) {
                        box.walls[2] = true;
                        nextMove.walls[0] = true;
                        currentBox = nextMove;
                        repaint();


                    }
                    if (box.x < nextMove.x && box.y == nextMove.y) {
                        box.walls[1] = true;
                        nextMove.walls[3] = true;
                        currentBox = nextMove;
                        repaint();

                    }
                    i++;
//                    sleep(100);
                    repaint();
                    startVisiting(nextMove);
                }
                repaint();
            }
        }

        private boolean checkBorders(Box box, Box nextMove) {
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

        private void DrawRects(Graphics g) {
            if (checkCollisions()) {
                size -= 10;
                level += 1;
                levelLabel.setText(" Level : " + level);
                start();
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
            for (Box[] box : boxes) {
                for (Box box2 : box) {
                    int x = box2.x * size + border;
                    int y = box2.y * size + border;
                    for (int i = 0; i < box2.walls.length; i++) {
                        if (!box2.walls[i]) {
                            switch (i) {
                                case 1 -> {
                                    g.drawLine(x+size, y, x+size , y+size);
                                    break;
                                }
                                case 0 -> {
                                    g.drawLine(x,y,x+size,y);
                                    break;
                                }
                                case 2 -> {
                                    g.drawLine(x , y + size, x+size, y+size);
                                    break;
                                }
                                case 3 -> {
                                    g.drawLine(x, y, x, y+size);
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            g.setColor(Color.green);
            g.fillRect(currentBox.x * size + border, currentBox.y * size + border, size - 2, size - 2);

            //target
            g.setColor(Color.yellow);
            g.fillRect(targetX * size + border, targetY * size + border, size - 2, size - 2);

            //menu
            g.setColor(Color.white);
            g.setFont(new Font("times new roman", Font.BOLD, 20));

        }

    }

    class MyListener extends MouseAdapter implements KeyListener {

        Box[][] grid;
        MyPanel panel;

        public MyListener(Box[][] grid_, MyPanel panel_) {
            grid = grid_;
            panel = panel_;
        }

        @Override
        public void keyPressed(KeyEvent e) {

            int x = panel.currentBox.x;
            int y = panel.currentBox.y;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if (x > 0 ) {
                        if (grid[x][y].walls[3] && grid[x - 1][y].walls[1]){
                            panel.currentBox.x = panel.currentBox.x - 1;
                        }
                    }

                }
                case KeyEvent.VK_RIGHT -> {
                    if (x < panel.xPixels ) {
                        if (grid[x][y].walls[1] && grid[x + 1][y].walls[3]) {
                            panel.currentBox.x = panel.currentBox.x + 1;
                        }
                    }
                }
                case KeyEvent.VK_UP -> {
                    if (y > 0 ) {
                        if (grid[x][y].walls[0] && grid[x][y - 1].walls[2]) {
                            panel.currentBox.y = panel.currentBox.y - 1;
                        }
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if (y < panel.yPixels ) {
                        if (grid[x][y].walls[2] && grid[x][y + 1].walls[0]) {
                            panel.currentBox.y = panel.currentBox.y + 1;
                        }
                    }
                }
            }
            panel.repaint();
            panel.repaint(panel.currentBox.x, panel.currentBox.y, panel.size, panel.size);
        }

        public Box getNeighbor(int x, int y, int from) {
            return grid[x][y].neighbors.get(from);
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }


        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() instanceof JLabel) {
                panel.restart();
            }
        }

    }

    class Menus extends JMenu implements ActionListener {
        File filename;
        int[][] grid;

        public Menus(String s, int[][] grid_) {
            super(s);
            grid = grid_;
        }

        protected JMenu addMenuItems() {
            JMenuItem save = new JMenuItem("Save");
            save.addActionListener(this);
            JMenuItem open = new JMenuItem("open");
            open.addActionListener(this);
            JMenuItem New = new JMenuItem("New");
            New.addActionListener(this);

            save.setActionCommand("save");
            open.setActionCommand("open");
            New.setActionCommand("New");
            save.setAccelerator(KeyStroke.getKeyStroke("control s"));
            open.setAccelerator(KeyStroke.getKeyStroke("control o"));
            this.add(save);
            this.add(open);
            this.add(New);
            return this;

        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Window());

    }
}

interface empty {
    Border apply();
}
