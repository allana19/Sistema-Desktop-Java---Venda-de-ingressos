package org.example.model;

public class Assento {
    private int id;
    private int idSala;
    private String fileira;
    private int numero;

    public Assento() {}

    public Assento(int id, int idSala, String fileira, int numero) {
        this.id = id;
        this.idSala = idSala;
        this.fileira = fileira;
        this.numero = numero;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdSala() { return idSala; }
    public void setIdSala(int idSala) { this.idSala = idSala; }
    public String getFileira() { return fileira; }
    public void setFileira(String fileira) { this.fileira = fileira; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    /** Ex.: "A1", "B3" */
    public String getRotulo() { return fileira + numero; }

    @Override public String toString() { return getRotulo(); }
}
