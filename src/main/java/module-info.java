module com.example.taskmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.game to javafx.graphics;

    exports com.example.game;

}
