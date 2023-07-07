module com.moviefix {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.moviefix to javafx.fxml;
    exports com.moviefix;
}