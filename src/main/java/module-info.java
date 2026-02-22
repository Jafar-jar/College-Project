module com.example.helpme {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires socket.io.client;
    requires engine.io.client;
    requires java.desktop;
    requires javafx.media;

    opens com.example.helpme to javafx.fxml;
    exports com.example.helpme;
}