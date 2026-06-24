package com.gestaonavios.gestaonavios.DAL;

import com.gestaonavios.gestaonavios.DAL.db.ConnectionManager;
import com.gestaonavios.gestaonavios.DAL.db.RowMapper;
import com.gestaonavios.gestaonavios.Model.Tanque;

import java.util.List;

public class TanqueDAL {

    private static final RowMapper<Tanque> MAPPER = rs -> new Tanque(
            rs.getInt("id_tanque"),
            rs.getInt("numero"),
            rs.getDouble("capacidade"),
            rs.getInt("id_navio")
    );

    public List<Tanque> listarPorNavio(int idNavio) {
        return ConnectionManager.select(
                "SELECT * FROM TANQUE WHERE id_navio = ? ORDER BY numero", MAPPER, idNavio);
    }

    public Tanque buscarPorId(int id) {
        List<Tanque> result = ConnectionManager.select(
                "SELECT * FROM TANQUE WHERE id_tanque = ?", MAPPER, id);
        return result.isEmpty() ? null : result.get(0);
    }

    public int contarPorNavio(int idNavio) {
        return listarPorNavio(idNavio).size();
    }

    public void adicionar(Tanque tanque) {
        ConnectionManager.create(
                "INSERT INTO TANQUE (numero, capacidade, id_navio) VALUES (?, ?, ?)",
                tanque.getNumero(), tanque.getCapacidade(), tanque.getIdNavio());
    }

    public boolean atualizar(Tanque tanque) {
        return ConnectionManager.create(
                "UPDATE TANQUE SET numero=?, capacidade=? WHERE id_tanque=?",
                tanque.getNumero(), tanque.getCapacidade(), tanque.getId()) > 0;
    }

    public boolean remover(int id) {
        return ConnectionManager.create("DELETE FROM TANQUE WHERE id_tanque=?", id) > 0;
    }

    public boolean removerPorNavio(int idNavio) {
        return ConnectionManager.create("DELETE FROM TANQUE WHERE id_navio=?", idNavio) > 0;
    }
}
