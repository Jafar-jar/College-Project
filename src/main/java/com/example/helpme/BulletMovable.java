package com.example.helpme;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

public interface BulletMovable extends Movable {
    void move();
    void move(double dx, double dy, Pane pain, ArrayList<Walls> walls, Train train, ArrayList<DamageableWalls> damageableWalls);
}
