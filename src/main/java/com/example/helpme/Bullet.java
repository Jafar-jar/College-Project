package com.example.helpme;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;

public class Bullet extends ImageView implements BulletMovable, Collision {
    private static final int bSpeed = 40;
    private double angle;
    private PlayerTank notMine;
    private PlayerTank mine;
    private int damage = 1;
    private Timeline bM;
    private boolean power1;
    private boolean pause = false;

    public Bullet(double x, double y, double angle, Pane pain, PlayerTank you, PlayerTank enemy, boolean power, ArrayList<Walls> walls, Train train, ArrayList<DamageableWalls> damageableWalls) {
        super();
        setImage(new Image(Objects.requireNonNull(getClass().getResource("bullet.png")).toExternalForm()));
        this.mine = you;
        this.notMine = enemy;
        this.setFitWidth(10);
        this.setFitHeight(10);
        this.power1 = power;
        if (power1) {
            damage = 2;
        }

        this.setLayoutX(x);
        this.setLayoutY(y);
        this.angle = angle;
        this.setRotate(angle - 82);

        double rad = Math.toRadians(this.angle);
        double dx = -Math.cos(rad) * bSpeed;
        double dy = -Math.sin(rad) * bSpeed;

//        pain.getChildren().add(this);
        this.move(dx, dy, pain, walls, train, damageableWalls);
    }

    @Override
    public void move(double dx, double dy, Pane pain, ArrayList<Walls> walls, Train train, ArrayList<DamageableWalls> damageableWalls) {
        bM = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            setLayoutX(getLayoutX() + dx);
            setLayoutY(getLayoutY() + dy);
            if (this.checkCollision(notMine)) {
                notMine.decreaseHealth(damage, pain);
                pain.getChildren().remove(this);
                stopIt(bM);
                System.out.println("Target hit! Target health: " + notMine.getHealth());
            }
            for (Walls wall : walls) {
                if (this.checkCollision(wall)) {
                    pain.getChildren().remove(this);
                    stopIt(bM);
                    System.out.println("Bullet destroyed by wall.");
                    break;
                }
            }
            for (DamageableWalls damageableWall : damageableWalls) {
                if (this.checkCollision(damageableWall)) {
                    pain.getChildren().remove(this);
                    damageableWall.decreaseHealth(this.damage, pain);
                    System.out.println("Bullet destroyed by damaged wall.");
                    break;
                }
            }
            if (train != null && this.checkCollision(train)) {
                pain.getChildren().remove(this);
                stopIt(bM);
                System.out.println("Bullet destroyed by train.");
            }
        }));
        bM.setCycleCount(Animation.INDEFINITE);
        bM.play();
        HelloController.timelineArrayList.add(bM);
    }

    private void stopIt(Timeline bM) {
        bM.stop();
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
