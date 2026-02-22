package com.example.helpme;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class DamageableWalls extends Walls implements DamageableWall,Collision{
    private int wallHp;
    private int stage;
    public DamageableWalls(int x, int y, int w, int h, double e, int hp) {
        super(x, y, w, h, e);
        this.wallHp = hp;
        stage = hp;
        setImage(new Image(Objects.requireNonNull(getClass().getResource("wall.png")).toExternalForm()));
        setImage(wallHp);
    }
    public void setImage(int wallHp){
        if (wallHp == 1){
        } else if (wallHp == 2){
            setImage(new Image(Objects.requireNonNull(getClass().getResource("wall5.png")).toExternalForm()));
        } else if (wallHp == 3){
            setImage(new Image(Objects.requireNonNull(getClass().getResource("wall4.png")).toExternalForm()));
        } else if (wallHp == 4) {
            setImage(new Image(Objects.requireNonNull(getClass().getResource("wall3.png")).toExternalForm()));
        } else if (wallHp == 5) {
            setImage(new Image(Objects.requireNonNull(getClass().getResource("wall2.png")).toExternalForm()));
        } else if (wallHp == 6) {
            setImage(new Image(Objects.requireNonNull(getClass().getResource("wall.png")).toExternalForm()));
        }
    }

    @Override
    public void decreaseHealth() {
        throw new UnsupportedOperationException("Not used");
    }

    @Override
    public void decreaseHealth(int damage, Pane pain){
        if (wallHp > 0){
            wallHp-=damage;
            setImage(wallHp);
        }
        if (wallHp <= 0) {
            pain.getChildren().remove(this);
            HelloController.damageableWallsArrayList.remove(this);
        }
    }
    @Override
    public boolean checkCollision(Collision other) {
        if (other instanceof javafx.scene.Node otherNode) {
            return this.getBoundsInParent().intersects(otherNode.getBoundsInParent());
        }
        return false;
    }
}
