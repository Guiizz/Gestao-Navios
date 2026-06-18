package com.gestaonavios.gestaonavios.DAL;

import com.gestaonavios.gestaonavios.DAL.db.ConnectionManager;
import com.gestaonavios.gestaonavios.DAL.db.RowMapper;
import com.gestaonavios.gestaonavios.Model.TipoNavio;

import java.util.List;

public class TipoNavioDAL {

    private static final RowMapper<TipoNavio> MAPPER = rs -> new TipoNavio(
            rs.getInt("id_tipo_navio"),
            rs.getString("designacao"),
            rs.getString("categoria"),
            rs.getInt("max_cargas_viagem")
    );

    public List<TipoNavio> listarTodos() {
        return ConnectionManager.select("SELECT * FROM TIPO_NAVIO", MAPPER);
    }

    public TipoNavio buscarPorId(int id) {
        List<TipoNavio> result = ConnectionManager.select(
                "SELECT * FROM TIPO_NAVIO WHERE id_tipo_navio = " + id, MAPPER);
        return result.isEmpty() ? null : result.get(0);
    }

    // Usado pelo NavioDAL para obter o id a partir do nome do enum Java
    public TipoNavio buscarPorDesignacao(String designacao) {
        List<TipoNavio> result = ConnectionManager.select(
                "SELECT * FROM TIPO_NAVIO WHERE designacao = '" + designacao + "'", MAPPER);
        return result.isEmpty() ? null : result.get(0);
    }

    public void adicionar(TipoNavio tipoNavio) {
        ConnectionManager.create(
                "INSERT INTO TIPO_NAVIO (designacao, categoria, max_cargas_viagem) VALUES ('"
                        + tipoNavio.getDesignacao() + "', '"
                        + tipoNavio.getCategoria() + "', "
                        + tipoNavio.getMaxCargasPorViagem() + ")");
    }

    public boolean atualizar(TipoNavio tipoNavio) {
        ConnectionManager.create(
                "UPDATE TIPO_NAVIO SET designacao='" + tipoNavio.getDesignacao()
                        + "', categoria='" + tipoNavio.getCategoria()
                        + "', max_cargas_viagem=" + tipoNavio.getMaxCargasPorViagem()
                        + " WHERE id_tipo_navio=" + tipoNavio.getId());
        return true;
    }

    public boolean remover(int id) {
        ConnectionManager.create("DELETE FROM TIPO_NAVIO WHERE id_tipo_navio=" + id);
        return true;
    }
}
