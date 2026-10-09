package org.example.view;

import org.example.controller.FilmeController;
import org.example.controller.SalaController;
import org.example.controller.SessaoController;
import org.example.model.Filme;
import org.example.model.Sala;
import org.example.model.Sessao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PainelSessoes extends PainelBase {
    private final SessaoController controller;
    private final FilmeController filmeController;
    private final SalaController salaController;
    private final DefaultTableModel modelo = modeloSomenteLeitura("ID", "Filme", "Sala", "Data/hora", "Preço");
    private final JTable tabela = new JTable(modelo);

    private final JComboBox<Filme> cmbFilme = new JComboBox<>();
    private final JComboBox<Sala> cmbSala = new JComboBox<>();
    private final JTextField txtDataHora = new JTextField();
    private final JTextField txtPreco = new JTextField();

    private List<Sessao> sessoes = new ArrayList<>();
    private Integer idSelecionado = null;

    public PainelSessoes(SessaoController controller, FilmeController filmeController, SalaController salaController) {
        this.controller = controller;
        this.filmeController = filmeController;
        this.salaController = salaController;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selecionar();
        });
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        txtDataHora.setToolTipText("dd/MM/aaaa HH:mm  (ex.: 10/10/2026 19:00)");
        JPanel form = novoFormulario();
        form.add(new JLabel("Filme:"));              form.add(cmbFilme);
        form.add(new JLabel("Sala:"));               form.add(cmbSala);
        form.add(new JLabel("Data/hora:"));          form.add(txtDataHora);
        form.add(new JLabel("Preço (R$):"));         form.add(txtPreco);

        JLabel dica = new JLabel("<html><i>Formato da data/hora: dd/MM/aaaa HH:mm (ex.: 10/10/2026 19:00)</i></html>");
        JPanel formCompleto = new JPanel(new BorderLayout(0, 8));
        formCompleto.add(form, BorderLayout.NORTH);
        formCompleto.add(dica, BorderLayout.CENTER);

        JButton btnNova = new JButton("Nova");
        JButton btnSalvar = new JButton("Salvar");
        JButton btnExcluir = new JButton("Excluir");
        btnNova.addActionListener(e -> limpar());
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        add(montarLateral(formCompleto, btnNova, btnSalvar, btnExcluir), BorderLayout.EAST);
    }

    @Override
    public void recarregar() {
        try {
            cmbFilme.removeAllItems();
            for (Filme f : filmeController.listar()) cmbFilme.addItem(f);
            cmbSala.removeAllItems();
            for (Sala s : salaController.listar()) cmbSala.addItem(s);

            sessoes = controller.listar();
            modelo.setRowCount(0);
            for (Sessao s : sessoes) {
                modelo.addRow(new Object[]{s.getId(), s.getFilme().getTitulo(), s.getSala().getNome(),
                        s.getDataHora().format(Sessao.FORMATO), Ui.moeda(s.getPreco())});
            }
            limpar();
        } catch (Exception e) {
            Ui.erro(this, e);
        }
    }

    private void selecionar() {
        int linha = tabela.getSelectedRow();
        if (linha < 0 || linha >= sessoes.size()) return;
        Sessao s = sessoes.get(linha);
        idSelecionado = s.getId();
        cmbFilme.setSelectedItem(s.getFilme());   // equals() compara pelo id
        cmbSala.setSelectedItem(s.getSala());
        txtDataHora.setText(s.getDataHora().format(Sessao.FORMATO));
        txtPreco.setText(s.getPreco().toPlainString());
    }

    private void limpar() {
        tabela.clearSelection();
        idSelecionado = null;
        if (cmbFilme.getItemCount() > 0) cmbFilme.setSelectedIndex(0);
        if (cmbSala.getItemCount() > 0) cmbSala.setSelectedIndex(0);
        txtDataHora.setText("");
        txtPreco.setText("");
    }

    private void salvar() {
        try {
            controller.salvar(idSelecionado, (Filme) cmbFilme.getSelectedItem(),
                    (Sala) cmbSala.getSelectedItem(), txtDataHora.getText(), txtPreco.getText());
            recarregar();
        } catch (Exception e) {
            Ui.erro(this, e);
        }
    }

    private void excluir() {
        if (idSelecionado == null) {
            Ui.info(this, "Selecione uma sessão na tabela.");
            return;
        }
        if (!Ui.confirmar(this, "Excluir a sessão selecionada?\nOs ingressos vendidos para ela também serão removidos.")) return;
        try {
            controller.excluir(idSelecionado);
            recarregar();
        } catch (Exception e) {
            Ui.erro(this, e);
        }
    }
}
