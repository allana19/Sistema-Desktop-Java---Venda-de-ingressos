package org.example;

import org.example.controller.MenuPrincipalController;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipalController().iniciar());
    }
}
