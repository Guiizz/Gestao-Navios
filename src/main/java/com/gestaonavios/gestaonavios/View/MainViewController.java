package com.gestaonavios.gestaonavios.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.util.List;

public class MainViewController {

    @FXML private StackPane contentArea;
    @FXML private Button btnNavios;
    @FXML private Button btnViagens;
    @FXML private Button btnCargas;
    @FXML private Button btnTripulacao;
    @FXML private Button btnPortos;
    @FXML private Button btnEstatisticas;

    private final List<Button> navButtons = new java.util.ArrayList<>();

    @FXML
    public void initialize() {
        navButtons.addAll(List.of(
                btnNavios, btnViagens, btnCargas,
                btnTripulacao, btnPortos, btnEstatisticas));
    }

    @FXML private void mostrarNavios()       { carregar("/com/gestaonavios/gestaonavios/NavioView.fxml",       btnNavios); }
    @FXML private void mostrarViagens()      { carregar("/com/gestaonavios/gestaonavios/ViagemView.fxml",      btnViagens); }
    @FXML private void mostrarCargas()       { carregar("/com/gestaonavios/gestaonavios/CargaView.fxml",       btnCargas); }
    @FXML private void mostrarTripulacao()   { carregar("/com/gestaonavios/gestaonavios/TripulanteView.fxml",  btnTripulacao); }
    @FXML private void mostrarPortos()       { carregar("/com/gestaonavios/gestaonavios/PortoView.fxml",       btnPortos); }
    @FXML private void mostrarEstatisticas() { carregar("/com/gestaonavios/gestaonavios/EstatisticasView.fxml",btnEstatisticas); }

    private void carregar(String caminho, Button botaoAtivo) {
        try {
            var url = getClass().getResource(caminho);
            if (url == null) {
                mostrarErro("Recurso não encontrado", "Não foi possível encontrar: " + caminho);
                return;
            }
            FXMLLoader loader = new FXMLLoader(url);
            Node vista = loader.load();
            contentArea.getChildren().setAll(vista);

            navButtons.forEach(b -> b.getStyleClass().remove("nav-button-active"));
            botaoAtivo.getStyleClass().add("nav-button-active");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao carregar a vista",
                    "Ocorreu um erro ao abrir a página.\n\nDetalhe: " + e.getMessage());
        }
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}