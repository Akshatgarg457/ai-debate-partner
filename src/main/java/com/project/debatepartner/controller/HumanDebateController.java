package com.project.debatepartner.controller;

import com.project.debatepartner.model.*;
import com.project.debatepartner.repository.DebateRepository;
import com.project.debatepartner.service.*;

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

    @Autowired
    private DebateRepository debateRepository;


    @PostMapping("/create")
    public DebateRoom createRoom(
            @RequestParam String topic,
            @RequestParam String username){

        return service.createRoom(
                topic,
                username
        );
    }


    @PostMapping("/join")
    public DebateRoom joinRoom(
            @RequestParam String roomId,
            @RequestParam String username){

        return service.joinRoom(
                roomId,
                username
        );
    }


    @GetMapping("/room/{roomId}")
    public DebateRoom getRoom(
            @PathVariable String roomId){

        return service.getRoom(
                roomId
        );
    }


    @PostMapping("/end/{roomId}")
    public DebateRoom endDebate(
            @PathVariable String roomId){

        DebateRoom room =
                service.getRoom(
                        roomId
                );

        if(room==null){

            return null;
        }

        StringBuilder debateText =
                new StringBuilder();

        for(
                Message msg
                : room.getMessages()
        ){

            debateText.append(
                    msg.getSender()
            )

            .append(": ")

            .append(
                    msg.getContent()
            )

            .append("\n");
        }

        String result =

                groqService
                        .judgeHumanDebate(

                                room.getTopic(),

                                debateText.toString()
                        );

        room.setResult(
                result
        );

        if(
                result.toLowerCase()
                .contains(
                        room.getPlayer1()
                                .toLowerCase()
                )
        ){

            room.setWinner(
                    room.getPlayer1()
            );
        }

        else{

            room.setWinner(
                    room.getPlayer2()
            );
        }


        // DATABASE SAVE

        Debate debate =
                new Debate();

        debate.setUsername(

                room.getPlayer1()

                +

                " vs "

                +

                room.getPlayer2()
        );

        debate.setTopic(
                room.getTopic()
        );

        debate.setUserArgument(
                debateText.toString()
        );

        debate.setAiArgument(
                "Human Debate"
        );

        debate.setResult(
                result
        );

        debate.setWinner(
                room.getWinner()
        );

        debateRepository.save(
                debate
        );

        return room;
    }

}