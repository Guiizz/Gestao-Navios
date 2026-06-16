package Model;

import Model.enums.FuncaoTripulante;
import java.time.LocalDate;

public class TripulacaoViagem {
    private int id;
    private Tripulante tripulante;
    private FuncaoTripulante funcaoNaViagem;
    private LocalDate dataEmbarque;
    private LocalDate dataDesembarque;

    public TripulacaoViagem() {}

    public TripulacaoViagem(int id, Tripulante tripulante, FuncaoTripulante funcaoNaViagem,
                            LocalDate dataEmbarque, LocalDate dataDesembarque) {
        this.id = id;
        this.tripulante = tripulante;
        this.funcaoNaViagem = funcaoNaViagem;
        this.dataEmbarque = dataEmbarque;
        this.dataDesembarque = dataDesembarque;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Tripulante getTripulante() { return tripulante; }
    public void setTripulante(Tripulante tripulante) { this.tripulante = tripulante; }

    public FuncaoTripulante getFuncaoNaViagem() { return funcaoNaViagem; }
    public void setFuncaoNaViagem(FuncaoTripulante funcaoNaViagem) { this.funcaoNaViagem = funcaoNaViagem; }

    public LocalDate getDataEmbarque() { return dataEmbarque; }
    public void setDataEmbarque(LocalDate dataEmbarque) { this.dataEmbarque = dataEmbarque; }

    public LocalDate getDataDesembarque() { return dataDesembarque; }
    public void setDataDesembarque(LocalDate dataDesembarque) { this.dataDesembarque = dataDesembarque; }

    @Override
    public String toString() {
        return tripulante + " | " + funcaoNaViagem + " | Embarque: " + dataEmbarque + " | Desembarque: " + dataDesembarque;
    }
}