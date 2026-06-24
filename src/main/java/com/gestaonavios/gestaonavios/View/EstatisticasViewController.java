package com.gestaonavios.gestaonavios.View;

import com.gestaonavios.gestaonavios.BLL.CargaBLL;
import com.gestaonavios.gestaonavios.BLL.PortoBLL;
import com.gestaonavios.gestaonavios.BLL.TripulanteBLL;
import com.gestaonavios.gestaonavios.DAL.CargaDAL;
import com.gestaonavios.gestaonavios.DAL.EstatisticasDAL;
import com.gestaonavios.gestaonavios.DAL.NavioDAL;
import com.gestaonavios.gestaonavios.DAL.PortoDAL;
import com.gestaonavios.gestaonavios.DAL.TipoCargaDAL;
import com.gestaonavios.gestaonavios.DAL.TipoNavioDAL;
import com.gestaonavios.gestaonavios.DAL.TripulanteDAL;
import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.Carga;
import com.gestaonavios.gestaonavios.Model.EstatisticasFrota;
import com.gestaonavios.gestaonavios.Model.Porto;
import com.gestaonavios.gestaonavios.Model.Tripulante;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class EstatisticasViewController {

    @FXML
    private Label lblTotalNavios;
    @FXML
    private Label lblTotalViagens;
    @FXML
    private Label lblTotalCargas;
    @FXML
    private Label lblTotalTripulantes;

    @FXML
    private Label lblNaviosAtivos;
    @FXML
    private Label lblNaviosManutencao;
    @FXML
    private Label lblNaviosInativos;

    @FXML
    private Label lblViagensPlaneadas;
    @FXML
    private Label lblViagensEmCurso;
    @FXML
    private Label lblViagensConcluidas;
    @FXML
    private Label lblViagensCanceladas;
    @FXML
    private Label lblTotalTransportado;

    @FXML
    private Label lblTripDisp;
    @FXML
    private Label lblTripEmViagem;

    @FXML
    private Label lblTotalPortos;
    @FXML
    private Label lblPesoTotalCargas;

    // Estatísticas globais vêm da view dbo.vw_EstatisticasFrota (objeto SQL da BD).
    private EstatisticasDAL estatisticasDAL;
    // BLLs apenas para os indicadores que a view não cobre.
    private CargaBLL cargaBLL;
    private TripulanteBLL tripulanteBLL;
    private PortoBLL portoBLL;

    @FXML
    public void initialize() {
        PortoDAL portoDAL = new PortoDAL();
        TipoNavioDAL tipoNavioDAL = new TipoNavioDAL();
        TipoCargaDAL tipoCargaDAL = new TipoCargaDAL();
        NavioDAL navioDAL = new NavioDAL(portoDAL, tipoNavioDAL);
        CargaDAL cargaDAL = new CargaDAL(tipoCargaDAL, portoDAL);
        TripulanteDAL tripulanteDAL = new TripulanteDAL();
        ViagemDAL viagemDAL = new ViagemDAL(portoDAL, navioDAL);

        this.estatisticasDAL = new EstatisticasDAL();
        this.tripulanteBLL = new TripulanteBLL(tripulanteDAL, viagemDAL);
        this.cargaBLL = new CargaBLL(cargaDAL, viagemDAL);
        this.portoBLL = new PortoBLL(portoDAL);

        atualizar();
    }

    @FXML
    public void atualizar() {
        // Indicadores de frota, viagens e transporte: lidos diretamente da view da BD.
        EstatisticasFrota ef = estatisticasDAL.obterEstatisticasFrota();
        if (ef != null) {
            lblTotalNavios.setText(String.valueOf(ef.getTotalNavios()));
            lblNaviosAtivos.setText(String.valueOf(ef.getNaviosAtivos()));
            lblNaviosManutencao.setText(String.valueOf(ef.getNaviosManutencao()));
            lblNaviosInativos.setText(String.valueOf(ef.getNaviosInativos()));

            lblTotalViagens.setText(String.valueOf(ef.getTotalViagens()));
            lblViagensPlaneadas.setText(String.valueOf(ef.getViagensPlaneadas()));
            lblViagensEmCurso.setText(String.valueOf(ef.getViagensEmCurso()));
            lblViagensConcluidas.setText(String.valueOf(ef.getViagensConcluidas()));
            lblViagensCanceladas.setText(String.valueOf(ef.getViagensCanceladas()));
            lblTotalTransportado.setText(String.format("%.1f t", ef.getPesoTotalTransportado()));

            lblTotalCargas.setText(String.valueOf(ef.getTotalCargas()));
            lblTripDisp.setText(String.valueOf(ef.getTripulantesDisponiveis()));
        }

        // Indicadores que a view não cobre: calculados a partir das BLLs.
        double pesoTotal = 0;
        for (Carga c : cargaBLL.listarTodos()) pesoTotal += c.getPeso();
        lblPesoTotalCargas.setText(String.format("%.1f t", pesoTotal));

        List<Tripulante> trip = tripulanteBLL.listarTodos();
        int dispView = (ef != null) ? ef.getTripulantesDisponiveis() : 0;
        lblTotalTripulantes.setText(String.valueOf(trip.size()));
        lblTripEmViagem.setText(String.valueOf(trip.size() - dispView));

        List<Porto> portos = portoBLL.listarTodos();
        lblTotalPortos.setText(String.valueOf(portos.size()));
    }
}
