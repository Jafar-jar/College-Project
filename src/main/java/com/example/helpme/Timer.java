package com.example.helpme;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Timer {
    private int x=540;
    private int y=50;
    public int time = (int)((Math.random()+.5)*60);
//public int time = 1000000;
    private Timeline time1;

    public Timer(Pane pain){
        Label timer = new Label();
        timer.setStyle("-fx-font-size: 20px");
        timer.setLayoutX(x);
        timer.setLayoutY(y);
        Rectangle timerBack = new Rectangle(80,30, Color.WHEAT);
        timerBack.setX(x);
        timerBack.setY(y);
        pain.getChildren().add(timerBack);
        pain.getChildren().add(timer);
        startCountdown(timer);
    }
    private void startCountdown(Label timer) {
        updateDisplay(timer);
        time1 = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (time <=0){
                timer.setText("00:00:00");
                return;
            }
            time--;
            updateDisplay(timer);
        }));

        time1.setCycleCount(Animation.INDEFINITE);
        time1.play();
    }

    private void updateDisplay(Label timer) {
        int hours = time / 3600;
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;

        timer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    public void stopIt() {
        time1.stop();
    }

    public void resume() {
        time1.play();
    }
}
