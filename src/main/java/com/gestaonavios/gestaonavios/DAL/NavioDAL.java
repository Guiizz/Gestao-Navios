package com.gestaonavios.gestaonavios.DAL;

import com.gestaonavios.gestaonavios.DAL.db.ConnectionManager;
import com.gestaonavios.gestaonavios.DAL.db.RowMapper;
import com.gestaonavios.gestaonavios.Model.Navio;
import com.gestaonavios.gestaonavios.Model.Porto;
import com.gestaonavios.gestaonavios.Model.TipoNavio;
import com.gestaonavios.gestaonavios.Model.enums.EstadoOperacional;
import com.gestaonavios.gestaonavios.Model.enums.TipoNavioEnums;

import java.util.List;

public class NavioDAL {

    private final PortoDAL portoDAL;
    private final TipoNavioDAL tipoNavioDAL;

    public NavioDAL(PortoDAL portoDAL, TipoNavioDAL tipoNavioDAL) {
        this.portoDAL    = portoDAL;
        this.tipoNavioDAL = tipoNavioDAL;
    }

    private RowMapper<Navio> mapper() {
        return rs -> {
            // Resolver FK id_tipo_navio → TipoNavioEnums
            // A designacao em TIPO_NAVIO deve coincidir com o nome do enum (ex: PETROLEIRO_CRUDE)
            TipoNavio tipoNavio = tipoNavioDAL.buscarPorId(rs.getInt("id_tipo_navio"));
            TipoNavioEnums tipo = tipoNavio != null
                    ? TipoNavioEnums.valueOf(tipoNavio.getDesignacao())
                    : TipoNavioEnums.PETROLEIRO_CRUDE;

            EstadoOperacional estado = EstadoOperacional.valueOf(rs.getString("estado_op"));

            int idPorto = rs.getInt("id_porto_atual");
            Porto porto = rs.wasNull() ? null : portoDAL.buscarPorId(idPorto);

            return new Navio(
                    rs.getInt("id_navio"),
                    rs.getString("nome"),
                    rs.getString("codigo_imo"),
                    tipo,
                    rs.getDouble("cap_maxima"),
                    rs.getInt("n_tanques"),
                    rs.getString("bandeira"),
                    rs.getInt("ano_fabrico"),
                    estado,
                    porto
            );
        };
    }

    // Obtém o id_tipo_navio a partir do enum Java, consultando TIPO_NAVIO por designacao
    private int getIdTipoNavio(TipoNavioEnums tipo) {
        TipoNavio tipoNavio = tipoNavioDAL.buscarPorDesignacao(tipo.name());
        return tipoNavio != null ? tipoNavio.getId() : 0;
    }

    public List<Navio> listarTodos() {
        return ConnectionManager.select("SELECT * FROM NAVIO", mapper());
    }

    public Navio buscarPorId(int id) {
        List<Navio> result = ConnectionManager.select(
                "SELECT * FROM NAVIO WHERE id_navio = " + id, mapper());
        return result.isEmpty() ? null : result.get(0);
    }

    public List<Navio> listarPorEstado(EstadoOperacional estado) {
        return ConnectionManager.select(
                "SELECT * FROM NAVIO WHERE estado_op = '" + estado.name() + "'", mapper());
    }

    public void adicionar(Navio navio) {
        int idTipoNavio = getIdTipoNavio(navio.getTipoNavio());
        String idPorto  = navio.getPortoAtual() != null
                ? String.valueOf(navio.getPortoAtual().getId()) : "NULL";
        ConnectionManager.create(
                "INSERT INTO NAVIO (nome, codigo_imo, cap_maxima, n_tanques, bandeira, ano_fabrico, estado_op, id_tipo_navio, id_porto_atual) VALUES ('"
                        + navio.getNome()       + "', '"
                        + navio.getCodigoIMO()  + "', "
                        + navio.getCapacidadeMaxima() + ", "
                        + navio.getNumeroTanques()    + ", '"
                        + navio.getBandeira()    + "', "
                        + navio.getAnoFabrico()  + ", '"
                        + navio.getEstadoOperacional().name() + "', "
                        + idTipoNavio + ", "
                        + idPorto + ")");
    }

    public boolean atualizar(Navio navio) {
        int idTipoNavio = getIdTipoNavio(navio.getTipoNavio());
        String idPorto  = navio.getPortoAtual() != null
                ? String.valueOf(navio.getPortoAtual().getId()) : "NULL";
        ConnectionManager.create(
                "UPDATE NAVIO SET nome='"        + navio.getNome()
                        + "', codigo_imo='"      + navio.getCodigoIMO()
                        + "', cap_maxima="       + navio.getCapacidadeMaxima()
                        + ", n_tanques="         + navio.getNumeroTanques()
                        + ", bandeira='"         + navio.getBandeira()
                        + "', ano_fabrico="      + navio.getAnoFabrico()
                        + ", estado_op='"        + navio.getEstadoOperacional().name()
                        + "', id_tipo_navio="    + idTipoNavio
                        + ", id_porto_atual="    + idPorto
                        + " WHERE id_navio="     + navio.getId());
        return true;
    }

    public boolean remover(int id) {
        ConnectionManager.create("DELETE FROM NAVIO WHERE id_navio=" + id);
        return true;
    }
}
