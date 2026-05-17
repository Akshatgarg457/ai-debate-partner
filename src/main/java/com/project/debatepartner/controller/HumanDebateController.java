package com.project.debatepartner.controller;

import com.project.debatepartner.model.Debate;
import com.project.debatepartner.model.DebateRoom;
import com.project.debatepartner.model.Message;
import com.project.debatepartner.repository.DebateRepository;
import com.project.debatepartner.service.DebateRoomService;
import com.project.debatepartner.service.GroqService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    // ======================
    // CREATE ROOM
    // ======================

    @PostMapping("/create")
    public DebateRoom createRoom(

            @RequestParam String topic,

            @RequestParam String username
    ){

        return service.createRoom(
                topic,
                username
        );
    }


    // ======================
    // JOIN ROOM
    // ======================

    @PostMapping("/join")
    public DebateRoom joinRoom(

            @RequestParam String roomId,

            @RequestParam String username
    ){

        return service.joinRoom(
                roomId,
                username
        );
    }


    // ======================
    // GET ROOM
    // ======================

    @GetMapping("/room/{roomId}")
    public DebateRoom getRoom(

            @PathVariable String roomId
    ){

        return service.getRoom(
                roomId
        );
    }


    // ======================
    // END DEBATE + RESULT
    // ======================

    @PostMapping("/end/{roomId}")
    public DebateRoom endDebate(

            @PathVariable
            String roomId
    ){

        DebateRoom room =

                service.getRoom(
                        roomId
                );

        if(room==null){

            return null;
        }


        // combine debate messages

        StringBuilder debateText =
                new StringBuilder();


        for(
                Message msg :
                room.getMessages()
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


        // judge debate

        String result =

                groqService
                .judgeHumanDebate(

                        room.getTopic(),

                        debateText.toString()
                );


        // fallback result

        if(
                result==null
                ||
                result.trim().isEmpty()
        ){

            result=

            "Winner: Draw\n"+

            "Reason: Unable to analyze debate\n"+

            "Score: Player1 0/10, Player2 0/10";
        }


        room.setResult(
                result
        );


        // detect winner

        String winner="Draw";

        String lower=

                result.toLowerCase();


        if(

                lower.contains(

                        room.getPlayer1()
                        .toLowerCase()

                )

        ){

            winner=
                    room.getPlayer1();
        }

        else if(

                lower.contains(

                        room.getPlayer2()
                        .toLowerCase()

                )

        ){

            winner=
                    room.getPlayer2();
        }


        room.setWinner(
                winner
        );


        // ======================
        // SAVE DATABASE HISTORY
        // ======================

        Debate debate=
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
                winner
        );

        debateRepository.save(
                debate
        );


        return room;
    }

}