package org.example.model;

import java.util.Objects;

public class Sala {
    private int id;
    private String nome;
    private int capacidade;

    public Sala() {}

    public Sala(int id, String nome, int capacidade) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getCapacidade() { return capacidade; }
    public void setCapacidade(int capacidade) { this.capacidade = capacidade; }

    @Override public String toString() { return nome; }
    @Override public boolean equals(Object o) { return o instanceof Sala s && s.id == id; }
    @Override public int hashCode() { return Objects.hash(id); }
}
