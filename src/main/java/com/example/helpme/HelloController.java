package com.example.helpme;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.helpme.User.currentUser;


public class HelloController {
    @FXML
    public Pane pain;
    private final boolean isOnline = false;
    private boolean pause = false;

    public static ArrayList<Timeline> timelineArrayList = new ArrayList<>();
    public static ArrayList<TrackHolder> trackHolderArrayList = new ArrayList<>();
    public ArrayList<hpItem> items1 = new ArrayList<>();
    public ArrayList<addBullet> items2 = new ArrayList<>();
    public ArrayList<powerBullet> items3 = new ArrayList<>();
    public ArrayList<speedy> items4 = new ArrayList<>();
    public ArrayList<GhostMode> items5 = new ArrayList<>();
    public ArrayList<Mine> mineArrayList = new ArrayList<>();
    public ArrayList<Walls> wallsArrayList = new ArrayList<>();
    public static ArrayList<DamageableWalls> damageableWallsArrayList = new ArrayList<>();
    public ArrayList<Fence> fenceArrayList = new ArrayList<>();
//    public ArrayList<Rail> railArrayList = new ArrayList<>();

    public Train currentTrain = null;
    public Timeline toRemove = null;
    public Timer gameTimer = null;

    public String winner = null;
    public String gameStatus = null;
    public static String gameID;
    public static int totalGames = 0;
    public int wins = 0;
    public int loss = 0;
    public int draws = 0;


    Timeline keyTimeline = new Timeline();

//    public TrackHolder ride = null;


    //    public void initialize() {
//        intGame();
//    }

    public int Randomizer(int max, int min) {
        return (int) (Math.random() * (max + 1)) + min;
    }

    public void intGame() {
        gameID = UUID.randomUUID().toString();
        Image temp1 = new Image(Objects.requireNonNull(getClass().getResource("back.png")).toExternalForm());
        BackgroundImage temp = new BackgroundImage(temp1, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background backStage = new Background(temp);
        pain.setBackground(backStage);
        this.MapGen();
        DamageableWalls test = new DamageableWalls(250, 250, 50, 50, 0, Randomizer(6, 1));
        pain.getChildren().add(test);
        damageableWallsArrayList.add(test);
//        new MapGen(pain);
//        System.out.println(wallsArrayList.toString());
        PlayerTank playerTank1 = new PlayerTank("tank1.png", 180, "hp.png");
        playerTank1.setPos(0, 360);
        playerTank1.setHealthBarDirection(true);
        playerTank1.setAmmoLabelDirection(true);
        pain.getChildren().add(playerTank1);
        PlayerTank playerTank2 = new PlayerTank("tank2.png", 0, "hp2.png");
        playerTank2.setPos(850, 360);
        playerTank2.setHealthBarDirection(false);
        playerTank2.setAmmoLabelDirection(false);
        pain.getChildren().add(playerTank2);

        playerTank1.initTank(pain, 0, 0, 50, 0, 0, 0);
        playerTank2.initTank(pain, 975, 0, 925, 0, 925, 0);


        Timeline focus = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            pain.requestFocus();
        }));
        focus.setCycleCount(1);
        focus.play();
        gameTimer = new Timer(pain);

        timelineArrayList.add(keyTimeline);
        HashSet<KeyCode> moving = new HashSet<>();
        int millis = 16;
        keyTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(millis), event -> {

            for (KeyCode key : moving) {
//                ride = new TrackHolder("C://Users//alire//IdeaProjects//helpMe//src//main//resources//com//example//helpme//Tracks//tank-track-ratteling-197409.mp3",1.0);
                if (!pause) {
                    if (key == KeyCode.W) {
                        playerTank1.move(Move.UP, wallsArrayList, pain, fenceArrayList, playerTank2, millis / 1000.0, damageableWallsArrayList);
                    }
                    if (key == KeyCode.A) {
                        playerTank1.move(Move.LEFT, wallsArrayList, pain, fenceArrayList, playerTank2, millis / 1000.0, damageableWallsArrayList);
                    }
                    if (key == KeyCode.S) {
                        playerTank1.move(Move.DOWN, wallsArrayList, pain, fenceArrayList, playerTank2, millis / 1000.0, damageableWallsArrayList);
                    }
                    if (key == KeyCode.D) {
                        playerTank1.move(Move.RIGHT, wallsArrayList, pain, fenceArrayList, playerTank2, millis / 1000.0, damageableWallsArrayList);
                    }
                    if (key == KeyCode.SPACE) {
                        playerTank1.shoot(pain, playerTank2, wallsArrayList, currentTrain, damageableWallsArrayList);
                    }
                    if (key == KeyCode.UP) {
                        playerTank2.move(Move.UP, wallsArrayList, pain, fenceArrayList, playerTank1, millis / 1000.0, damageableWallsArrayList);
                    }
                    if (key == KeyCode.LEFT) {
                        playerTank2.move(Move.LEFT, wallsArrayList, pain, fenceArrayList, playerTank1, millis / 1000.0, damageableWallsArrayList);
                    }
                    if (key == KeyCode.DOWN) {
                        playerTank2.move(Move.DOWN, wallsArrayList, pain, fenceArrayList, playerTank1, millis / 1000.0, damageableWallsArrayList);
                    }
                    if (key == KeyCode.RIGHT) {
                        playerTank2.move(Move.RIGHT, wallsArrayList, pain, fenceArrayList, playerTank1, millis / 1000.0, damageableWallsArrayList);
                    }
                    if (key == KeyCode.ENTER) {
                        playerTank2.shoot(pain, playerTank1, wallsArrayList, currentTrain, damageableWallsArrayList);
                    }
                }
            }
        }));
        keyTimeline.setCycleCount(Animation.INDEFINITE);
        keyTimeline.play();


        if (isOnline) {
            try {
                Socket socket = IO.socket("wss://localhost");
                socket.on("moveUp", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        playerTank2.move(Move.UP, wallsArrayList, pain, fenceArrayList, playerTank1, 1, damageableWallsArrayList);
                    }
                });
                socket.on("moveDown", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        playerTank2.move(Move.LEFT, wallsArrayList, pain, fenceArrayList, playerTank1, 1, damageableWallsArrayList);
                    }
                });
                socket.on("moveRight", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        playerTank2.move(Move.RIGHT, wallsArrayList, pain, fenceArrayList, playerTank1, 1, damageableWallsArrayList);
                    }
                });
                socket.on("moveLeft", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        playerTank2.move(Move.LEFT, wallsArrayList, pain, fenceArrayList, playerTank1, 1, damageableWallsArrayList);
                    }
                });
                socket.on("shoot", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        playerTank2.shoot(pain, playerTank1, wallsArrayList, currentTrain, damageableWallsArrayList);
                    }
                });
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {

            pain.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    this.pause();
//                    pause = !pause;
                    System.out.println("Pause status: " + pause);
                } else {
                    moving.add(keyEvent.getCode());
                }

            });
            pain.setOnKeyReleased(keyEvent -> {
                moving.remove(keyEvent.getCode());
            });

        }

        Timeline trainMove = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            Train train = new Train();
            train.move(train, playerTank1, playerTank2, pain);
            pain.getChildren().add(train);
            currentTrain = train;
        }));
        trainMove.setCycleCount(Animation.INDEFINITE);
        trainMove.play();
        Timeline itemSpawn = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            int type = Randomizer(20, 1);
            int limit = items1.size() + items2.size() + items3.size() + items4.size() + items5.size();
            if (limit < 20) {
                if (type == 1) {
                    hpItem hpItem = new hpItem();
                    items1.add(hpItem);
                    pain.getChildren().add(hpItem);
                } else if (type == 2) {
                    addBullet addBullet = new addBullet();
                    items2.add(addBullet);
                    pain.getChildren().add(addBullet);
                } else if (type == 3) {
                    powerBullet powerBullet = new powerBullet();
                    items3.add(powerBullet);
                    pain.getChildren().add(powerBullet);
                } else if (type == 4) {
                    speedy speedy = new speedy();
                    items4.add(speedy);
                    pain.getChildren().add(speedy);
                } else if (type == 5) {
                    GhostMode ghostMode = new GhostMode();
                    items5.add(ghostMode);
                    pain.getChildren().add(ghostMode);
                }
            }
            if (mineArrayList.size() < 4) {
                Mine mine = new Mine();
                mineArrayList.add(mine);
                pain.getChildren().add(mine);
            }
        }));
        itemSpawn.setCycleCount(Animation.INDEFINITE);
        itemSpawn.play();
        Timeline mainGameTimeLine = new Timeline(new KeyFrame(Duration.millis(1), event -> {
            for (int i = 0; i < items1.size(); i++) {
                hpItem item = items1.get(i);
                if (playerTank1.checkCollision(item)) {
                    playerTank1.increaseHealth(item.getHpAmount());
                    pain.getChildren().remove(item);
                    items1.remove(item);
                }
                if (playerTank2.checkCollision(item)) {
                    playerTank2.increaseHealth(item.getHpAmount());
                    pain.getChildren().remove(item);
                    items1.remove(item);
                }
            }
            for (int i = 0; i < items2.size(); i++) {
                addBullet item2 = items2.get(i);
                if (playerTank1.checkCollision(item2)) {
                    playerTank1.replenishAmmo(item2.getBulletAdd(), pain);
                    pain.getChildren().remove(item2);
                    items2.remove(item2);
                }
                if (playerTank2.checkCollision(item2)) {
                    playerTank2.replenishAmmo(item2.getBulletAdd(), pain);
                    pain.getChildren().remove(item2);
                    items2.remove(item2);
                }
            }
            for (int i = 0; i < items3.size(); i++) {
                powerBullet item3 = items3.get(i);
                if (playerTank1.checkCollision(item3)) {
                    playerTank1.powerUp();
                    pain.getChildren().remove(item3);
                    items3.remove(item3);
                }
                if (playerTank2.checkCollision(item3)) {
                    playerTank2.powerUp();
                    pain.getChildren().remove(item3);
                    items3.remove(item3);
                }
            }
            for (int i = 0; i < items4.size(); i++) {
                speedy item = items4.get(i);
                if (playerTank1.checkCollision(item)) {
                    playerTank1.speeding();
                    pain.getChildren().remove(item);
                    items4.remove(item);
                }
                if (playerTank2.checkCollision(item)) {
                    playerTank2.speeding();
                    pain.getChildren().remove(item);
                    items4.remove(item);
                }
            }
            for (int i = 0; i < items5.size(); i++) {
                GhostMode item = items5.get(i);
                if (playerTank1.checkCollision(item)) {
                    playerTank1.ghostMode(wallsArrayList, pain, fenceArrayList);
                    pain.getChildren().remove(item);
                    items5.remove(item);
                }
                if (playerTank2.checkCollision(item)) {
                    playerTank2.ghostMode(wallsArrayList, pain, fenceArrayList);
                    pain.getChildren().remove(item);
                    items5.remove(item);
                }
            }
            for (int i = 0; i < mineArrayList.size(); i++) {
                Mine item = mineArrayList.get(i);
                if (playerTank1.checkCollision(item)) {
                    playerTank1.decreaseHealth(5, pain);
                    pain.getChildren().remove(item);
                    mineArrayList.remove(item);
                }
                if (playerTank2.checkCollision(item)) {
                    playerTank2.decreaseHealth(5, pain);
                    pain.getChildren().remove(item);
                    mineArrayList.remove(item);
                }
            }
//            playerTank1.updateInfo(pain,0,0);
//            playerTank2.updateInfo(pain,1080,0);
            if (playerTank1.getHealth() <= 0 || playerTank2.getHealth() <= 0 || gameTimer.time <= 0) {
                if (playerTank1.getHealth() > playerTank2.getHealth()) {
                    winner = "Player 1 win!!";
                    wins++;
                    totalGames++;
                    gameStatus = "win";
                } else if (playerTank1.getHealth() < playerTank2.getHealth()) {
                    winner = "Player 2 win!!";
                    loss++;
                    totalGames++;
                    gameStatus = "loss";
                } else {
                    winner = "Draw";
                    draws++;
                    totalGames++;
                    gameStatus = "draw";
                }
                endGame(winner, gameID, totalGames, wins, loss, draws, gameStatus);
            }
        }));
        mainGameTimeLine.setCycleCount(Animation.INDEFINITE);
        mainGameTimeLine.play();

        Timeline airStrike = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            if (!pause) {
                double dangerZoneX = (Math.random() * 700) + 1;
                double dangerZoneY = (Math.random() * 850) + 1;
                double dangerZoneR = (Math.random() * 50) + 1;
                Circle marker = new Circle(dangerZoneX, dangerZoneY, dangerZoneR, Color.RED);
                marker.setOpacity(0.5);
//            Circle marker = new Circle(250,250,100, Color.RED);


                pain.getChildren().add(marker);
                Airplane ap = new Airplane(dangerZoneX, 0, marker, pain);
                toRemove = new Timeline(new KeyFrame(Duration.seconds(3), event1 -> {
                    pain.getChildren().remove(marker);
                    TrackHolder apSound = new TrackHolder("C://Users//alire//IdeaProjects//helpMe//src//main//resources//com//example//helpme//Tracks//airplane-fly-over-01.mp3", 3.0, 0.5, 1);
                    trackHolderArrayList.add(apSound);
                    ap.move(marker, playerTank1, playerTank2);
                }));
                toRemove.setCycleCount(1);
                toRemove.play();
                pain.getChildren().add(ap);
                timelineArrayList.add(toRemove);
//            System.out.println(pain.getChildren().indexOf(ap));
            }
        }));
        airStrike.setCycleCount(Animation.INDEFINITE);
        airStrike.play();


        timelineArrayList.add(focus);
        timelineArrayList.add(trainMove);
        timelineArrayList.add(itemSpawn);
        timelineArrayList.add(mainGameTimeLine);
        timelineArrayList.add(airStrike);

    }

    private void pause() {
        pause = !pause;
        for (Timeline t : timelineArrayList) {
            if (t == keyTimeline) continue;
            if (t != null) {
                if (pause) {
                    t.pause();
                    gameTimer.stopIt();
                } else {
                    t.play();
                    gameTimer.resume();
                }
            }
        }
        for (TrackHolder t : trackHolderArrayList) {
            if (t != null) {
                if (pause) {
                    t.pause();
                } else {
                    t.resume();
                }
            }
        }


//        setPaused(pause);
    }

    public void endGame(String winnerName, String gameID, int totalGames, int wins, int loss, int draws, String gameStatus) {

        for (Timeline t : timelineArrayList) {
            t.stop();
        }
        gameTimer.stopIt();
        for (TrackHolder t : trackHolderArrayList) {
            t.stop();
        }

        pain.setOnKeyPressed(null);
        pain.setOnKeyReleased(null);

        VBox overlay = new VBox(20);
        overlay.setPrefSize(pain.getWidth(), pain.getHeight());
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        overlay.setAlignment(Pos.CENTER);

        Label gameOverLabel = new Label("\t Game Over\nWinner: " + winnerName);
        gameOverLabel.setTextFill(Color.WHITE);
        gameOverLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> {
            Platform.exit();
        });
        Button back = new Button("Back");
        back.setOnAction(e -> {
            SceneSwitcher.switchTo("entering.fxml", e, LoginScreen.class, LoginScreen::start);
        });
        Button playAgainButton = getButton(overlay);

        overlay.getChildren().addAll(gameOverLabel, playAgainButton, back, quitButton);
        pain.getChildren().add(overlay);

        String user = getCurrentPlayerUser();
        String nationalId = getCurrentPlayerNationalId();
        String gameDataPath = "src/main/resources/com/example/helpme/Data/Games_data.txt";
        File gameDataFile = new File(gameDataPath);
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(gameDataFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
//                System.out.println("Checking line: " + line);
//                System.out.println("parts[0]: '" + parts[0] + "', user: '" + user + "'");
                if (parts.length >= 6 && parts[0].equals(user)) {
                    String updatedLine = user + "," + nationalId + "," + totalGames + "," + wins + "," + loss + "," + draws;
                    updatedLines.add(updatedLine);
                    System.out.println(updatedLine);
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading game data.");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(gameDataFile))) {
            for (String updatedLine : updatedLines) {
                bw.write(updatedLine);
                bw.newLine();
            }
            System.out.println("Game data updated.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error writing game data.");
        }

        String gameLogPath = "src/main/resources/com/example/helpme/Data/Game_logs.txt";
        File gameLogFile = new File(gameLogPath);

        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (gameID == null || gameID.isEmpty()) {
            gameID = UUID.randomUUID().toString();
        }

        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(gameLogFile, true))) {
            String logLine = user + "," + nationalId + gameID + "," + gameStatus + "," + formattedTime;
            logWriter.write(logLine);
            logWriter.newLine();
            System.out.println("Game log saved: " + logLine);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error writing game log.");
        }

    }

    private String getCurrentPlayerUser() {
        return currentUser;
    }

    public String getCurrentPlayerNationalId() {
        return User.currentNationalId;
    }

    private Button getButton(VBox overlay) {
        Button playAgainButton = new Button("Play Again");
        playAgainButton.setOnAction(e -> {
            for (Timeline t : timelineArrayList) {
                if (t != null) {
                    t.stop();
                }
            }

            pain.getChildren().clear();
            items1.clear();
            items2.clear();
            items3.clear();
            items4.clear();
            items5.clear();
            mineArrayList.clear();
            wallsArrayList.clear();
            fenceArrayList.clear();
            timelineArrayList.clear();
            currentTrain = null;

            intGame();
        });
        return playAgainButton;
    }

    public void MapGen() {
        //      /////////////////////////////<Walls adder>///////////////////////////////////////
        Walls wall1 = new Walls(300, 0, 50, 200, 0);
        Walls wall2 = new Walls(750, 0, 50, 200, 90);
        Walls wall3 = new Walls(750, 450, 50, 200, 90);
        Walls wall4 = new Walls(1030, 0, 50, 200, 180);
        pain.getChildren().add(wall1);
        pain.getChildren().add(wall2);
        pain.getChildren().add(wall3);
        pain.getChildren().add(wall4);
        wallsArrayList.add(wall1);
        wallsArrayList.add(wall2);
        wallsArrayList.add(wall3);
        wallsArrayList.add(wall4);
//      /////////////////////////////////<Fence adder>///////////////////////////////////////////////
        Fence fence1 = new Fence(500, 0, 50, 200, 0);
        Fence fence2 = new Fence(950, 0, 50, 200, 90);
        Fence fence3 = new Fence(950, 450, 50, 200, 90);
        Fence fence4 = new Fence(1230, 0, 50, 200, 180);
        pain.getChildren().add(fence1);
        pain.getChildren().add(fence2);
        pain.getChildren().add(fence3);
        pain.getChildren().add(fence4);
        fenceArrayList.add(fence1);
        fenceArrayList.add(fence2);
        fenceArrayList.add(fence3);
        fenceArrayList.add(fence4);
//  ////////////////////////////<Rail adder>//////////////////////////////////////////
        for (int i = 1005; i >= -45; i = i - 75) {
            Rail rail = new Rail(i, 570, 75, 75, 90);
            pain.getChildren().add(rail);
        }
//        Rail rail1 = new Rail(1005, 570, 75, 75, 90);
//        Rail rail2 = new Rail(930, 570, 75, 75, 90);
//        Rail rail3 = new Rail(855, 570, 75, 75, 90);
//        Rail rail4 = new Rail(780, 570, 75, 75, 90);
//        Rail rail5 = new Rail(705, 570, 75, 75, 90);
//        Rail rail6 = new Rail(630, 570, 75, 75, 90);
//        Rail rail7 = new Rail(555, 570, 75, 75, 90);
//        Rail rail8 = new Rail(480, 570, 75, 75, 90);
//        Rail rail9 = new Rail(405, 570, 75, 75, 90);
//        Rail rail10 = new Rail(330, 570, 75, 75, 90);
//        Rail rail11 = new Rail(255, 570, 75, 75, 90);
//        Rail rail12 = new Rail(180, 570, 75, 75, 90);
//        Rail rail13 = new Rail(105, 570, 75, 75, 90);
//        Rail rail14 = new Rail(30, 570, 75, 75, 90);
//        Rail rail15 = new Rail(-45, 570, 75, 75, 90);

//        pain.getChildren().addAll(rail1, rail2, rail3, rail4, rail5, rail6, rail7, rail8, rail9, rail10, rail11, rail12, rail13, rail14, rail15);
//        railArrayList.addAll(Arrays.asList(rail1, rail2, rail3, rail4, rail5, rail6, rail7, rail8, rail9, rail10, rail11, rail12, rail13, rail14, rail15));

    }
}
