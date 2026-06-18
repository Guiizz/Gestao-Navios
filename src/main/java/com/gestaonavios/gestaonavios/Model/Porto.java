package com.gestaonavios.gestaonavios.Model;

public class Porto {

    private int id;
    private String nome;
    private String pais;
    private String codigoUNLOCODE;

    public Porto() {}

    public Porto(int id, String nome, String pais, String codigoUNLOCODE) {
        this.id = id;
        this.nome = nome;
        this.pais = pais;
        this.codigoUNLOCODE = codigoUNLOCODE;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getCodigoUNLOCODE() { return codigoUNLOCODE; }
    public void setCodigoUNLOCODE(String codigoUNLOCODE) { this.codigoUNLOCODE = codigoUNLOCODE; }

    @Override
    public String toString() {
        return nome + " (" + codigoUNLOCODE + ")";
    }
}