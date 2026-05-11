package com.project.debatepartner.controller;

import com.project.debatepartner.model.User;
import com.project.debatepartner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // ================= SIGNUP =================

    @PostMapping("/signup")
    public Map<String, Object> signup(
            @RequestBody User user) {

        Map<String, Object> response =
                new HashMap<>();

        if(userRepository.findByUsername(
                user.getUsername()) != null){

            response.put("success", false);
            response.put("error",
                    "Username already exists");

            return response;
        }

        userRepository.save(user);

        response.put("success", true);

        return response;
    }

    // ================= LOGIN =================

    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestBody Map<String,String> data){

        Map<String, Object> response =
                new HashMap<>();

        String username =
                data.get("username");

        String password =
                data.get("password");

        User user =
                userRepository.findByUsername(
                        username);

        if(user == null){

            response.put("success", false);
            response.put("error",
                    "User not found");

            return response;
        }

        if(!user.getPassword()
                .equals(password)){

            response.put("success", false);
            response.put("error",
                    "Wrong password");

            return response;
        }

        response.put("success", true);

        return response;
    }

    // ================= GET QUESTION =================

    @GetMapping("/question")
    public Map<String,Object> getQuestion(
            @RequestParam String username){

        Map<String,Object> response =
                new HashMap<>();

        User user =
                userRepository.findByUsername(
                        username);

        if(user == null){

            response.put("success", false);
            response.put("error",
                    "User not found");

            return response;
        }

        response.put("success", true);

        response.put(
                "question",
                user.getSecurityQuestion()
        );

        return response;
    }

    // ================= VERIFY ANSWER =================

    @PostMapping("/verify")
    public Map<String,Object> verifyAnswer(
            @RequestBody Map<String,String> data){

        Map<String,Object> response =
                new HashMap<>();

        User user =
                userRepository.findByUsername(
                        data.get("username"));

        if(user == null){

            response.put("success", false);
            response.put("error",
                    "User not found");

            return response;
        }

        if(!user.getSecurityAnswer()
                .equalsIgnoreCase(
                        data.get("answer")
                )){

            response.put("success", false);
            response.put("error",
                    "Wrong answer");

            return response;
        }

        response.put("success", true);

        return response;
    }

    // ================= RESET PASSWORD =================

    @PostMapping("/reset")
    public Map<String,Object> resetPassword(
            @RequestBody Map<String,String> data){

        Map<String,Object> response =
                new HashMap<>();

        User user =
                userRepository.findByUsername(
                        data.get("username"));

        if(user == null){

            response.put("success", false);
            response.put("error",
                    "User not found");

            return response;
        }

        user.setPassword(
                data.get("password")
        );

        userRepository.save(user);

        response.put("success", true);

        return response;
    }
}