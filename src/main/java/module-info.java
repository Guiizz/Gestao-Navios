open module com.gestaonavios.gestaonavios {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;

    exports com.gestaonavios.gestaonavios;
    exports com.gestaonavios.gestaonavios.View;
    exports com.gestaonavios.gestaonavios.Controller;
    exports com.gestaonavios.gestaonavios.Model;
}
