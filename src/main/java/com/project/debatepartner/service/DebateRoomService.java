package com.project.debatepartner.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DebateRoom {

    private String roomId;

    private String topic;

    private String player1;

    private String player2;

    private boolean started;

    private List<Message> messages =
            new ArrayList<>();


    // ADD THESE

    private String result;

    private String winner;



    public DebateRoom(
            String topic,
            String username
    ){

        this.roomId=
                UUID.randomUUID()
                .toString();

        this.topic=
                topic;

        this.player1=
                username;

        this.started=
                false;
    }


    public void addMessage(
            Message msg
    ){

        messages.add(msg);
    }



    // GETTERS / SETTERS

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

    public void setPlayer2(
            String player2
    ){
        this.player2=player2;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(
            boolean started
    ){
        this.started=started;
    }

    public List<Message> getMessages() {
        return messages;
    }


    // RESULT

    public String getResult() {
        return result;
    }

    public void setResult(
            String result
    ){
        this.result=result;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(
            String winner
    ){
        this.winner=winner;
    }

}