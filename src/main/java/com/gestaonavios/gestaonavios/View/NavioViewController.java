package com.gestaonavios.gestaonavios.View;

import com.gestaonavios.gestaonavios.BLL.CompatibilidadeBLL;
import com.gestaonavios.gestaonavios.BLL.NavioBLL;
import com.gestaonavios.gestaonavios.BLL.PortoBLL;
import com.gestaonavios.gestaonavios.BLL.TipoNavioBLL;
import com.gestaonavios.gestaonavios.Controller.NavioController;
import com.gestaonavios.gestaonavios.DAL.CompatibilidadeCargaDAL;
import com.gestaonavios.gestaonavios.DAL.NavioDAL;
import com.gestaonavios.gestaonavios.DAL.PortoDAL;
import com.gestaonavios.gestaonavios.DAL.TipoCargaDAL;
import com.gestaonavios.gestaonavios.DAL.TipoNavioDAL;
import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.Navio;
import com.gestaonavios.gestaonavios.Model.Porto;
import com.gestaonavios.gestaonavios.Model.TipoCarga;
import com.gestaonavios.gestaonavios.Model.enums.EstadoOperacional;
import com.gestaonavios.gestaonavios.Model.enums.TipoNavioEnums;
import com.gestaonavios.gestaonavios.Utils.AlertUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NavioViewController {

    @FXML
    private TextField campoPesquisa;
    @FXML
    private TableView<Navio> tabela;
    @FXML
    private TableColumn<Navio, Integer> colId;
    @FXML
    private TableColumn<Navio, String> colNome;
    @FXML
    private TableColumn<Navio, String> colImo;
    @FXML
    private TableColumn<Navio, String> colTipo;
    @FXML
    private TableColumn<Navio, String> colEstado;
    @FXML
    private TableColumn<Navio, String> colCapacidade;
    @FXML
    private TableColumn<Navio, Integer> colTanques;
    @FXML
    private TableColumn<Navio, String> colBandeira;
    @FXML
    private TableColumn<Navio, String> colAno;
    @FXML
    private TableColumn<Navio, String> colPorto;

    private NavioController navioController;
    private CompatibilidadeBLL compatibilidadeBLL;

    @FXML
    public void initialize() {
        PortoDAL portoDAL = new PortoDAL();
        TipoNavioDAL tipoNavioDAL = new TipoNavioDAL();
        NavioDAL navioDAL = new NavioDAL(portoDAL, tipoNavioDAL);
        ViagemDAL viagemDAL = new ViagemDAL(portoDAL, navioDAL);
        NavioBLL navioBLL = new NavioBLL(navioDAL, viagemDAL);
        PortoBLL portoBLL = new PortoBLL(portoDAL);
        TipoNavioBLL tipoNavioBLL = new TipoNavioBLL(tipoNavioDAL);
        navioController = new NavioController(navioBLL, portoBLL, tipoNavioBLL);
        TipoCargaDAL tipoCargaDAL = new TipoCargaDAL();
        CompatibilidadeCargaDAL compatibilidadeCargaDAL = new CompatibilidadeCargaDAL(tipoNavioDAL, tipoCargaDAL);
        compatibilidadeBLL = new CompatibilidadeBLL(tipoNavioDAL, compatibilidadeCargaDAL);

        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getId()).asObject());
        colNome.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNome()));
        colImo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCodigoIMO()));
        colTipo.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTipoNavio() != null ? d.getValue().getTipoNavio().name() : ""));
        colEstado.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getEstadoOperacional() != null ? d.getValue().getEstadoOperacional().name() : ""));
        colCapacidade.setCellValueFactory(d -> new SimpleStringProperty(
                String.format("%.1f", d.getValue().getCapacidadeMaxima())));
        colTanques.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getNumeroTanques()).asObject());
        colBandeira.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getBandeira()));
        colAno.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getAnoFabrico())));
        colPorto.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getPortoAtual() != null ? d.getValue().getPortoAtual().getNome() : "—"));

        tabela.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && tabela.getSelectionModel().getSelectedItem() != null) {
                verDetalhes();
            }
        });

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
            tabela.setItems(FXCollections.observableArrayList(navioController.pesquisarPorNome(termo)));
        }
    }

    @FXML
    private void novo() {
        mostrarDialogoNavio(null).ifPresent(navio -> {
            try {
                navioController.registar(navio.getNome(), navio.getCodigoIMO(),
                        navio.getTipoNavio(), navio.getCapacidadeMaxima(),
                        navio.getNumeroTanques(), navio.getBandeira(),
                        navio.getAnoFabrico(), navio.getEstadoOperacional(),
                        navio.getPortoAtual());
                carregarDados();
                AlertUtils.sucesso("Navio registado com sucesso.");
            } catch (Exception e) {
                AlertUtils.erro(e.getMessage());
            }
        });
    }

    @FXML
    private void editar() {
        Navio selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            AlertUtils.aviso("Selecione um navio para editar.");
            return;
        }
        mostrarDialogoNavio(selecionado).ifPresent(navio -> {
            try {
                navioController.atualizar(navio);
                carregarDados();
                AlertUtils.sucesso("Navio atualizado com sucesso.");
            } catch (Exception e) {
                AlertUtils.erro(e.getMessage());
            }
        });
    }

    @FXML
    private void remover() {
        Navio selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            AlertUtils.aviso("Selecione um navio para remover.");
            return;
        }
        if (AlertUtils.confirmar("Confirma a remoção do navio '" + selecionado.getNome() + "'?")) {
            try {
                navioController.remover(selecionado.getId());
                carregarDados();
                AlertUtils.sucesso("Navio removido.");
            } catch (Exception e) {
                AlertUtils.erro(e.getMessage());
            }
        }
    }

    @FXML
    private void verDetalhes() {
        Navio n = tabela.getSelectionModel().getSelectedItem();
        if (n == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("ID:               ").append(n.getId()).append("\n");
        sb.append("Nome:             ").append(n.getNome()).append("\n");
        sb.append("Código IMO:       ").append(n.getCodigoIMO()).append("\n");
        sb.append("Tipo:             ").append(n.getTipoNavio()).append("\n");
        sb.append("Estado:           ").append(n.getEstadoOperacional()).append("\n");
        sb.append("Capacidade máx.:  ").append(n.getCapacidadeMaxima()).append(" t\n");
        sb.append("Nº de tanques:    ").append(n.getNumeroTanques()).append("\n");
        sb.append("Slots por viagem: ").append(compatibilidadeBLL.maxCargasPorViagem(n.getTipoNavio())).append("\n");
        sb.append("Bandeira:         ").append(n.getBandeira()).append("\n");
        sb.append("Ano de fabrico:   ").append(n.getAnoFabrico()).append("\n");
        sb.append("Porto atual:      ").append(n.getPortoAtual() != null ? n.getPortoAtual().getNome() : "—").append("\n");

        List<TipoCarga> compat = compatibilidadeBLL.cargasCompativeis(n.getTipoNavio());
        sb.append("\nCargas compatíveis:\n");
        if (compat.isEmpty()) {
            sb.append("  (nenhuma)\n");
        } else {
            for (TipoCarga tc : compat) {
                List<String> props = new ArrayList<>();
                if (tc.isInflamavel()) props.add("inflamável");
                if (tc.isCorrosiva()) props.add("corrosiva");
                if (tc.isToxica()) props.add("tóxica");
                sb.append("  • ").append(tc.getDesignacao());
                if (!props.isEmpty()) sb.append("  [").append(String.join(", ", props)).append("]");
                sb.append("\n");
            }
        }

        Alert dlg = new Alert(Alert.AlertType.INFORMATION);
        dlg.setTitle("Detalhes do Navio");
        dlg.setHeaderText(n.getNome());
        TextArea ta = new TextArea(sb.toString());
        ta.setEditable(false);
        ta.setPrefSize(480, 300);
        dlg.getDialogPane().setContent(ta);
        dlg.showAndWait();
    }

    // ── Dialog ────────────────────────────────────────────────────────────────

    private Optional<Navio> mostrarDialogoNavio(Navio existente) {
        Dialog<Navio> dialog = new Dialog<>();
        dialog.setTitle(existente == null ? "Novo Navio" : "Editar Navio");
        dialog.setHeaderText(null);

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));

        TextField tfNome = new TextField();
        TextField tfImo = new TextField();
        TextField tfCapacidade = new TextField();
        TextField tfTanques = new TextField();
        TextField tfBandeira = new TextField();
        TextField tfAno = new TextField();
        ComboBox<TipoNavioEnums> cbTipo = new ComboBox<>(FXCollections.observableArrayList(TipoNavioEnums.values()));
        ComboBox<EstadoOperacional> cbEstado = new ComboBox<>(FXCollections.observableArrayList(EstadoOperacional.values()));
        ComboBox<Porto> cbPorto = new ComboBox<>(FXCollections.observableArrayList(navioController.listarPortos()));
        cbPorto.setPromptText("(nenhum)");
        cbPorto.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Porto p) {
                return p == null ? "" : p.getNome() + " (" + p.getCodigoUNLOCODE() + ")";
            }

            @Override
            public Porto fromString(String s) {
                return null;
            }
        });

        if (existente != null) {
            tfNome.setText(existente.getNome());
            tfImo.setText(existente.getCodigoIMO());
            cbTipo.setValue(existente.getTipoNavio());
            tfCapacidade.setText(String.valueOf(existente.getCapacidadeMaxima()));
            tfTanques.setText(String.valueOf(existente.getNumeroTanques()));
            tfBandeira.setText(existente.getBandeira());
            tfAno.setText(String.valueOf(existente.getAnoFabrico()));
            cbEstado.setValue(existente.getEstadoOperacional());
            cbPorto.setValue(existente.getPortoAtual());
        } else {
            cbEstado.setValue(EstadoOperacional.ATIVO);
        }

        int row = 0;
        form.add(new Label("Nome:"), 0, row);
        form.add(tfNome, 1, row++);
        form.add(new Label("Código IMO:"), 0, row);
        form.add(tfImo, 1, row++);
        form.add(new Label("Tipo:"), 0, row);
        form.add(cbTipo, 1, row++);
        form.add(new Label("Capacidade máx. (t):"), 0, row);
        form.add(tfCapacidade, 1, row++);
        form.add(new Label("Nº de tanques:"), 0, row);
        form.add(tfTanques, 1, row++);
        form.add(new Label("Bandeira:"), 0, row);
        form.add(tfBandeira, 1, row++);
        form.add(new Label("Ano de fabrico:"), 0, row);
        form.add(tfAno, 1, row++);
        form.add(new Label("Estado operacional:"), 0, row);
        form.add(cbEstado, 1, row++);
        form.add(new Label("Porto atual:"), 0, row);
        form.add(cbPorto, 1, row++);

        tfNome.setPrefWidth(220);
        tfImo.setPrefWidth(220);
        tfCapacidade.setPrefWidth(220);
        tfTanques.setPrefWidth(220);
        cbTipo.setPrefWidth(220);
        cbEstado.setPrefWidth(220);
        cbPorto.setPrefWidth(220);

        dialog.getDialogPane().setContent(form);

        Node btnOk = dialog.getDialogPane().lookupButton(btnGuardar);
        btnOk.setDisable(tfNome.getText().isBlank());
        tfNome.textProperty().addListener((obs, o, n) -> btnOk.setDisable(n.trim().isBlank()));

        dialog.setResultConverter(bt -> {
            if (bt != btnGuardar) return null;
            try {
                String nome = tfNome.getText().trim();
                String imo = tfImo.getText().trim();
                TipoNavioEnums tipo = cbTipo.getValue();
                double capacidade = Double.parseDouble(tfCapacidade.getText().trim().replace(",", "."));
                int tanques = Integer.parseInt(tfTanques.getText().trim());
                String bandeira = tfBandeira.getText().trim();
                int ano = Integer.parseInt(tfAno.getText().trim());
                EstadoOperacional estado = cbEstado.getValue();
                Porto porto = cbPorto.getValue();
                int id = existente != null ? existente.getId() : 0;
                return new Navio(id, nome, imo, tipo, capacidade, tanques, bandeira, ano, estado, porto);
            } catch (NumberFormatException e) {
                AlertUtils.erro("Verifique os campos numéricos (capacidade, tanques, ano).");
                return null;
            }
        });

        return dialog.showAndWait();
    }

    private void carregarDados() {
        tabela.setItems(FXCollections.observableArrayList(navioController.listarTodos()));
    }
}
