package com.gestaonavios.gestaonavios.DAL;

import com.gestaonavios.gestaonavios.DAL.db.ConnectionManager;
import com.gestaonavios.gestaonavios.DAL.db.RowMapper;
import com.gestaonavios.gestaonavios.Model.EstatisticasFrota;

import java.util.List;

/**
 * Acede aos objetos de estatística da base de dados.
 * Consome a view dbo.vw_EstatisticasFrota (objeto SQL da entrega TBD).
 */
public class EstatisticasDAL {

    private static final RowMapper<EstatisticasFrota> MAPPER = rs -> new EstatisticasFrota(
            rs.getInt("total_navios"),
            rs.getInt("navios_ativos"),
            rs.getInt("navios_manutencao"),
            rs.getInt("navios_inativos"),
            rs.getInt("total_viagens"),
            rs.getInt("viagens_planeadas"),
            rs.getInt("viagens_em_curso"),
            rs.getInt("viagens_concluidas"),
            rs.getInt("viagens_canceladas"),
            rs.getInt("total_cargas"),
            rs.getInt("tripulantes_disponiveis"),
            rs.getDouble("peso_total_transportado")
    );

    public EstatisticasFrota obterEstatisticasFrota() {
        List<EstatisticasFrota> result =
                ConnectionManager.select("SELECT * FROM vw_EstatisticasFrota", MAPPER);
        return result.isEmpty() ? null : result.get(0);
    }
}
