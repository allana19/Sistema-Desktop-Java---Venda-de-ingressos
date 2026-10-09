package org.example.view;

import javax.swing.*;
import java.awt.*;

public class JanelaPrincipal extends JFrame {

    public JanelaPrincipal(String[] titulos, PainelBase[] paineis) {
        super("Sistema de Gerenciamento de Cinema");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(980, 560);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        for (int i = 0; i < paineis.length; i++) abas.addTab(titulos[i], paineis[i]);

        // ao trocar de aba, ela relê o banco (ex.: sessões enxergam filmes recém-cadastrados)
        abas.addChangeListener(e -> paineis[abas.getSelectedIndex()].recarregar());

        add(abas, BorderLayout.CENTER);
        paineis[0].recarregar();
    }
}
