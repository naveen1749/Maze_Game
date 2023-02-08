
package org.example.New.Liseteners;


import org.example.New.Models.Box;
import org.example.New.Components.Panel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyListener extends MouseAdapter implements KeyListener {

    org.example.New.Models.Box[][] grid;
    Panel panel;

    public MyListener(org.example.New.Models.Box[][] grid_, Panel panel_) {
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
                    if (grid[x][y].walls[0] && grid[x - 1][y].walls[1]){
                        panel.currentBox.x = panel.currentBox.x - 1;
                    }
                }

            }
            case KeyEvent.VK_RIGHT -> {
                if (x < panel.xPixels-1 ) {
                    if (grid[x][y].walls[1] && grid[x + 1][y].walls[0]) {
                        panel.currentBox.x = panel.currentBox.x + 1;
                    }
                }
            }
            case KeyEvent.VK_UP -> {
                if (y > 0 ) {
                    if (grid[x][y].walls[2] && grid[x][y - 1].walls[3]) {
                        panel.currentBox.y = panel.currentBox.y - 1;
                    }
                }
            }
            case KeyEvent.VK_DOWN -> {
                if (y < panel.yPixels-1 ) {
                    if (grid[x][y].walls[3] && grid[x][y + 1].walls[2]) {
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

