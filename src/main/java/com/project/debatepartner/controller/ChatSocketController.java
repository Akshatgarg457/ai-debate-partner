package com.project.debatepartner.controller;

import com.project.debatepartner.model.Message;
import com.project.debatepartner.model.SocketMessage;
import com.project.debatepartner.service.DebateRoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatSocketController {

    @Autowired
    private DebateRoomService debateRoomService;


    // =========================
    // SEND CHAT
    // =========================

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public SocketMessage sendMessage(
            SocketMessage message
    ){

        Message msg =
                new Message();

        msg.setSender(
                message.getSender()
        );

        msg.setContent(
                message.getContent()
        );


        // FIXED HERE
        debateRoomService.addMessage(

                message.getRoomId(),

                msg
        );

        return message;
    }



    // =========================
    // USER JOIN
    // =========================

    @MessageMapping("/join")
    @SendTo("/topic/status")
    public SocketMessage join(

            SocketMessage message
    ){

        message.setType(
                "JOIN"
        );

        return message;
    }



    // =========================
    // END DEBATE
    // =========================

    @MessageMapping("/end")
    @SendTo("/topic/end")
    public SocketMessage endDebate(

            SocketMessage message
    ){

        message.setType(
                "END"
        );

        return message;
    }

}