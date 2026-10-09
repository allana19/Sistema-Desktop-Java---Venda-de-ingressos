package org.example.model;

import java.util.Objects;

public class Filme {
    private int id;
    private String titulo;
    private String genero;
    private int duracaoMin;
    private String classificacao;

    public Filme() {}

    public Filme(int id, String titulo, String genero, int duracaoMin, String classificacao) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.duracaoMin = duracaoMin;
        this.classificacao = classificacao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public int getDuracaoMin() { return duracaoMin; }
    public void setDuracaoMin(int duracaoMin) { this.duracaoMin = duracaoMin; }
    public String getClassificacao() { return classificacao; }
    public void setClassificacao(String classificacao) { this.classificacao = classificacao; }

    @Override public String toString() { return titulo; }
    @Override public boolean equals(Object o) { return o instanceof Filme f && f.id == id; }
    @Override public int hashCode() { return Objects.hash(id); }
}
