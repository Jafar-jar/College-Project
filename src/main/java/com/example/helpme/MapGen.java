package com.example.helpme;

import javafx.scene.layout.Pane;

import java.util.Arrays;

public class MapGen extends HelloController {
    public MapGen(Pane pain){
        //      /////////////////////////////<Walls adder>///////////////////////////////////////
        Walls wall1 = new Walls(300, 0, 50, 200, 0);
        Walls wall2 = new Walls(750, 0, 50, 200, 90);
        Walls wall3 = new Walls(750, 450, 50, 200, 90);
        Walls wall4 = new Walls(1030, 0, 50, 200, 180);
        pain.getChildren().add(wall1);
        pain.getChildren().add(wall2);
        pain.getChildren().add(wall3);
        pain.getChildren().add(wall4);
        wallsArrayList.add(wall1);
        wallsArrayList.add(wall2);
        wallsArrayList.add(wall3);
        wallsArrayList.add(wall4);
//      /////////////////////////////////<Fence adder>///////////////////////////////////////////////
        Fence fence1 = new Fence(500, 0, 50, 200, 0);
        Fence fence2 = new Fence(950, 0, 50, 200, 90);
        Fence fence3 = new Fence(950, 450, 50, 200, 90);
        Fence fence4 = new Fence(1230, 0, 50, 200, 180);
        pain.getChildren().add(fence1);
        pain.getChildren().add(fence2);
        pain.getChildren().add(fence3);
        pain.getChildren().add(fence4);
        fenceArrayList.add(fence1);
        fenceArrayList.add(fence2);
        fenceArrayList.add(fence3);
        fenceArrayList.add(fence4);
//  ////////////////////////////<Rail adder>//////////////////////////////////////////
        for (int i = 1005; i >= -45; i = i-75) {
            Rail rail = new Rail(i, 570, 75, 75, 90);
            pain.getChildren().add(rail);
        }
//        Rail rail1 = new Rail(1005, 570, 75, 75, 90);
//        Rail rail2 = new Rail(930, 570, 75, 75, 90);
//        Rail rail3 = new Rail(855, 570, 75, 75, 90);
//        Rail rail4 = new Rail(780, 570, 75, 75, 90);
//        Rail rail5 = new Rail(705, 570, 75, 75, 90);
//        Rail rail6 = new Rail(630, 570, 75, 75, 90);
//        Rail rail7 = new Rail(555, 570, 75, 75, 90);
//        Rail rail8 = new Rail(480, 570, 75, 75, 90);
//        Rail rail9 = new Rail(405, 570, 75, 75, 90);
//        Rail rail10 = new Rail(330, 570, 75, 75, 90);
//        Rail rail11 = new Rail(255, 570, 75, 75, 90);
//        Rail rail12 = new Rail(180, 570, 75, 75, 90);
//        Rail rail13 = new Rail(105, 570, 75, 75, 90);
//        Rail rail14 = new Rail(30, 570, 75, 75, 90);
//        Rail rail15 = new Rail(-45, 570, 75, 75, 90);

//        pain.getChildren().addAll(rail1, rail2, rail3, rail4, rail5, rail6, rail7, rail8, rail9, rail10, rail11, rail12, rail13, rail14, rail15);
//        railArrayList.addAll(Arrays.asList(rail1, rail2, rail3, rail4, rail5, rail6, rail7, rail8, rail9, rail10, rail11, rail12, rail13, rail14, rail15));

    }
}
