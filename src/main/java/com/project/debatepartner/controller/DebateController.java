package com.project.debatepartner.controller;

import com.project.debatepartner.model.Debate;
import com.project.debatepartner.repository.DebateRepository;
import com.project.debatepartner.service.GroqService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/debate")
@CrossOrigin("*")
public class DebateController {

    @Autowired
    private GroqService groqService;

    @Autowired
    private DebateRepository debateRepository;


    // =========================
    // AI REPLY
    // =========================

    @PostMapping("/reply")
    public String reply(
            @RequestBody Map<String,String> body
    ){

        return groqService.getDebateResponse(

                body.get("topic"),
                body.get("argument"),
                body.get("side")
        );
    }



    // =========================
    // ANALYZE + SAVE
    // =========================

    @PostMapping("/analyze")
    public Map<String,String> analyze(

            @RequestBody
            Map<String,String> body
    ){

        String topic=
                body.get("topic");

        String userArg=
                body.get("userArg");

        String aiArg=
                body.get("aiArg");

        String username=
                body.getOrDefault(
                        "username",
                        "guest"
                );

        String result=

                groqService.analyzeDebate(

                        topic,
                        userArg,
                        aiArg
                );

        String winner="AI";

        if(

                result!=null

                &&

                result.toLowerCase()
                .contains(
                        "winner: user"
                )
        ){

            winner="User";
        }


        Debate debate=
                new Debate();

        debate.setUsername(
                username
        );

        debate.setTopic(
                topic
        );

        debate.setUserArgument(
                userArg
        );

        debate.setAiArgument(
                aiArg
        );

        debate.setResult(
                result
        );

        debate.setWinner(
                winner
        );


        Debate saved=

                debateRepository.save(
                        debate
                );

        System.out.println(

                "Saved ID: "

                +

                saved.getId()
        );


        Map<String,String>
                response=
                new HashMap<>();


        response.put(
                "topic",
                topic
        );

        response.put(
                "result",
                result
        );

        response.put(
                "winner",
                winner
        );


        return response;
    }



    // =========================
    // HISTORY
    // =========================

    @GetMapping("/history")
    public List<Debate>
    getHistory(){

        return debateRepository
                .findAll();
    }



    // =========================
    // LAST RESULT
    // =========================

    @GetMapping("/last")
    public Debate getLastResult(){

        List<Debate>
        debates=

        debateRepository.findAll();

        if(
                debates.isEmpty()
        ){

            return null;
        }

        return debates.get(
                debates.size()-1
        );
    }

}