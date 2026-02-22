package com.example.helpme;

import javafx.scene.layout.Pane;

public interface DamageableWall extends Damageable{
    void decreaseHealth();
    void decreaseHealth(int damage, Pane pain);
}
