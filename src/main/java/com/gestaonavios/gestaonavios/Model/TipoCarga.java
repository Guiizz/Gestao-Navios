package Model;

import Model.enums.TipoCargaEnums;

public class TipoCarga {
    private int id;
    private String designacao;
    private TipoCargaEnums categoria;
    private boolean inflamavel;
    private boolean corrosiva;
    private boolean toxica;

    public TipoCarga() {}

    public TipoCarga(int id, String designacao, TipoCargaEnums categoria,
                     boolean inflamavel, boolean corrosiva, boolean toxica) {
        this.id = id;
        this.designacao = designacao;
        this.categoria = categoria;
        this.inflamavel = inflamavel;
        this.corrosiva = corrosiva;
        this.toxica = toxica;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDesignacao() { return designacao; }
    public void setDesignacao(String designacao) { this.designacao = designacao; }

    public TipoCargaEnums getCategoria() { return categoria; }
    public void setCategoria(TipoCargaEnums categoria) { this.categoria = categoria; }

    public boolean isInflamavel() { return inflamavel; }
    public void setInflamavel(boolean inflamavel) { this.inflamavel = inflamavel; }

    public boolean isCorrosiva() { return corrosiva; }
    public void setCorrosiva(boolean corrosiva) { this.corrosiva = corrosiva; }

    public boolean isToxica() { return toxica; }
    public void setToxica(boolean toxica) { this.toxica = toxica; }

    @Override
    public String toString() { return designacao + " (" + categoria + ")"; }
}