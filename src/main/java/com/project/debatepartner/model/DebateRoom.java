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

    private String result;
    private String winner;

    public DebateRoom() {
        this.roomId = UUID.randomUUID().toString();
    }

    public DebateRoom(String topic, String player1) {
        this.roomId = UUID.randomUUID().toString();
        this.topic = topic;
        this.player1 = player1;
    }

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

    public String getResult() {
        return result;
    }

    public String getWinner() {
        return winner;
    }

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

    public void setResult(String result) {
        this.result = result;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }
}