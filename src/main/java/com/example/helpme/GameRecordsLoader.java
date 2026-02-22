package com.example.helpme;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class GameRecordsLoader {

    public List<GameRecords> loadIt(String currentUser) {
        List<GameRecords> gameHistory = new ArrayList<>();
        String gameLogPath = "src/main/resources/com/example/helpme/Data/Game_logs.txt";
        boolean notInfo = true;

        try {
            List<String> lines = Files.readAllLines(Paths.get(gameLogPath));
            for (String line : lines) {
                String[] parts = line.split(",");
                System.out.println("Line: '" + line + "', parts[1]: '" + parts[1] + "', currentUser: '" + currentUser + "'");
                if (parts.length >= 4 && parts[0].equals(currentUser)) {
                    if (notInfo) {
                        notInfo = false;
                        continue;
                    }
                    gameHistory.add(new GameRecords(parts[1], parts[1], parts[2], parts[3]));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gameHistory;
    }

    public String loadUserSummary(String currentUser) {
        String gameLogPath = "src/main/resources/com/example/helpme/Data/Game_Logs.txt";

        try {
            List<String> lines = Files.readAllLines(Paths.get(gameLogPath));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[0].equals(currentUser)) {
                    return "User: " + parts[0] + " | Wins: " + parts[2] + " | Losses: " + parts[3] + " | Draws: " + parts[4];
                } else {
                    throw new Exceptions.NoSummeryForAdmin("Admins will NOT have summery");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No summary found";
    }
}
