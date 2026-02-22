package com.example.helpme;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public abstract class Tank extends ImageView {
    String imagePath;

    public Tank(String imagePath) {
        super();
        setImage(new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm()));
        this.imagePath = imagePath;

    }

    public void setPos(double x, double y) {
        this.setLayoutX(x);
        this.setLayoutY(y);
    }
    public double getWidth() {
        return this.getImage().getWidth();
    }

    public double getHeight() {
        return this.getImage().getHeight();
    }
}
