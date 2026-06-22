package com.gestaonavios.gestaonavios.DAL;

import com.gestaonavios.gestaonavios.DAL.db.ConnectionManager;
import com.gestaonavios.gestaonavios.DAL.db.RowMapper;
import com.gestaonavios.gestaonavios.Model.AtribuicaoCarga;
import com.gestaonavios.gestaonavios.Model.Navio;
import com.gestaonavios.gestaonavios.Model.Porto;
import com.gestaonavios.gestaonavios.Model.TripulacaoViagem;
import com.gestaonavios.gestaonavios.Model.Viagem;
import com.gestaonavios.gestaonavios.Model.enums.EstadoViagem;

import java.time.LocalDate;
import java.util.List;

public class ViagemDAL {

    private final PortoDAL portoDAL;
    private final NavioDAL navioDAL;
    private final AtribuicaoCargaDAL atribuicaoCargaDAL;
    private final TripulacaoViagemDAL tripulacaoViagemDAL;

    public ViagemDAL(PortoDAL portoDAL, NavioDAL navioDAL) {
        this.portoDAL = portoDAL;
        this.navioDAL = navioDAL;
        TipoCargaDAL tipoCargaDAL = new TipoCargaDAL();
        CargaDAL cargaDAL = new CargaDAL(tipoCargaDAL, portoDAL);
        TripulanteDAL tripulanteDAL = new TripulanteDAL();
        this.atribuicaoCargaDAL = new AtribuicaoCargaDAL(cargaDAL);
        this.tripulacaoViagemDAL = new TripulacaoViagemDAL(tripulanteDAL);
    }

    private RowMapper<Viagem> mapper() {
        return rs -> {
            int id = rs.getInt("id_viagem");
            LocalDate dataPartida = rs.getDate("data_partida").toLocalDate();
            LocalDate dataChegadaPrevista = rs.getDate("data_chegada_prevista").toLocalDate();
            java.sql.Date dataRealSql = rs.getDate("data_chegada_real");
            LocalDate dataChegadaReal = dataRealSql != null ? dataRealSql.toLocalDate() : null;
            // estado guardado como nome do enum (ex: PLANEADA)
            EstadoViagem estado = EstadoViagem.valueOf(rs.getString("estado"));
            Porto origem = portoDAL.buscarPorId(rs.getInt("id_porto_origem"));
            Porto destino = portoDAL.buscarPorId(rs.getInt("id_porto_destino"));
            String observacoes = rs.getString("observacoes");
            Navio navio = navioDAL.buscarPorId(rs.getInt("id_navio"));
            Viagem viagem = new Viagem(id, dataPartida, dataChegadaPrevista,
                    estado, origem, destino, observacoes, navio);
            viagem.setDataChegadaReal(dataChegadaReal);
            viagem.setCargas(atribuicaoCargaDAL.listarPorViagem(id));
            viagem.setTripulacao(tripulacaoViagemDAL.listarPorViagem(id));
            return viagem;
        };
    }

    public List<Viagem> listarTodos() {
        return ConnectionManager.select("SELECT * FROM VIAGEM", mapper());
    }

    public Viagem buscarPorId(int id) {
        List<Viagem> result = ConnectionManager.select(
                "SELECT * FROM VIAGEM WHERE id_viagem = ?", mapper(), id);
        return result.isEmpty() ? null : result.get(0);
    }

    public List<Viagem> listarPorNavio(int idNavio) {
        return ConnectionManager.select(
                "SELECT * FROM VIAGEM WHERE id_navio = ?", mapper(), idNavio);
    }

    public List<Viagem> listarPorTripulante(int idTripulante) {
        return ConnectionManager.select(
                "SELECT v.* FROM VIAGEM v"
                        + " INNER JOIN TRIPULACAO_VIAGEM tv ON v.id_viagem = tv.id_viagem"
                        + " WHERE tv.id_tripulante = ?", mapper(), idTripulante);
    }

    public boolean cargaEmViagemAtiva(int idCarga) {
        for (Viagem v : listarTodos()) {
            if (v.getEstado() != EstadoViagem.PLANEADA && v.getEstado() != EstadoViagem.EM_CURSO) continue;
            for (AtribuicaoCarga ac : v.getCargas())
                if (ac.getCarga() != null && ac.getCarga().getId() == idCarga) return true;
        }
        return false;
    }

    public boolean tripulanteEmViagemAtiva(int idTripulante) {
        for (Viagem v : listarTodos()) {
            if (v.getEstado() != EstadoViagem.PLANEADA && v.getEstado() != EstadoViagem.EM_CURSO) continue;
            for (TripulacaoViagem tv : v.getTripulacao())
                if (tv.getTripulante() != null && tv.getTripulante().getId() == idTripulante) return true;
        }
        return false;
    }

    public void adicionar(Viagem viagem) {
        Integer idOrigem = viagem.getOrigem() != null ? viagem.getOrigem().getId() : null;
        Integer idDestino = viagem.getDestino() != null ? viagem.getDestino().getId() : null;
        Integer idNavio = viagem.getNavio() != null ? viagem.getNavio().getId() : null;
        ConnectionManager.create(
                "INSERT INTO VIAGEM (data_partida, data_chegada_prevista, data_chegada_real, estado, id_porto_origem, id_porto_destino, observacoes, id_navio) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                viagem.getDataPartida(), viagem.getDataChegadaPrevista(), viagem.getDataChegadaReal(),
                viagem.getEstado().name(), idOrigem, idDestino, viagem.getObservacoes(), idNavio);
    }

    public boolean atualizar(Viagem viagem) {
        Integer idOrigem = viagem.getOrigem() != null ? viagem.getOrigem().getId() : null;
        Integer idDestino = viagem.getDestino() != null ? viagem.getDestino().getId() : null;
        Integer idNavio = viagem.getNavio() != null ? viagem.getNavio().getId() : null;
        ConnectionManager.create(
                "UPDATE VIAGEM SET data_partida=?, data_chegada_prevista=?, data_chegada_real=?, estado=?, id_porto_origem=?, id_porto_destino=?, observacoes=?, id_navio=? WHERE id_viagem=?",
                viagem.getDataPartida(), viagem.getDataChegadaPrevista(), viagem.getDataChegadaReal(),
                viagem.getEstado().name(), idOrigem, idDestino, viagem.getObservacoes(), idNavio, viagem.getId());
        return true;
    }

    public boolean remover(int id) {
        atribuicaoCargaDAL.removerPorViagem(id);
        tripulacaoViagemDAL.removerPorViagem(id);
        ConnectionManager.create("DELETE FROM VIAGEM WHERE id_viagem=?", id);
        return true;
    }

    public void adicionarCargaViagem(int idViagem, AtribuicaoCarga ac) {
        atribuicaoCargaDAL.adicionar(idViagem, ac);
    }

    public boolean removerCargaViagem(int idViagem, int idCarga) {
        return atribuicaoCargaDAL.removerPorViagemECarga(idViagem, idCarga);
    }

    public void adicionarTripulanteViagem(int idViagem, TripulacaoViagem tv) {
        tripulacaoViagemDAL.adicionar(idViagem, tv);
    }

    public boolean removerTripulanteViagem(int idViagem, int idTripulante) {
        return tripulacaoViagemDAL.removerPorViagemETripulante(idViagem, idTripulante);
    }
}
