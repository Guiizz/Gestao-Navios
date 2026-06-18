open module com.gestaonavios.gestaonavios {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.gestaonavios.gestaonavios;
    exports com.gestaonavios.gestaonavios.BLL;
    exports com.gestaonavios.gestaonavios.Controller;
    exports com.gestaonavios.gestaonavios.DAL;
    exports com.gestaonavios.gestaonavios.Model;
    exports com.gestaonavios.gestaonavios.Model.enums;
    exports com.gestaonavios.gestaonavios.Utils;
}