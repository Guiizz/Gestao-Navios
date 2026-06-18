package com.gestaonavios.gestaonavios.Model;

public class TipoNavio {
    private int id;
    private String designacao;
    private String categoria;
    private int maxCargasPorViagem;

    public TipoNavio() {}

    public TipoNavio(int id, String designacao, String categoria, int maxCargasPorViagem) {
        this.id = id;
        this.designacao = designacao;
        this.categoria = categoria;
        this.maxCargasPorViagem = maxCargasPorViagem;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDesignacao() { return designacao; }
    public void setDesignacao(String designacao) { this.designacao = designacao; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getMaxCargasPorViagem() { return maxCargasPorViagem; }
    public void setMaxCargasPorViagem(int maxCargasPorViagem) { this.maxCargasPorViagem = maxCargasPorViagem; }

    @Override
    public String toString() { return designacao; }
}