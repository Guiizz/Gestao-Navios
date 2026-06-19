module com.gestaonavios.gestaonavios {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.gestaonavios.gestaonavios to javafx.fxml;
    exports com.gestaonavios.gestaonavios;
}