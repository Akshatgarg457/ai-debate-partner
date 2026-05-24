package com.project.debatepartner.service;

import com.project.debatepartner.model.DebateRoom;
import com.project.debatepartner.model.Message;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DebateRoomService {

    // Store active rooms in memory

    private final Map<String, DebateRoom> rooms =
            new ConcurrentHashMap<>();


    // ======================
    // CREATE ROOM
    // ======================

    public DebateRoom createRoom(

            String topic,

            String username
    ){

        DebateRoom room =

                new DebateRoom(

                        topic,

                        username
                );

        rooms.put(

                room.getRoomId(),

                room
        );

        return room;
    }


    // ======================
    // JOIN ROOM
    // ======================

    public DebateRoom joinRoom(

            String roomId,

            String username
    ){

        DebateRoom room =

                rooms.get(
                        roomId
                );

        if(
                room == null
        ){

            return null;
        }


        // second player joins

        if(
                room.getPlayer2() == null
        ){

            room.setPlayer2(
                    username
            );

            room.setStarted(
                    true
            );
        }

        return room;
    }


    // ======================
    // GET ROOM
    // ======================

    public DebateRoom getRoom(

            String roomId
    ){

        return rooms.get(
                roomId
        );
    }


    // ======================
    // ADD MESSAGE
    // ======================

    public void addMessage(

            String roomId,

            Message message
    ){

        DebateRoom room =

                rooms.get(
                        roomId
                );

        if(
                room != null
        ){

            room.addMessage(
                    message
            );
        }
    }


    // ======================
    // SAVE RESULT TO ROOM
    // ======================

    public void saveResult(

            String roomId,

            String result,

            String winner
    ){

        DebateRoom room =

                rooms.get(
                        roomId
                );

        if(
                room != null
        ){

            room.setResult(
                    result
            );

            room.setWinner(
                    winner
            );
        }
    }


    // ======================
    // REMOVE ROOM
    // ======================

    public void removeRoom(

            String roomId
    ){

        rooms.remove(
                roomId
        );
    }

}