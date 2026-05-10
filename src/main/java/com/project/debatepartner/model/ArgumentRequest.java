package com.project.debatepartner.model;

public class ArgumentRequest {

    private String topic;
    private String argument;

    // GET TOPIC
    public String getTopic() {
        return topic;
    }

    // SET TOPIC
    public void setTopic(String topic) {
        this.topic = topic;
    }

    // GET ARGUMENT
    public String getArgument() {
        return argument;
    }

    // SET ARGUMENT
    public void setArgument(String argument) {
        this.argument = argument;
    }
}