package com.gestaonavios.gestaonavios.Model;

public class Tanque {
    private int id;
    private int numero;
    private double capacidade;
    private int idNavio;

    public Tanque() {
    }

    public Tanque(int id, int numero, double capacidade, int idNavio) {
        this.id = id;
        this.numero = numero;
        this.capacidade = capacidade;
        this.idNavio = idNavio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public double getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(double capacidade) {
        this.capacidade = capacidade;
    }

    public int getIdNavio() {
        return idNavio;
    }

    public void setIdNavio(int idNavio) {
        this.idNavio = idNavio;
    }

    @Override
    public String toString() {
        return "Tanque #" + numero;
    }
}
