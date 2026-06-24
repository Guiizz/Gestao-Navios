package com.gestaonavios.gestaonavios.Model;

public class CompatibilidadeCarga {
    private int id;
    private TipoNavio tipoNavio;
    private TipoCarga tipoCarga;
    private int limiteCarga;

    public CompatibilidadeCarga() {
    }

    public CompatibilidadeCarga(int id, TipoNavio tipoNavio, TipoCarga tipoCarga, int limiteCarga) {
        this.id = id;
        this.tipoNavio = tipoNavio;
        this.tipoCarga = tipoCarga;
        this.limiteCarga = limiteCarga;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoNavio getTipoNavio() {
        return tipoNavio;
    }

    public void setTipoNavio(TipoNavio tipoNavio) {
        this.tipoNavio = tipoNavio;
    }

    public TipoCarga getTipoCarga() {
        return tipoCarga;
    }

    public void setTipoCarga(TipoCarga tipoCarga) {
        this.tipoCarga = tipoCarga;
    }

    public int getLimiteCarga() {
        return limiteCarga;
    }

    public void setLimiteCarga(int limiteCarga) {
        this.limiteCarga = limiteCarga;
    }

    @Override
    public String toString() {
        return tipoNavio + " -> " + tipoCarga + " (limite: " + limiteCarga + ")";
    }
}