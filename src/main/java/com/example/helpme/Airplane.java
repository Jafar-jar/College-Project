package com.example.helpme;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Airplane extends ImageView implements airplaneMove, Collision {
    Timeline nine11;
    Pane pane;
    private boolean airplaneInZone = false;
    private TrackHolder bomb = null;

    public Airplane(double x, double y, Circle marker, Pane pain) {
        super();
        setImage(new Image(Objects.requireNonNull(getClass().getResource("911.png")).toExternalForm()));
        this.pane = pain;
        this.setScaleX(.5);
        this.setScaleY(.5);
        //منهای 250 تا بیاد رو کونش
        this.setX(x - 250);
        this.setY(y - 500);
        this.setRotate(180);


    }

    @Override
    public void move(Circle marker, PlayerTank player1, PlayerTank player2) {
        boolean[] player1Hit = {false};
        boolean[] player2Hit = {false};
        boolean[] bombPlayed = {false};
        nine11 = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            this.setY(getY() - 3);
            Timeline hitting = new Timeline(new KeyFrame(Duration.millis(1), event1 -> {

                airplaneInZone = this.getBoundsInParent().intersects(marker.getBoundsInParent());

                if (airplaneInZone && !bombPlayed[0]) {
                    bombPlayed[0] = true;
                    bomb = new TrackHolder("C://Users//alire//IdeaProjects//helpMe//src//main//resources//com//example//helpme//Tracks//heavy-cineamtic-hit-166888.mp3", 1.0, 1.0, 1);
                    HelloController.trackHolderArrayList.add(bomb);
                }

                if (airplaneInZone) {
                    if (!player1Hit[0] && player1.getBoundsInParent().intersects(marker.getBoundsInParent())) {
                        player1.decreaseHealth(20, pane);
                        player1Hit[0] = true;
                        System.out.println("Player 1 hit by airstrike!");
                    }

                    if (!player2Hit[0] && player2.getBoundsInParent().intersects(marker.getBoundsInParent())) {
                        player2.decreaseHealth(20, pane);
                        player2Hit[0] = true;
                        System.out.println("Player 2 hit by airstrike!");
                    }
                }
            }));
            hitting.setCycleCount(1);
            hitting.play();
            HelloController.timelineArrayList.add(nine11);

            if (this.getY() >= pane.getHeight()) {
                nine11.stop();
                pane.getChildren().remove(this);
            }
        }));
        nine11.setCycleCount(Animation.INDEFINITE);
        nine11.play();
    }

    @Override
    public void move() {

    }

    @Override
    public boolean checkCollision(Collision other) {
        if (other instanceof javafx.scene.Node otherNode) {
            return this.getBoundsInParent().intersects(otherNode.getBoundsInParent());
        }
        return false;
    }
}
