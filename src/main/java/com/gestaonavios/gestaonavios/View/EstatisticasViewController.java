package com.gestaonavios.gestaonavios.View;

import com.gestaonavios.gestaonavios.DAL.CargaDAL;
import com.gestaonavios.gestaonavios.DAL.NavioDAL;
import com.gestaonavios.gestaonavios.DAL.PortoDAL;
import com.gestaonavios.gestaonavios.DAL.TipoCargaDAL;
import com.gestaonavios.gestaonavios.DAL.TipoNavioDAL;
import com.gestaonavios.gestaonavios.DAL.TripulanteDAL;
import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.Carga;
import com.gestaonavios.gestaonavios.Model.Navio;
import com.gestaonavios.gestaonavios.Model.Porto;
import com.gestaonavios.gestaonavios.Model.Tripulante;
import com.gestaonavios.gestaonavios.Model.Viagem;
import com.gestaonavios.gestaonavios.Model.enums.EstadoOperacional;
import com.gestaonavios.gestaonavios.Model.enums.EstadoViagem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class EstatisticasViewController {

    @FXML private Label lblTotalNavios;
    @FXML private Label lblTotalViagens;
    @FXML private Label lblTotalCargas;
    @FXML private Label lblTotalTripulantes;

    @FXML private Label lblNaviosAtivos;
    @FXML private Label lblNaviosManutencao;
    @FXML private Label lblNaviosInativos;

    @FXML private Label lblViagensPlaneadas;
    @FXML private Label lblViagensEmCurso;
    @FXML private Label lblViagensConcluidas;
    @FXML private Label lblViagensCanceladas;
    @FXML private Label lblTotalTransportado;

    @FXML private Label lblTripDisp;
    @FXML private Label lblTripEmViagem;

    @FXML private Label lblTotalPortos;
    @FXML private Label lblPesoTotalCargas;

    @FXML
    public void initialize() {
        atualizar();
    }

    @FXML
    public void atualizar() {
        PortoDAL     portoDAL     = new PortoDAL();
        TipoNavioDAL tipoNavioDAL = new TipoNavioDAL();
        TipoCargaDAL tipoCargaDAL = new TipoCargaDAL();
        NavioDAL     navioDAL     = new NavioDAL(portoDAL, tipoNavioDAL);
        CargaDAL     cargaDAL     = new CargaDAL(tipoCargaDAL, portoDAL);
        TripulanteDAL tripulanteDAL = new TripulanteDAL();
        ViagemDAL    viagemDAL    = new ViagemDAL(portoDAL, navioDAL);

        // Navios
        List<Navio> navios = navioDAL.listarTodos();
        int nAtivo = 0, nMan = 0, nInat = 0;
        for (Navio n : navios) {
            if (n.getEstadoOperacional() == EstadoOperacional.ATIVO)              nAtivo++;
            else if (n.getEstadoOperacional() == EstadoOperacional.EM_MANUTENCAO) nMan++;
            else                                                                   nInat++;
        }
        lblTotalNavios.setText(String.valueOf(navios.size()));
        lblNaviosAtivos.setText(String.valueOf(nAtivo));
        lblNaviosManutencao.setText(String.valueOf(nMan));
        lblNaviosInativos.setText(String.valueOf(nInat));

        // Viagens
        List<Viagem> viagens = viagemDAL.listarTodos();
        int vPlan = 0, vCur = 0, vConc = 0, vCanc = 0;
        double totalTransp = 0;
        for (Viagem v : viagens) {
            switch (v.getEstado()) {
                case PLANEADA:  vPlan++;  break;
                case EM_CURSO:  vCur++;   break;
                case CONCLUIDA: vConc++; totalTransp += v.getPesoTotalCargas(); break;
                case CANCELADA: vCanc++;  break;
            }
        }
        lblTotalViagens.setText(String.valueOf(viagens.size()));
        lblViagensPlaneadas.setText(String.valueOf(vPlan));
        lblViagensEmCurso.setText(String.valueOf(vCur));
        lblViagensConcluidas.setText(String.valueOf(vConc));
        lblViagensCanceladas.setText(String.valueOf(vCanc));
        lblTotalTransportado.setText(String.format("%.1f t", totalTransp));

        // Cargas
        List<Carga> cargas = cargaDAL.listarTodos();
        double pesoTotal = 0;
        for (Carga c : cargas) pesoTotal += c.getPeso();
        lblTotalCargas.setText(String.valueOf(cargas.size()));
        lblPesoTotalCargas.setText(String.format("%.1f t", pesoTotal));

        // Tripulantes
        List<Tripulante> trip = tripulanteDAL.listarTodos();
        int tDisp = 0, tViagem = 0;
        for (Tripulante t : trip) { if (t.isDisponivel()) tDisp++; else tViagem++; }
        lblTotalTripulantes.setText(String.valueOf(trip.size()));
        lblTripDisp.setText(String.valueOf(tDisp));
        lblTripEmViagem.setText(String.valueOf(tViagem));

        // Portos
        List<Porto> portos = portoDAL.listarTodos();
        lblTotalPortos.setText(String.valueOf(portos.size()));
    }
}
