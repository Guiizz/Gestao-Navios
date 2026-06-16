package DAL;

import DAL.db.ConnectionManager;
import DAL.db.RowMapper;
import Model.TipoCarga;

import java.util.List;

public class TipoCargaDAL {

    // O modelo relacional tem coluna 'categoria' em TIPO_CARGA.
    // O Java TipoCarga não tem esse campo, por isso usamos 'Geral' como valor padrão.
    private static final String CATEGORIA_PADRAO = "Geral";

    private static final RowMapper<TipoCarga> MAPPER = rs -> new TipoCarga(
            rs.getInt("id_tipo_carga"),
            rs.getString("designacao"),
            rs.getBoolean("inflamavel"),
            rs.getBoolean("corrosiva"),
            rs.getBoolean("toxica")
    );

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
                        + CATEGORIA_PADRAO + "', "
                        + (tipoCarga.isInflamavel() ? 1 : 0) + ", "
                        + (tipoCarga.isCorrosiva()  ? 1 : 0) + ", "
                        + (tipoCarga.isToxica()     ? 1 : 0) + ")");
    }

    public boolean atualizar(TipoCarga tipoCarga) {
        ConnectionManager.create(
                "UPDATE TIPO_CARGA SET designacao='" + tipoCarga.getDesignacao()
                        + "', inflamavel=" + (tipoCarga.isInflamavel() ? 1 : 0)
                        + ", corrosiva="  + (tipoCarga.isCorrosiva()  ? 1 : 0)
                        + ", toxica="     + (tipoCarga.isToxica()     ? 1 : 0)
                        + " WHERE id_tipo_carga=" + tipoCarga.getId());
        return true;
    }

    public boolean remover(int id) {
        ConnectionManager.create("DELETE FROM TIPO_CARGA WHERE id_tipo_carga=" + id);
        return true;
    }
}
