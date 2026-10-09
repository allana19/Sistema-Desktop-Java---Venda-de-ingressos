package org.example.view;

import javax.swing.*;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import org.example.util.Console;

/** mensagens e formatação */
public final class Ui {
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    private Ui() {}

    public static void erro(Component pai, Exception e) {
        if (!(e instanceof IllegalArgumentException)) Console.erro("Falha de banco/sistema", e);
        String msg = (e instanceof IllegalArgumentException)
                ? e.getMessage()
                : "Erro ao acessar o banco de dados:\n" + e.getMessage();
        JOptionPane.showMessageDialog(pai, msg, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    public static void info(Component pai, String msg) {
        JOptionPane.showMessageDialog(pai, msg, "Cinema", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirmar(Component pai, String msg) {
        return JOptionPane.showConfirmDialog(pai, msg, "Confirmar", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION;
    }

    public static String moeda(BigDecimal valor) {
        return MOEDA.format(valor);
    }
}
