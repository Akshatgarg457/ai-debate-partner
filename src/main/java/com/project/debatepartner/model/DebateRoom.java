package com.project.debatepartner.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DebateRoom {

    private String roomId;

    private String topic;

    private String player1;

    private String player2;

    private boolean started = false;

    private List<Message> messages = new ArrayList<>();

    // =========================
    // CONSTRUCTOR
    // =========================
    public DebateRoom() {
        this.roomId = UUID.randomUUID().toString();
    }

    public DebateRoom(String topic, String player1) {

        this.roomId = UUID.randomUUID().toString();

        this.topic = topic;

        this.player1 = player1;
    }

    // =========================
    // GETTERS
    // =========================
    public String getRoomId() {
        return roomId;
    }

    public String getTopic() {
        return topic;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public boolean isStarted() {
        return started;
    }

    public List<Message> getMessages() {
        return messages;
    }

    // =========================
    // SETTERS
    // =========================
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    // =========================
    // ADD MESSAGE
    // =========================
    public void addMessage(Message message) {
        this.messages.add(message);
    }

    // =========================
    // PLAYER COUNT
    // =========================
    public int getPlayerCount(){

        int count = 0;

        if(player1 != null) count++;

        if(player2 != null) count++;

        return count;
    }
    // =========================
    // RESULT
    // =========================
    private String result;

    private String winner;

    // =========================
    // GET RESULT
    // =========================
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    // =========================
    // GET WINNER
    // =========================
    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}