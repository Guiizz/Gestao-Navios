package com.gestaonavios.gestaonavios.DAL;

import com.gestaonavios.gestaonavios.DAL.db.ConnectionManager;
import com.gestaonavios.gestaonavios.DAL.db.RowMapper;
import com.gestaonavios.gestaonavios.Model.Carga;
import com.gestaonavios.gestaonavios.Model.Porto;
import com.gestaonavios.gestaonavios.Model.TipoCarga;

import java.util.List;

public class CargaDAL {

    private final TipoCargaDAL tipoCargaDAL;
    private final PortoDAL portoDAL;

    public CargaDAL(TipoCargaDAL tipoCargaDAL, PortoDAL portoDAL) {
        this.tipoCargaDAL = tipoCargaDAL;
        this.portoDAL = portoDAL;
    }

    private RowMapper<Carga> mapper() {
        return rs -> {
            TipoCarga tipoCarga = tipoCargaDAL.buscarPorId(rs.getInt("id_tipo_carga"));
            Porto portoCarga = portoDAL.buscarPorId(rs.getInt("id_porto_carga"));
            Porto portoDescarga = portoDAL.buscarPorId(rs.getInt("id_porto_descarga"));
            return new Carga(
                    rs.getInt("id_carga"),
                    rs.getString("designacao"),
                    tipoCarga,
                    rs.getDouble("volume"),
                    rs.getDouble("peso"),
                    portoCarga,
                    portoDescarga
            );
        };
    }

    public List<Carga> listarTodos() {
        return ConnectionManager.select("SELECT * FROM CARGA", mapper());
    }

    public Carga buscarPorId(int id) {
        List<Carga> result = ConnectionManager.select(
                "SELECT * FROM CARGA WHERE id_carga = ?", mapper(), id);
        return result.isEmpty() ? null : result.get(0);
    }

    public void adicionar(Carga carga) {
        Integer idTipoCarga = carga.getTipoCarga() != null ? carga.getTipoCarga().getId() : null;
        Integer idPortoCarga = carga.getPortoCarga() != null ? carga.getPortoCarga().getId() : null;
        Integer idPortoDescarga = carga.getPortoDescarga() != null ? carga.getPortoDescarga().getId() : null;
        ConnectionManager.create(
                "INSERT INTO CARGA (designacao, id_tipo_carga, volume, peso, id_porto_carga, id_porto_descarga) VALUES (?, ?, ?, ?, ?, ?)",
                carga.getDesignacao(), idTipoCarga, carga.getVolume(), carga.getPeso(),
                idPortoCarga, idPortoDescarga);
    }

    public void atualizar(Carga carga) {
        Integer idTipoCarga = carga.getTipoCarga() != null ? carga.getTipoCarga().getId() : null;
        Integer idPortoCarga = carga.getPortoCarga() != null ? carga.getPortoCarga().getId() : null;
        Integer idPortoDescarga = carga.getPortoDescarga() != null ? carga.getPortoDescarga().getId() : null;
        ConnectionManager.create(
                "UPDATE CARGA SET designacao=?, id_tipo_carga=?, volume=?, peso=?, id_porto_carga=?, id_porto_descarga=? WHERE id_carga=?",
                carga.getDesignacao(), idTipoCarga, carga.getVolume(), carga.getPeso(),
                idPortoCarga, idPortoDescarga, carga.getId());
    }

    public void remover(int id) {
        ConnectionManager.create("DELETE FROM CARGA WHERE id_carga=?", id);
    }
}
