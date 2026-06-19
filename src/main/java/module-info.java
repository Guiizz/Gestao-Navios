open module com.gestaonavios.gestaonavios {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.gestaonavios.gestaonavios to javafx.fxml;
    opens com.gestaonavios.gestaonavios.View to javafx.fxml;       // ADICIONADO
    opens com.gestaonavios.gestaonavios.Controller to javafx.fxml; // ADICIONADO
    opens com.gestaonavios.gestaonavios.Model to javafx.fxml;      // ADICIONADO
    opens com.gestaonavios.gestaonavios.BLL to javafx.fxml;        // ADICIONADO
    opens com.gestaonavios.gestaonavios.DAL to javafx.fxml;        // ADICIONADO

    exports com.gestaonavios.gestaonavios;
    exports com.gestaonavios.gestaonavios.View;       // ADICIONADO
    exports com.gestaonavios.gestaonavios.Controller; // ADICIONADO
    exports com.gestaonavios.gestaonavios.Model;      // ADICIONADO
}