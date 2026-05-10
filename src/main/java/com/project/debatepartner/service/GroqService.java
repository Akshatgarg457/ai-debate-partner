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

    // =========================
    // API URL
    // =========================
    private final String API_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    // =========================
    // REST TEMPLATE
    // =========================
    private final RestTemplate restTemplate =
            new RestTemplate();

    // =========================
    // MAIN API CALL
    // =========================
    public String callGroq(String prompt){

        try{

            // HEADERS
            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON
            );

            headers.setBearerAuth(apiKey);

            // MESSAGE
            Map<String, String> message =
                    new HashMap<>();

            message.put("role", "user");

            message.put("content", prompt);

            // BODY
            Map<String, Object> body =
                    new HashMap<>();

            // ✅ WORKING MODEL
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

            // REQUEST
            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            // RESPONSE
            ResponseEntity<Map> response =
                    restTemplate.exchange(
                            API_URL,
                            HttpMethod.POST,
                            request,
                            Map.class
                    );

            // EXTRACT RESPONSE
            List choices =
                    (List) response.getBody()
                            .get("choices");

            Map choice =
                    (Map) choices.get(0);

            Map msg =
                    (Map) choice.get("message");

            return msg.get("content").toString();
        }

        catch(Exception e){

            e.printStackTrace();

            return "AI judge unavailable right now.";
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
    // AI VS USER ANALYSIS
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

                "Analyze shortly.\n\n" +

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

                "VERY IMPORTANT:\n" +
                "- Use ONLY the names already present in debate\n" +
                "- Never invent fake names\n" +
                "- Never use names like Emma or Ryan\n" +
                "- Use exact usernames from messages\n\n" +

                "Topic:\n" +
                topic + "\n\n" +

                "Debate Messages:\n" +
                fullDebate + "\n\n" +

                "Analyze shortly and professionally.\n\n" +

                "RULES:\n" +
                "- Keep response SHORT\n" +
                "- Max 4-5 lines\n" +
                "- Mention actual winner name\n" +
                "- Give short reason\n" +
                "- Give score using real usernames\n\n" +

                "FORMAT:\n\n" +

                "Winner: username\n" +
                "Reason: short reason\n" +
                "Score: username X/10 | username X/10";

        return callGroq(prompt);
    }
}