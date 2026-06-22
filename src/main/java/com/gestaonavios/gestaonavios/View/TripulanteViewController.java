package com.gestaonavios.gestaonavios.View;

import com.gestaonavios.gestaonavios.BLL.CompatibilidadeBLL;
import com.gestaonavios.gestaonavios.BLL.NavioBLL;
import com.gestaonavios.gestaonavios.BLL.TripulanteBLL;
import com.gestaonavios.gestaonavios.BLL.ViagemBLL;
import com.gestaonavios.gestaonavios.Controller.TripulanteController;
import com.gestaonavios.gestaonavios.DAL.CompatibilidadeCargaDAL;
import com.gestaonavios.gestaonavios.DAL.NavioDAL;
import com.gestaonavios.gestaonavios.DAL.PortoDAL;
import com.gestaonavios.gestaonavios.DAL.TipoCargaDAL;
import com.gestaonavios.gestaonavios.DAL.TipoNavioDAL;
import com.gestaonavios.gestaonavios.DAL.TripulanteDAL;
import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.Tripulante;
import com.gestaonavios.gestaonavios.Model.TripulacaoViagem;
import com.gestaonavios.gestaonavios.Model.Viagem;
import com.gestaonavios.gestaonavios.Model.enums.FuncaoTripulante;
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

public class TripulanteViewController {

    @FXML
    private TextField campoPesquisa;
    @FXML
    private TableView<Tripulante> tabela;
    @FXML
    private TableColumn<Tripulante, Integer> colId;
    @FXML
    private TableColumn<Tripulante, String> colNome;
    @FXML
    private TableColumn<Tripulante, String> colNif;
    @FXML
    private TableColumn<Tripulante, String> colFuncao;
    @FXML
    private TableColumn<Tripulante, String> colNacionalidade;
    @FXML
    private TableColumn<Tripulante, String> colDisponivel;
    @FXML
    private TableColumn<Tripulante, String> colCertificacoes;

    private TripulanteController tripulanteController;

    @FXML
    public void initialize() {
        PortoDAL portoDAL = new PortoDAL();
        TipoNavioDAL tipoNavioDAL = new TipoNavioDAL();
        NavioDAL navioDAL = new NavioDAL(portoDAL, tipoNavioDAL);
        TripulanteDAL tripulanteDAL = new TripulanteDAL();
        ViagemDAL viagemDAL = new ViagemDAL(portoDAL, navioDAL);
        TripulanteBLL tripulanteBLL = new TripulanteBLL(tripulanteDAL, viagemDAL);
        NavioBLL navioBLL = new NavioBLL(navioDAL, viagemDAL);
        TipoCargaDAL tipoCargaDAL = new TipoCargaDAL();
        CompatibilidadeCargaDAL compatibilidadeCargaDAL = new CompatibilidadeCargaDAL(tipoNavioDAL, tipoCargaDAL);
        CompatibilidadeBLL compatibilidadeBLL = new CompatibilidadeBLL(tipoNavioDAL, compatibilidadeCargaDAL);
        ViagemBLL viagemBLL = new ViagemBLL(viagemDAL, navioBLL, tripulanteBLL, compatibilidadeBLL);
        tripulanteController = new TripulanteController(tripulanteBLL, viagemBLL);

        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getId()).asObject());
        colNome.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNome()));
        colNif.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNif()));
        colFuncao.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getFuncao() != null ? d.getValue().getFuncao() : ""));
        colNacionalidade.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNacionalidade()));
        colDisponivel.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().isDisponivel() ? "Sim" : "Não"));
        colCertificacoes.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getCertificacoes() != null ? d.getValue().getCertificacoes() : ""));

        carregarDados();
    }

    @FXML
    private void atualizar() {
        carregarDados();
    }

    @FXML
    private void pesquisar() {
        String termo = campoPesquisa.getText().trim();
        if (termo.isEmpty()) carregarDados();
        else tabela.setItems(FXCollections.observableArrayList(tripulanteController.pesquisarPorNome(termo)));
    }

    @FXML
    private void novo() {
        mostrarDialogoTripulante(null).ifPresent(ok -> {
            carregarDados();
            AlertUtils.sucesso("Tripulante registado com sucesso.");
        });
    }

    @FXML
    private void editar() {
        Tripulante sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) {
            AlertUtils.aviso("Selecione um tripulante para editar.");
            return;
        }
        mostrarDialogoEditar(sel).ifPresent(ok -> {
            carregarDados();
            AlertUtils.sucesso("Tripulante atualizado.");
        });
    }

    @FXML
    private void remover() {
        Tripulante sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) {
            AlertUtils.aviso("Selecione um tripulante para remover.");
            return;
        }
        if (AlertUtils.confirmar("Confirma a remoção de '" + sel.getNome() + "'?")) {
            try {
                tripulanteController.remover(sel.getId());
                carregarDados();
                AlertUtils.sucesso("Tripulante removido.");
            } catch (Exception e) {
                AlertUtils.erro(e.getMessage());
            }
        }
    }

    @FXML
    private void historico() {
        Tripulante sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) {
            AlertUtils.aviso("Selecione um tripulante para ver o histórico.");
            return;
        }

        List<Viagem> viagens = tripulanteController.historicoPorTripulante(sel.getId());
        StringBuilder sb = new StringBuilder();
        if (viagens.isEmpty()) {
            sb.append("Sem viagens registadas para este tripulante.");
        } else {
            for (Viagem v : viagens) {
                String funcaoViagem = "—";
                for (TripulacaoViagem tv : v.getTripulacao()) {
                    if (tv.getTripulante() != null && tv.getTripulante().getId() == sel.getId()) {
                        funcaoViagem = tv.getFuncaoNaViagem().name();
                        break;
                    }
                }
                sb.append("Viagem #").append(v.getId())
                        .append("  ").append(v.getOrigem() != null ? v.getOrigem().getNome() : "?")
                        .append(" → ").append(v.getDestino() != null ? v.getDestino().getNome() : "?")
                        .append("  [").append(v.getEstado()).append("]")
                        .append("  Função: ").append(funcaoViagem)
                        .append("\n");
            }
        }

        Alert dlg = new Alert(Alert.AlertType.INFORMATION);
        dlg.setTitle("Histórico de Viagens");
        dlg.setHeaderText(sel.getNome());
        TextArea ta = new TextArea(sb.toString());
        ta.setEditable(false);
        ta.setPrefSize(500, 300);
        dlg.getDialogPane().setContent(ta);
        dlg.showAndWait();
    }

    // ── Dialogs ───────────────────────────────────────────────────────────────

    /**
     * Returns Object[] {nome, nif, funcao, nacionalidade, certificacoes, disponivel}
     */
    private Optional<Boolean> mostrarDialogoTripulante(Tripulante existente) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Novo Tripulante");
        dialog.setHeaderText(null);

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));

        TextField tfN = new TextField();
        TextField tfNif = new TextField();
        tfNif.setPromptText("ex: 123456789");
        TextField tfNac = new TextField();
        TextField tfCert = new TextField();
        tfCert.setPromptText("(opcional)");
        CheckBox cbDisp = new CheckBox("Disponível");
        cbDisp.setSelected(true);
        ComboBox<FuncaoTripulante> cbFunc = new ComboBox<>(
                FXCollections.observableArrayList(FuncaoTripulante.values()));

        int r = 0;
        form.add(new Label("Nome:"), 0, r);
        form.add(tfN, 1, r++);
        form.add(new Label("NIF:"), 0, r);
        form.add(tfNif, 1, r++);
        form.add(new Label("Função:"), 0, r);
        form.add(cbFunc, 1, r++);
        form.add(new Label("Nacionalidade:"), 0, r);
        form.add(tfNac, 1, r++);
        form.add(new Label("Certificações:"), 0, r);
        form.add(tfCert, 1, r++);
        form.add(cbDisp, 1, r++);

        tfN.setPrefWidth(220);
        cbFunc.setPrefWidth(220);

        Label lblErro = new Label();
        lblErro.setStyle("-fx-text-fill: #d33;");
        lblErro.setWrapText(true);
        lblErro.setMaxWidth(320);
        form.add(lblErro, 0, r, 2, 1);

        dialog.getDialogPane().setContent(form);

        ValidacaoUI val = new ValidacaoUI(lblErro);
        ValidacaoUI.limparAoEditar(tfN, tfNif, tfNac, tfCert);
        ValidacaoUI.limparAoEditar(cbFunc);

        Boolean[] resultado = new Boolean[1];
        Button btnOk = (Button) dialog.getDialogPane().lookupButton(btnGuardar);
        btnOk.addEventFilter(ActionEvent.ACTION, ev -> {
            val.reset();
            String nome = tfN.getText().trim();
            String nif = tfNif.getText().trim();
            String nac = tfNac.getText().trim();
            String cert = tfCert.getText().trim();
            FuncaoTripulante func = cbFunc.getValue();

            val.verificar(tfN, () -> ValidacaoUtils.exigirTexto(nome, "O nome do tripulante"));
            val.verificar(tfNif, () -> ValidacaoUtils.exigirTexto(nif, "O NIF do tripulante"));
            if (!nif.isEmpty()) val.verificar(tfNif, () -> ValidacaoUtils.exigirFormatoNif(nif));
            val.verificar(tfNac, () -> ValidacaoUtils.exigirTexto(nac, "A nacionalidade do tripulante"));
            if (func == null) val.marcar(cbFunc, "A função do tripulante é obrigatória.");

            if (!val.valido()) {
                ev.consume();
                return;
            }

            try {
                tripulanteController.registar(nome, nif, func, nac, cert, cbDisp.isSelected());
                resultado[0] = Boolean.TRUE;
            } catch (Exception e) {
                String m = e.getMessage();
                if (m != null && m.toUpperCase().contains("NIF")) val.marcar(tfNif, m);
                else lblErro.setText(m);
                ev.consume();
            }
        });

        dialog.setResultConverter(bt -> bt == btnGuardar ? resultado[0] : null);
        return dialog.showAndWait();
    }

    /**
     * Returns Object[] {novaFuncao, tripulanteAtualizado}
     */
    private Optional<Boolean> mostrarDialogoEditar(Tripulante existente) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Editar Tripulante");
        dialog.setHeaderText(null);

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));

        TextField tfN = new TextField(existente.getNome());
        TextField tfNif = new TextField(existente.getNif());
        TextField tfNac = new TextField(existente.getNacionalidade());
        TextField tfCert = new TextField(existente.getCertificacoes() != null ? existente.getCertificacoes() : "");
        CheckBox cbDisp = new CheckBox("Disponível");
        cbDisp.setSelected(existente.isDisponivel());
        ComboBox<FuncaoTripulante> cbFunc = new ComboBox<>(
                FXCollections.observableArrayList(FuncaoTripulante.values()));
        cbFunc.setValue(existente.getFuncaoEnum());

        int r = 0;
        form.add(new Label("Nome:"), 0, r);
        form.add(tfN, 1, r++);
        form.add(new Label("NIF:"), 0, r);
        form.add(tfNif, 1, r++);
        form.add(new Label("Função:"), 0, r);
        form.add(cbFunc, 1, r++);
        form.add(new Label("Nacionalidade:"), 0, r);
        form.add(tfNac, 1, r++);
        form.add(new Label("Certificações:"), 0, r);
        form.add(tfCert, 1, r++);
        form.add(cbDisp, 1, r++);

        tfN.setPrefWidth(220);
        cbFunc.setPrefWidth(220);

        Label lblErro = new Label();
        lblErro.setStyle("-fx-text-fill: #d33;");
        lblErro.setWrapText(true);
        lblErro.setMaxWidth(320);
        form.add(lblErro, 0, r, 2, 1);

        dialog.getDialogPane().setContent(form);

        ValidacaoUI val = new ValidacaoUI(lblErro);
        ValidacaoUI.limparAoEditar(tfN, tfNif, tfNac, tfCert);
        ValidacaoUI.limparAoEditar(cbFunc);

        Boolean[] resultado = new Boolean[1];
        Button btnOk = (Button) dialog.getDialogPane().lookupButton(btnGuardar);
        btnOk.addEventFilter(ActionEvent.ACTION, ev -> {
            val.reset();
            String nome = tfN.getText().trim();
            String nif = tfNif.getText().trim();
            String nac = tfNac.getText().trim();
            String cert = tfCert.getText().trim();
            FuncaoTripulante novaFuncao = cbFunc.getValue();

            val.verificar(tfN, () -> ValidacaoUtils.exigirTexto(nome, "O nome do tripulante"));
            val.verificar(tfNif, () -> ValidacaoUtils.exigirTexto(nif, "O NIF do tripulante"));
            if (!nif.isEmpty()) val.verificar(tfNif, () -> ValidacaoUtils.exigirFormatoNif(nif));
            val.verificar(tfNac, () -> ValidacaoUtils.exigirTexto(nac, "A nacionalidade do tripulante"));
            if (novaFuncao == null) val.marcar(cbFunc, "A função do tripulante é obrigatória.");

            if (!val.valido()) {
                ev.consume();
                return;
            }

            try {
                existente.setNome(nome);
                existente.setNif(nif);
                existente.setNacionalidade(nac);
                existente.setCertificacoes(cert);
                existente.setDisponivel(cbDisp.isSelected());
                if (novaFuncao != existente.getFuncaoEnum()) {
                    tripulanteController.alterarFuncao(existente, novaFuncao);
                    Tripulante reloaded = tripulanteController.buscarPorId(existente.getId());
                    if (reloaded != null) {
                        reloaded.setNome(nome);
                        reloaded.setNif(nif);
                        reloaded.setNacionalidade(nac);
                        reloaded.setCertificacoes(cert);
                        reloaded.setDisponivel(cbDisp.isSelected());
                        tripulanteController.atualizar(reloaded);
                    }
                } else {
                    tripulanteController.atualizar(existente);
                }
                resultado[0] = Boolean.TRUE;
            } catch (Exception e) {
                String m = e.getMessage();
                if (m != null && m.toUpperCase().contains("NIF")) val.marcar(tfNif, m);
                else lblErro.setText(m);
                ev.consume();
            }
        });

        dialog.setResultConverter(bt -> bt == btnGuardar ? resultado[0] : null);
        return dialog.showAndWait();
    }

    private void carregarDados() {
        tabela.setItems(FXCollections.observableArrayList(tripulanteController.listarTodos()));
    }
}
