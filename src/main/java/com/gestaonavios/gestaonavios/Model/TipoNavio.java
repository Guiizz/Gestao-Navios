package Model;

import Model.enums.TipoNavioEnums;

public class TipoNavio {
    private int id;
    private String designacao;
    private TipoNavioEnums categoria;
    private int maxCargasPorViagem;

    public TipoNavio() {}

    public TipoNavio(int id, String designacao, TipoNavioEnums categoria, int maxCargasPorViagem) {
        this.id = id;
        this.designacao = designacao;
        this.categoria = categoria;
        this.maxCargasPorViagem = maxCargasPorViagem;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDesignacao() { return designacao; }
    public void setDesignacao(String designacao) { this.designacao = designacao; }

    public TipoNavioEnums getCategoria() { return categoria; }
    public void setCategoria(TipoNavioEnums categoria) { this.categoria = categoria; }

    public int getMaxCargasPorViagem() { return maxCargasPorViagem; }
    public void setMaxCargasPorViagem(int maxCargasPorViagem) { this.maxCargasPorViagem = maxCargasPorViagem; }

    @Override
    public String toString() { return designacao; }
}