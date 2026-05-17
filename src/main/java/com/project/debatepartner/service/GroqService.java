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
    // MAIN API
    // ======================

    public String callGroq(String prompt){

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
                    100
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
            String userArgument
    ){

        String prompt =

                "You are a human debate opponent.\n\n"+

                "Rules:\n"+

                "- Keep replies short (2-4 sentences only).\n"+

                "- Speak naturally like a student.\n"+

                "- No essays.\n"+

                "- No formal language.\n"+

                "- Give logical counterarguments.\n"+

                "- Sound like a real person.\n"+

                "- Avoid greetings.\n"+

                "- If user says something unrelated, briefly mention it is off-topic and naturally return to the debate.\n"+

                "- Do not completely ignore what the user said.\n\n"+

                "Debate topic:\n"

                + topic +

                "\n\nUser argument:\n"

                + userArgument +

                "\n\nReply:";

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

    // ======================
    // HUMAN JUDGE
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

                "\n\nGive:\n"+

                "Winner:\n"+
                "Reason:\n"+
                "Score:";

        return callGroq(
                prompt
        );
    }

}