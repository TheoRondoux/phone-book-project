module isen.java.projet {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;
	requires sqlite.jdbc;
	requires javafx.graphics;
	requires junit;

    opens isen.java.projet to javafx.fxml;
    opens isen.java.projet.controllers to javafx.fxml;
    opens isen.java.projet.object to javafx.base;
    exports isen.java.projet;
}
