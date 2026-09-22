package com.example.demo.dto;

import java.util.List;

public class ErrorResponse {
    private int status;
    private List<String> messages;

    public ErrorResponse(int status, List<String> messages) {
        this.status = status;
        this.messages = messages;
    }

    public int getStatus() {
        return status;
    }

    public List<String> getMessages() {
        return messages;
    }
}
