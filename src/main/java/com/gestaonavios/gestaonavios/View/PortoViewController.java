package com.gestaonavios.gestaonavios.View;

import com.gestaonavios.gestaonavios.BLL.PortoBLL;
import com.gestaonavios.gestaonavios.Controller.PortoController;
import com.gestaonavios.gestaonavios.DAL.CargaDAL;
import com.gestaonavios.gestaonavios.DAL.NavioDAL;
import com.gestaonavios.gestaonavios.DAL.PortoDAL;
import com.gestaonavios.gestaonavios.DAL.TipoCargaDAL;
import com.gestaonavios.gestaonavios.DAL.TipoNavioDAL;
import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.Porto;
import com.gestaonavios.gestaonavios.Utils.AlertUtils;
import com.gestaonavios.gestaonavios.Utils.ValidacaoUI;
import com.gestaonavios.gestaonavios.Utils.ValidacaoUtils;
import javafx.event.ActionEvent;
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

    @FXML
    private TableView<Porto> tabela;
    @FXML
    private TableColumn<Porto, Integer> colId;
    @FXML
    private TableColumn<Porto, String> colNome;
    @FXML
    private TableColumn<Porto, String> colPais;
    @FXML
    private TableColumn<Porto, String> colLocode;

    private PortoController portoController;

    @FXML
    public void initialize() {
        PortoDAL portoDAL = new PortoDAL();
        TipoNavioDAL tipoNavioDAL = new TipoNavioDAL();
        TipoCargaDAL tipoCargaDAL = new TipoCargaDAL();
        NavioDAL navioDAL = new NavioDAL(portoDAL, tipoNavioDAL);
        CargaDAL cargaDAL = new CargaDAL(tipoCargaDAL, portoDAL);
        ViagemDAL viagemDAL = new ViagemDAL(portoDAL, navioDAL);
        PortoBLL portoBLL = new PortoBLL(portoDAL, navioDAL, cargaDAL, viagemDAL);
        portoController = new PortoController(portoBLL);

        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getId()).asObject());
        colNome.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNome()));
        colPais.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPais()));
        colLocode.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCodigoUNLOCODE()));

        carregarDados();
    }

    @FXML
    private void atualizar() {
        carregarDados();
    }

    @FXML
    private void novo() {
        mostrarDialogoPorto(null).ifPresent(p -> {
            carregarDados();
            AlertUtils.sucesso("Porto registado com sucesso.");
        });
    }

    @FXML
    private void editar() {
        Porto sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) {
            AlertUtils.aviso("Selecione um porto para editar.");
            return;
        }
        mostrarDialogoPorto(sel).ifPresent(p -> {
            carregarDados();
            AlertUtils.sucesso("Porto atualizado com sucesso.");
        });
    }

    @FXML
    private void remover() {
        Porto sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) {
            AlertUtils.aviso("Selecione um porto para remover.");
            return;
        }
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
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));

        TextField tfNome = new TextField();
        TextField tfPais = new TextField();
        TextField tfLocode = new TextField();
        tfLocode.setPromptText("ex: PTLEI, GBLON");

        if (existente != null) {
            tfNome.setText(existente.getNome());
            tfPais.setText(existente.getPais());
            tfLocode.setText(existente.getCodigoUNLOCODE());
        }

        form.add(new Label("Nome:"), 0, 0);
        form.add(tfNome, 1, 0);
        form.add(new Label("País:"), 0, 1);
        form.add(tfPais, 1, 1);
        form.add(new Label("UNLOCODE:"), 0, 2);
        form.add(tfLocode, 1, 2);

        tfNome.setPrefWidth(220);
        tfPais.setPrefWidth(220);
        tfLocode.setPrefWidth(220);

        Label lblErro = new Label();
        lblErro.setStyle("-fx-text-fill: #d33;");
        lblErro.setWrapText(true);
        lblErro.setMaxWidth(320);
        form.add(lblErro, 0, 3, 2, 1);

        dialog.getDialogPane().setContent(form);

        ValidacaoUI val = new ValidacaoUI(lblErro);
        ValidacaoUI.limparAoEditar(tfNome, tfPais, tfLocode);

        Porto[] resultado = new Porto[1];
        Button btnOk = (Button) dialog.getDialogPane().lookupButton(btnGuardar);
        btnOk.addEventFilter(ActionEvent.ACTION, ev -> {
            val.reset();

            String nome = tfNome.getText().trim();
            String pais = tfPais.getText().trim();
            String locode = tfLocode.getText().trim().toUpperCase();

            val.verificar(tfNome, () -> ValidacaoUtils.exigirTexto(nome, "O nome do porto"));
            val.verificar(tfPais, () -> ValidacaoUtils.exigirTexto(pais, "O país do porto"));
            val.verificar(tfLocode, () -> ValidacaoUtils.exigirTexto(locode, "O código UNLOCODE"));
            if (!locode.isEmpty())
                val.verificar(tfLocode, () -> ValidacaoUtils.exigirFormatoUnlocode(locode));

            if (!val.valido()) {
                ev.consume();
                return;
            }

            Porto porto = new Porto(existente != null ? existente.getId() : 0, nome, pais, locode);
            try {
                if (existente == null) portoController.registar(porto.getNome(), porto.getPais(), porto.getCodigoUNLOCODE());
                else portoController.atualizar(porto);
                resultado[0] = porto;
            } catch (Exception e) {
                String m = e.getMessage();
                if (m != null && m.toUpperCase().contains("UNLOCODE")) val.marcar(tfLocode, m);
                else lblErro.setText(m);
                ev.consume();
            }
        });

        dialog.setResultConverter(bt -> bt == btnGuardar ? resultado[0] : null);
        return dialog.showAndWait();
    }

    private void carregarDados() {
        tabela.setItems(FXCollections.observableArrayList(portoController.listarTodos()));
    }
}
