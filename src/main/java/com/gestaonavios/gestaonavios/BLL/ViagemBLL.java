package com.gestaonavios.gestaonavios.BLL;

import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.AtribuicaoCarga;
import com.gestaonavios.gestaonavios.Model.Carga;
import com.gestaonavios.gestaonavios.Model.Navio;
import com.gestaonavios.gestaonavios.Model.Tripulante;
import com.gestaonavios.gestaonavios.Model.TripulacaoViagem;
import com.gestaonavios.gestaonavios.Model.Viagem;
import com.gestaonavios.gestaonavios.Model.enums.EstadoViagem;
import com.gestaonavios.gestaonavios.Utils.ValidacaoUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ViagemBLL {

    private final ViagemDAL viagemDAL;
    private final NavioBLL navioBLL;
    private final TripulanteBLL tripulanteBLL;
    private final CompatibilidadeBLL compatibilidadeBLL;

    public ViagemBLL(ViagemDAL viagemDAL, NavioBLL navioBLL, TripulanteBLL tripulanteBLL,
                     CompatibilidadeBLL compatibilidadeBLL) {
        this.viagemDAL = viagemDAL;
        this.navioBLL = navioBLL;
        this.tripulanteBLL = tripulanteBLL;
        this.compatibilidadeBLL = compatibilidadeBLL;
    }

    public List<Viagem> listarTodos() {
        return viagemDAL.listarTodos();
    }

    public Viagem buscarPorId(int id) {
        return viagemDAL.buscarPorId(id);
    }

    public List<Viagem> listarPorEstado(EstadoViagem estado) {
        List<Viagem> resultado = new ArrayList<>();
        for (Viagem v : viagemDAL.listarTodos())
            if (v.getEstado() == estado) resultado.add(v);
        return resultado;
    }

    public List<Viagem> listarPorTripulante(int idTripulante) {
        return viagemDAL.listarPorTripulante(idTripulante);
    }

    public void criarViagem(Viagem viagem) throws Exception {
        ValidacaoUtils.exigirObjeto(viagem.getNavio(), "O navio");
        ValidacaoUtils.exigirObjeto(viagem.getOrigem(), "O porto de origem");
        ValidacaoUtils.exigirObjeto(viagem.getDestino(), "O porto de destino");
        ValidacaoUtils.exigirObjeto(viagem.getDataPartida(), "A data de partida");
        ValidacaoUtils.exigirObjeto(viagem.getDataChegadaPrevista(), "A data prevista de chegada");

        if (viagem.getOrigem().getId() == viagem.getDestino().getId())
            throw new Exception("O porto de origem e o porto de destino não podem ser iguais.");
        if (viagem.getDataPartida().isBefore(LocalDate.now()))
            throw new Exception("A data de partida não pode ser no passado.");
        if (!viagem.getDataChegadaPrevista().isAfter(viagem.getDataPartida()))
            throw new Exception("A data de chegada prevista tem de ser posterior à data de partida.");
        if (!navioBLL.podeIniciarViagem(viagem.getNavio()))
            throw new Exception("O navio '" + viagem.getNavio().getNome()
                    + "' não está disponível (inativo, em manutenção ou já tem viagem ativa).");

        viagem.setEstado(EstadoViagem.PLANEADA);
        viagemDAL.adicionar(viagem);
    }

    public void avancarEstado(int idViagem, LocalDate dataChegadaReal) throws Exception {
        Viagem viagem = viagemDAL.buscarPorId(idViagem);
        ValidacaoUtils.exigirExistencia(viagem, "Viagem", idViagem);

        switch (viagem.getEstado()) {
            case PLANEADA:
                viagem.setEstado(EstadoViagem.EM_CURSO);
                break;
            case EM_CURSO:
                LocalDate chegada = (dataChegadaReal != null) ? dataChegadaReal : LocalDate.now();
                if (viagem.getDataPartida() != null && chegada.isBefore(viagem.getDataPartida()))
                    throw new Exception("A data de chegada real não pode ser anterior à data de partida ("
                            + viagem.getDataPartida() + ").");
                viagem.setEstado(EstadoViagem.CONCLUIDA);
                viagem.setDataChegadaReal(chegada);
                Navio navio = viagem.getNavio();
                if (navio != null && viagem.getDestino() != null) {
                    navio.setPortoAtual(viagem.getDestino());
                    navioBLL.atualizar(navio);
                }
                libertarTripulantes(viagem);
                break;
            case CONCLUIDA:
                throw new Exception("A viagem já está concluída.");
            case CANCELADA:
                throw new Exception("Não é possível avançar o estado de uma viagem cancelada.");
        }
        viagemDAL.atualizar(viagem);
    }

    public void cancelarViagem(int idViagem) throws Exception {
        Viagem viagem = viagemDAL.buscarPorId(idViagem);
        ValidacaoUtils.exigirExistencia(viagem, "Viagem", idViagem);
        if (viagem.getEstado() == EstadoViagem.CONCLUIDA)
            throw new Exception("Não é possível cancelar uma viagem já concluída.");
        if (viagem.getEstado() == EstadoViagem.CANCELADA)
            throw new Exception("A viagem já está cancelada.");
        viagem.setEstado(EstadoViagem.CANCELADA);
        libertarTripulantes(viagem);
        viagemDAL.atualizar(viagem);
    }

    private void libertarTripulantes(Viagem viagem) throws Exception {
        for (TripulacaoViagem tv : viagem.getTripulacao()) {
            Tripulante t = tv.getTripulante();
            if (t != null && !t.isDisponivel()) {
                t.setDisponivel(true);
                tripulanteBLL.atualizar(t);
            }
        }
    }

    public void adicionarCarga(int idViagem, AtribuicaoCarga atribuicao) throws Exception {
        Viagem viagem = viagemDAL.buscarPorId(idViagem);
        ValidacaoUtils.exigirExistencia(viagem, "Viagem", idViagem);
        if (viagem.getEstado() != EstadoViagem.PLANEADA)
            throw new Exception("Só é possível adicionar cargas a viagens no estado PLANEADA.");

        Navio navio = viagem.getNavio();
        if (!compatibilidadeBLL.aceita(navio.getTipoNavio(), atribuicao.getCarga().getTipoCarga()))
            throw new Exception("O navio '" + navio.getNome() + "' não é compatível com o tipo de carga '"
                    + atribuicao.getCarga().getTipoCarga() + "'.");
        int maxCargas = compatibilidadeBLL.maxCargasPorViagem(navio.getTipoNavio());
        if (viagem.getCargas().size() >= maxCargas)
            throw new Exception("O navio já atingiu o limite máximo de cargas por viagem ("
                    + maxCargas + ").");
        if (viagem.getPesoTotalCargas() + atribuicao.getPesoAtribuido() > navio.getCapacidadeMaxima())
            throw new Exception("A adição desta carga excede a capacidade máxima do navio ("
                    + navio.getCapacidadeMaxima() + " t).");

        int idCarga = atribuicao.getCarga().getId();
        for (AtribuicaoCarga ac : viagem.getCargas())
            if (ac.getCarga().getId() == idCarga)
                throw new Exception("A carga '" + atribuicao.getCarga().getDesignacao()
                        + "' já está associada a esta viagem.");
        for (Viagem outra : viagemDAL.listarTodos()) {
            if (outra.getId() == idViagem) continue;
            if (outra.getEstado() != EstadoViagem.PLANEADA && outra.getEstado() != EstadoViagem.EM_CURSO) continue;
            for (AtribuicaoCarga ac : outra.getCargas()) {
                if (ac.getCarga().getId() == idCarga)
                    throw new Exception("A carga '" + atribuicao.getCarga().getDesignacao()
                            + "' já está associada à viagem #" + outra.getId() + " (ativa).");
            }
        }

        viagemDAL.adicionarCargaViagem(idViagem, atribuicao);
    }

    public void adicionarTripulante(int idViagem, TripulacaoViagem tv) throws Exception {
        Viagem viagem = viagemDAL.buscarPorId(idViagem);
        ValidacaoUtils.exigirExistencia(viagem, "Viagem", idViagem);
        if (viagem.getEstado() != EstadoViagem.PLANEADA)
            throw new Exception("Só é possível adicionar tripulantes a viagens no estado PLANEADA.");

        Tripulante t = tv.getTripulante();
        if (!t.isDisponivel())
            throw new Exception("O tripulante '" + t.getNome() + "' não está disponível.");
        for (TripulacaoViagem existente : viagem.getTripulacao()) {
            if (existente.getTripulante().getId() == t.getId())
                throw new Exception("O tripulante '" + t.getNome() + "' já está associado a esta viagem.");
        }

        viagemDAL.adicionarTripulanteViagem(idViagem, tv);
        t.setDisponivel(false);
        tripulanteBLL.atualizar(t);
    }

    public void removerCargaViagem(int idViagem, int idCarga) throws Exception {
        Viagem viagem = viagemDAL.buscarPorId(idViagem);
        ValidacaoUtils.exigirExistencia(viagem, "Viagem", idViagem);
        if (viagem.getEstado() != EstadoViagem.PLANEADA)
            throw new Exception("Só é possível remover cargas de viagens no estado PLANEADA.");
        boolean ok = viagemDAL.removerCargaViagem(idViagem, idCarga);
        if (!ok)
            throw new Exception("A carga indicada não está associada a esta viagem.");
    }

    public void removerTripulanteViagem(int idViagem, int idTripulante) throws Exception {
        Viagem viagem = viagemDAL.buscarPorId(idViagem);
        ValidacaoUtils.exigirExistencia(viagem, "Viagem", idViagem);
        if (viagem.getEstado() != EstadoViagem.PLANEADA)
            throw new Exception("Só é possível remover tripulantes de viagens no estado PLANEADA.");

        Tripulante tripulante = null;
        for (TripulacaoViagem tvItem : viagem.getTripulacao())
            if (tvItem.getTripulante().getId() == idTripulante) {
                tripulante = tvItem.getTripulante();
                break;
            }
        if (tripulante == null)
            throw new Exception("O tripulante indicado não está associado a esta viagem.");

        boolean ok = viagemDAL.removerTripulanteViagem(idViagem, idTripulante);
        if (ok) {
            tripulante.setDisponivel(true);
            tripulanteBLL.atualizar(tripulante);
        }
    }

    public void editarViagem(int idViagem, LocalDate novaPartida,
                             LocalDate novaChegada, String observacoes) throws Exception {
        Viagem viagem = viagemDAL.buscarPorId(idViagem);
        ValidacaoUtils.exigirExistencia(viagem, "Viagem", idViagem);
        if (viagem.getEstado() != EstadoViagem.PLANEADA)
            throw new Exception("Só é possível editar viagens no estado PLANEADA.");
        if (novaPartida.isBefore(LocalDate.now()))
            throw new Exception("A nova data de partida não pode ser no passado.");
        if (!novaChegada.isAfter(novaPartida))
            throw new Exception("A data de chegada prevista tem de ser posterior à data de partida.");
        viagem.setDataPartida(novaPartida);
        viagem.setDataChegadaPrevista(novaChegada);
        viagem.setObservacoes(observacoes);
        viagemDAL.atualizar(viagem);
    }

    public void validarCapacidade(Viagem viagem) throws Exception {
        double pesoTotal = viagem.getPesoTotalCargas();
        if (pesoTotal > viagem.getNavio().getCapacidadeMaxima())
            throw new Exception("O peso total das cargas (" + pesoTotal
                    + " t) excede a capacidade máxima do navio ("
                    + viagem.getNavio().getCapacidadeMaxima() + " t).");
    }

    public boolean remover(int id) {
        return viagemDAL.remover(id);
    }

    /** Compatibilidade navio-carga segundo a BD (usado também para filtrar a UI). */
    public boolean aceitaCarga(Navio navio, Carga carga) {
        if (navio == null || carga == null) return false;
        return compatibilidadeBLL.aceita(navio.getTipoNavio(), carga.getTipoCarga());
    }
}
