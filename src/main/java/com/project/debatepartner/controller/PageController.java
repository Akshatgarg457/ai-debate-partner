package com.project.debatepartner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/topic")
    public String topic() {
        return "topic";
    }

    @GetMapping("/debate")
    public String debate() {
        return "debate";
    }

    @GetMapping("/create-room")
    public String createRoom() {
        return "create-room";
    }

    @GetMapping("/join-room")
    public String joinRoom() {
        return "join-room";
    }

    @GetMapping("/human-debate")
    public String humanDebate() {
        return "human-debate";
    }

    @GetMapping("/forget")
    public String forget() {
        return "forget";
    }

    @GetMapping("/result")
    public String result() {
        return "result";
    }

    @GetMapping("/history")
    public String history() {
        return "history";
    }
}