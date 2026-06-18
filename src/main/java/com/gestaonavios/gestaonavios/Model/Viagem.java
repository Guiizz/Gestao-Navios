package com.gestaonavios.gestaonavios.Model;

import com.gestaonavios.gestaonavios.Model.enums.EstadoViagem;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Viagem {
    private int id;
    private LocalDate dataPartida;
    private LocalDate dataChegadaPrevista;
    private LocalDate dataChegadaReal;
    private EstadoViagem estado;
    private Porto origem;
    private Porto destino;
    private String observacoes;
    private Navio navio;
    private List<TripulacaoViagem> tripulacao;
    private List<AtribuicaoCarga> cargas;

    public Viagem() {
        this.tripulacao = new ArrayList<>();
        this.cargas = new ArrayList<>();
    }

    public Viagem(int id, LocalDate dataPartida, LocalDate dataChegadaPrevista,
                  EstadoViagem estado, Porto origem, Porto destino,
                  String observacoes, Navio navio) {
        this.id = id;
        this.dataPartida = dataPartida;
        this.dataChegadaPrevista = dataChegadaPrevista;
        this.estado = estado;
        this.origem = origem;
        this.destino = destino;
        this.observacoes = observacoes;
        this.navio = navio;
        this.tripulacao = new ArrayList<>();
        this.cargas = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getDataPartida() { return dataPartida; }
    public void setDataPartida(LocalDate dataPartida) { this.dataPartida = dataPartida; }

    public LocalDate getDataChegadaPrevista() { return dataChegadaPrevista; }
    public void setDataChegadaPrevista(LocalDate d) { this.dataChegadaPrevista = d; }

    public LocalDate getDataChegadaReal() { return dataChegadaReal; }
    public void setDataChegadaReal(LocalDate dataChegadaReal) { this.dataChegadaReal = dataChegadaReal; }

    public EstadoViagem getEstado() { return estado; }
    public void setEstado(EstadoViagem estado) { this.estado = estado; }

    public Porto getOrigem() { return origem; }
    public void setOrigem(Porto origem) { this.origem = origem; }

    public Porto getDestino() { return destino; }
    public void setDestino(Porto destino) { this.destino = destino; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Navio getNavio() { return navio; }
    public void setNavio(Navio navio) { this.navio = navio; }

    public List<TripulacaoViagem> getTripulacao() { return tripulacao; }
    public void setTripulacao(List<TripulacaoViagem> tripulacao) { this.tripulacao = tripulacao; }

    public List<AtribuicaoCarga> getCargas() { return cargas; }
    public void setCargas(List<AtribuicaoCarga> cargas) { this.cargas = cargas; }

    public double getPesoTotalCargas() {
        double total = 0;
        for (AtribuicaoCarga ac : cargas) {
            total += ac.getPesoAtribuido();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Viagem #" + id + " [" + origem + " -> " + destino + "] " + estado;
    }
}
