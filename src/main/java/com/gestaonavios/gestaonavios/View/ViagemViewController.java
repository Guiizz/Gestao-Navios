package View;

import BLL.CargaBLL;
import BLL.NavioBLL;
import BLL.PortoBLL;
import BLL.TripulanteBLL;
import BLL.ViagemBLL;
import Controller.ViagemController;
import DAL.CargaDAL;
import DAL.NavioDAL;
import DAL.PortoDAL;
import DAL.TipoCargaDAL;
import DAL.TipoNavioDAL;
import DAL.TripulanteDAL;
import DAL.ViagemDAL;
import Model.AtribuicaoCarga;
import Model.Carga;
import Model.Navio;
import Model.Porto;
import Model.Tripulante;
import Model.TripulacaoViagem;
import Model.Viagem;
import Model.enums.EstadoViagem;
import Model.enums.FuncaoTripulante;
import Utils.AlertUtils;
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
    @FXML private TableView<Viagem> tabelaViagens;
    @FXML private TableColumn<Viagem, Integer> colId;
    @FXML private TableColumn<Viagem, String>  colOrigem;
    @FXML private TableColumn<Viagem, String>  colDestino;
    @FXML private TableColumn<Viagem, String>  colPartida;
    @FXML private TableColumn<Viagem, String>  colChegadaP;
    @FXML private TableColumn<Viagem, String>  colChegadaR;
    @FXML private TableColumn<Viagem, String>  colEstado;
    @FXML private TableColumn<Viagem, String>  colNavio;
    @FXML private TableColumn<Viagem, Integer> colNCargas;
    @FXML private TableColumn<Viagem, Integer> colNTrip;

    // ── Cargas sub-table ──────────────────────────────────────────────────────
    @FXML private TableView<AtribuicaoCarga> tabelaCargas;
    @FXML private TableColumn<AtribuicaoCarga, String> colCargaDesig;
    @FXML private TableColumn<AtribuicaoCarga, String> colCargaTipo;
    @FXML private TableColumn<AtribuicaoCarga, String> colCargaPeso;
    @FXML private TableColumn<AtribuicaoCarga, String> colCargaVolume;

    // ── Tripulação sub-table ──────────────────────────────────────────────────
    @FXML private TableView<TripulacaoViagem> tabelaTripulacao;
    @FXML private TableColumn<TripulacaoViagem, String> colTripNome;
    @FXML private TableColumn<TripulacaoViagem, String> colTripFuncao;
    @FXML private TableColumn<TripulacaoViagem, String> colTripEmbarque;
    @FXML private TableColumn<TripulacaoViagem, String> colTripDesembarque;

    private ViagemController viagemController;

    @FXML
    public void initialize() {
        PortoDAL     portoDAL     = new PortoDAL();
        TipoNavioDAL tipoNavioDAL = new TipoNavioDAL();
        TipoCargaDAL tipoCargaDAL = new TipoCargaDAL();
        NavioDAL     navioDAL     = new NavioDAL(portoDAL, tipoNavioDAL);
        CargaDAL     cargaDAL     = new CargaDAL(tipoCargaDAL, portoDAL);
        TripulanteDAL tripulanteDAL = new TripulanteDAL();
        ViagemDAL    viagemDAL    = new ViagemDAL(portoDAL, navioDAL);

        PortoBLL      portoBLL      = new PortoBLL(portoDAL);
        CargaBLL      cargaBLL      = new CargaBLL(cargaDAL, viagemDAL);
        TripulanteBLL tripulanteBLL = new TripulanteBLL(tripulanteDAL, viagemDAL);
        NavioBLL      navioBLL      = new NavioBLL(navioDAL, viagemDAL);
        ViagemBLL     viagemBLL     = new ViagemBLL(viagemDAL, navioBLL, tripulanteBLL);
        viagemController = new ViagemController(viagemBLL, navioBLL, portoBLL, cargaBLL, tripulanteBLL);

        // Viagens table columns
        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getId()).asObject());
        colOrigem.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getOrigem()  != null ? d.getValue().getOrigem().getNome()  : "—"));
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

    @FXML private void atualizar() { carregarDados(); }

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

        mostrarDialogoCriarViagem(navios, portos).ifPresent(v -> {
            try {
                viagemController.criarViagem(v.getNavio(), v.getOrigem(), v.getDestino(),
                        v.getDataPartida(), v.getDataChegadaPrevista(), v.getObservacoes());
                carregarDados();
                AlertUtils.sucesso("Viagem criada com sucesso.");
            } catch (Exception e) {
                AlertUtils.erro(e.getMessage());
            }
        });
    }

    @FXML
    private void editar() {
        Viagem sel = tabelaViagens.getSelectionModel().getSelectedItem();
        if (sel == null) { AlertUtils.aviso("Selecione uma viagem para editar."); return; }
        if (sel.getEstado() != EstadoViagem.PLANEADA) {
            AlertUtils.aviso("Só é possível editar viagens no estado PLANEADA.");
            return;
        }
        mostrarDialogoEditarViagem(sel).ifPresent(dados -> {
            try {
                viagemController.editarViagem(sel.getId(),
                        (LocalDate) dados[0], (LocalDate) dados[1], (String) dados[2]);
                carregarDados();
                AlertUtils.sucesso("Viagem atualizada.");
            } catch (Exception e) {
                AlertUtils.erro(e.getMessage());
            }
        });
    }

    @FXML
    private void avancarEstado() {
        Viagem sel = tabelaViagens.getSelectionModel().getSelectedItem();
        if (sel == null) { AlertUtils.aviso("Selecione uma viagem para avançar o estado."); return; }
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
                try { chegadaReal = LocalDate.parse(res.get().trim()); }
                catch (Exception e) { AlertUtils.erro("Formato de data inválido (use AAAA-MM-DD)."); return; }
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
        if (sel == null) { AlertUtils.aviso("Selecione uma viagem para cancelar."); return; }
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
        if (sel == null) { AlertUtils.aviso("Selecione uma viagem para remover."); return; }
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
        if (sel == null) { AlertUtils.aviso("Selecione uma viagem."); return; }
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
            if (sel.getNavio() != null && !sel.getNavio().aceitaTipoCarga(c.getTipoCarga())) continue;
            boolean excluir = false;
            for (AtribuicaoCarga ac : sel.getCargas()) if (ac.getCarga().getId() == c.getId()) { excluir = true; break; }
            if (!excluir) for (int id : emUso) if (id == c.getId()) { excluir = true; break; }
            if (!excluir) compativeis.add(c);
        }

        if (compativeis.isEmpty()) {
            AlertUtils.aviso("Não há cargas disponíveis compatíveis com este navio.");
            return;
        }

        mostrarDialogoAdicionarCarga(compativeis).ifPresent(dados -> {
            try {
                Carga carga  = (Carga)  dados[0];
                double peso  = (Double) dados[1];
                double vol   = (Double) dados[2];
                viagemController.adicionarCarga(sel.getId(), carga, peso, vol);
                carregarDados();
                tabelaViagens.getSelectionModel().select(
                        tabelaViagens.getItems().stream()
                                .filter(v -> v.getId() == sel.getId()).findFirst().orElse(null));
                AlertUtils.sucesso("Carga adicionada à viagem.");
            } catch (Exception e) {
                AlertUtils.erro(e.getMessage());
            }
        });
    }

    @FXML
    private void removerCarga() {
        Viagem sel = tabelaViagens.getSelectionModel().getSelectedItem();
        if (sel == null) { AlertUtils.aviso("Selecione uma viagem."); return; }
        if (sel.getEstado() != EstadoViagem.PLANEADA) {
            AlertUtils.aviso("Só é possível remover cargas de viagens no estado PLANEADA.");
            return;
        }

        AtribuicaoCarga ac = tabelaCargas.getSelectionModel().getSelectedItem();
        if (ac == null) { AlertUtils.aviso("Selecione uma carga na tabela de cargas."); return; }

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
        if (sel == null) { AlertUtils.aviso("Selecione uma viagem."); return; }
        if (sel.getEstado() != EstadoViagem.PLANEADA) {
            AlertUtils.aviso("Só é possível adicionar tripulantes a viagens no estado PLANEADA.");
            return;
        }

        List<Tripulante> disponiveis = viagemController.listarTripulantesDisponiveis();
        if (disponiveis.isEmpty()) {
            AlertUtils.aviso("Não há tripulantes disponíveis.");
            return;
        }

        mostrarDialogoAdicionarTripulante(disponiveis).ifPresent(dados -> {
            try {
                Tripulante      t        = (Tripulante)      dados[0];
                FuncaoTripulante funcao  = (FuncaoTripulante) dados[1];
                LocalDate       embarque = (LocalDate)        dados[2];
                LocalDate       desemb   = (LocalDate)        dados[3];
                viagemController.adicionarTripulante(sel.getId(), t, funcao, embarque, desemb);
                carregarDados();
                tabelaViagens.getSelectionModel().select(
                        tabelaViagens.getItems().stream()
                                .filter(v -> v.getId() == sel.getId()).findFirst().orElse(null));
                AlertUtils.sucesso("Tripulante adicionado à viagem.");
            } catch (Exception e) {
                AlertUtils.erro(e.getMessage());
            }
        });
    }

    @FXML
    private void removerTripulante() {
        Viagem sel = tabelaViagens.getSelectionModel().getSelectedItem();
        if (sel == null) { AlertUtils.aviso("Selecione uma viagem."); return; }
        if (sel.getEstado() != EstadoViagem.PLANEADA) {
            AlertUtils.aviso("Só é possível remover tripulantes de viagens no estado PLANEADA.");
            return;
        }

        TripulacaoViagem tv = tabelaTripulacao.getSelectionModel().getSelectedItem();
        if (tv == null) { AlertUtils.aviso("Selecione um tripulante na tabela de tripulação."); return; }

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

    private Optional<Viagem> mostrarDialogoCriarViagem(List<Navio> navios, List<Porto> portos) {
        Dialog<Viagem> dialog = new Dialog<>();
        dialog.setTitle("Nova Viagem");
        dialog.setHeaderText(null);

        ButtonType btnGuardar = new ButtonType("Criar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(20));

        javafx.util.StringConverter<Navio> navioConv = new javafx.util.StringConverter<>() {
            @Override public String toString(Navio n)   { return n == null ? "" : n.getNome() + " (IMO: " + n.getCodigoIMO() + ")"; }
            @Override public Navio fromString(String s) { return null; }
        };
        javafx.util.StringConverter<Porto> portoConv = new javafx.util.StringConverter<>() {
            @Override public String toString(Porto p)   { return p == null ? "" : p.getNome() + " (" + p.getCodigoUNLOCODE() + ")"; }
            @Override public Porto fromString(String s) { return null; }
        };

        ComboBox<Navio>  cbNavio   = new ComboBox<>(FXCollections.observableArrayList(navios));  cbNavio.setConverter(navioConv);
        ComboBox<Porto>  cbOrigem  = new ComboBox<>(FXCollections.observableArrayList(portos)); cbOrigem.setConverter(portoConv);
        ComboBox<Porto>  cbDestino = new ComboBox<>(FXCollections.observableArrayList(portos)); cbDestino.setConverter(portoConv);
        DatePicker dpPartida  = new DatePicker(LocalDate.now());
        DatePicker dpChegada  = new DatePicker(LocalDate.now().plusDays(7));
        TextField  tfObs      = new TextField(); tfObs.setPromptText("(opcional)");

        int r = 0;
        form.add(new Label("Navio:"), 0, r);           form.add(cbNavio, 1, r++);
        form.add(new Label("Porto de origem:"), 0, r); form.add(cbOrigem, 1, r++);
        form.add(new Label("Porto de destino:"), 0, r);form.add(cbDestino, 1, r++);
        form.add(new Label("Data de partida:"), 0, r); form.add(dpPartida, 1, r++);
        form.add(new Label("Chegada prevista:"), 0, r);form.add(dpChegada, 1, r++);
        form.add(new Label("Observações:"), 0, r);     form.add(tfObs, 1, r++);

        cbNavio.setPrefWidth(280); cbOrigem.setPrefWidth(280); cbDestino.setPrefWidth(280);
        dialog.getDialogPane().setContent(form);

        Node btnOk = dialog.getDialogPane().lookupButton(btnGuardar);
        btnOk.setDisable(true);
        Runnable check = () -> btnOk.setDisable(
                cbNavio.getValue() == null || cbOrigem.getValue() == null ||
                cbDestino.getValue() == null || dpPartida.getValue() == null || dpChegada.getValue() == null);
        cbNavio.valueProperty().addListener((o,a,b) -> check.run());
        cbOrigem.valueProperty().addListener((o,a,b) -> check.run());
        cbDestino.valueProperty().addListener((o,a,b) -> check.run());

        dialog.setResultConverter(bt -> {
            if (bt != btnGuardar) return null;
            Navio n  = cbNavio.getValue();
            Porto or = cbOrigem.getValue();
            Porto de = cbDestino.getValue();
            LocalDate part  = dpPartida.getValue();
            LocalDate cheg  = dpChegada.getValue();
            String obs = tfObs.getText().trim();
            return new Viagem(0, part, cheg, null, or, de, obs.isEmpty() ? null : obs, n);
        });

        return dialog.showAndWait();
    }

    /** Returns Object[] {LocalDate partida, LocalDate chegada, String obs} */
    private Optional<Object[]> mostrarDialogoEditarViagem(Viagem v) {
        Dialog<Object[]> dialog = new Dialog<>();
        dialog.setTitle("Editar Viagem #" + v.getId());
        dialog.setHeaderText(null);

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(20));

        DatePicker dpPartida = new DatePicker(v.getDataPartida());
        DatePicker dpChegada = new DatePicker(v.getDataChegadaPrevista());
        TextField  tfObs     = new TextField(v.getObservacoes() != null ? v.getObservacoes() : "");

        form.add(new Label("Data de partida:"),  0, 0); form.add(dpPartida, 1, 0);
        form.add(new Label("Chegada prevista:"),  0, 1); form.add(dpChegada, 1, 1);
        form.add(new Label("Observações:"),       0, 2); form.add(tfObs, 1, 2);

        tfObs.setPrefWidth(220);
        dialog.getDialogPane().setContent(form);

        dialog.setResultConverter(bt -> {
            if (bt != btnGuardar) return null;
            String obs = tfObs.getText().trim();
            return new Object[]{dpPartida.getValue(), dpChegada.getValue(), obs.isEmpty() ? null : obs};
        });

        return dialog.showAndWait();
    }

    /** Returns Object[] {Carga, Double peso, Double volume} */
    private Optional<Object[]> mostrarDialogoAdicionarCarga(List<Carga> compativeis) {
        Dialog<Object[]> dialog = new Dialog<>();
        dialog.setTitle("Adicionar Carga à Viagem");
        dialog.setHeaderText(null);

        ButtonType btnGuardar = new ButtonType("Adicionar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(20));

        ComboBox<Carga> cbCarga = new ComboBox<>(FXCollections.observableArrayList(compativeis));
        cbCarga.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Carga c)   {
                return c == null ? "" : c.getDesignacao() + " — " +
                        (c.getTipoCarga() != null ? c.getTipoCarga().getDesignacao() : "?") +
                        " (" + c.getPeso() + " t)";
            }
            @Override public Carga fromString(String s) { return null; }
        });
        TextField tfPeso = new TextField(); tfPeso.setPromptText("toneladas");
        TextField tfVol  = new TextField(); tfVol.setPromptText("m³");

        cbCarga.valueProperty().addListener((obs, o, c) -> {
            if (c != null) {
                tfPeso.setText(String.valueOf(c.getPeso()));
                tfVol.setText(String.valueOf(c.getVolume()));
            }
        });

        form.add(new Label("Carga:"), 0, 0);           form.add(cbCarga, 1, 0);
        form.add(new Label("Peso atribuído (t):"), 0, 1); form.add(tfPeso, 1, 1);
        form.add(new Label("Volume atribuído (m³):"), 0, 2); form.add(tfVol, 1, 2);

        cbCarga.setPrefWidth(320); tfPeso.setPrefWidth(180); tfVol.setPrefWidth(180);
        dialog.getDialogPane().setContent(form);

        Node btnOk = dialog.getDialogPane().lookupButton(btnGuardar);
        btnOk.setDisable(true);
        cbCarga.valueProperty().addListener((o, a, b) -> btnOk.setDisable(b == null));

        dialog.setResultConverter(bt -> {
            if (bt != btnGuardar) return null;
            try {
                Carga c = cbCarga.getValue();
                double peso = Double.parseDouble(tfPeso.getText().trim().replace(",", "."));
                double vol  = Double.parseDouble(tfVol.getText().trim().replace(",", "."));
                return new Object[]{c, peso, vol};
            } catch (NumberFormatException e) {
                AlertUtils.erro("Verifique os campos numéricos (peso, volume).");
                return null;
            }
        });

        return dialog.showAndWait();
    }

    /** Returns Object[] {Tripulante, FuncaoTripulante, LocalDate embarque, LocalDate desembarque} */
    private Optional<Object[]> mostrarDialogoAdicionarTripulante(List<Tripulante> disponiveis) {
        Dialog<Object[]> dialog = new Dialog<>();
        dialog.setTitle("Adicionar Tripulante à Viagem");
        dialog.setHeaderText(null);

        ButtonType btnGuardar = new ButtonType("Adicionar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(20));

        ComboBox<Tripulante> cbTrip = new ComboBox<>(FXCollections.observableArrayList(disponiveis));
        cbTrip.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Tripulante t)   {
                return t == null ? "" : t.getNome() + " — " + t.getFuncao();
            }
            @Override public Tripulante fromString(String s) { return null; }
        });
        ComboBox<FuncaoTripulante> cbFuncao = new ComboBox<>(
                FXCollections.observableArrayList(FuncaoTripulante.values()));
        DatePicker dpEmbarque    = new DatePicker(LocalDate.now());
        DatePicker dpDesembarque = new DatePicker(LocalDate.now().plusDays(30));

        cbTrip.valueProperty().addListener((obs, o, t) -> {
            if (t != null) cbFuncao.setValue(t.getFuncaoEnum());
        });

        form.add(new Label("Tripulante:"), 0, 0);      form.add(cbTrip, 1, 0);
        form.add(new Label("Função na viagem:"), 0, 1); form.add(cbFuncao, 1, 1);
        form.add(new Label("Data de embarque:"), 0, 2); form.add(dpEmbarque, 1, 2);
        form.add(new Label("Data de desembarque:"), 0, 3); form.add(dpDesembarque, 1, 3);

        cbTrip.setPrefWidth(280); cbFuncao.setPrefWidth(280);
        dialog.getDialogPane().setContent(form);

        Node btnOk = dialog.getDialogPane().lookupButton(btnGuardar);
        btnOk.setDisable(true);
        Runnable check = () -> btnOk.setDisable(cbTrip.getValue() == null || cbFuncao.getValue() == null);
        cbTrip.valueProperty().addListener((o, a, b) -> check.run());
        cbFuncao.valueProperty().addListener((o, a, b) -> check.run());

        dialog.setResultConverter(bt -> {
            if (bt != btnGuardar) return null;
            return new Object[]{
                    cbTrip.getValue(), cbFuncao.getValue(),
                    dpEmbarque.getValue(), dpDesembarque.getValue()
            };
        });

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
