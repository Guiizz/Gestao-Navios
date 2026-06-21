package com.gestaonavios.gestaonavios.Model;


public class AtribuicaoCarga {
    private int id;
    private Carga carga;
    private double volumeAtribuido;
    private double pesoAtribuido;
    private Tanque tanque;

    public AtribuicaoCarga() {
    }

    public AtribuicaoCarga(int id, Carga carga, double volumeAtribuido,
                           double pesoAtribuido, Tanque tanque) {
        this.id = id;
        this.carga = carga;
        this.volumeAtribuido = volumeAtribuido;
        this.pesoAtribuido = pesoAtribuido;
        this.tanque = tanque;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Carga getCarga() {
        return carga;
    }

    public void setCarga(Carga carga) {
        this.carga = carga;
    }

    public double getVolumeAtribuido() {
        return volumeAtribuido;
    }

    public void setVolumeAtribuido(double volumeAtribuido) {
        this.volumeAtribuido = volumeAtribuido;
    }

    public double getPesoAtribuido() {
        return pesoAtribuido;
    }

    public void setPesoAtribuido(double pesoAtribuido) {
        this.pesoAtribuido = pesoAtribuido;
    }

    public Tanque getTanque() {
        return tanque;
    }

    public void setTanque(Tanque tanque) {
        this.tanque = tanque;
    }
}
