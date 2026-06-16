package DAL;

import DAL.db.ConnectionManager;
import DAL.db.RowMapper;
import Model.Porto;

import java.util.List;

public class PortoDAL {

    private static final RowMapper<Porto> MAPPER = rs -> new Porto(
            rs.getInt("id_porto"),
            rs.getString("nome"),
            rs.getString("pais"),
            rs.getString("locode")
    );

    public List<Porto> listarTodos() {
        return ConnectionManager.select("SELECT * FROM PORTO", MAPPER);
    }

    public Porto buscarPorId(int id) {
        List<Porto> result = ConnectionManager.select(
                "SELECT * FROM PORTO WHERE id_porto = " + id, MAPPER);
        return result.isEmpty() ? null : result.get(0);
    }

    public void adicionar(Porto porto) {
        ConnectionManager.create(
                "INSERT INTO PORTO (nome, pais, locode) VALUES ('"
                        + porto.getNome() + "', '"
                        + porto.getPais() + "', '"
                        + porto.getCodigoUNLOCODE() + "')");
    }

    public boolean atualizar(Porto porto) {
        ConnectionManager.create(
                "UPDATE PORTO SET nome='" + porto.getNome()
                        + "', pais='" + porto.getPais()
                        + "', locode='" + porto.getCodigoUNLOCODE()
                        + "' WHERE id_porto=" + porto.getId());
        return true;
    }

    public boolean remover(int id) {
        ConnectionManager.create("DELETE FROM PORTO WHERE id_porto=" + id);
        return true;
    }
}
