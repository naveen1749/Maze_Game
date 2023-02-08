package org.example.New;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
