package com.project.debatepartner.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GroqService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final String API_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private final RestTemplate restTemplate =
            new RestTemplate();

    // =========================
    // MAIN API CALL
    // =========================
    public String callGroq(String prompt){

        try{

            if(apiKey == null || apiKey.isEmpty()){
                return "Groq API key missing.";
            }

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON
            );

            headers.setBearerAuth(apiKey);

            Map<String, String> message =
                    new HashMap<>();

            message.put("role", "user");
            message.put("content", prompt);

            Map<String, Object> body =
                    new HashMap<>();

            body.put(
                    "model",
                    "llama-3.3-70b-versatile"
            );

            body.put(
                    "messages",
                    Collections.singletonList(message)
            );

            body.put(
                    "temperature",
                    0.5
            );

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map> response =
                    restTemplate.exchange(
                            API_URL,
                            HttpMethod.POST,
                            request,
                            Map.class
                    );

            if(response.getBody() == null){
                return "Empty AI response.";
            }

            List choices =
                    (List) response.getBody()
                            .get("choices");

            if(choices == null || choices.isEmpty()){
                return "No AI response.";
            }

            Map choice =
                    (Map) choices.get(0);

            Map msg =
                    (Map) choice.get("message");

            return msg.get("content").toString();
        }

        catch(Exception e){

            e.printStackTrace();

            return "AI service error.";
        }
    }

    // =========================
    // AI DEBATE RESPONSE
    // =========================
    public String getDebateResponse(String topic,
                                    String userArgument){

        String prompt =

                "You are an intelligent debate opponent.\n\n" +

                "Topic: " + topic + "\n\n" +

                "User Argument:\n" +
                userArgument + "\n\n" +

                "Reply professionally with a strong counter argument.";

        return callGroq(prompt);
    }

    // =========================
    // ANALYZE DEBATE
    // =========================
    public String analyzeDebate(String topic,
                                String userArg,
                                String aiArg){

        String prompt =

                "You are an AI debate judge.\n\n" +

                "Topic: " + topic + "\n\n" +

                "User Argument:\n" +
                userArg + "\n\n" +

                "AI Argument:\n" +
                aiArg + "\n\n" +

                "Give:\n" +
                "1. Winner\n" +
                "2. Reason\n" +
                "3. Score";

        return callGroq(prompt);
    }

    // =========================
    // HUMAN VS HUMAN JUDGE
    // =========================
    public String judgeHumanDebate(String topic,
                                   String fullDebate){

        String prompt =

                "You are an AI debate judge.\n\n" +

                "Topic:\n" +
                topic + "\n\n" +

                "Debate Messages:\n" +
                fullDebate + "\n\n" +

                "Give:\n" +
                "1. Winner\n" +
                "2. Reason\n" +
                "3. Score";

        return callGroq(prompt);
    }
}