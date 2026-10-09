package org.example.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class Console {
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Console() {}

    public static void info(String mensagem) {
        System.out.println("[" + LocalTime.now().format(HORA) + "] INFO  " + mensagem);
    }

    public static void erro(String mensagem, Throwable causa) {
        System.err.println("[" + LocalTime.now().format(HORA) + "] ERRO  " + mensagem);
        if (causa != null) causa.printStackTrace();
    }
}
