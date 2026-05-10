package com.project.debatepartner.repository;

import com.project.debatepartner.model.DebateRoom;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class DebateRoomRepository {

    private Map<String, DebateRoom> rooms = new HashMap<>();

    public DebateRoom save(DebateRoom room) {
        rooms.put(room.getRoomId(), room);
        return room;
    }

    public DebateRoom findById(String roomId) {
        return rooms.get(roomId);
    }

    public Collection<DebateRoom> findAll() {
        return rooms.values();
    }
}