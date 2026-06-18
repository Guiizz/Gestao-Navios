package com.gestaonavios.gestaonavios.Controller;

import com.gestaonavios.gestaonavios.BLL.CargaBLL;
import com.gestaonavios.gestaonavios.BLL.NavioBLL;
import com.gestaonavios.gestaonavios.BLL.PortoBLL;
import com.gestaonavios.gestaonavios.BLL.TripulanteBLL;
import com.gestaonavios.gestaonavios.BLL.ViagemBLL;
import com.gestaonavios.gestaonavios.Model.AtribuicaoCarga;
import com.gestaonavios.gestaonavios.Model.Carga;
import com.gestaonavios.gestaonavios.Model.Navio;
import com.gestaonavios.gestaonavios.Model.Porto;
import com.gestaonavios.gestaonavios.Model.Tripulante;
import com.gestaonavios.gestaonavios.Model.TripulacaoViagem;
import com.gestaonavios.gestaonavios.Model.Viagem;
import com.gestaonavios.gestaonavios.Model.enums.EstadoViagem;
import com.gestaonavios.gestaonavios.Model.enums.FuncaoTripulante;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ViagemController {

    private final ViagemBLL     viagemBLL;
    private final NavioBLL      navioBLL;
    private final PortoBLL      portoBLL;
    private final CargaBLL      cargaBLL;
    private final TripulanteBLL tripulanteBLL;

    public ViagemController(ViagemBLL viagemBLL, NavioBLL navioBLL,
                            PortoBLL portoBLL, CargaBLL cargaBLL,
                            TripulanteBLL tripulanteBLL) {
        this.viagemBLL     = viagemBLL;
        this.navioBLL      = navioBLL;
        this.portoBLL      = portoBLL;
        this.cargaBLL      = cargaBLL;
        this.tripulanteBLL = tripulanteBLL;
    }

    public List<Viagem> listarTodos() {
        return viagemBLL.listarTodos();
    }

    public List<Viagem> listarPorEstado(EstadoViagem estado) {
        return viagemBLL.listarPorEstado(estado);
    }

    /** Viagens que ainda podem avançar ou ser canceladas (PLANEADA ou EM_CURSO). */
    public List<Viagem> listarViagensAtivas() {
        List<Viagem> resultado = new ArrayList<>();
        for (Viagem v : viagemBLL.listarTodos()) {
            if (v.getEstado() == EstadoViagem.PLANEADA || v.getEstado() == EstadoViagem.EM_CURSO)
                resultado.add(v);
        }
        return resultado;
    }

    /** Viagens apenas no estado PLANEADA (editáveis, com cargas/tripulantes modificáveis). */
    public List<Viagem> listarViagensPlaneadas() {
        return viagemBLL.listarPorEstado(EstadoViagem.PLANEADA);
    }

    public List<Navio> listarNavios() {
        return navioBLL.listarTodos();
    }

    public List<Navio> listarNaviosDisponiveis() {
        return navioBLL.listarDisponiveis();
    }

    public List<Porto> listarPortos() {
        return portoBLL.listarTodos();
    }

    public List<Carga> listarCargas() {
        return cargaBLL.listarTodos();
    }

    public List<Tripulante> listarTripulantesDisponiveis() {
        return tripulanteBLL.listarDisponiveis();
    }

    public void criarViagem(Navio navio, Porto origem, Porto destino,
                            LocalDate partida, LocalDate chegada, String observacoes) throws Exception {
        viagemBLL.criarViagem(new Viagem(0, partida, chegada, null, origem, destino, observacoes, navio));
    }

    public void avancarEstado(int idViagem, LocalDate dataChegadaReal) throws Exception {
        viagemBLL.avancarEstado(idViagem, dataChegadaReal);
    }

    public void cancelarViagem(int idViagem) throws Exception {
        viagemBLL.cancelarViagem(idViagem);
    }

    public void adicionarCarga(int idViagem, Carga carga, double peso, double volume) throws Exception {
        viagemBLL.adicionarCarga(idViagem, new AtribuicaoCarga(0, carga, volume, peso, null));
    }

    public void adicionarTripulante(int idViagem, Tripulante tripulante,
                                    FuncaoTripulante funcao, LocalDate embarque, LocalDate desembarque) throws Exception {
        viagemBLL.adicionarTripulante(idViagem,
                new TripulacaoViagem(0, tripulante, funcao, embarque, desembarque));
    }

    public void removerCargaViagem(int idViagem, int idCarga) throws Exception {
        viagemBLL.removerCargaViagem(idViagem, idCarga);
    }

    public void removerTripulanteViagem(int idViagem, int idTripulante) throws Exception {
        viagemBLL.removerTripulanteViagem(idViagem, idTripulante);
    }

    public void editarViagem(int idViagem, LocalDate novaPartida,
                             LocalDate novaChegada, String observacoes) throws Exception {
        viagemBLL.editarViagem(idViagem, novaPartida, novaChegada, observacoes);
    }

    public boolean remover(int idViagem) {
        return viagemBLL.remover(idViagem);
    }
}
