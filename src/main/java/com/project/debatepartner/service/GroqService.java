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

            headers.setBearerAuth(
                    apiKey
            );

            Map<String,String> message =
                    new HashMap<>();

            message.put(
                    "role",
                    "user"
            );

            message.put(
                    "content",
                    prompt
            );

            Map<String,Object> body =
                    new HashMap<>();

            body.put(
                    "model",
                    "llama-3.3-70b-versatile"
            );

            body.put(
                    "messages",
                    Collections.singletonList(
                            message
                    )
            );

            // Slight creativity
            body.put(
                    "temperature",
                    0.7
            );

            // Limit huge outputs
            body.put(
                    "max_tokens",
                    120
            );

            HttpEntity<Map<String,Object>>
                    request =

                    new HttpEntity<>(
                            body,
                            headers
                    );

            ResponseEntity<Map> response =

                    restTemplate.exchange(

                            API_URL,
                            HttpMethod.POST,
                            request,
                            Map.class
                    );

            if(response.getBody()==null){

                return "Empty AI response.";
            }

            List choices =

                    (List)
                            response.getBody()
                                    .get(
                                            "choices"
                                    );

            if(
                    choices==null
                            ||
                            choices.isEmpty()
            ){

                return "No AI response.";
            }

            Map choice =
                    (Map)
                            choices.get(0);

            Map msg =
                    (Map)
                            choice.get(
                                    "message"
                            );

            return msg
                    .get("content")
                    .toString();
        }

        catch(Exception e){

            e.printStackTrace();

            return "AI service error.";
        }
    }

    // =========================
    // HUMAN-LIKE AI DEBATE
    // =========================

    public String getDebateResponse(
            String topic,
            String userArgument
    ){

        String prompt =

                "You are a human debate opponent.\n\n"+

                "Rules:\n"+

                "- Keep response short.\n"+

                "- Maximum 2 to 4 sentences only.\n"+

                "- Speak naturally like a student.\n"+

                "- Do not write essays.\n"+

                "- Give logical counter arguments.\n"+

                "- Do not say 'Greetings', 'I respectfully disagree', or formal words.\n"+

                "- Sound like a real person arguing.\n"+

                "- Avoid repeating the user's argument.\n\n"+

                "Topic: "
                + topic +

                "\n\nOpponent argument:\n"

                + userArgument +

                "\n\nYour response:";

        return callGroq(prompt);
    }

    // =========================
    // AI JUDGE
    // =========================

    public String analyzeDebate(

            String topic,
            String userArg,
            String aiArg
    ){

        String prompt =

                "You are a debate judge.\n\n"+

                "Topic:\n"

                + topic +

                "\n\nUser argument:\n"

                + userArg +

                "\n\nAI argument:\n"

                + aiArg +

                "\n\nGive:\n"+

                "Winner:\n"+
                "Reason:\n"+
                "Score:";
        
        return callGroq(
                prompt
        );
    }

    // =========================
    // HUMAN VS HUMAN JUDGE
    // =========================

    public String judgeHumanDebate(

            String topic,
            String fullDebate
    ){

        String prompt =

                "You are a debate judge.\n\n"+

                "Topic:\n"

                + topic +

                "\n\nDebate:\n"

                + fullDebate +

                "\n\nGive:\n"+

                "Winner:\n"+
                "Reason:\n"+
                "Score:";

        return callGroq(
                prompt
        );
    }

}