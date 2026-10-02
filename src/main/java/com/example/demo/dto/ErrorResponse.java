package com.example.demo.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class ErrorResponse {
    @Schema(description = "HTTP status code of the error")
    private int status;

    @Schema(description = "Validation or error messages")
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
