package com.example.helpme;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Rail extends ImageView {
    public Rail(int x , int y , int w , int h , double e){
        super();
        setImage(new Image(Objects.requireNonNull(getClass().getResource("rail.png")).toExternalForm()));
        this.setX(x);
        this.setY(y);
        this.setFitWidth(w);
        this.setFitHeight(h);
        this.setRotate(e);
    }
}
