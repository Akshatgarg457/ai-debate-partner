package com.project.debatepartner.controller;

import com.project.debatepartner.model.DebateRoom;
import com.project.debatepartner.model.Message;
import com.project.debatepartner.service.DebateRoomService;
import com.project.debatepartner.service.GroqService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/human")
@CrossOrigin("*")
public class HumanDebateController {

    @Autowired
    private DebateRoomService service;

    @Autowired
    private GroqService groqService;

    // =========================
    // CREATE ROOM
    // =========================
    @PostMapping("/create")
    public DebateRoom createRoom(
            @RequestParam String topic,
            @RequestParam String username) {

        return service.createRoom(
                topic,
                username
        );
    }

    // =========================
    // JOIN ROOM
    // =========================
    @PostMapping("/join")
    public DebateRoom joinRoom(
            @RequestParam String roomId,
            @RequestParam String username) {

        return service.joinRoom(
                roomId,
                username
        );
    }

    // =========================
    // SEND MESSAGE
    // =========================
    @PostMapping("/send")
    public void sendMessage(
            @RequestParam String roomId,
            @RequestBody Message message) {

        service.sendMessage(
                roomId,
                message
        );
    }

    // =========================
    // GET MESSAGES
    // =========================
    @GetMapping("/messages/{roomId}")
    public List<Message> getMessages(
            @PathVariable String roomId) {

        return service.getMessages(roomId);
    }

    // =========================
    // GET ROOM
    // =========================
    @GetMapping("/room/{roomId}")
    public DebateRoom getRoom(
            @PathVariable String roomId) {

        return service.getRoom(roomId);
    }

    // =========================
    // END DEBATE
    // =========================
    @PostMapping("/end/{roomId}")
    public DebateRoom endDebate(
            @PathVariable String roomId){

        DebateRoom room =
                service.getRoom(roomId);

        if(room == null){
            return null;
        }

        // BUILD CHAT
        StringBuilder debateText =
                new StringBuilder();

        for(Message msg : room.getMessages()){

            debateText.append(
                    msg.getSender()
            )
            .append(": ")
            .append(msg.getContent())
            .append("\n");
        }

        // AI RESULT
        String result =
                groqService.judgeHumanDebate(
                        room.getTopic(),
                        debateText.toString()
                );

        room.setResult(result);

        // WINNER
        if(result.toLowerCase().contains(
                room.getPlayer1().toLowerCase())){

            room.setWinner(
                    room.getPlayer1()
            );
        }

        else if(room.getPlayer2() != null &&
                result.toLowerCase().contains(
                        room.getPlayer2().toLowerCase())){

            room.setWinner(
                    room.getPlayer2()
            );
        }

        return room;
    }
}