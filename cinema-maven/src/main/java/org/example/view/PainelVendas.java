package org.example.view;

import org.example.controller.AssentoController;
import org.example.controller.IngressoController;
import org.example.controller.SessaoController;
import org.example.model.Assento;
import org.example.model.Sessao;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PainelVendas extends PainelBase {
    private static final Color LIVRE = new Color(39, 174, 96);
    private static final Color OCUPADO = new Color(192, 57, 43);

    private final SessaoController sessaoController;
    private final AssentoController assentoController;
    private final IngressoController ingressoController;
    private final JComboBox<Sessao> cmbSessao = new JComboBox<>();
    private final JPanel mapa = new JPanel();
    private final JLabel lblInfo = new JLabel(" ");
    private boolean carregando = false;

    public PainelVendas(SessaoController sessaoController, AssentoController assentoController,
                        IngressoController ingressoController) {
        this.sessaoController = sessaoController;
        this.assentoController = assentoController;
        this.ingressoController = ingressoController;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel topo = new JPanel(new BorderLayout(8, 0));
        topo.add(new JLabel("Sessão:"), BorderLayout.WEST);
        topo.add(cmbSessao, BorderLayout.CENTER);
        add(topo, BorderLayout.NORTH);

        JLabel tela = new JLabel("TELA", SwingConstants.CENTER);
        tela.setOpaque(true);
        tela.setBackground(Color.DARK_GRAY);
        tela.setForeground(Color.WHITE);
        tela.setPreferredSize(new Dimension(0, 28));

        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.add(tela, BorderLayout.NORTH);
        centro.add(mapa, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
        add(lblInfo, BorderLayout.SOUTH);

        cmbSessao.addActionListener(e -> {
            if (!carregando) montarMapa();
        });
    }

    @Override
    public void recarregar() {
        carregando = true;
        try {
            cmbSessao.removeAllItems();
            for (Sessao s : sessaoController.listar()) cmbSessao.addItem(s);
        } catch (Exception e) {
            Ui.erro(this, e);
        } finally {
            carregando = false;
        }
        montarMapa();
    }

    private void montarMapa() {
        mapa.removeAll();
        Sessao sessao = (Sessao) cmbSessao.getSelectedItem();
        if (sessao == null) {
            lblInfo.setText("Nenhuma sessão cadastrada.");
        } else {
            try {
                List<Assento> assentos = assentoController.listarPorSala(sessao.getSala());
                Set<Integer> ocupados = ingressoController.assentosOcupados(sessao);

                Map<String, List<Assento>> fileiras = new LinkedHashMap<>();
                for (Assento a : assentos) {
                    fileiras.computeIfAbsent(a.getFileira(), k -> new ArrayList<>()).add(a);
                }
                int colunas = fileiras.values().stream().mapToInt(List::size).max().orElse(1);
                mapa.setLayout(new GridLayout(Math.max(1, fileiras.size()), colunas, 6, 6));

                for (List<Assento> linha : fileiras.values()) {
                    for (int i = 0; i < colunas; i++) {
                        if (i >= linha.size()) {
                            mapa.add(new JLabel());
                            continue;
                        }
                        Assento a = linha.get(i);
                        boolean ocupado = ocupados.contains(a.getId());
                        JButton b = new JButton(a.getRotulo());
                        b.setOpaque(true);
                        b.setBorderPainted(false);
                        b.setForeground(Color.WHITE);
                        b.setBackground(ocupado ? OCUPADO : LIVRE);
                        b.addActionListener(e -> clicar(sessao, a, ocupado));
                        mapa.add(b);
                    }
                }
                lblInfo.setText("Preço: " + Ui.moeda(sessao.getPreco())
                        + "   |   Vendidos: " + ocupados.size() + " de " + assentos.size()
                        + "   |   Verde = livre, vermelho = ocupado (clique para vender / cancelar)");
            } catch (Exception e) {
                Ui.erro(this, e);
            }
        }
        mapa.revalidate();
        mapa.repaint();
    }

    private void clicar(Sessao sessao, Assento assento, boolean ocupado) {
        try {
            if (ocupado) {
                if (Ui.confirmar(this, "Cancelar o ingresso do assento " + assento.getRotulo() + "?")) {
                    ingressoController.cancelar(sessao, assento);
                }
            } else {
                if (Ui.confirmar(this, "Vender o assento " + assento.getRotulo() + " por "
                        + Ui.moeda(sessao.getPreco()) + "?")) {
                    ingressoController.vender(sessao, assento);
                }
            }
        } catch (Exception e) {
            Ui.erro(this, e);
        }
        montarMapa();
    }
}
