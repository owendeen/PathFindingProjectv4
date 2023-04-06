module com.example.pathfindingprojectv4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.pathfindingprojectv4 to javafx.fxml;
    exports com.example.pathfindingprojectv4;
}