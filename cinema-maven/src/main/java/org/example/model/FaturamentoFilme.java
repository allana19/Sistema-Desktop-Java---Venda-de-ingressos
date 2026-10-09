package org.example.model;

import java.math.BigDecimal;

/** Resultado do relatório de faturamento  */
public record FaturamentoFilme(String titulo, int ingressos, BigDecimal total) {}
