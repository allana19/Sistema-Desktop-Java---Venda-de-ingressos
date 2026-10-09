package org.example.view;

import org.example.controller.FilmeController;
import org.example.model.Filme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PainelFilmes extends PainelBase {
    private final FilmeController controller;
    private final DefaultTableModel modelo =
            modeloSomenteLeitura("ID", "Título", "Gênero", "Duração (min)", "Classif.");
    private final JTable tabela = new JTable(modelo);

    private final JTextField txtTitulo = new JTextField();
    private final JTextField txtGenero = new JTextField();
    private final JTextField txtDuracao = new JTextField();
    private final JComboBox<String> cmbClassif = new JComboBox<>(new String[]{"L", "10", "12", "14", "16", "18"});

    private List<Filme> filmes = new ArrayList<>();
    private Integer idSelecionado = null;

    public PainelFilmes(FilmeController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selecionar();
        });
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel form = novoFormulario();
        form.add(new JLabel("Título:"));        form.add(txtTitulo);
        form.add(new JLabel("Gênero:"));        form.add(txtGenero);
        form.add(new JLabel("Duração (min):")); form.add(txtDuracao);
        form.add(new JLabel("Classificação:")); form.add(cmbClassif);

        JButton btnNovo = new JButton("Novo");
        JButton btnSalvar = new JButton("Salvar");
        JButton btnExcluir = new JButton("Excluir");
        btnNovo.addActionListener(e -> limpar());
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        add(montarLateral(form, btnNovo, btnSalvar, btnExcluir), BorderLayout.EAST);
    }

    @Override
    public void recarregar() {
        try {
            filmes = controller.listar();
            modelo.setRowCount(0);
            for (Filme f : filmes) {
                modelo.addRow(new Object[]{f.getId(), f.getTitulo(), f.getGenero(),
                        f.getDuracaoMin(), f.getClassificacao()});
            }
            limpar();
        } catch (Exception e) {
            Ui.erro(this, e);
        }
    }

    private void selecionar() {
        int linha = tabela.getSelectedRow();
        if (linha < 0 || linha >= filmes.size()) return;
        Filme f = filmes.get(linha);
        idSelecionado = f.getId();
        txtTitulo.setText(f.getTitulo());
        txtGenero.setText(f.getGenero());
        txtDuracao.setText(String.valueOf(f.getDuracaoMin()));
        cmbClassif.setSelectedItem(f.getClassificacao());
    }

    private void limpar() {
        tabela.clearSelection();
        idSelecionado = null;
        txtTitulo.setText("");
        txtGenero.setText("");
        txtDuracao.setText("");
        cmbClassif.setSelectedIndex(0);
    }

    private void salvar() {
        try {
            controller.salvar(idSelecionado, txtTitulo.getText(), txtGenero.getText(),
                    txtDuracao.getText(), (String) cmbClassif.getSelectedItem());
            recarregar();
        } catch (Exception e) {
            Ui.erro(this, e);
        }
    }

    private void excluir() {
        if (idSelecionado == null) {
            Ui.info(this, "Selecione um filme na tabela.");
            return;
        }
        if (!Ui.confirmar(this, "Excluir o filme selecionado?")) return;
        try {
            controller.excluir(idSelecionado);
            recarregar();
        } catch (Exception e) {
            Ui.erro(this, e);
        }
    }
}
