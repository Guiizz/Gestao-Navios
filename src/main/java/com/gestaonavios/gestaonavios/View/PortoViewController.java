package View;

import BLL.PortoBLL;
import Controller.PortoController;
import DAL.CargaDAL;
import DAL.NavioDAL;
import DAL.PortoDAL;
import DAL.TipoCargaDAL;
import DAL.TipoNavioDAL;
import DAL.ViagemDAL;
import Model.Porto;
import Utils.AlertUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class PortoViewController {

    @FXML private TableView<Porto> tabela;
    @FXML private TableColumn<Porto, Integer> colId;
    @FXML private TableColumn<Porto, String>  colNome;
    @FXML private TableColumn<Porto, String>  colPais;
    @FXML private TableColumn<Porto, String>  colLocode;

    private PortoController portoController;

    @FXML
    public void initialize() {
        PortoDAL    portoDAL    = new PortoDAL();
        TipoNavioDAL tipoNavioDAL = new TipoNavioDAL();
        TipoCargaDAL tipoCargaDAL = new TipoCargaDAL();
        NavioDAL    navioDAL    = new NavioDAL(portoDAL, tipoNavioDAL);
        CargaDAL    cargaDAL    = new CargaDAL(tipoCargaDAL, portoDAL);
        ViagemDAL   viagemDAL   = new ViagemDAL(portoDAL, navioDAL);
        PortoBLL    portoBLL    = new PortoBLL(portoDAL, navioDAL, cargaDAL, viagemDAL);
        portoController = new PortoController(portoBLL);

        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getId()).asObject());
        colNome.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNome()));
        colPais.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPais()));
        colLocode.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCodigoUNLOCODE()));

        carregarDados();
    }

    @FXML private void atualizar() { carregarDados(); }

    @FXML
    private void novo() {
        mostrarDialogoPorto(null).ifPresent(p -> {
            try {
                portoController.registar(p.getNome(), p.getPais(), p.getCodigoUNLOCODE());
                carregarDados();
                AlertUtils.sucesso("Porto registado com sucesso.");
            } catch (Exception e) {
                AlertUtils.erro(e.getMessage());
            }
        });
    }

    @FXML
    private void editar() {
        Porto sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { AlertUtils.aviso("Selecione um porto para editar."); return; }
        mostrarDialogoPorto(sel).ifPresent(p -> {
            try {
                portoController.atualizar(p);
                carregarDados();
                AlertUtils.sucesso("Porto atualizado com sucesso.");
            } catch (Exception e) {
                AlertUtils.erro(e.getMessage());
            }
        });
    }

    @FXML
    private void remover() {
        Porto sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { AlertUtils.aviso("Selecione um porto para remover."); return; }
        if (AlertUtils.confirmar("Confirma a remoção do porto '" + sel.getNome() + "'?")) {
            try {
                portoController.remover(sel.getId());
                carregarDados();
                AlertUtils.sucesso("Porto removido.");
            } catch (Exception e) {
                AlertUtils.erro(e.getMessage());
            }
        }
    }

    // ── Dialog ────────────────────────────────────────────────────────────────

    private Optional<Porto> mostrarDialogoPorto(Porto existente) {
        Dialog<Porto> dialog = new Dialog<>();
        dialog.setTitle(existente == null ? "Novo Porto" : "Editar Porto");
        dialog.setHeaderText(null);

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10);
        form.setPadding(new Insets(20));

        TextField tfNome   = new TextField();
        TextField tfPais   = new TextField();
        TextField tfLocode = new TextField();
        tfLocode.setPromptText("ex: PTLEI, GBLON");

        if (existente != null) {
            tfNome.setText(existente.getNome());
            tfPais.setText(existente.getPais());
            tfLocode.setText(existente.getCodigoUNLOCODE());
        }

        form.add(new Label("Nome:"), 0, 0);    form.add(tfNome, 1, 0);
        form.add(new Label("País:"), 0, 1);    form.add(tfPais, 1, 1);
        form.add(new Label("UNLOCODE:"), 0, 2); form.add(tfLocode, 1, 2);

        tfNome.setPrefWidth(220); tfPais.setPrefWidth(220); tfLocode.setPrefWidth(220);
        dialog.getDialogPane().setContent(form);

        Node btnOk = dialog.getDialogPane().lookupButton(btnGuardar);
        btnOk.setDisable(tfNome.getText().isBlank());
        tfNome.textProperty().addListener((obs, o, n) -> btnOk.setDisable(n.trim().isBlank()));

        dialog.setResultConverter(bt -> {
            if (bt != btnGuardar) return null;
            int id = existente != null ? existente.getId() : 0;
            Porto p = new Porto(id, tfNome.getText().trim(), tfPais.getText().trim(), tfLocode.getText().trim());
            return p;
        });

        return dialog.showAndWait();
    }

    private void carregarDados() {
        tabela.setItems(FXCollections.observableArrayList(portoController.listarTodos()));
    }
}
