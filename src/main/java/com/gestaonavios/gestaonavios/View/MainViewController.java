package View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

    @FXML private void mostrarNavios()       { carregar("/fxml/NavioView.fxml",       btnNavios); }
    @FXML private void mostrarViagens()      { carregar("/fxml/ViagemView.fxml",      btnViagens); }
    @FXML private void mostrarCargas()       { carregar("/fxml/CargaView.fxml",       btnCargas); }
    @FXML private void mostrarTripulacao()   { carregar("/fxml/TripulanteView.fxml",  btnTripulacao); }
    @FXML private void mostrarPortos()       { carregar("/fxml/PortoView.fxml",       btnPortos); }
    @FXML private void mostrarEstatisticas() { carregar("/fxml/EstatisticasView.fxml",btnEstatisticas); }

    private void carregar(String caminho, Button botaoAtivo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            Node vista = loader.load();
            contentArea.getChildren().setAll(vista);

            navButtons.forEach(b -> b.getStyleClass().remove("nav-button-active"));
            botaoAtivo.getStyleClass().add("nav-button-active");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
