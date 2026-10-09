package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Ingresso {
    private int id;
    private int idSessao;
    private int idAssento;
    private BigDecimal valorPago;
    private LocalDateTime dataVenda;

    public Ingresso() {}

    public Ingresso(int id, int idSessao, int idAssento, BigDecimal valorPago, LocalDateTime dataVenda) {
        this.id = id;
        this.idSessao = idSessao;
        this.idAssento = idAssento;
        this.valorPago = valorPago;
        this.dataVenda = dataVenda;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdSessao() { return idSessao; }
    public void setIdSessao(int idSessao) { this.idSessao = idSessao; }
    public int getIdAssento() { return idAssento; }
    public void setIdAssento(int idAssento) { this.idAssento = idAssento; }
    public BigDecimal getValorPago() { return valorPago; }
    public void setValorPago(BigDecimal valorPago) { this.valorPago = valorPago; }
    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }
}
