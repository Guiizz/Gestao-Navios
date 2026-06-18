package com.gestaonavios.gestaonavios.Model;

public class Carga {
    private int id;
    private String designacao;
    private TipoCarga tipoCarga;
    private double volume;
    private double peso;
    private Porto portoCarga;
    private Porto portoDescarga;

    public Carga() {}

    public Carga(int id, String designacao, TipoCarga tipoCarga,
                 double volume, double peso, Porto portoCarga, Porto portoDescarga) {
        this.id = id;
        this.designacao = designacao;
        this.tipoCarga = tipoCarga;
        this.volume = volume;
        this.peso = peso;
        this.portoCarga = portoCarga;
        this.portoDescarga = portoDescarga;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDesignacao() { return designacao; }
    public void setDesignacao(String designacao) { this.designacao = designacao; }

    public TipoCarga getTipoCarga() { return tipoCarga; }
    public void setTipoCarga(TipoCarga tipoCarga) { this.tipoCarga = tipoCarga; }

    public double getVolume() { return volume; }
    public void setVolume(double volume) { this.volume = volume; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public Porto getPortoCarga() { return portoCarga; }
    public void setPortoCarga(Porto portoCarga) { this.portoCarga = portoCarga; }

    public Porto getPortoDescarga() { return portoDescarga; }
    public void setPortoDescarga(Porto portoDescarga) { this.portoDescarga = portoDescarga; }

    @Override
    public String toString() {
        return "[" + id + "] " + designacao + " (" + tipoCarga + ")";
    }
}