package org.example.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.example.model.Sessao;


public abstract class BaseController {

    /** @param campo ex.: "o título do filme" -> "Informe o título do filme." */
    protected String exigirTexto(String valor, String campo) {
        String texto = valor == null ? "" : valor.trim();
        if (texto.isEmpty()) throw new IllegalArgumentException("Informe " + campo + ".");
        return texto;
    }

    protected int lerInteiro(String valor, String campo) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(capitalizar(campo) + " deve ser um número inteiro.");
        }
    }

    protected BigDecimal lerDecimal(String valor, String campo) {
        try {
            return new BigDecimal(valor.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(capitalizar(campo) + " inválido. Exemplo: 32,00");
        }
    }

    protected LocalDateTime lerDataHora(String valor) {
        try {
            return LocalDateTime.parse(valor.trim(), Sessao.FORMATO);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Data/hora inválida. Use o formato dd/MM/aaaa HH:mm (ex.: 10/10/2026 19:00).");
        }
    }

    private String capitalizar(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
