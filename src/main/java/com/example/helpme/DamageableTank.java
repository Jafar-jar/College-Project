package com.example.helpme;

public interface DamageableTank extends Damageable {
    void decreaseHealth();
    void decreaseHealth(int amount, javafx.scene.layout.Pane pane);
    int getHealth();
}
