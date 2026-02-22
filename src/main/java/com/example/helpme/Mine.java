package com.example.helpme;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;
import java.util.Random;

public class Mine extends ImageView implements Collision {
    public Mine() {
        super();
        setImage(new Image(Objects.requireNonNull(getClass().getResource("landMine.png")).toExternalForm()));
        Random random = new Random();
        double x = random.nextDouble() * (1000 - 50);
        double y = random.nextDouble() * (720 - 20);


        this.setFitHeight(50);
        this.setFitWidth(50);
        this.setLayoutX(x);
        this.setLayoutY(y);
    }
    @Override
    public boolean checkCollision(Collision other) {
        if (other instanceof javafx.scene.Node otherNode) {
            return this.getBoundsInParent().intersects(otherNode.getBoundsInParent());
        }
        return false;
    }

}
