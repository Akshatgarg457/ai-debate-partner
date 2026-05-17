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

    // ======================
    // MAIN API CALL
    // ======================

    public String callGroq(
            String prompt
    ){

        try{

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

            body.put(
                    "temperature",
                    0.7
            );

            body.put(
                    "max_tokens",
                    140
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

            List choices =

                    (List)

                    response.getBody()
                            .get(
                                    "choices"
                            );

            Map choice =

                    (Map)
                    choices.get(0);

            Map msg =

                    (Map)
                    choice.get(
                            "message"
                    );

            return msg
                    .get(
                            "content"
                    )
                    .toString();
        }

        catch(Exception e){

            e.printStackTrace();

            return "AI service error";
        }
    }

    // ======================
    // AI DEBATE RESPONSE
    // ======================

    public String getDebateResponse(

            String topic,

            String userArgument,

            String side
    ){

        // AI TAKES OPPOSITE SIDE

        String aiSide =

                side.equalsIgnoreCase(
                        "for"
                )

                ?

                "against"

                :

                "for";


        String prompt =

        "You are participating in a real student debate.\n\n"+

        "STRICT RULES:\n"+

        "1. Your debate side is FIXED as: " + aiSide + "\n"+

        "2. Never switch sides.\n"+

        "3. Never support the user's side.\n"+

        "4. Speak like a student trying to win.\n"+

        "5. Never act friendly.\n"+

        "6. Never greet.\n"+

        "7. Never write essays.\n"+

        "8. Use simple natural language.\n"+

        "9. Normal replies should be 1-3 sentences.\n"+

        "10. If the user's message is unrelated to the topic:\n"+

        "- Say briefly that it is off-topic.\n"+

        "- Then continue defending your side strongly.\n"+

        "- Give 3-5 logical counterpoints.\n"+

        "- Make off-topic responses longer than normal.\n"+

        "11. Stay focused on the debate topic.\n"+

        "12. Sound like a real human opponent.\n\n"+

        "Debate Topic:\n"

        + topic +

        "\n\nUser Argument:\n"

        + userArgument +

        "\n\nDebate Response:";

        return callGroq(
                prompt
        );
    }

    // ======================
    // AI JUDGE
    // ======================

    public String analyzeDebate(

            String topic,

            String userArg,

            String aiArg
    ){

        String prompt =

                "You are a debate judge.\n\n"+

                "Topic:\n"

                + topic +

                "\n\nUser Argument:\n"

                + userArg +

                "\n\nAI Argument:\n"

                + aiArg +

                "\n\nGive result in this format:\n"+

                "Winner:\n"+
                "Reason:\n"+
                "Score:";

        return callGroq(
                prompt
        );
    }

    // ======================
    // HUMAN DEBATE JUDGE
    // ======================

    public String judgeHumanDebate(

            String topic,

            String debateText
    ){

        String prompt =

                "You are a debate judge.\n\n"+

                "Topic:\n"

                + topic +

                "\n\nDebate:\n"

                + debateText +

                "\n\nGive result in this format:\n"+

                "Winner:\n"+
                "Reason:\n"+
                "Score:";

        return callGroq(
                prompt
        );
    }
}