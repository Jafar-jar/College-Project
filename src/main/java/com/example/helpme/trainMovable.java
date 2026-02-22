package com.example.helpme;

import javafx.scene.layout.Pane;

public interface trainMovable extends Movable {
    void move();
    void move(Train t, PlayerTank playerTank1, PlayerTank playerTank2, Pane pain);
}
