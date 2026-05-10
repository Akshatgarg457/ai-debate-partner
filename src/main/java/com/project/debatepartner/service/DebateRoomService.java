package com.project.debatepartner.service;

import com.project.debatepartner.model.DebateRoom;
import com.project.debatepartner.model.Message;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DebateRoomService {

    private final Map<String, DebateRoom> rooms = new HashMap<>();

    // CREATE ROOM
    public DebateRoom createRoom(String topic, String username) {

        DebateRoom room = new DebateRoom(topic, username);

        rooms.put(room.getRoomId(), room);

        return room;
    }

    // JOIN ROOM
    public DebateRoom joinRoom(String roomId, String username) {

        DebateRoom room = rooms.get(roomId);

        if (room == null) {
            return null;
        }

        if (room.getPlayer2() != null) {
            return null;
        }

        room.setPlayer2(username);
        room.setStarted(true);

        return room;
    }

    // SEND MESSAGE
    public void sendMessage(String roomId, Message message) {

        DebateRoom room = rooms.get(roomId);

        if (room != null) {
            room.addMessage(message);
        }
    }

    // GET MESSAGES
    public List<Message> getMessages(String roomId) {

        DebateRoom room = rooms.get(roomId);

        if (room == null) {
            return new ArrayList<>();
        }

        return room.getMessages();
    }

    // GET ROOM
    public DebateRoom getRoom(String roomId) {

        return rooms.get(roomId);
    }
}