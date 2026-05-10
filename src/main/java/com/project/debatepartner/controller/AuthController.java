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

        User user = userRepository.findByUsername(username);

        if(user != null && user.getPassword().equals(password)) {
            return "redirect:/dashboard";
        }

        return "redirect:/login?error=true";
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

        if(!password.equals(confirmPassword)){
            return "redirect:/signup?error=password";
        }

        if(userRepository.findByUsername(username) != null){
            return "redirect:/signup?error=username";
        }

        if(userRepository.findByEmail(email) != null){
            return "redirect:/signup?error=email";
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setSecurityQuestion(securityQuestion);
        user.setAnswer(answer);

        userRepository.save(user);

        return "redirect:/login?signup=success";
    }

    // =========================
    // GET SECURITY QUESTION
    // =========================
    @GetMapping("/get-question")
    @ResponseBody
    public String getQuestion(@RequestParam String username){

        User user = userRepository.findByUsername(username);

        if(user == null){
            return "NOT_FOUND";
        }

        if(user.getSecurityQuestion() == null){
            return "NO_SECURITY";
        }

        return user.getSecurityQuestion();
    }

    // =========================
    // VERIFY ANSWER
    // =========================
    @PostMapping("/verify-answer")
    @ResponseBody
    public String verifyAnswer(@RequestBody Map<String, String> body){

        String username = body.get("username");
        String question = body.get("question");
        String answer = body.get("answer");

        User user = userRepository.findByUsername(username);

        if(user == null){
            return "INVALID";
        }

        if(user.getSecurityQuestion() == null || user.getAnswer() == null){
            return "NO_SECURITY";
        }

        if(user.getSecurityQuestion().equals(question)
                && user.getAnswer().equalsIgnoreCase(answer)){
            return "VALID";
        }

        return "INVALID";
    }

    // =========================
    // RESET PASSWORD
    // =========================
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String username,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword){

        if(!newPassword.equals(confirmPassword)){
            return "redirect:/forget?error=password";
        }

        User user = userRepository.findByUsername(username);

        if(user == null){
            return "redirect:/forget?error=user";
        }

        user.setPassword(newPassword);
        userRepository.save(user);

        return "redirect:/login?reset=true";
    }
}