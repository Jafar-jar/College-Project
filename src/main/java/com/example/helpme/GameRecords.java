package com.example.helpme;

public class GameRecords {
    private final String gameId;
    private final String username;
    private final String result;
    private final String dateTime;

    public GameRecords(String gameId, String username, String result, String dateTime) {
        this.gameId = gameId;
        this.username = username;
        this.result = result;
        this.dateTime = dateTime;
    }

    public String getGameId() {
        return gameId;
    }

    public String getUsername() {
        return username;
    }

    public String getResult() {
        return result;
    }

    public String getDateTime() {
        return dateTime;
    }
}
