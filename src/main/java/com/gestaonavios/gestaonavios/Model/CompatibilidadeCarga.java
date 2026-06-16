package Model;

import Model.enums.TipoCargaEnums;
import Model.enums.TipoNavioEnums;

public class CompatibilidadeCarga {
    private int id;
    private TipoNavioEnums tipoNavio;
    private TipoCargaEnums tipoCarga;
    private double limiteCarga;

    public CompatibilidadeCarga() {}

    public CompatibilidadeCarga(int id, TipoNavioEnums tipoNavio, TipoCargaEnums tipoCarga, double limiteCarga) {
        this.id = id;
        this.tipoNavio = tipoNavio;
        this.tipoCarga = tipoCarga;
        this.limiteCarga = limiteCarga;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public TipoNavioEnums getTipoNavio() { return tipoNavio; }
    public void setTipoNavio(TipoNavioEnums tipoNavio) { this.tipoNavio = tipoNavio; }

    public TipoCargaEnums getTipoCarga() { return tipoCarga; }
    public void setTipoCarga(TipoCargaEnums tipoCarga) { this.tipoCarga = tipoCarga; }

    public double getLimiteCarga() { return limiteCarga; }
    public void setLimiteCarga(double limiteCarga) { this.limiteCarga = limiteCarga; }

    @Override
    public String toString() {
        return tipoNavio + " -> " + tipoCarga + " (limite: " + limiteCarga + ")";
    }
}