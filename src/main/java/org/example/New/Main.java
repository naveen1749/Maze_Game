package org.example.New;

import org.example.New.Components.Window;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Window::new);

    }
}
