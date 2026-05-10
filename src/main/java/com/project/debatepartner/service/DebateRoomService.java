package com.project.debatepartner.service;

import com.project.debatepartner.model.DebateRoom;
import com.project.debatepartner.model.Message;
import com.project.debatepartner.repository.DebateRoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebateRoomService {

    @Autowired
    private DebateRoomRepository repository;

    // =========================
    // CREATE ROOM
    // =========================
    public DebateRoom createRoom(String topic, String username) {

        DebateRoom room =
                new DebateRoom(topic, username);

        return repository.save(room);
    }

    // =========================
    // JOIN ROOM
    // =========================
    public DebateRoom joinRoom(String roomId,
                               String username) {

        DebateRoom room =
                repository.findById(roomId);

        // ROOM NOT FOUND
        if(room == null){
            return null;
        }

        // ROOM FULL
        if(room.getPlayer2() != null){
            return null;
        }

        // SAME USER
        if(username.equals(room.getPlayer1())){
            return room;
        }

        // JOIN PLAYER 2
        room.setPlayer2(username);

        // START ROOM
        room.setStarted(true);

        return room;
    }

    // =========================
    // SEND MESSAGE
    // =========================
    public void sendMessage(String roomId,
                            Message message) {

        DebateRoom room =
                repository.findById(roomId);

        if(room != null){
            room.addMessage(message);
        }
    }

    // =========================
    // GET MESSAGES
    // =========================
    public List<Message> getMessages(String roomId) {

        DebateRoom room =
                repository.findById(roomId);

        return room != null
                ? room.getMessages()
                : null;
    }

    // =========================
    // GET ROOM
    // =========================
    public DebateRoom getRoom(String roomId) {

        return repository.findById(roomId);
    }
}