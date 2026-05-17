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
                    250
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

                return "";
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

                return "";
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
                    .get(
                            "content"
                    )
                    .toString();

        }

        catch(Exception e){

            e.printStackTrace();

            return "";
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

        String aiSide =

                side.equalsIgnoreCase(
                        "for"
                )

                ?

                "against"

                :

                "for";


        String prompt =

        "You are a student debate opponent.\n\n"+

        "STRICT RULES:\n"+

        "Your side is FIXED as: "

        + aiSide +

        "\n"+

        "Never switch sides.\n"+

        "Never support user's side.\n"+

        "Keep normal replies to 1-3 sentences.\n"+

        "If off-topic: briefly mention it and continue defending your side.\n"+

        "Act like a student trying to win.\n\n"+

        "Topic:\n"

        + topic +

        "\n\nUser Argument:\n"

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

        String prompt =

        "You are a debate judge.\n\n"+

        "Analyze carefully.\n\n"+

        "Topic:\n"

        + topic +

        "\n\nUser Argument:\n"

        + userArg +

        "\n\nAI Argument:\n"

        + aiArg +

        "\n\nReturn EXACTLY this format:\n"+

        "Winner: User or AI\n"+
        "Reason: short explanation\n"+
        "Score: User x/10 , AI x/10";


        String result =

                callGroq(
                        prompt
                );


        if(
                result==null
                ||
                result.trim().isEmpty()
        ){

            return
            "Winner: AI\n"+
            "Reason: Debate analysis unavailable\n"+
            "Score: User 0/10 , AI 0/10";
        }

        return result;
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

        "\n\nReturn EXACTLY:\n"+

        "Winner:\n"+
        "Reason:\n"+
        "Score:";

        return callGroq(
                prompt
        );
    }

}