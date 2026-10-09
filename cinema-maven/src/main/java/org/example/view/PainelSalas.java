package org.example.view;

import org.example.controller.SalaController;
import org.example.model.Sala;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PainelSalas extends PainelBase {
    private final SalaController controller;
    private final DefaultTableModel modelo = modeloSomenteLeitura("ID", "Nome", "Capacidade");
    private final JTable tabela = new JTable(modelo);

    private final JTextField txtNome = new JTextField();
    private final JTextField txtCapacidade = new JTextField();
    private final JTextField txtPorFileira = new JTextField("10");

    private List<Sala> salas = new ArrayList<>();
    private Integer idSelecionado = null;

    public PainelSalas(SalaController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selecionar();
        });
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel form = novoFormulario();
        form.add(new JLabel("Nome:"));               form.add(txtNome);
        form.add(new JLabel("Capacidade:"));         form.add(txtCapacidade);
        form.add(new JLabel("Assentos/fileira:"));   form.add(txtPorFileira);

        JLabel dica = new JLabel("<html><i>Ao criar a sala, os assentos (A1, A2, B1...) são gerados automaticamente. "
                + "Em uma sala existente só o nome pode ser alterado.</i></html>");
        JPanel formCompleto = new JPanel(new BorderLayout(0, 8));
        formCompleto.add(form, BorderLayout.NORTH);
        formCompleto.add(dica, BorderLayout.CENTER);

        JButton btnNova = new JButton("Nova");
        JButton btnSalvar = new JButton("Salvar");
        JButton btnExcluir = new JButton("Excluir");
        btnNova.addActionListener(e -> limpar());
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());

        JPanel lateral = montarLateral(formCompleto, btnNova, btnSalvar, btnExcluir);
        add(lateral, BorderLayout.EAST);
    }

    @Override
    public void recarregar() {
        try {
            salas = controller.listar();
            modelo.setRowCount(0);
            for (Sala s : salas) modelo.addRow(new Object[]{s.getId(), s.getNome(), s.getCapacidade()});
            limpar();
        } catch (Exception e) {
            Ui.erro(this, e);
        }
    }

    private void selecionar() {
        int linha = tabela.getSelectedRow();
        if (linha < 0 || linha >= salas.size()) return;
        Sala s = salas.get(linha);
        idSelecionado = s.getId();
        txtNome.setText(s.getNome());
        txtCapacidade.setText(String.valueOf(s.getCapacidade()));
        txtCapacidade.setEnabled(false);
        txtPorFileira.setEnabled(false);
    }

    private void limpar() {
        tabela.clearSelection();
        idSelecionado = null;
        txtNome.setText("");
        txtCapacidade.setText("");
        txtPorFileira.setText("10");
        txtCapacidade.setEnabled(true);
        txtPorFileira.setEnabled(true);
    }

    private void salvar() {
        try {
            if (idSelecionado == null) {
                controller.criar(txtNome.getText(), txtCapacidade.getText(), txtPorFileira.getText());
            } else {
                controller.renomear(idSelecionado, txtNome.getText());
            }
            recarregar();
        } catch (Exception e) {
            Ui.erro(this, e);
        }
    }

    private void excluir() {
        if (idSelecionado == null) {
            Ui.info(this, "Selecione uma sala na tabela.");
            return;
        }
        if (!Ui.confirmar(this, "Excluir a sala selecionada e todos os seus assentos?")) return;
        try {
            controller.excluir(idSelecionado);
            recarregar();
        } catch (Exception e) {
            Ui.erro(this, e);
        }
    }
}
