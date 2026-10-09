package org.example.view;

import org.example.controller.IngressoController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import org.example.model.FaturamentoFilme;

public class PainelRelatorio extends PainelBase {
    private final IngressoController controller;
    private final DefaultTableModel modelo = modeloSomenteLeitura("Filme", "Ingressos vendidos", "Faturamento");
    private final JLabel lblTotal = new JLabel(" ");

    public PainelRelatorio(IngressoController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> recarregar());
        JPanel sul = new JPanel(new BorderLayout());
        sul.add(lblTotal, BorderLayout.CENTER);
        sul.add(btnAtualizar, BorderLayout.EAST);
        add(sul, BorderLayout.SOUTH);
    }

    @Override
    public void recarregar() {
        try {
            List<FaturamentoFilme> linhas = controller.faturamentoPorFilme();
            modelo.setRowCount(0);
            BigDecimal geral = BigDecimal.ZERO;
            int totalIngressos = 0;
            for (FaturamentoFilme l : linhas) {
                modelo.addRow(new Object[]{l.titulo(), l.ingressos(), Ui.moeda(l.total())});
                geral = geral.add(l.total());
                totalIngressos += l.ingressos();
            }
            lblTotal.setText("Total geral: " + totalIngressos + " ingressos  |  " + Ui.moeda(geral));
        } catch (Exception e) {
            Ui.erro(this, e);
        }
    }
}
