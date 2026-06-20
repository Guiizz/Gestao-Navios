package com.gestaonavios.gestaonavios.DAL;

import com.gestaonavios.gestaonavios.DAL.db.ConnectionManager;
import com.gestaonavios.gestaonavios.DAL.db.RowMapper;
import com.gestaonavios.gestaonavios.Model.TipoCarga;

import java.util.List;

public class TipoCargaDAL {

    // A coluna 'categoria' em TIPO_CARGA é NOT NULL; usamos 'Geral' quando não for indicada.
    private static final String CATEGORIA_PADRAO = "Geral";

    private static final RowMapper<TipoCarga> MAPPER = rs -> new TipoCarga(
            rs.getInt("id_tipo_carga"),
            rs.getString("designacao"),
            rs.getString("categoria"),
            rs.getBoolean("inflamavel"),
            rs.getBoolean("corrosiva"),
            rs.getBoolean("toxica")
    );

    private static String categoriaOuPadrao(TipoCarga tipoCarga) {
        return tipoCarga.getCategoria() != null && !tipoCarga.getCategoria().isBlank()
                ? tipoCarga.getCategoria() : CATEGORIA_PADRAO;
    }

    public List<TipoCarga> listarTodos() {
        return ConnectionManager.select("SELECT * FROM TIPO_CARGA", MAPPER);
    }

    public TipoCarga buscarPorId(int id) {
        List<TipoCarga> result = ConnectionManager.select(
                "SELECT * FROM TIPO_CARGA WHERE id_tipo_carga = " + id, MAPPER);
        return result.isEmpty() ? null : result.get(0);
    }

    public void adicionar(TipoCarga tipoCarga) {
        ConnectionManager.create(
                "INSERT INTO TIPO_CARGA (designacao, categoria, inflamavel, corrosiva, toxica) VALUES ('"
                        + tipoCarga.getDesignacao() + "', '"
                        + categoriaOuPadrao(tipoCarga) + "', "
                        + (tipoCarga.isInflamavel() ? 1 : 0) + ", "
                        + (tipoCarga.isCorrosiva() ? 1 : 0) + ", "
                        + (tipoCarga.isToxica() ? 1 : 0) + ")");
    }

    public boolean atualizar(TipoCarga tipoCarga) {
        ConnectionManager.create(
                "UPDATE TIPO_CARGA SET designacao='" + tipoCarga.getDesignacao()
                        + "', categoria='" + categoriaOuPadrao(tipoCarga)
                        + "', inflamavel=" + (tipoCarga.isInflamavel() ? 1 : 0)
                        + ", corrosiva=" + (tipoCarga.isCorrosiva() ? 1 : 0)
                        + ", toxica=" + (tipoCarga.isToxica() ? 1 : 0)
                        + " WHERE id_tipo_carga=" + tipoCarga.getId());
        return true;
    }

    public boolean remover(int id) {
        ConnectionManager.create("DELETE FROM TIPO_CARGA WHERE id_tipo_carga=" + id);
        return true;
    }
}
