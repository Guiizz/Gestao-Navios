package com.gestaonavios.gestaonavios.Utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;

import java.util.ArrayList;
import java.util.List;

/**
 * Validação visual de formulários: marca a vermelho os campos inválidos e
 * mantém o diálogo aberto, para o utilizador corrigir só o que está errado
 * sem ter de reescrever tudo.
 */
public class ValidacaoUI {

    private static final String ESTILO_ERRO =
            "-fx-border-color: #d33; -fx-border-width: 1.5px; -fx-border-radius: 3px; -fx-background-radius: 3px;";

    private final Label lblErro;
    private final List<Control> marcados = new ArrayList<>();
    private Control primeiro;
    private boolean ok = true;

    public ValidacaoUI(Label lblErro) {
        this.lblErro = lblErro;
    }

    /** Limpa marcas e mensagem antes de uma nova tentativa de validação. */
    public void reset() {
        for (Control c : marcados) c.setStyle("");
        marcados.clear();
        primeiro = null;
        ok = true;
        if (lblErro != null) lblErro.setText("");
    }

    /** Marca um campo a vermelho; a mensagem do 1.º campo inválido é a mostrada. */
    public void marcar(Control campo, String mensagem) {
        campo.setStyle(ESTILO_ERRO);
        if (!marcados.contains(campo)) marcados.add(campo);
        if (primeiro == null) {
            primeiro = campo;
            if (lblErro != null && mensagem != null) lblErro.setText(mensagem);
        }
        ok = false;
    }

    /** Corre uma verificação que lança Exception (reutiliza ValidacaoUtils) e marca se falhar. */
    public void verificar(Control campo, Verificacao v) {
        try {
            v.run();
        } catch (Exception e) {
            marcar(campo, e.getMessage());
        }
    }

    /** True se não houve erros; caso contrário foca o 1.º campo inválido. */
    public boolean valido() {
        if (!ok && primeiro != null) primeiro.requestFocus();
        return ok;
    }

    /** Faz os campos deixarem de estar vermelhos assim que o utilizador os editar. */
    public static void limparAoEditar(TextInputControl... campos) {
        for (TextInputControl c : campos)
            c.textProperty().addListener((o, a, b) -> c.setStyle(""));
    }

    public static void limparAoEditar(ComboBox<?>... combos) {
        for (ComboBox<?> c : combos)
            c.valueProperty().addListener((o, a, b) -> c.setStyle(""));
    }

    public static void limparAoEditar(javafx.scene.control.DatePicker... pickers) {
        for (javafx.scene.control.DatePicker p : pickers)
            p.valueProperty().addListener((o, a, b) -> p.setStyle(""));
    }

    @FunctionalInterface
    public interface Verificacao {
        void run() throws Exception;
    }
}
