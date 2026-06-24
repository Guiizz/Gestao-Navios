package com.gestaonavios.gestaonavios.Model;

import com.gestaonavios.gestaonavios.Model.enums.EstadoOperacional;
import com.gestaonavios.gestaonavios.Model.enums.TipoNavioEnums;

public class Navio {

    private int id;
    private String nome;
    private String codigoIMO;
    private TipoNavioEnums tipoNavioEnums;
    private double capacidadeMaxima;
    private int numeroTanques;
    private String bandeira;
    private int anoFabrico;
    private EstadoOperacional estadoOperacional;
    private Porto portoAtual;

    public Navio() {
    }

    public Navio(int id, String nome, String codigoIMO, TipoNavioEnums tipoNavioEnums, double capacidadeMaxima, int numeroTanques, String bandeira, int anoFabrico, EstadoOperacional estadoOperacional, Porto portoAtual) {
        this.id = id;
        this.nome = nome;
        this.codigoIMO = codigoIMO;
        this.tipoNavioEnums = tipoNavioEnums;
        this.capacidadeMaxima = capacidadeMaxima;
        this.numeroTanques = numeroTanques;
        this.bandeira = bandeira;
        this.anoFabrico = anoFabrico;
        this.estadoOperacional = estadoOperacional;
        this.portoAtual = portoAtual;
    }

    public boolean podeIniciarViagem() {
        return estadoOperacional == EstadoOperacional.ATIVO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoIMO() {
        return codigoIMO;
    }

    public void setCodigoIMO(String codigoIMO) {
        this.codigoIMO = codigoIMO;
    }

    public TipoNavioEnums getTipoNavio() {
        return tipoNavioEnums;
    }

    public void setTipoNavio(TipoNavioEnums tipoNavioEnums) {
        this.tipoNavioEnums = tipoNavioEnums;
    }

    public double getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(double capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public int getNumeroTanques() {
        return numeroTanques;
    }

    public void setNumeroTanques(int numeroTanques) {
        this.numeroTanques = numeroTanques;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public int getAnoFabrico() {
        return anoFabrico;
    }

    public void setAnoFabrico(int anoFabrico) {
        this.anoFabrico = anoFabrico;
    }

    public EstadoOperacional getEstadoOperacional() {
        return estadoOperacional;
    }

    public void setEstadoOperacional(EstadoOperacional e) {
        this.estadoOperacional = e;
    }

    public Porto getPortoAtual() {
        return portoAtual;
    }

    public void setPortoAtual(Porto portoAtual) {
        this.portoAtual = portoAtual;
    }

    public boolean isDisponivel() {
        return estadoOperacional == EstadoOperacional.ATIVO;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nome + " (IMO: " + codigoIMO + ") - " + tipoNavioEnums;
    }
}