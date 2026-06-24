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
                "SELECT * FROM TIPO_NAVIO WHERE id_tipo_navio = ?", MAPPER, id);
        return result.isEmpty() ? null : result.get(0);
    }

    // Usado pelo NavioDAL para obter o id a partir do nome do enum Java.
    // Tolerante a diferenças de espaços/maiúsculas para reduzir o acoplamento
    // entre o name() do enum e a designação gravada na BD.
    public TipoNavio buscarPorDesignacao(String designacao) {
        if (designacao == null) return null;
        List<TipoNavio> result = ConnectionManager.select(
                "SELECT * FROM TIPO_NAVIO WHERE UPPER(LTRIM(RTRIM(designacao))) = UPPER(LTRIM(RTRIM(?)))",
                MAPPER, designacao);
        return result.isEmpty() ? null : result.get(0);
    }

    public void adicionar(TipoNavio tipoNavio) {
        ConnectionManager.create(
                "INSERT INTO TIPO_NAVIO (designacao, categoria, max_cargas_viagem) VALUES (?, ?, ?)",
                tipoNavio.getDesignacao(), tipoNavio.getCategoria(), tipoNavio.getMaxCargasPorViagem());
    }

    public boolean atualizar(TipoNavio tipoNavio) {
        ConnectionManager.create(
                "UPDATE TIPO_NAVIO SET designacao=?, categoria=?, max_cargas_viagem=? WHERE id_tipo_navio=?",
                tipoNavio.getDesignacao(), tipoNavio.getCategoria(),
                tipoNavio.getMaxCargasPorViagem(), tipoNavio.getId());
        return true;
    }

    public boolean remover(int id) {
        ConnectionManager.create("DELETE FROM TIPO_NAVIO WHERE id_tipo_navio=?", id);
        return true;
    }
}
