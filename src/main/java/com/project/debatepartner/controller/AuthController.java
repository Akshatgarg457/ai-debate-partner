package com.project.debatepartner.controller;

import com.project.debatepartner.model.User;
import com.project.debatepartner.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {

        User user = userRepository
                .findByUsername(username)
                .orElse(null);

        // USER NOT FOUND
        if(user == null){
            return "redirect:/login?error=user";
        }

        // WRONG PASSWORD
        if(!user.getPassword().equals(password)){
            return "redirect:/login?error=password";
        }

        // SUCCESS
        return "redirect:/dashboard";
    }

    // =========================
    // SIGNUP
    // =========================
    @PostMapping("/signup")
    public String signup(@RequestParam String fullName,
                         @RequestParam String email,
                         @RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String confirmPassword,
                         @RequestParam("securityQ") String securityQuestion,
                         @RequestParam String answer) {

        // PASSWORD CHECK
        if (!password.equals(confirmPassword)) {
            return "redirect:/signup?error=password";
        }

        // USERNAME EXISTS
        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/signup?error=username";
        }

        // EMAIL EXISTS
        if (userRepository.findByEmail(email).isPresent()) {
            return "redirect:/signup?error=email";
        }

        // CREATE USER
        User user = new User();

        user.setFullName(fullName);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setSecurityQuestion(securityQuestion);
        user.setAnswer(answer);

        userRepository.save(user);

        // SUCCESS
        return "redirect:/login?success=signup";
    }

    // =========================
    // GET SECURITY QUESTION
    // =========================
    @GetMapping("/get-question")
    @ResponseBody
    public String getQuestion(@RequestParam String username) {

        User user = userRepository
                .findByUsername(username)
                .orElse(null);

        if (user == null) {
            return "NOT_FOUND";
        }

        return user.getSecurityQuestion();
    }

    // =========================
    // VERIFY ANSWER
    // =========================
    @PostMapping("/verify-answer")
    @ResponseBody
    public String verifyAnswer(
            @RequestBody Map<String, String> body) {

        String username = body.get("username");
        String question = body.get("question");
        String answer = body.get("answer");

        User user = userRepository
                .findByUsername(username)
                .orElse(null);

        if (user == null) {
            return "INVALID";
        }

        if (user.getSecurityQuestion().equals(question)
                && user.getAnswer().equalsIgnoreCase(answer)) {

            return "VALID";
        }

        return "INVALID";
    }

    // =========================
    // RESET PASSWORD
    // =========================
    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String username,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword) {

        // PASSWORD MATCH CHECK
        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/forget?error=password";
        }

        User user = userRepository
                .findByUsername(username)
                .orElse(null);

        // USER NOT FOUND
        if (user == null) {
            return "redirect:/forget?error=user";
        }

        // UPDATE PASSWORD
        user.setPassword(newPassword);

        userRepository.save(user);

        // SUCCESS
        return "redirect:/login?success=reset";
    }
}