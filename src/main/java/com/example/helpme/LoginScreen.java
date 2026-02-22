package com.example.helpme;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.example.helpme.User.currentUser;

public class LoginScreen {
    @FXML
    public Pane enterPain;
    @FXML
    public VBox result;
    @FXML
    public Label summery;

    public void start() {
        Image temp1 = new Image(Objects.requireNonNull(getClass().getResource("enterBG.png")).toExternalForm());
        BackgroundImage temp = new BackgroundImage(temp1, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background backStage = new Background(temp);
        enterPain.setBackground(backStage);

        String currentUser = getCurrentPlayerUser();


        summery.setWrapText(true);
        GameRecordsLoader loader = new GameRecordsLoader();
        String userSummary = loader.loadUserSummary(currentUser);
        summery.setText(userSummary);
        System.out.println("Summary: " + userSummary);


        List<GameRecords> history = new GameRecordsLoader().loadIt(currentUser);
        TableView<GameRecords> table = createGameHistoryTable(history);

        result.setLayoutY(520);
        summery.setLayoutX(summery.getLayoutX()-15);
        summery.setLayoutY(475);

        result.getChildren().add(table);

    }

    private String getCurrentPlayerUser() {
        return currentUser;
    }

    private TableView<GameRecords> createGameHistoryTable(List<GameRecords> records) {
        TableView<GameRecords> table = new TableView<>();

        TableColumn<GameRecords, String> gameIdCol = new TableColumn<>("Game ID");
        gameIdCol.setCellValueFactory(new PropertyValueFactory<>("gameId"));

        TableColumn<GameRecords, String> resultCol = new TableColumn<>("Result");
        resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));

        TableColumn<GameRecords, String> dateTimeCol = new TableColumn<>("Date & Time");
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        table.getColumns().addAll(gameIdCol, resultCol, dateTimeCol);

        ObservableList<GameRecords> data = FXCollections.observableArrayList(records);
        table.setItems(data);

        return table;
    }

    public void entering(ActionEvent event) throws IOException {
//        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Pane root = loader.load();
//
//        HelloController controller = loader.getController();
//
//        Scene scene = new Scene(root, 1024, 720);
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(scene);
//        stage.show();
//
//        controller.intGame();

        SceneSwitcher.switchTo("hello-view.fxml",event,HelloController.class,HelloController::intGame);
    }
    public void back(ActionEvent event){
        SceneSwitcher.switchTo("trueFirst.fxml",event, User.class,User::initialize);
        System.out.println("?");
    }
    public void quitGame(){
        Platform.exit();
    }
}
