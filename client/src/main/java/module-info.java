module zxc.kyoto.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens zxc.kyoto.client to javafx.fxml;
    exports zxc.kyoto.client;
    exports zxc.kyoto;
    opens zxc.kyoto to javafx.fxml;
}