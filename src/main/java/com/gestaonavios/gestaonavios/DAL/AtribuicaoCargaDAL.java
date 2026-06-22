package com.gestaonavios.gestaonavios.DAL;

import com.gestaonavios.gestaonavios.DAL.db.ConnectionManager;
import com.gestaonavios.gestaonavios.DAL.db.RowMapper;
import com.gestaonavios.gestaonavios.Model.AtribuicaoCarga;
import com.gestaonavios.gestaonavios.Model.Carga;
import com.gestaonavios.gestaonavios.Model.Tanque;

import java.util.List;

public class AtribuicaoCargaDAL {

    private final CargaDAL cargaDAL;

    public AtribuicaoCargaDAL(CargaDAL cargaDAL) {
        this.cargaDAL = cargaDAL;
    }

    private RowMapper<AtribuicaoCarga> mapper() {
        return rs -> {
            Carga carga = cargaDAL.buscarPorId(rs.getInt("id_carga"));
            // id_tanque é nullable — se NULL, tanque fica null
            int idTanque = rs.getInt("ac_id_tanque");
            Tanque tanque = rs.wasNull() ? null : new Tanque(
                    idTanque,
                    rs.getInt("t_numero"),
                    rs.getDouble("t_capacidade"),
                    rs.getInt("t_id_navio")
            );
            return new AtribuicaoCarga(
                    rs.getInt("id_viagem"),
                    carga,
                    rs.getDouble("volume_atribuido"),
                    rs.getDouble("peso_atribuido"),
                    tanque
            );
        };
    }

    public List<AtribuicaoCarga> listarPorViagem(int idViagem) {
        return ConnectionManager.select(
                "SELECT ac.*, "
                        + "ac.id_tanque AS ac_id_tanque, "
                        + "t.numero AS t_numero, "
                        + "t.capacidade AS t_capacidade, "
                        + "t.id_navio AS t_id_navio "
                        + "FROM ATRIBUICAO_CARGA ac "
                        + "LEFT JOIN TANQUE t ON ac.id_tanque = t.id_tanque "
                        + "WHERE ac.id_viagem = ?", mapper(), idViagem);
    }

    public void adicionar(int idViagem, AtribuicaoCarga ac) {
        Integer idTanque = ac.getTanque() != null ? ac.getTanque().getId() : null;
        ConnectionManager.create(
                "INSERT INTO ATRIBUICAO_CARGA (id_viagem, id_carga, id_tanque, volume_atribuido, peso_atribuido) VALUES (?, ?, ?, ?, ?)",
                idViagem, ac.getCarga().getId(), idTanque, ac.getVolumeAtribuido(), ac.getPesoAtribuido());
    }

    public void removerPorViagem(int idViagem) {
        ConnectionManager.create("DELETE FROM ATRIBUICAO_CARGA WHERE id_viagem=?", idViagem);
    }

    public boolean removerPorViagemECarga(int idViagem, int idCarga) {
        ConnectionManager.create(
                "DELETE FROM ATRIBUICAO_CARGA WHERE id_viagem=? AND id_carga=?", idViagem, idCarga);
        return true;
    }
}
