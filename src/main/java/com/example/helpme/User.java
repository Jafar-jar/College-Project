package com.example.helpme;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;

public class User {
    @FXML
    AnchorPane lastPainLogin, lastPainReg;
    @FXML
    TextField loginUser, firstNameReg, lastNameReg, nationalID, regUser;
    @FXML
    PasswordField regPassword, confirmPassword, loginPassword;
    @FXML
    ComboBox gender;
    @FXML
    Slider age;
    @FXML
    Label linkToSlider, eLabelReg, eLabelLogin;

    public static String currentNationalId;
    public static String currentUser;


    public void initialize() {
        ObservableList<String> type = FXCollections.observableArrayList();
        type.add("Male");
        type.add("Female");
        type.add("Walmart Bag");
        gender.setItems(type);

        age.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                linkToSlider.setText(String.valueOf(Math.round(age.getValue())));
            }
        });
    }

    public void tester() {
        try {
            if (firstNameReg.getText().trim().isEmpty() || lastNameReg.getText().trim().isEmpty() || regPassword.getText().trim().isEmpty() || nationalID.getText().trim().isEmpty()) {
                String error = "The Text Fields Should NOT be Empty";
//                eLabelReg.setText(error);
                throw new Exceptions.EmptyFieldException(error);
            }
            if (regPassword.getText().trim().length() < 8) {
                String error = "Password Must be at Least 8 Characters";
//                eLabelReg.setText(error);
                throw new Exceptions.WeakPasswordException(error);
            }
            if (!regPassword.getText().trim().equals(confirmPassword.getText().trim())) {
                String error = "Passwords Doesn't match!";
                throw new Exceptions.PasswordsDoNotMatch(error);
            }
            if (age.getValue() <= 0) {
                String error = "Age must be at Least 1";
//                eLabelReg.setText(error);
                throw new Exceptions.AgeInvalidException(error);
            }
            if (gender.getValue() == null) {
                String error = "Gender must be Male or Female Only!";
//                eLabelReg.setText(error);
                throw new Exceptions.GenderNotSelectedException(error);
            }
            if (Character.isDigit(regUser.getText().trim().charAt(0))) {
                throw new Exceptions.FirstCharacterIsNumber("The Username Should not Start with a Digit");
            }
            if (nationalID.getText().trim().length() != 10) {
                String error = "کد ملی باید دقیقاً ۱۰ رقم باشد.";
//                eLabelReg.setText(error);
                throw new Exceptions.InvalidNationalIDException(error);
            }
            for (int i = 0; i < nationalID.getText().trim().length(); i++) {
                char c = nationalID.getText().trim().charAt(i);
                if (c < '0' || c > '9') {
                    String error = "کد ملی فقط باید شامل اعداد باشد.";
//                    eLabelReg.setText(error);
                    throw new Exceptions.InvalidNationalIDException(error);
                }
            }

        } catch (Exception e) {
            eLabelReg.setText("Unexpected error: " + e.getMessage());
            return;
        }

        String fileName = "src/main/resources/com/example/helpme/Data/Users_data.txt";
        String gamesDataFile = "src/main/resources/com/example/helpme/Data/Games_data.txt";
        String gamesLogFile = "src/main/resources/com/example/helpme/Data/Game_logs.txt";
        String usernameToCheck = regUser.getText().trim();

        File file = new File(fileName);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String existingUsername = parts[3].trim();
                    if (existingUsername.equalsIgnoreCase(usernameToCheck)) {
//                        eLabelReg.setText("Username already exists. Please choose another.");
                        throw new Exceptions.DuplicateUsernameException("Username already exists. Please choose another.");

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            eLabelReg.setText("Error reading user data.");
            return;
        } catch (Exception e) {
            eLabelReg.setText(e.getMessage());
        }

        String dataLine = firstNameReg.getText().trim() + "," + lastNameReg.getText().trim() + "," + nationalID.getText().trim() + "," + usernameToCheck + "," + regPassword.getText().trim() + "," + gender.getValue().toString() + "," + Math.round(age.getValue());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(dataLine);
            bw.newLine();
            eLabelReg.setText("User registered successfully!");
            System.out.println("Data saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            eLabelReg.setText("Failed to write data to file.");
            return;
        }

        File gamesData = new File(gamesDataFile);
        if (!gamesData.exists()) {
            try {
                gamesData.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (BufferedWriter gameWriter = new BufferedWriter(new FileWriter(gamesDataFile, true))) {
            String gameInitLine = usernameToCheck + "," + nationalID.getText().trim() + ",0,0,0,0";
            gameWriter.write(gameInitLine);
            gameWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to write initial game data.");
        }


        File gamesLog = new File(gamesLogFile);
        if (!gamesLog.exists()) {
            try {
                gamesLog.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (BufferedWriter gameWriter = new BufferedWriter(new FileWriter(gamesLogFile, true))) {
            String gameInitLine = usernameToCheck + "," + nationalID.getText().trim() + ",0" + ",0,0";
            gameWriter.write(gameInitLine);
            gameWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to write initial game data.");
        }
    }

    public void login(ActionEvent event) {
        try {
            String inputUsername = loginUser.getText().trim();
            String inputPassword = loginPassword.getText();

            boolean found = false;

            if (inputUsername.isEmpty() || inputPassword.isEmpty() && !inputUsername.equalsIgnoreCase("Admin")) {
                throw new Exceptions.EmptyFieldException("Username and password must not be empty.");
            }

            if (inputUsername.equalsIgnoreCase("Admin")) {
                found = true;
                currentUser = "Admin";
                currentNationalId = "41";
            }

            String fileName = "src/main/resources/com/example/helpme/Data/Users_data.txt";
            File file = new File(fileName);

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        String storedUsername = parts[3].trim();
                        String storedPassword = parts[4];
                        if (storedUsername.equalsIgnoreCase(inputUsername) && storedPassword.equals(inputPassword)) {
                            currentUser = parts[3];
                            currentNationalId = parts[2];
                            found = true;
                            break;
                        }
                    }
                }

                if (found) {
//                    FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("entering.fxml"));
//                    Pane root = loader.load();
//
//                    LoginScreen loginScreen = loader.getController();
//
//                    Scene scene = new Scene(root, 1024, 720);
//                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                    stage.setScene(scene);
//                    stage.show();
//
//                    loginScreen.start();
                    SceneSwitcher.switchTo("entering.fxml", event, LoginScreen.class, LoginScreen::start);
                } else {
                    throw new Exceptions.InvalidUsernameOrPassword("Invalid username or password.");

                }

            } catch (IOException e) {
                e.printStackTrace();
                eLabelLogin.setText("Error reading user data.");
            }
        } catch (Exception e) {
            eLabelLogin.setText(e.getMessage());
        }
    }

    public void saveGameData(String nationalID, String gameID, int score, String result, String endTime, String date) {
        String gamesDataFile = "src/main/resources/com/example/helpme/Data/Games_data.txt";
        String gameLine = String.join(",", nationalID, gameID, String.valueOf(score), result, endTime, date);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(gamesDataFile, true))) {
            bw.write(gameLine);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
