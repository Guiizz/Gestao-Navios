package com.gestaonavios.gestaonavios.View;

import com.gestaonavios.gestaonavios.BLL.CargaBLL;
import com.gestaonavios.gestaonavios.BLL.PortoBLL;
import com.gestaonavios.gestaonavios.BLL.TipoCargaBLL;
import com.gestaonavios.gestaonavios.Controller.CargaController;
import com.gestaonavios.gestaonavios.DAL.CargaDAL;
import com.gestaonavios.gestaonavios.DAL.NavioDAL;
import com.gestaonavios.gestaonavios.DAL.PortoDAL;
import com.gestaonavios.gestaonavios.DAL.TipoCargaDAL;
import com.gestaonavios.gestaonavios.DAL.TipoNavioDAL;
import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.Carga;
import com.gestaonavios.gestaonavios.Model.Porto;
import com.gestaonavios.gestaonavios.Model.TipoCarga;
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

import java.util.List;
import java.util.Optional;

public class CargaViewController {

    @FXML
    private TextField campoPesquisa;
    @FXML
    private TableView<Carga> tabela;
    @FXML
    private TableColumn<Carga, Integer> colId;
    @FXML
    private TableColumn<Carga, String> colDesignacao;
    @FXML
    private TableColumn<Carga, String> colTipo;
    @FXML
    private TableColumn<Carga, String> colPeso;
    @FXML
    private TableColumn<Carga, String> colVolume;
    @FXML
    private TableColumn<Carga, String> colPortoCarga;
    @FXML
    private TableColumn<Carga, String> colPortoDescarga;

    private CargaController cargaController;
    private PortoBLL portoBLL;
    private TipoCargaBLL tipoCargaBLL;

    @FXML
    public void initialize() {
        PortoDAL portoDAL = new PortoDAL();
        TipoNavioDAL tipoNavioDAL = new TipoNavioDAL();
        TipoCargaDAL tipoCargaDAL = new TipoCargaDAL();
        NavioDAL navioDAL = new NavioDAL(portoDAL, tipoNavioDAL);
        CargaDAL cargaDAL = new CargaDAL(tipoCargaDAL, portoDAL);
        ViagemDAL viagemDAL = new ViagemDAL(portoDAL, navioDAL);
        CargaBLL cargaBLL = new CargaBLL(cargaDAL, viagemDAL);
        portoBLL = new PortoBLL(portoDAL);
        tipoCargaBLL = new TipoCargaBLL(tipoCargaDAL);
        cargaController = new CargaController(cargaBLL);

        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getId()).asObject());
        colDesignacao.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDesignacao()));
        colTipo.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTipoCarga() != null ? d.getValue().getTipoCarga().getDesignacao() : "—"));
        colPeso.setCellValueFactory(d -> new SimpleStringProperty(
                String.format("%.2f", d.getValue().getPeso())));
        colVolume.setCellValueFactory(d -> new SimpleStringProperty(
                String.format("%.2f", d.getValue().getVolume())));
        colPortoCarga.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getPortoCarga() != null ? d.getValue().getPortoCarga().getNome() : "—"));
        colPortoDescarga.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getPortoDescarga() != null ? d.getValue().getPortoDescarga().getNome() : "—"));

        carregarDados();
    }

    @FXML
    private void atualizar() {
        carregarDados();
    }

    @FXML
    private void pesquisar() {
        String termo = campoPesquisa.getText().trim();
        if (termo.isEmpty()) {
            carregarDados();
        } else {
            tabela.setItems(FXCollections.observableArrayList(cargaController.pesquisarPorNome(termo)));
        }
    }

    @FXML
    private void nova() {
        mostrarDialogoCarga(null).ifPresent(c -> {
            carregarDados();
            AlertUtils.sucesso("Carga registada com sucesso.");
        });
    }

    @FXML
    private void editar() {
        Carga sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) {
            AlertUtils.aviso("Selecione uma carga para editar.");
            return;
        }
        mostrarDialogoCarga(sel).ifPresent(c -> {
            carregarDados();
            AlertUtils.sucesso("Carga atualizada com sucesso.");
        });
    }

    @FXML
    private void remover() {
        Carga sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) {
            AlertUtils.aviso("Selecione uma carga para remover.");
            return;
        }
        if (AlertUtils.confirmar("Confirma a remoção da carga '" + sel.getDesignacao() + "'?")) {
            try {
                cargaController.remover(sel.getId());
                carregarDados();
                AlertUtils.sucesso("Carga removida.");
            } catch (Exception e) {
                AlertUtils.erro(e.getMessage());
            }
        }
    }

    // ── Dialog ────────────────────────────────────────────────────────────────

    private Optional<Carga> mostrarDialogoCarga(Carga existente) {
        Dialog<Carga> dialog = new Dialog<>();
        dialog.setTitle(existente == null ? "Nova Carga" : "Editar Carga");
        dialog.setHeaderText(null);

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));

        TextField tfDesignacao = new TextField();
        TextField tfPeso = new TextField();
        TextField tfVolume = new TextField();

        List<TipoCarga> tipos = tipoCargaBLL.listarTodos();
        List<Porto> portos = portoBLL.listarTodos();

        ComboBox<TipoCarga> cbTipo = new ComboBox<>(FXCollections.observableArrayList(tipos));
        cbTipo.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(TipoCarga t) {
                return t == null ? "" : t.getDesignacao();
            }

            @Override
            public TipoCarga fromString(String s) {
                return null;
            }
        });

        ComboBox<Porto> cbPortoCarga = new ComboBox<>(FXCollections.observableArrayList(portos));
        ComboBox<Porto> cbPortoDescarga = new ComboBox<>(FXCollections.observableArrayList(portos));
        cbPortoCarga.setPromptText("(nenhum)");
        cbPortoDescarga.setPromptText("(nenhum)");
        javafx.util.StringConverter<Porto> portoConv = new javafx.util.StringConverter<>() {
            @Override
            public String toString(Porto p) {
                return p == null ? "" : p.getNome() + " (" + p.getCodigoUNLOCODE() + ")";
            }

            @Override
            public Porto fromString(String s) {
                return null;
            }
        };
        cbPortoCarga.setConverter(portoConv);
        cbPortoDescarga.setConverter(portoConv);

        if (existente != null) {
            tfDesignacao.setText(existente.getDesignacao());
            tfPeso.setText(String.valueOf(existente.getPeso()));
            tfVolume.setText(String.valueOf(existente.getVolume()));
            cbTipo.setValue(existente.getTipoCarga());
            cbPortoCarga.setValue(existente.getPortoCarga());
            cbPortoDescarga.setValue(existente.getPortoDescarga());
        }

        int r = 0;
        form.add(new Label("Designação:"), 0, r);
        form.add(tfDesignacao, 1, r++);
        form.add(new Label("Tipo de carga:"), 0, r);
        form.add(cbTipo, 1, r++);
        form.add(new Label("Peso (t):"), 0, r);
        form.add(tfPeso, 1, r++);
        form.add(new Label("Volume (m³):"), 0, r);
        form.add(tfVolume, 1, r++);
        form.add(new Label("Porto de carga:"), 0, r);
        form.add(cbPortoCarga, 1, r++);
        form.add(new Label("Porto descarga:"), 0, r);
        form.add(cbPortoDescarga, 1, r++);

        tfDesignacao.setPrefWidth(220);
        cbTipo.setPrefWidth(220);
        cbPortoCarga.setPrefWidth(220);
        cbPortoDescarga.setPrefWidth(220);

        Label lblErro = new Label();
        lblErro.setStyle("-fx-text-fill: #d33;");
        lblErro.setWrapText(true);
        lblErro.setMaxWidth(320);
        form.add(lblErro, 0, r, 2, 1);

        dialog.getDialogPane().setContent(form);

        ValidacaoUI val = new ValidacaoUI(lblErro);
        ValidacaoUI.limparAoEditar(tfDesignacao, tfPeso, tfVolume);
        ValidacaoUI.limparAoEditar(cbTipo, cbPortoCarga, cbPortoDescarga);

        Carga[] resultado = new Carga[1];
        Button btnOk = (Button) dialog.getDialogPane().lookupButton(btnGuardar);
        btnOk.addEventFilter(ActionEvent.ACTION, ev -> {
            val.reset();

            String des = tfDesignacao.getText().trim();
            val.verificar(tfDesignacao, () -> ValidacaoUtils.exigirTexto(des, "A designação da carga"));
            if (cbTipo.getValue() == null) val.marcar(cbTipo, "O tipo de carga é obrigatório.");

            double peso = 0;
            try {
                peso = Double.parseDouble(tfPeso.getText().trim().replace(",", "."));
                double p = peso;
                val.verificar(tfPeso, () -> ValidacaoUtils.exigirPositivo(p, "O peso"));
            } catch (NumberFormatException e) {
                val.marcar(tfPeso, "Peso inválido — indique um número (ex: 25000).");
            }

            double volume = 0;
            try {
                volume = Double.parseDouble(tfVolume.getText().trim().replace(",", "."));
                double v = volume;
                val.verificar(tfVolume, () -> ValidacaoUtils.exigirPositivo(v, "O volume"));
            } catch (NumberFormatException e) {
                val.marcar(tfVolume, "Volume inválido — indique um número (ex: 26000).");
            }

            Porto pCarga = cbPortoCarga.getValue();
            Porto pDescarga = cbPortoDescarga.getValue();
            if (pCarga == null) val.marcar(cbPortoCarga, "O porto de carga é obrigatório.");
            if (pDescarga == null) val.marcar(cbPortoDescarga, "O porto de descarga é obrigatório.");
            if (pCarga != null && pDescarga != null && pCarga.getId() == pDescarga.getId())
                val.marcar(cbPortoDescarga, "O porto de carga e o porto de descarga não podem ser o mesmo.");

            if (!val.valido()) {
                ev.consume();
                return;
            }

            Carga carga = new Carga(existente != null ? existente.getId() : 0,
                    des, cbTipo.getValue(), volume, peso, pCarga, pDescarga);
            try {
                if (existente == null) cargaController.registar(carga.getDesignacao(), carga.getTipoCarga(),
                        carga.getVolume(), carga.getPeso(), carga.getPortoCarga(), carga.getPortoDescarga());
                else cargaController.atualizar(carga);
                resultado[0] = carga;
            } catch (Exception e) {
                lblErro.setText(e.getMessage());
                ev.consume();
            }
        });

        dialog.setResultConverter(bt -> bt == btnGuardar ? resultado[0] : null);
        return dialog.showAndWait();
    }

    private void carregarDados() {
        tabela.setItems(FXCollections.observableArrayList(cargaController.listarTodos()));
    }
}
