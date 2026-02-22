package com.example.helpme;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Objects;

public class Train extends ImageView implements trainMovable,Collision{
    TrackHolder moveSound = null;
    public Train(){
        super();
        setImage(new Image(Objects.requireNonNull(getClass().getResource("tnt.png")).toExternalForm()));
        this.setFitHeight(70);
        this.setFitWidth(70);
        this.setLayoutY(575);
        this.setLayoutX(1005);
    }

    @Override
    public void move(Train t, PlayerTank playerTank1, PlayerTank playerTank2, Pane pain) {
        Timeline moveIt = new Timeline(new KeyFrame(Duration.millis(16),event -> {
            t.setLayoutX(t.getLayoutX()-5);
            if (getLayoutX()+this.getFitWidth() < 0) {
                pain.getChildren().remove(this);
                moveSound.stop();
            }
//            System.out.println(t.getLayoutX());
//            System.out.println(t.getLayoutY());
            if (moveSound == null) {
                moveSound = new TrackHolder("C://Users//alire//IdeaProjects//helpMe//src//main//resources//com//example//helpme//Tracks//Minecart_rolling.mp3", 1.0,1.0,Animation.INDEFINITE);
                HelloController.trackHolderArrayList.add(moveSound);
            }
            if (playerTank1.isAlive() && playerTank2.isAlive()){
                if (playerTank1.checkCollision(t)) {
                    playerTank1.decreaseHealth(Integer.MAX_VALUE,pain);
                }
                if (playerTank2.checkCollision(t)) {
                    playerTank2.decreaseHealth(Integer.MAX_VALUE,pain);
                }
            }
        }));
        moveIt.setCycleCount(Animation.INDEFINITE);
        moveIt.play();
        HelloController.timelineArrayList.add(moveIt);
    }

    @Override
    public void move() {
        throw new UnsupportedOperationException("Not used");
    }

    @Override
    public boolean checkCollision(Collision other) {
        if (other instanceof javafx.scene.Node otherNode) {
            return this.getBoundsInParent().intersects(otherNode.getBoundsInParent());
        }
        return false;
    }

}
