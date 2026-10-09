package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sessao {
    public static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int id;
    private Filme filme;
    private Sala sala;
    private LocalDateTime dataHora;
    private BigDecimal preco;

    public Sessao() {}

    public Sessao(int id, Filme filme, Sala sala, LocalDateTime dataHora, BigDecimal preco) {
        this.id = id;
        this.filme = filme;
        this.sala = sala;
        this.dataHora = dataHora;
        this.preco = preco;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Filme getFilme() { return filme; }
    public void setFilme(Filme filme) { this.filme = filme; }
    public Sala getSala() { return sala; }
    public void setSala(Sala sala) { this.sala = sala; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    @Override public String toString() {
        return filme.getTitulo() + "  |  " + sala.getNome() + "  |  " + dataHora.format(FORMATO);
    }
}
