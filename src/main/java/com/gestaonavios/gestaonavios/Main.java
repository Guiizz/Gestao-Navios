package com.gestaonavios.gestaonavios;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/gestaonavios/gestaonavios/MainView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1280, 760);
        scene.getStylesheets().add(
                Main.class.getResource("/com/gestaonavios/gestaonavios/styles.css").toExternalForm());
        primaryStage.setTitle("Sistema de Gestão de Navios Petroleiros");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }
}
