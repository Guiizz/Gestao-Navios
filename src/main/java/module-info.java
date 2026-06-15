module com.gestaonavios.gestaonavios {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gestaonavios.gestaonavios to javafx.fxml;
    exports com.gestaonavios.gestaonavios;
}