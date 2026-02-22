package com.example.helpme;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

public interface TankMovable extends Movable {
    void move();
    void move(Move direction, ArrayList<Walls> walls, Pane pane, ArrayList<Fence> fences, PlayerTank enemy, double ratio, ArrayList<DamageableWalls> damageableWallsArrayList);
}
