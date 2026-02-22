package com.example.helpme;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class addBullet extends ImageView implements Collision {
    private final int bulletAdd = 3;
    private double x = (Math.random() * 1080) + 1;
    private double y = (Math.random() * 720) + 1;

    public int getBulletAdd() {
        return bulletAdd;
    }

    public addBullet() {
        super();
        setImage(new Image(Objects.requireNonNull(getClass().getResource("mag.png")).toExternalForm()));
        this.setFitWidth(40);
        this.setFitHeight(25);

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
