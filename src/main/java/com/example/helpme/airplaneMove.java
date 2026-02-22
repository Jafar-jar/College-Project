package com.example.helpme;

import javafx.scene.shape.Circle;

public interface airplaneMove extends Movable {
    void move();
    void move(Circle marker, PlayerTank player1, PlayerTank player2);
}
