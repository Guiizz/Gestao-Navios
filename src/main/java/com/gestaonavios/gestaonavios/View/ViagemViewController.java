package com.gestaonavios.gestaonavios.View;

import com.gestaonavios.gestaonavios.BLL.CargaBLL;
import com.gestaonavios.gestaonavios.BLL.CompatibilidadeBLL;
import com.gestaonavios.gestaonavios.BLL.NavioBLL;
import com.gestaonavios.gestaonavios.BLL.PortoBLL;
import com.gestaonavios.gestaonavios.BLL.TanqueBLL;
import com.gestaonavios.gestaonavios.BLL.TripulanteBLL;
import com.gestaonavios.gestaonavios.BLL.ViagemBLL;
import com.gestaonavios.gestaonavios.Controller.ViagemController;
import com.gestaonavios.gestaonavios.DAL.CargaDAL;
import com.gestaonavios.gestaonavios.DAL.CompatibilidadeCargaDAL;
import com.gestaonavios.gestaonavios.DAL.NavioDAL;
import com.gestaonavios.gestaonavios.DAL.PortoDAL;
import com.gestaonavios.gestaonavios.DAL.TanqueDAL;
import com.gestaonavios.gestaonavios.DAL.TipoCargaDAL;
import com.gestaonavios.gestaonavios.DAL.TipoNavioDAL;
import com.gestaonavios.gestaonavios.DAL.TripulanteDAL;
import com.gestaonavios.gestaonavios.DAL.ViagemDAL;
import com.gestaonavios.gestaonavios.Model.AtribuicaoCarga;
import com.gestaonavios.gestaonavios.Model.Carga;
import com.gestaonavios.gestaonavios.Model.Navio;
import com.gestaonavios.gestaonavios.Model.Porto;
import com.gestaonavios.gestaonavios.Model.Tanque;
import com.gestaonavios.gestaonavios.Model.Tripulante;
import com.gestaonavios.gestaonavios.Model.TripulacaoViagem;
import com.gestaonavios.gestaonavios.Model.Viagem;
import com.gestaonavios.gestaonavios.Model.enums.EstadoViagem;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ViagemViewController {

    // ── Viagens table ─────────────────────────────────────────────────────────
    @FXML
    private TableView<Viagem> tabelaViagens;
    @FXML
    private TableColumn<Viagem, Integer> colId;
    @FXML
    private TableColumn<Viagem, String> colOrigem;
    @FXML
    private TableColumn<Viagem, String> colDestino;
    @FXML
    private TableColumn<Viagem, String> colPartida;
    @FXML
    private TableColumn<Viagem, String> colChegadaP;
    @FXML
    private TableColumn<Viagem, String> colChegadaR;
    @FXML
    private TableColumn<Viagem, String> colEstado;
    @FXML
    private TableColumn<Viagem, String> colNavio;
    @FXML
    private TableColumn<Viagem, Integer> colNCargas;
    @FXML
    private TableColumn<Viagem, Integer> colNTrip;

    // ── Cargas sub-table ──────────────────────────────────────────────────────
    @FXML
    private TableView<AtribuicaoCarga> tabelaCargas;
    @FXML
    private TableColumn<AtribuicaoCarga, String> colCargaDesig;
    @FXML
    private TableColumn<AtribuicaoCarga, String> colCargaTipo;
    @FXML
    private TableColumn<AtribuicaoCarga, String> colCargaPeso;
    @FXML
    private TableColumn<AtribuicaoCarga, String> colCargaVolume;

    // ── Tripulação sub-table ──────────────────────────────────────────────────
    @FXML
    private TableView<TripulacaoViagem> tabelaTripulacao;
    @FXML
    private TableColumn<TripulacaoViagem, String> colTripNome;
    @FXML
    private TableColumn<TripulacaoViagem, String> colTripFuncao;
    @FXML
    private TableColumn<TripulacaoViagem, String> colTripEmbarque;
    @FXML
    private TableColumn<TripulacaoViagem, String> colTripDesembarque;

    private ViagemController viagemController;

    @FXML
    public void initialize() {
        PortoDAL portoDAL = new PortoDAL();
        TipoNavioDAL tipoNavioDAL = new TipoNavioDAL();
        TipoCargaDAL tipoCargaDAL = new TipoCargaDAL();
        NavioDAL navioDAL = new NavioDAL(portoDAL, tipoNavioDAL);
        CargaDAL cargaDAL = new CargaDAL(tipoCargaDAL, portoDAL);
        TripulanteDAL tripulanteDAL = new TripulanteDAL();
        ViagemDAL viagemDAL = new ViagemDAL(portoDAL, navioDAL);

        PortoBLL portoBLL = new PortoBLL(portoDAL);
        CargaBLL cargaBLL = new CargaBLL(cargaDAL, viagemDAL);
        TripulanteBLL tripulanteBLL = new TripulanteBLL(tripulanteDAL, viagemDAL);
        NavioBLL navioBLL = new NavioBLL(navioDAL, viagemDAL);
        CompatibilidadeCargaDAL compatibilidadeCargaDAL = new CompatibilidadeCargaDAL(tipoNavioDAL, tipoCargaDAL);
        CompatibilidadeBLL compatibilidadeBLL = new CompatibilidadeBLL(tipoNavioDAL, compatibilidadeCargaDAL);
        ViagemBLL viagemBLL = new ViagemBLL(viagemDAL, navioBLL, tripulanteBLL, compatibilidadeBLL);
        TanqueBLL tanqueBLL = new TanqueBLL(new TanqueDAL());
        viagemController = new ViagemController(viagemBLL, navioBLL, portoBLL, cargaBLL, tripulanteBLL, tanqueBLL);

        // Viagens table columns
        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getId()).asObject());
        colOrigem.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getOrigem() != null ? d.getValue().getOrigem().getNome() : "—"));
        colDestino.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDestino() != null ? d.getValue().getDestino().getNome() : "—"));
        colPartida.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDataPartida() != null ? d.getValue().getDataPartida().toString() : "—"));
        colChegadaP.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDataChegadaPrevista() != null ? d.getValue().getDataChegadaPrevista().toString() : "—"));
        colChegadaR.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDataChegadaReal() != null ? d.getValue().getDataChegadaReal().toString() : "—"));
        colEstado.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getEstado() != null ? d.getValue().getEstado().name() : "—"));
        colNavio.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getNavio() != null ? d.getValue().getNavio().getNome() : "—"));
        colNCargas.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getCargas().size()).asObject());
        colNTrip.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getTripulacao().size()).asObject());

        // Cargas sub-table columns
        colCargaDesig.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getCarga() != null ? d.getValue().getCarga().getDesignacao() : "—"));
        colCargaTipo.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getCarga() != null && d.getValue().getCarga().getTipoCarga() != null
                        ? d.getValue().getCarga().getTipoCarga().getDesignacao() : "—"));
        colCargaPeso.setCellValueFactory(d -> new SimpleStringProperty(
                String.format("%.2f", d.getValue().getPesoAtribuido())));
        colCargaVolume.setCellValueFactory(d -> new SimpleStringProperty(
                String.format("%.2f", d.getValue().getVolumeAtribuido())));

        // Tripulação sub-table columns
        colTripNome.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTripulante() != null ? d.getValue().getTripulante().getNome() : "—"));
        colTripFuncao.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getFuncaoNaViagem() != null ? d.getValue().getFuncaoNaViagem().name() : "—"));
        colTripEmbarque.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDataEmbarque() != null ? d.getValue().getDataEmbarque().toString() : "—"));
        colTripDesembarque.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDataDesembarque() != null ? d.getValue().getDataDesembarque().toString() : "—"));

        // Update detail tables when viagem selection changes
        tabelaViagens.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, novo) -> atualizarDetalheViagem(novo));

        carregarDados();
    }

    @FXML
    private void atualizar() {
        carregarDados();
    }

    @FXML
    private void nova() {
        List<Porto> portos = viagemController.listarPortos();
        List<Navio> navios = viagemController.listarNaviosDisponiveis();

        if (portos.size() < 2) {
            AlertUtils.aviso("São necessários pelo menos dois portos registados para criar uma viagem.");
            return;
        }
        if (navios.isEmpty()) {
            AlertUtils.aviso("Não há navios disponíveis (ATIVO e sem viagem ativa).");
            return;
        }

        mostrarDialogoCriarViagem(navios, portos).ifPresent(ok -> {
            carregarDados();
            AlertUtils.sucesso("Viagem criada com sucesso.");
        });
    }

    @FXML
    private void editar() {
        Viagem sel = tabelaViagens.getSelectionModel().getSelectedItem();
        if (sel == null) {
            AlertUtils.aviso("Selecione uma viagem para editar.");
            return;
        }
        if (sel.getEstado() != EstadoViagem.PLANEADA) {
            AlertUtils.aviso("Só é possível editar viagens no estado PLANEADA.");
            return;
        }
        mostrarDialogoEditarViagem(sel).ifPresent(ok -> {
            carregarDados();
            AlertUtils.sucesso("Viagem atualizada.");
        });
    }

    @FXML
    private void avancarEstado() {
        Viagem sel = tabelaViagens.getSelectionModel().getSelectedItem();
        if (sel == null) {
            AlertUtils.aviso("Selecione uma viagem para avançar o estado.");
            return;
        }
        if (sel.getEstado() != EstadoViagem.PLANEADA && sel.getEstado() != EstadoViagem.EM_CURSO) {
            AlertUtils.aviso("A viagem não pode ter o estado avançado no estado atual.");
            return;
        }

        LocalDate chegadaReal = null;
        if (sel.getEstado() == EstadoViagem.EM_CURSO) {
            TextInputDialog dlg = new TextInputDialog(LocalDate.now().toString());
            dlg.setTitle("Data de Chegada Real");
            dlg.setHeaderText(null);
            dlg.setContentText("Data de chegada real (AAAA-MM-DD):");
            Optional<String> res = dlg.showAndWait();
            if (res.isPresent() && !res.get().isBlank()) {
                try {
                    chegadaReal = LocalDate.parse(res.get().trim());
                } catch (Exception e) {
                    AlertUtils.erro("Formato de data inválido (use AAAA-MM-DD).");
                    return;
                }
            }
        }

        String prox = sel.getEstado() == EstadoViagem.PLANEADA ? "EM_CURSO" : "CONCLUIDA";
        if (!AlertUtils.confirmar("Avançar viagem #" + sel.getId() + " para " + prox + "?")) return;

        try {
            viagemController.avancarEstado(sel.getId(), chegadaReal);
            carregarDados();
            AlertUtils.sucesso("Estado avançado para " + prox + ".");
        } catch (Exception e) {
            AlertUtils.erro(e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        Viagem sel = tabelaViagens.getSelectionModel().getSelectedItem();
        if (sel == null) {
            AlertUtils.aviso("Selecione uma viagem para cancelar.");
            return;
        }
        if (sel.getEstado() != EstadoViagem.PLANEADA && sel.getEstado() != EstadoViagem.EM_CURSO) {
            AlertUtils.aviso("Só é possível cancelar viagens PLANEADA ou EM_CURSO.");
            return;
        }
        if (AlertUtils.confirmar("Confirma o cancelamento da viagem #" + sel.getId() + "?")) {
            try {
                viagemController.cancelarViagem(sel.getId());
                carregarDados();
                AlertUtils.sucesso("Viagem cancelada.");
            } catch (Exception e) {
                AlertUtils.erro(e.getMessage());
            }
        }
    }

    @FXML
    private void remover() {
        Viagem sel = tabelaViagens.getSelectionModel().getSelectedItem();
        if (sel == null) {
            AlertUtils.aviso("Selecione uma viagem para remover.");
            return;
        }
        if (AlertUtils.confirmar("Confirma a remoção da viagem #" + sel.getId() + "?")) {
            viagemController.remover(sel.getId());
            carregarDados();
            AlertUtils.sucesso("Viagem removida.");
        }
    }

    // ── Cargas ────────────────────────────────────────────────────────────────

    @FXML
    private void adicionarCarga() {
        Viagem sel = tabelaViagens.getSelectionModel().getSelectedItem();
        if (sel == null) {
            AlertUtils.aviso("Selecione uma viagem.");
            return;
        }
        if (sel.getEstado() != EstadoViagem.PLANEADA) {
            AlertUtils.aviso("Só é possível adicionar cargas a viagens no estado PLANEADA.");
            return;
        }

        // Collect IDs of cargos already in use in other active voyages
        List<Integer> emUso = new ArrayList<>();
        for (Viagem outra : viagemController.listarTodos()) {
            if (outra.getId() == sel.getId()) continue;
            if (outra.getEstado() != EstadoViagem.PLANEADA && outra.getEstado() != EstadoViagem.EM_CURSO) continue;
            for (AtribuicaoCarga ac : outra.getCargas()) emUso.add(ac.getCarga().getId());
        }

        List<Carga> compativeis = new ArrayList<>();
        for (Carga c : viagemController.listarCargas()) {
            if (sel.getNavio() != null && !viagemController.aceitaCarga(sel.getNavio(), c)) continue;
            boolean excluir = false;
            for (AtribuicaoCarga ac : sel.getCargas())
                if (ac.getCarga().getId() == c.getId()) {
                    excluir = true;
                    break;
                }
            if (!excluir) for (int id : emUso)
                if (id == c.getId()) {
                    excluir = true;
                    break;
                }
            if (!excluir) compativeis.add(c);
        }

        if (compativeis.isEmpty()) {
            AlertUtils.aviso("Não há cargas disponíveis compatíveis com este navio.");
            return;
        }

        mostrarDialogoAdicionarCarga(sel, compativeis).ifPresent(ok -> {
            carregarDados();
            tabelaViagens.getSelectionModel().select(
                    tabelaViagens.getItems().stream()
                            .filter(v -> v.getId() == sel.getId()).findFirst().orElse(null));
            AlertUtils.sucesso("Carga adicionada à viagem.");
        });
    }

    @FXML
    private void removerCarga() {
        Viagem sel = tabelaViagens.getSelectionModel().getSelectedItem();
        if (sel == null) {
            AlertUtils.aviso("Selecione uma viagem.");
            return;
        }
        if (sel.getEstado() != EstadoViagem.PLANEADA) {
            AlertUtils.aviso("Só é possível remover cargas de viagens no estado PLANEADA.");
            return;
        }

        AtribuicaoCarga ac = tabelaCargas.getSelectionModel().getSelectedItem();
        if (ac == null) {
            AlertUtils.aviso("Selecione uma carga na tabela de cargas.");
            return;
        }

        if (AlertUtils.confirmar("Remover carga '" + ac.getCarga().getDesignacao() + "' da viagem?")) {
            try {
                viagemController.removerCargaViagem(sel.getId(), ac.getCarga().getId());
                carregarDados();
                tabelaViagens.getSelectionModel().select(
                        tabelaViagens.getItems().stream()
                                .filter(v -> v.getId() == sel.getId()).findFirst().orElse(null));
                AlertUtils.sucesso("Carga removida da viagem.");
            } catch (Exception e) {
                AlertUtils.erro(e.getMessage());
            }
        }
    }

    // ── Tripulação ────────────────────────────────────────────────────────────

    @FXML
    private void adicionarTripulante() {
        Viagem sel = tabelaViagens.getSelectionModel().getSelectedItem();
        if (sel == null) {
            AlertUtils.aviso("Selecione uma viagem.");
            return;
        }
        if (sel.getEstado() != EstadoViagem.PLANEADA) {
            AlertUtils.aviso("Só é possível adicionar tripulantes a viagens no estado PLANEADA.");
            return;
        }

        List<Tripulante> disponiveis = viagemController.listarTripulantesDisponiveis();
        if (disponiveis.isEmpty()) {
            AlertUtils.aviso("Não há tripulantes disponíveis.");
            return;
        }

        mostrarDialogoAdicionarTripulante(sel, disponiveis).ifPresent(ok -> {
            carregarDados();
            tabelaViagens.getSelectionModel().select(
                    tabelaViagens.getItems().stream()
                            .filter(v -> v.getId() == sel.getId()).findFirst().orElse(null));
            AlertUtils.sucesso("Tripulante adicionado à viagem.");
        });
    }

    @FXML
    private void removerTripulante() {
        Viagem sel = tabelaViagens.getSelectionModel().getSelectedItem();
        if (sel == null) {
            AlertUtils.aviso("Selecione uma viagem.");
            return;
        }
        if (sel.getEstado() != EstadoViagem.PLANEADA) {
            AlertUtils.aviso("Só é possível remover tripulantes de viagens no estado PLANEADA.");
            return;
        }

        TripulacaoViagem tv = tabelaTripulacao.getSelectionModel().getSelectedItem();
        if (tv == null) {
            AlertUtils.aviso("Selecione um tripulante na tabela de tripulação.");
            return;
        }

        if (AlertUtils.confirmar("Remover '" + tv.getTripulante().getNome() + "' da viagem?")) {
            try {
                viagemController.removerTripulanteViagem(sel.getId(), tv.getTripulante().getId());
                carregarDados();
                tabelaViagens.getSelectionModel().select(
                        tabelaViagens.getItems().stream()
                                .filter(v -> v.getId() == sel.getId()).findFirst().orElse(null));
                AlertUtils.sucesso("Tripulante removido da viagem.");
            } catch (Exception e) {
                AlertUtils.erro(e.getMessage());
            }
        }
    }

    // ── Detail panel ──────────────────────────────────────────────────────────

    private void atualizarDetalheViagem(Viagem v) {
        if (v == null) {
            tabelaCargas.getItems().clear();
            tabelaTripulacao.getItems().clear();
        } else {
            tabelaCargas.setItems(FXCollections.observableArrayList(v.getCargas()));
            tabelaTripulacao.setItems(FXCollections.observableArrayList(v.getTripulacao()));
        }
    }

    // ── Dialogs ───────────────────────────────────────────────────────────────

    private Optional<Boolean> mostrarDialogoCriarViagem(List<Navio> navios, List<Porto> portos) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Nova Viagem");
        dialog.setHeaderText(null);

        ButtonType btnGuardar = new ButtonType("Criar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));

        javafx.util.StringConverter<Navio> navioConv = new javafx.util.StringConverter<>() {
            @Override
            public String toString(Navio n) {
                return n == null ? "" : n.getNome() + " (IMO: " + n.getCodigoIMO() + ")";
            }

            @Override
            public Navio fromString(String s) {
                return null;
            }
        };
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

        ComboBox<Navio> cbNavio = new ComboBox<>(FXCollections.observableArrayList(navios));
        cbNavio.setConverter(navioConv);
        ComboBox<Porto> cbOrigem = new ComboBox<>(FXCollections.observableArrayList(portos));
        cbOrigem.setConverter(portoConv);
        ComboBox<Porto> cbDestino = new ComboBox<>(FXCollections.observableArrayList(portos));
        cbDestino.setConverter(portoConv);
        DatePicker dpPartida = new DatePicker(LocalDate.now());
        DatePicker dpChegada = new DatePicker(LocalDate.now().plusDays(7));
        TextField tfObs = new TextField();
        tfObs.setPromptText("(opcional)");

        int r = 0;
        form.add(new Label("Navio:"), 0, r);
        form.add(cbNavio, 1, r++);
        form.add(new Label("Porto de origem:"), 0, r);
        form.add(cbOrigem, 1, r++);
        form.add(new Label("Porto de destino:"), 0, r);
        form.add(cbDestino, 1, r++);
        form.add(new Label("Data de partida:"), 0, r);
        form.add(dpPartida, 1, r++);
        form.add(new Label("Chegada prevista:"), 0, r);
        form.add(dpChegada, 1, r++);
        form.add(new Label("Observações:"), 0, r);
        form.add(tfObs, 1, r++);

        cbNavio.setPrefWidth(280);
        cbOrigem.setPrefWidth(280);
        cbDestino.setPrefWidth(280);

        Label lblErro = new Label();
        lblErro.setStyle("-fx-text-fill: #d33;");
        lblErro.setWrapText(true);
        lblErro.setMaxWidth(360);
        form.add(lblErro, 0, r, 2, 1);

        dialog.getDialogPane().setContent(form);

        ValidacaoUI val = new ValidacaoUI(lblErro);
        ValidacaoUI.limparAoEditar(cbNavio, cbOrigem, cbDestino);
        ValidacaoUI.limparAoEditar(dpPartida, dpChegada);

        Boolean[] resultado = new Boolean[1];
        Button btnOk = (Button) dialog.getDialogPane().lookupButton(btnGuardar);
        btnOk.addEventFilter(ActionEvent.ACTION, ev -> {
            val.reset();
            Navio n = cbNavio.getValue();
            Porto or = cbOrigem.getValue();
            Porto de = cbDestino.getValue();
            LocalDate part = dpPartida.getValue();
            LocalDate cheg = dpChegada.getValue();
            String obs = tfObs.getText().trim();

            if (n == null) val.marcar(cbNavio, "O navio é obrigatório.");
            if (or == null) val.marcar(cbOrigem, "O porto de origem é obrigatório.");
            if (de == null) val.marcar(cbDestino, "O porto de destino é obrigatório.");
            if (part == null) val.marcar(dpPartida, "A data de partida é obrigatória.");
            if (cheg == null) val.marcar(dpChegada, "A chegada prevista é obrigatória.");
            if (or != null && de != null && or.getId() == de.getId())
                val.marcar(cbDestino, "A origem e o destino têm de ser diferentes.");
            if (part != null && part.isBefore(LocalDate.now()))
                val.marcar(dpPartida, "A data de partida não pode ser no passado.");
            if (part != null && cheg != null && !cheg.isAfter(part))
                val.marcar(dpChegada, "A chegada prevista tem de ser posterior à partida.");

            if (!val.valido()) {
                ev.consume();
                return;
            }

            try {
                viagemController.criarViagem(n, or, de, part, cheg, obs.isEmpty() ? null : obs);
                resultado[0] = Boolean.TRUE;
            } catch (Exception e) {
                String m = e.getMessage();
                if (m != null && m.toLowerCase().contains("navio")) val.marcar(cbNavio, m);
                else lblErro.setText(m);
                ev.consume();
            }
        });

        dialog.setResultConverter(bt -> bt == btnGuardar ? resultado[0] : null);
        return dialog.showAndWait();
    }

    /**
     * Returns Object[] {LocalDate partida, LocalDate chegada, String obs}
     */
    private Optional<Boolean> mostrarDialogoEditarViagem(Viagem v) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Editar Viagem #" + v.getId());
        dialog.setHeaderText(null);

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));

        DatePicker dpPartida = new DatePicker(v.getDataPartida());
        DatePicker dpChegada = new DatePicker(v.getDataChegadaPrevista());
        TextField tfObs = new TextField(v.getObservacoes() != null ? v.getObservacoes() : "");

        form.add(new Label("Data de partida:"), 0, 0);
        form.add(dpPartida, 1, 0);
        form.add(new Label("Chegada prevista:"), 0, 1);
        form.add(dpChegada, 1, 1);
        form.add(new Label("Observações:"), 0, 2);
        form.add(tfObs, 1, 2);

        tfObs.setPrefWidth(220);

        Label lblErro = new Label();
        lblErro.setStyle("-fx-text-fill: #d33;");
        lblErro.setWrapText(true);
        lblErro.setMaxWidth(360);
        form.add(lblErro, 0, 3, 2, 1);

        dialog.getDialogPane().setContent(form);

        ValidacaoUI val = new ValidacaoUI(lblErro);
        ValidacaoUI.limparAoEditar(dpPartida, dpChegada);

        Boolean[] resultado = new Boolean[1];
        Button btnOk = (Button) dialog.getDialogPane().lookupButton(btnGuardar);
        btnOk.addEventFilter(ActionEvent.ACTION, ev -> {
            val.reset();
            LocalDate part = dpPartida.getValue();
            LocalDate cheg = dpChegada.getValue();
            String obs = tfObs.getText().trim();

            if (part == null) val.marcar(dpPartida, "A data de partida é obrigatória.");
            if (cheg == null) val.marcar(dpChegada, "A chegada prevista é obrigatória.");
            if (part != null && part.isBefore(LocalDate.now()))
                val.marcar(dpPartida, "A data de partida não pode ser no passado.");
            if (part != null && cheg != null && !cheg.isAfter(part))
                val.marcar(dpChegada, "A chegada prevista tem de ser posterior à partida.");

            if (!val.valido()) {
                ev.consume();
                return;
            }

            try {
                viagemController.editarViagem(v.getId(), part, cheg, obs.isEmpty() ? null : obs);
                resultado[0] = Boolean.TRUE;
            } catch (Exception e) {
                lblErro.setText(e.getMessage());
                ev.consume();
            }
        });

        dialog.setResultConverter(bt -> bt == btnGuardar ? resultado[0] : null);
        return dialog.showAndWait();
    }

    /**
     * Returns Object[] {Carga, Double peso, Double volume}
     */
    private Optional<Boolean> mostrarDialogoAdicionarCarga(Viagem sel, List<Carga> compativeis) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Adicionar Carga à Viagem");
        dialog.setHeaderText(null);

        ButtonType btnGuardar = new ButtonType("Adicionar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));

        ComboBox<Carga> cbCarga = new ComboBox<>(FXCollections.observableArrayList(compativeis));
        cbCarga.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Carga c) {
                return c == null ? "" : c.getDesignacao() + " — " +
                        (c.getTipoCarga() != null ? c.getTipoCarga().getDesignacao() : "?") +
                        " (" + c.getPeso() + " t)";
            }

            @Override
            public Carga fromString(String s) {
                return null;
            }
        });
        TextField tfPeso = new TextField();
        tfPeso.setPromptText("toneladas");
        TextField tfVol = new TextField();
        tfVol.setPromptText("m³");

        // Tanque (compartimento) do navio onde a carga é alojada — opcional.
        // Os tanques são gerados na BD automaticamente se ainda não existirem.
        List<Tanque> tanques = new ArrayList<>();
        tanques.add(null); // opção "sem tanque específico"
        tanques.addAll(viagemController.listarTanquesDoNavio(sel.getNavio()));
        ComboBox<Tanque> cbTanque = new ComboBox<>(FXCollections.observableArrayList(tanques));
        cbTanque.getSelectionModel().selectFirst();
        cbTanque.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Tanque t) {
                return t == null ? "— sem tanque específico —"
                        : "Tanque #" + t.getNumero() + " (" + t.getCapacidade() + " t)";
            }

            @Override
            public Tanque fromString(String s) {
                return null;
            }
        });

        cbCarga.valueProperty().addListener((obs, o, c) -> {
            if (c != null) {
                tfPeso.setText(String.valueOf(c.getPeso()));
                tfVol.setText(String.valueOf(c.getVolume()));
            }
        });

        form.add(new Label("Carga:"), 0, 0);
        form.add(cbCarga, 1, 0);
        form.add(new Label("Peso atribuído (t):"), 0, 1);
        form.add(tfPeso, 1, 1);
        form.add(new Label("Volume atribuído (m³):"), 0, 2);
        form.add(tfVol, 1, 2);
        form.add(new Label("Tanque:"), 0, 3);
        form.add(cbTanque, 1, 3);

        cbCarga.setPrefWidth(320);
        tfPeso.setPrefWidth(180);
        tfVol.setPrefWidth(180);
        cbTanque.setPrefWidth(320);

        Label lblErro = new Label();
        lblErro.setStyle("-fx-text-fill: #d33;");
        lblErro.setWrapText(true);
        lblErro.setMaxWidth(360);
        form.add(lblErro, 0, 4, 2, 1);

        dialog.getDialogPane().setContent(form);

        ValidacaoUI val = new ValidacaoUI(lblErro);
        ValidacaoUI.limparAoEditar(tfPeso, tfVol);
        ValidacaoUI.limparAoEditar(cbCarga);

        Boolean[] resultado = new Boolean[1];
        Button btnOk = (Button) dialog.getDialogPane().lookupButton(btnGuardar);
        btnOk.addEventFilter(ActionEvent.ACTION, ev -> {
            val.reset();
            Carga c = cbCarga.getValue();
            if (c == null) val.marcar(cbCarga, "Selecione uma carga.");

            double peso = 0;
            try {
                peso = Double.parseDouble(tfPeso.getText().trim().replace(",", "."));
                double p = peso;
                val.verificar(tfPeso, () -> ValidacaoUtils.exigirPositivo(p, "O peso atribuído"));
            } catch (NumberFormatException e) {
                val.marcar(tfPeso, "Peso inválido — indique um número.");
            }

            double vol = 0;
            try {
                vol = Double.parseDouble(tfVol.getText().trim().replace(",", "."));
                double vv = vol;
                val.verificar(tfVol, () -> ValidacaoUtils.exigirPositivo(vv, "O volume atribuído"));
            } catch (NumberFormatException e) {
                val.marcar(tfVol, "Volume inválido — indique um número.");
            }

            if (!val.valido()) {
                ev.consume();
                return;
            }

            try {
                viagemController.adicionarCarga(sel.getId(), c, peso, vol, cbTanque.getValue());
                resultado[0] = Boolean.TRUE;
            } catch (Exception e) {
                String m = e.getMessage();
                if (m != null && m.toLowerCase().contains("capacidade")) val.marcar(tfPeso, m);
                else lblErro.setText(m);
                ev.consume();
            }
        });

        dialog.setResultConverter(bt -> bt == btnGuardar ? resultado[0] : null);
        return dialog.showAndWait();
    }

    /**
     * Returns Object[] {Tripulante, FuncaoTripulante, LocalDate embarque, LocalDate desembarque}
     */
    private Optional<Boolean> mostrarDialogoAdicionarTripulante(Viagem sel, List<Tripulante> disponiveis) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Adicionar Tripulante à Viagem");
        dialog.setHeaderText(null);

        ButtonType btnGuardar = new ButtonType("Adicionar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));

        ComboBox<Tripulante> cbTrip = new ComboBox<>(FXCollections.observableArrayList(disponiveis));
        cbTrip.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Tripulante t) {
                return t == null ? "" : t.getNome() + " — " + t.getFuncao();
            }

            @Override
            public Tripulante fromString(String s) {
                return null;
            }
        });
        ComboBox<FuncaoTripulante> cbFuncao = new ComboBox<>(
                FXCollections.observableArrayList(FuncaoTripulante.values()));
        DatePicker dpEmbarque = new DatePicker(LocalDate.now());
        DatePicker dpDesembarque = new DatePicker(LocalDate.now().plusDays(30));

        cbTrip.valueProperty().addListener((obs, o, t) -> {
            if (t != null) cbFuncao.setValue(t.getFuncaoEnum());
        });

        form.add(new Label("Tripulante:"), 0, 0);
        form.add(cbTrip, 1, 0);
        form.add(new Label("Função na viagem:"), 0, 1);
        form.add(cbFuncao, 1, 1);
        form.add(new Label("Data de embarque:"), 0, 2);
        form.add(dpEmbarque, 1, 2);
        form.add(new Label("Data de desembarque:"), 0, 3);
        form.add(dpDesembarque, 1, 3);

        cbTrip.setPrefWidth(280);
        cbFuncao.setPrefWidth(280);

        Label lblErro = new Label();
        lblErro.setStyle("-fx-text-fill: #d33;");
        lblErro.setWrapText(true);
        lblErro.setMaxWidth(360);
        form.add(lblErro, 0, 4, 2, 1);

        dialog.getDialogPane().setContent(form);

        ValidacaoUI val = new ValidacaoUI(lblErro);
        ValidacaoUI.limparAoEditar(cbTrip, cbFuncao);
        ValidacaoUI.limparAoEditar(dpEmbarque, dpDesembarque);

        Boolean[] resultado = new Boolean[1];
        Button btnOk = (Button) dialog.getDialogPane().lookupButton(btnGuardar);
        btnOk.addEventFilter(ActionEvent.ACTION, ev -> {
            val.reset();
            Tripulante t = cbTrip.getValue();
            FuncaoTripulante funcao = cbFuncao.getValue();
            LocalDate embarque = dpEmbarque.getValue();
            LocalDate desemb = dpDesembarque.getValue();

            if (t == null) val.marcar(cbTrip, "Selecione um tripulante.");
            if (funcao == null) val.marcar(cbFuncao, "A função na viagem é obrigatória.");
            if (embarque == null) val.marcar(dpEmbarque, "A data de embarque é obrigatória.");
            if (embarque != null && desemb != null && desemb.isBefore(embarque))
                val.marcar(dpDesembarque, "O desembarque não pode ser anterior ao embarque.");

            if (!val.valido()) {
                ev.consume();
                return;
            }

            try {
                viagemController.adicionarTripulante(sel.getId(), t, funcao, embarque, desemb);
                resultado[0] = Boolean.TRUE;
            } catch (Exception e) {
                String m = e.getMessage();
                if (m != null && m.toLowerCase().contains("disponível")) val.marcar(cbTrip, m);
                else lblErro.setText(m);
                ev.consume();
            }
        });

        dialog.setResultConverter(bt -> bt == btnGuardar ? resultado[0] : null);
        return dialog.showAndWait();
    }

    private void carregarDados() {
        int selId = tabelaViagens.getSelectionModel().getSelectedItem() != null
                ? tabelaViagens.getSelectionModel().getSelectedItem().getId() : -1;

        List<Viagem> viagens = viagemController.listarTodos();
        tabelaViagens.setItems(FXCollections.observableArrayList(viagens));

        if (selId != -1) {
            viagens.stream().filter(v -> v.getId() == selId).findFirst()
                    .ifPresent(v -> {
                        tabelaViagens.getSelectionModel().select(v);
                        atualizarDetalheViagem(v);
                    });
        }
    }
}
