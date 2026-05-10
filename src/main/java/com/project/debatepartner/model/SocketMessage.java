package com.project.debatepartner.model;

public class SocketMessage {

    private String roomId;
    private String sender;
    private String content;
    private String type; // CHAT / JOIN / TYPING

    public SocketMessage() {}

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}