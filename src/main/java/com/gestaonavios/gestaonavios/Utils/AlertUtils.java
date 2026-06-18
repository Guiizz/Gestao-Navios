package com.gestaonavios.gestaonavios.Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertUtils {

    public static void erro(String mensagem) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Erro");
        a.setHeaderText(null);
        a.setContentText(mensagem);
        a.showAndWait();
    }

    public static void sucesso(String mensagem) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Sucesso");
        a.setHeaderText(null);
        a.setContentText(mensagem);
        a.showAndWait();
    }

    public static void aviso(String mensagem) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Aviso");
        a.setHeaderText(null);
        a.setContentText(mensagem);
        a.showAndWait();
    }

    public static boolean confirmar(String mensagem) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmação");
        a.setHeaderText(null);
        a.setContentText(mensagem);
        Optional<ButtonType> res = a.showAndWait();
        return res.isPresent() && res.get() == ButtonType.OK;
    }
}
