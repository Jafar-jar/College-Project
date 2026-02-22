package com.example.helpme;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Fence extends ImageView implements Collision{

    public Fence(int x, int y, int w, int h, double e) {
        super();
        setImage(new Image(Objects.requireNonNull(getClass().getResource("fence.png")).toExternalForm()));
        this.setX(x);
        this.setY(y);
        this.setFitWidth(w);
        this.setFitHeight(h);
        this.setRotate(e);
    }
    @Override
    public boolean checkCollision(Collision other) {
        if (other instanceof javafx.scene.Node otherNode) {
            return this.getBoundsInParent().intersects(otherNode.getBoundsInParent());
        }
        return false;
    }
}
