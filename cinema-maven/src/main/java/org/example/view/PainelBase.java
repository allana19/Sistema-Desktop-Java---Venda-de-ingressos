package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public abstract class PainelBase extends JPanel {

    public abstract void recarregar();

    protected static DefaultTableModel modeloSomenteLeitura(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int linha, int coluna) { return false; }
        };
    }

    protected static JPanel montarLateral(JPanel formulario, JButton... botoes) {
        JPanel linhaBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        for (JButton b : botoes) linhaBotoes.add(b);
        JPanel lateral = new JPanel(new BorderLayout(0, 12));
        lateral.add(formulario, BorderLayout.NORTH);
        lateral.add(linhaBotoes, BorderLayout.CENTER);
        lateral.setPreferredSize(new Dimension(330, 0));
        return lateral;
    }

    protected static JPanel novoFormulario() {
        return new JPanel(new GridLayout(0, 2, 6, 8));
    }
}
