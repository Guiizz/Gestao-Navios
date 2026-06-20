package com.gestaonavios.gestaonavios.DAL;

import com.gestaonavios.gestaonavios.DAL.db.ConnectionManager;
import com.gestaonavios.gestaonavios.DAL.db.RowMapper;
import com.gestaonavios.gestaonavios.Model.TripulacaoViagem;
import com.gestaonavios.gestaonavios.Model.Tripulante;
import com.gestaonavios.gestaonavios.Model.enums.FuncaoTripulante;

import java.time.LocalDate;
import java.util.List;

public class TripulacaoViagemDAL {

    private final TripulanteDAL tripulanteDAL;

    public TripulacaoViagemDAL(TripulanteDAL tripulanteDAL) {
        this.tripulanteDAL = tripulanteDAL;
    }

    private RowMapper<TripulacaoViagem> mapper() {
        return rs -> {
            Tripulante tripulante = tripulanteDAL.buscarPorId(rs.getInt("id_tripulante"));
            FuncaoTripulante funcao = FuncaoTripulante.valueOf(rs.getString("funcao_na_viagem"));
            java.sql.Date embarqueSql = rs.getDate("data_embarque");
            java.sql.Date desembarqueSql = rs.getDate("data_desembarque");
            LocalDate embarque = embarqueSql != null ? embarqueSql.toLocalDate() : null;
            LocalDate desembarque = desembarqueSql != null ? desembarqueSql.toLocalDate() : null;
            return new TripulacaoViagem(0, tripulante, funcao, embarque, desembarque);
        };
    }

    public List<TripulacaoViagem> listarPorViagem(int idViagem) {
        return ConnectionManager.select(
                "SELECT * FROM TRIPULACAO_VIAGEM WHERE id_viagem = " + idViagem, mapper());
    }

    public void adicionar(int idViagem, TripulacaoViagem tv) {
        String embarque = tv.getDataEmbarque() != null ? "'" + tv.getDataEmbarque() + "'" : "NULL";
        String desembarque = tv.getDataDesembarque() != null ? "'" + tv.getDataDesembarque() + "'" : "NULL";
        ConnectionManager.create(
                "INSERT INTO TRIPULACAO_VIAGEM (id_viagem, id_tripulante, funcao_na_viagem, data_embarque, data_desembarque) VALUES ("
                        + idViagem + ", "
                        + tv.getTripulante().getId() + ", '"
                        + tv.getFuncaoNaViagem().name() + "', "
                        + embarque + ", "
                        + desembarque + ")");
    }

    public void removerPorViagem(int idViagem) {
        ConnectionManager.create("DELETE FROM TRIPULACAO_VIAGEM WHERE id_viagem=" + idViagem);
    }

    public boolean removerPorViagemETripulante(int idViagem, int idTripulante) {
        ConnectionManager.create(
                "DELETE FROM TRIPULACAO_VIAGEM WHERE id_viagem=" + idViagem
                        + " AND id_tripulante=" + idTripulante);
        return true;
    }
}
