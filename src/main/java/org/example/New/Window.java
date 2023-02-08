package org.example.New;

import javax.swing.*;

public class Window {


    JFrame frame;
    static Panel panel;
    int width = 600, height = 600;
    MyListener listener;


    int carX = 40, carY = 40;

    public Window() {
        frame = initailizeframe();
        panel = new Panel(frame);


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
}
