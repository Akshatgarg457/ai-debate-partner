package com.project.debatepartner.controller;

import com.project.debatepartner.model.Debate;
import com.project.debatepartner.repository.DebateRepository;
import com.project.debatepartner.service.GroqService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/debate")
public class DebateController {

    @Autowired
    private GroqService groqService;

    @Autowired
    private DebateRepository debateRepository;

    // =========================
    // AI REPLY
    // =========================
    @PostMapping("/reply")
    public String reply(@RequestBody Map<String, String> body) {

        return groqService.getDebateResponse(
                body.get("topic"),
                body.get("argument")
        );
    }

    // =========================
    // ANALYZE + SAVE
    // =========================
    @PostMapping("/analyze")
    public Map<String, String> analyze(@RequestBody Map<String, String> body) {

        String topic = body.get("topic");
        String userArg = body.get("userArg");
        String aiArg = body.get("aiArg");
        String username = body.get("username");

        // AI ANALYSIS
        String resultText =
                groqService.analyzeDebate(topic, userArg, aiArg);

        // WINNER
        String winner = "AI";

        if(resultText != null){

            if(resultText.toLowerCase().contains("winner: user")){
                winner = "User";
            }
            else if(resultText.toLowerCase().contains("winner: ai")){
                winner = "AI";
            }
        }

        if(resultText == null || resultText.isEmpty()){
            resultText = "Analysis failed.";
        }

        // =========================
        // SAVE TO DATABASE
        // =========================
        Debate debate = new Debate();

        debate.setUsername(username);
        debate.setTopic(topic);
        debate.setUserArgument(userArg);
        debate.setAiArgument(aiArg);
        debate.setResult(resultText);
        debate.setWinner(winner);

        debateRepository.save(debate);

        // =========================
        // RESPONSE
        // =========================
        Map<String, String> response = new HashMap<>();

        response.put("topic", topic);
        response.put("result", resultText);
        response.put("winner", winner);

        return response;
    }

    // =========================
    // HISTORY
    // =========================
    @GetMapping("/history")
    public List<Debate> getHistory() {
        return debateRepository.findAll();
    }

    // =========================
    // LAST RESULT
    // =========================
    @GetMapping("/last")
    public Debate getLastResult() {

        List<Debate> list = debateRepository.findAll();

        return list.isEmpty()
                ? null
                : list.get(list.size() - 1);
    }
}