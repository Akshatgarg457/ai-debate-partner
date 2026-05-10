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
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/forget")
    public String forgetPage() {
        return "forget";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/topic")
    public String topicPage() {
        return "topic";
    }

    @GetMapping("/debate")
    public String debate() {
        return "debate";
    }

    @GetMapping("/result")
    public String resultPage() {
        return "result";
    }

    @GetMapping("/history")
    public String history() {
        return "history";
    }
    
      @GetMapping("/create-room")
    public String createRoomPage() {
        return "create-room";
    }

    @GetMapping("/join-room")
    public String joinRoomPage() {
        return "join-room";
    }

    @GetMapping("/human-debate")
    public String debatePage() {
        return "human-debate";
    }
}