package com.example.helpme;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class SceneSwitcher {

    public static <T> void switchTo(String fxmlPath, ActionEvent event, Class<T> controllerClass, Consumer<T> controllerAction) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource("/com/example/helpme/" + fxmlPath));
            Parent root = loader.load();

            T controller = loader.getController();
            if (controllerAction != null && controllerClass.isInstance(controller)) {
                controllerAction.accept(controller);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1024, 720));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

