package com.example.helpme;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerTank extends Tank implements TankMovable, DamageableTank, Collision {

    private int health;
    private int ammo;
    private int speed;
    private final int rotationSpeed;
    private Rectangle hpBar;
    private boolean alive = true;
    private boolean canShoot = true;
    private boolean isPowered = false;
    private boolean isSpeeding = false;
    private boolean canPass = false;

    private ImageView hpIcon;
    private Label hpLabel;
    private Label ammoLabel;
    private Pane infoPane;
    private TrackHolder movementSound;


    private int infoX;
    private int infoY;
    private String hpImage;
    private boolean isLeftToRight = true;
    private boolean isAmmoLabelLeftToRight = true;

    public PlayerTank(String imagePath, int angle, String hpImage) {
        super(imagePath);
        this.hpImage = hpImage;
        this.health = 50;
        this.ammo = 10;
        this.speed = 250;
        this.rotationSpeed = 120;
        this.setFitWidth(125);
        this.setFitHeight(45);
        this.setRotate(angle);
    }

    public void setHealthBarDirection(boolean leftToRight) {
        this.isLeftToRight = leftToRight;
    }

    public void setAmmoLabelDirection(boolean leftToRight) {
        this.isAmmoLabelLeftToRight = leftToRight;
    }

    @Override
    public void move(Move pos, ArrayList<Walls> wallsArrayList, Pane pain, ArrayList<Fence> fenceArrayList, PlayerTank enemy, double ratio, ArrayList<DamageableWalls> damageableWallsArrayList) {
        if (isSpeeding) {
            this.speed = 1000;
        } else {
            this.speed = 250;
        }

        double deltaX = 0;
        double deltaY = 0;
        double deltaRotation = 0;

        if (!alive) return;

//        if (movementSound == null) {
//            movementSound = new TrackHolder("C://Users//alire//IdeaProjects//helpMe//src//main//resources//com//example//helpme//Tracks//tank-track-ratteling-197409.mp3", 1.0, 1.0, Animation.INDEFINITE);
//        } else if (!movementSound.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PLAYING)) {
//            movementSound.getMediaPlayer().play();
//        }


        if (pos.equals(Move.UP)) {
            double temp = Math.toRadians(this.getRotate());
            deltaX = -Math.cos(temp) * this.speed * ratio;
            deltaY = -Math.sin(temp) * this.speed * ratio;
        }
        if (pos.equals(Move.LEFT)) {
            deltaRotation = -rotationSpeed * ratio;
        }
        if (pos.equals(Move.DOWN)) {
            double temp = Math.toRadians(this.getRotate());
            deltaX = Math.cos(temp) * this.speed * ratio;
            deltaY = Math.sin(temp) * this.speed * ratio;
        }
        if (pos.equals(Move.RIGHT)) {
            deltaRotation = rotationSpeed * ratio;
        }

        double newX = this.getLayoutX() + deltaX;
        double newY = this.getLayoutY() + deltaY;
        double newRotation = this.getRotate() + deltaRotation;

        this.setLayoutX(newX);
        this.setLayoutY(newY);
        this.setRotate(newRotation);

        boolean blocked = false;
        if (!canPass) {
            for (Walls wall : wallsArrayList) {
                if (this.checkCollision(wall)) {
                    blocked = true;
                    break;
                }
            }
        }

        boolean fenceBlocked = false;
        if (!canPass) {
            for (Fence fence : fenceArrayList) {
                if (this.checkCollision(fence)) {
                    fenceBlocked = true;
                    break;
                }
            }
        }

        boolean damagedWallBlocked = false;
        if (!canPass) {
            for (DamageableWalls damageableWall : damageableWallsArrayList) {
                if (this.checkCollision(damageableWall)) {
                    damagedWallBlocked = true;
                    break;
                }
            }
        }

        boolean isAnotherTank = false;
        if (!canPass) {
            if (this.checkCollision(enemy)) {
                isAnotherTank = true;
            }
        }

        if (blocked || fenceBlocked || isAnotherTank || damagedWallBlocked) {
            this.setLayoutX(this.getLayoutX() - deltaX);
            this.setLayoutY(this.getLayoutY() - deltaY);
            this.setRotate(this.getRotate() - deltaRotation);
        }

        //System.out.println(newY +tankHeight);
//            System.out.println(paneHeight);
//            System.out.println(paneHeight);
//            System.out.println(paneWidth);
//            System.out.println(this.getY());

        double tankWidth = this.getFitWidth();
        double tankHeight = this.getFitHeight();

        if (newX < 0 || newX + tankWidth > pain.getScene().getWidth()) {
            this.setLayoutX(Math.max(0, Math.min(pain.getScene().getWidth() - tankWidth, newX)));

        }

        if (newY < 0 || newY + tankHeight > pain.getScene().getHeight()) {
            this.setLayoutY(Math.max(0, Math.min(pain.getScene().getHeight() - tankHeight, newY)));

        }
        System.out.println(this.getLayoutX());
        System.out.println(this.getLayoutY());


    }


    public void shoot(Pane pain, PlayerTank enemy, ArrayList<Walls> walls, Train train, ArrayList<DamageableWalls> damageableWalls) {
        if (ammo > 0 && canShoot && alive) {
            ammo--;
            double bulletX = this.getLayoutX() + this.getBoundsInLocal().getWidth() / 2;
            double bulletY = this.getLayoutY() + this.getBoundsInLocal().getHeight() / 2;
            double angle = this.getRotate();
            Bullet bullet = new Bullet(bulletX, bulletY, angle, pain, this, enemy, isPowered, walls, train, damageableWalls);
            canShoot = false;
            if (this.getParent() instanceof Pane pane) {
                pane.getChildren().add(bullet);
            }
            Timeline wait = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                canShoot = true;
            }));
            wait.setCycleCount(1);
            wait.play();
            HelloController.timelineArrayList.add(wait);
            System.out.println("Ammo: " + ammo);
            updateInfo();
        } else {
            System.out.println("No ammo left!");
        }
    }

    public void replenishAmmo(int amount, Pane pain) {
        ammo += amount;
        System.out.println("Ammo replenished: " + ammo);
        updateInfo();
    }

    @Override
    public void decreaseHealth() {
        throw new UnsupportedOperationException("Not used");
    }

    @Override
    public void decreaseHealth(int amount, Pane pain) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            alive = false;
            TrackHolder exp = new TrackHolder("C://Users//alire//IdeaProjects//helpMe//src//main//resources//com//example//helpme//Tracks//explosion-42132.mp3", 1.0, 1.0, 1);
            pain.getChildren().remove(this);
            System.out.println("Player destroyed!");
        }
        System.out.println("Health: " + health);
        updateInfo();
    }

    public void powerUp() {
        if (isPowered) return;
        isPowered = true;
        Timeline temp = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            isPowered = false;
        }));
        temp.play();
        HelloController.timelineArrayList.add(temp);
    }

    public void speeding() {
        if (isSpeeding) return;
        isSpeeding = true;
        Timeline temp = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            isSpeeding = false;
        }));
        temp.play();
        HelloController.timelineArrayList.add(temp);
    }

    public int getHealth() {
        return health;
    }

    public int getAmmo() {
        return ammo;
    }

    public void increaseHealth(int hpAmount) {
        this.health += hpAmount;
        updateInfo();
        System.out.println(health);
    }

    public boolean isAlive() {
        return alive;
    }
//    public void mineDamage(Pane pain) {
//        decreaseHealth(5, pain);
//    }
//
//    public void airDamage(Pane pain, Airplane ap, Circle marker) {
//        if (this.getBoundsInParent().intersects(marker.getBoundsInParent())) {
//            decreaseHealth(10, pain);
//            System.out.println("Tank hit by airstrike!");
//        }
//    }

    public void ghostMode(ArrayList<Walls> walls, Pane pain, ArrayList<Fence> fences) {
        this.canPass = true;
        FadeTransition ghostMode = new FadeTransition(Duration.millis(500), this);
        ghostMode.setFromValue(0.3);
        ghostMode.setToValue(1.0);
        ghostMode.setAutoReverse(true);
        ghostMode.setCycleCount(FadeTransition.INDEFINITE);
        ghostMode.play();

        Timeline toPass = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            this.canPass = false;
            ghostMode.stop();
            this.setOpacity(1.0);

            boolean stillInsideWall = false;

            for (Walls wall : walls) {
                if (this.checkCollision(wall)) {
                    stillInsideWall = true;
                    break;
                }
            }
            boolean stillInsideFence = false;

            for (Fence fence : fences) {
                if (this.checkCollision(fence)) {
                    stillInsideFence = true;
                    break;
                }
            }

            if (stillInsideWall || stillInsideFence) {
                System.out.println("Ghost mode ended: Tank is stuck in wall → destroyed!");
                decreaseHealth(Integer.MAX_VALUE, pain);
            }
        }));

        toPass.setCycleCount(1);
        toPass.play();
        HelloController.timelineArrayList.add(toPass);
    }

    public void updateInfo() {
        if (hpBar != null) {
            int displayHealth = Math.max(0, this.health * 2);
            if (isLeftToRight) {
                hpBar.setX(infoX);
            } else {
                hpBar.setX(infoX + 50 - displayHealth);
            }
            hpBar.setHeight(50);
            hpBar.setWidth(displayHealth);
        }

        if (hpLabel != null) {
            hpLabel.setText(this.getHealth() + "");
        }

        if (ammoLabel != null) {
            if (isAmmoLabelLeftToRight) {
                ammoLabel.setText("Ammo: " + this.ammo);
            } else {
                ammoLabel.setText(this.ammo + " :Ammo");
            }
        }
    }

    public void initTank(Pane pain, int x, int y, int x2, int y2, int x3, int y3) {
        this.infoPane = pain;
        this.infoX = x2;
        this.infoY = y2;

        if (hpIcon == null) {
            hpIcon = new ImageView();
            hpIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource(hpImage)).toExternalForm()));
            hpIcon.setLayoutX(x);
            hpIcon.setLayoutY(y);
            hpIcon.setFitWidth(50);
            hpIcon.setFitHeight(50);
            pain.getChildren().add(hpIcon);
        }

        if (hpBar == null) {
            hpBar = new Rectangle(infoX, y, Math.max(0, this.health), 50);
            hpBar.setFill(Color.RED);
            hpBar.setOpacity(0.25);
            pain.getChildren().add(hpBar);
        }

        if (hpLabel == null) {
            hpLabel = new Label();
            hpLabel.setLayoutX(infoX);
            hpLabel.setLayoutY(y);
            hpLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");
            hpLabel.setText(this.getHealth() + "");
            pain.getChildren().add(hpLabel);
        }

        if (ammoLabel == null) {
            ammoLabel = new Label("Ammo: " + this.ammo);
            ammoLabel.setLayoutX(x3);
            ammoLabel.setLayoutY(y3 + 60);
            ammoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
            pain.getChildren().add(ammoLabel);
        }

        updateInfo();
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
