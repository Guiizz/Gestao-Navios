package com.gestaonavios.gestaonavios.Model;

/**
 * Espelha uma linha da view dbo.vw_EstatisticasFrota da base de dados.
 * Permite que o ecrã de estatísticas consuma diretamente um objeto da BD
 * (coerência com os objetos SQL criados para TBD), em vez de recalcular tudo em Java.
 */
public class EstatisticasFrota {

    private final int totalNavios;
    private final int naviosAtivos;
    private final int naviosManutencao;
    private final int naviosInativos;
    private final int totalViagens;
    private final int viagensPlaneadas;
    private final int viagensEmCurso;
    private final int viagensConcluidas;
    private final int viagensCanceladas;
    private final int totalCargas;
    private final int tripulantesDisponiveis;
    private final double pesoTotalTransportado;

    public EstatisticasFrota(int totalNavios, int naviosAtivos, int naviosManutencao,
                             int naviosInativos, int totalViagens, int viagensPlaneadas,
                             int viagensEmCurso, int viagensConcluidas, int viagensCanceladas,
                             int totalCargas, int tripulantesDisponiveis, double pesoTotalTransportado) {
        this.totalNavios = totalNavios;
        this.naviosAtivos = naviosAtivos;
        this.naviosManutencao = naviosManutencao;
        this.naviosInativos = naviosInativos;
        this.totalViagens = totalViagens;
        this.viagensPlaneadas = viagensPlaneadas;
        this.viagensEmCurso = viagensEmCurso;
        this.viagensConcluidas = viagensConcluidas;
        this.viagensCanceladas = viagensCanceladas;
        this.totalCargas = totalCargas;
        this.tripulantesDisponiveis = tripulantesDisponiveis;
        this.pesoTotalTransportado = pesoTotalTransportado;
    }

    public int getTotalNavios() { return totalNavios; }
    public int getNaviosAtivos() { return naviosAtivos; }
    public int getNaviosManutencao() { return naviosManutencao; }
    public int getNaviosInativos() { return naviosInativos; }
    public int getTotalViagens() { return totalViagens; }
    public int getViagensPlaneadas() { return viagensPlaneadas; }
    public int getViagensEmCurso() { return viagensEmCurso; }
    public int getViagensConcluidas() { return viagensConcluidas; }
    public int getViagensCanceladas() { return viagensCanceladas; }
    public int getTotalCargas() { return totalCargas; }
    public int getTripulantesDisponiveis() { return tripulantesDisponiveis; }
    public double getPesoTotalTransportado() { return pesoTotalTransportado; }
}
