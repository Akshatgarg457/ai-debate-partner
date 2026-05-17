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
    // API CALL
    // ======================

    public String callGroq(
            String prompt
    ){

        try{

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON);

            headers.setBearerAuth(
                    apiKey);

            Map<String,String>
            message =
                    new HashMap<>();

            message.put(
                    "role",
                    "user"
            );

            message.put(
                    "content",
                    prompt
            );

            Map<String,Object>
            body=
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
                    80
            );

            HttpEntity<Map<String,Object>>
                    request =

                    new HttpEntity<>(
                            body,
                            headers
                    );

            ResponseEntity<Map>
                    response =

                    restTemplate.exchange(

                            API_URL,

                            HttpMethod.POST,

                            request,

                            Map.class
                    );

            List choices=

                    (List)

                    response
                    .getBody()
                    .get(
                            "choices"
                    );

            Map choice=

                    (Map)
                    choices.get(0);

            Map msg=

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

            return "AI error";
        }
    }

    // ======================
    // AI DEBATE
    // ======================

    public String getDebateResponse(

            String topic,

            String userArgument,

            String side
    ){

        String aiSide=

                side.equals(
                        "for"
                )

                ?

                "against"

                :

                "for";

        String prompt=

        "You are a human debate opponent.\n"+

        "Your fixed side is: "

        + aiSide +

        "\n"+

        "Never switch sides.\n"+

        "Never support user's side.\n"+

        "Reply only in 1-3 short sentences.\n"+

        "Do not write essays.\n"+

        "Never be friendly.\n"+

        "Act like a student trying to win.\n"+

        "If argument is unrelated, say it is off-topic and return to debate.\n\n"+

        "Topic:\n"

        + topic +

        "\n\nUser argument:\n"

        + userArgument +

        "\n\nResponse:";

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

        String prompt=

                "Topic:\n"

                + topic +

                "\n\nUser:\n"

                + userArg +

                "\n\nAI:\n"

                + aiArg +

                "\n\nWinner:\nReason:\nScore:";

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

        String prompt=

                "Topic:\n"

                + topic +

                "\n\nDebate:\n"

                + debateText +

                "\n\nWinner:\nReason:\nScore:";

        return callGroq(
                prompt
        );
    }
}