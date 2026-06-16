package DAL;

import DAL.db.ConnectionManager;
import DAL.db.RowMapper;
import Model.Carga;
import Model.Porto;
import Model.TipoCarga;

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
            Porto portoCarga    = portoDAL.buscarPorId(rs.getInt("id_porto_carga"));
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
                "SELECT * FROM CARGA WHERE id_carga = " + id, mapper());
        return result.isEmpty() ? null : result.get(0);
    }

    public void adicionar(Carga carga) {
        String idTipoCarga    = carga.getTipoCarga()     != null ? String.valueOf(carga.getTipoCarga().getId())     : "NULL";
        String idPortoCarga   = carga.getPortoCarga()    != null ? String.valueOf(carga.getPortoCarga().getId())    : "NULL";
        String idPortoDescarga = carga.getPortoDescarga() != null ? String.valueOf(carga.getPortoDescarga().getId()) : "NULL";
        ConnectionManager.create(
                "INSERT INTO CARGA (designacao, id_tipo_carga, volume, peso, id_porto_carga, id_porto_descarga) VALUES ('"
                        + carga.getDesignacao() + "', "
                        + idTipoCarga + ", "
                        + carga.getVolume() + ", "
                        + carga.getPeso() + ", "
                        + idPortoCarga + ", "
                        + idPortoDescarga + ")");
    }

    public void atualizar(Carga carga) {
        String idTipoCarga    = carga.getTipoCarga()     != null ? String.valueOf(carga.getTipoCarga().getId())     : "NULL";
        String idPortoCarga   = carga.getPortoCarga()    != null ? String.valueOf(carga.getPortoCarga().getId())    : "NULL";
        String idPortoDescarga = carga.getPortoDescarga() != null ? String.valueOf(carga.getPortoDescarga().getId()) : "NULL";
        ConnectionManager.create(
                "UPDATE CARGA SET designacao='" + carga.getDesignacao()
                        + "', id_tipo_carga=" + idTipoCarga
                        + ", volume=" + carga.getVolume()
                        + ", peso=" + carga.getPeso()
                        + ", id_porto_carga=" + idPortoCarga
                        + ", id_porto_descarga=" + idPortoDescarga
                        + " WHERE id_carga=" + carga.getId());
    }

    public void remover(int id) {
        ConnectionManager.create("DELETE FROM CARGA WHERE id_carga=" + id);
    }
}
