package org.example.controller;

import org.example.util.Console;
import org.example.view.*;


public class MenuPrincipalController {
    private final FilmeController filmeController = new FilmeController();
    private final SalaController salaController = new SalaController();
    private final AssentoController assentoController = new AssentoController();
    private final SessaoController sessaoController = new SessaoController();
    private final IngressoController ingressoController = new IngressoController();

    public void iniciar() {
        Console.info("Iniciando o sistema de cinema...");
        String[] titulos = {"Filmes", "Salas", "Sessões", "Venda de ingressos", "Faturamento"};
        PainelBase[] paineis = {
                new PainelFilmes(filmeController),
                new PainelSalas(salaController),
                new PainelSessoes(sessaoController, filmeController, salaController),
                new PainelVendas(sessaoController, assentoController, ingressoController),
                new PainelRelatorio(ingressoController)
        };
        new JanelaPrincipal(titulos, paineis).setVisible(true);
    }
}
