package com.gestaonavios.gestaonavios.DAL;

import com.gestaonavios.gestaonavios.DAL.db.ConnectionManager;
import com.gestaonavios.gestaonavios.DAL.db.RowMapper;
import com.gestaonavios.gestaonavios.Model.CompatibilidadeCarga;
import com.gestaonavios.gestaonavios.Model.TipoCarga;
import com.gestaonavios.gestaonavios.Model.TipoNavio;

import java.util.List;

public class CompatibilidadeCargaDAL {

    private final TipoNavioDAL tipoNavioDAL;
    private final TipoCargaDAL tipoCargaDAL;

    public CompatibilidadeCargaDAL(TipoNavioDAL tipoNavioDAL, TipoCargaDAL tipoCargaDAL) {
        this.tipoNavioDAL = tipoNavioDAL;
        this.tipoCargaDAL = tipoCargaDAL;
    }

    private RowMapper<CompatibilidadeCarga> mapper() {
        return rs -> {
            TipoNavio tipoNavio = tipoNavioDAL.buscarPorId(rs.getInt("id_tipo_navio"));
            TipoCarga tipoCarga = tipoCargaDAL.buscarPorId(rs.getInt("id_tipo_carga"));
            // DDL usa PK composta (sem id próprio); usamos 0 como placeholder
            return new CompatibilidadeCarga(0, tipoNavio, tipoCarga, rs.getInt("limite_cargas"));
        };
    }

    public List<CompatibilidadeCarga> listarTodos() {
        return ConnectionManager.select("SELECT * FROM COMPATIBILIDADE_CARGA", mapper());
    }

    public List<CompatibilidadeCarga> listarPorTipoNavio(int idTipoNavio) {
        return ConnectionManager.select(
                "SELECT * FROM COMPATIBILIDADE_CARGA WHERE id_tipo_navio = " + idTipoNavio, mapper());
    }

    public void adicionar(CompatibilidadeCarga compatibilidade) {
        String idTipoNavio = compatibilidade.getTipoNavio() != null
                ? String.valueOf(compatibilidade.getTipoNavio().getId()) : "NULL";
        String idTipoCarga = compatibilidade.getTipoCarga() != null
                ? String.valueOf(compatibilidade.getTipoCarga().getId()) : "NULL";
        ConnectionManager.create(
                "INSERT INTO COMPATIBILIDADE_CARGA (id_tipo_navio, id_tipo_carga, limite_cargas) VALUES ("
                        + idTipoNavio + ", "
                        + idTipoCarga + ", "
                        + compatibilidade.getLimiteCarga() + ")");
    }

    public boolean remover(int idTipoNavio, int idTipoCarga) {
        ConnectionManager.create(
                "DELETE FROM COMPATIBILIDADE_CARGA WHERE id_tipo_navio=" + idTipoNavio
                        + " AND id_tipo_carga=" + idTipoCarga);
        return true;
    }
}
