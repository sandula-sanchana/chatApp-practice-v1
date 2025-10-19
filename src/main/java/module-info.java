module edu.ijse.chatapp_practice_demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    requires javafx.graphics;
    requires javafx.base;

    exports edu.ijse.chatapp_practice_demo.controller;


    opens edu.ijse.chatapp_practice_demo to javafx.fxml;
    exports edu.ijse.chatapp_practice_demo;
    opens  edu.ijse.chatapp_practice_demo.controller to javafx.fxml;
}