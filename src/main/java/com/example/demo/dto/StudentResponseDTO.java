package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class StudentResponseDTO {
    @Schema(description = "Unique identifier of the student")
    private int id;
    private String name;
    private String email;

    public StudentResponseDTO() {
    }

    public StudentResponseDTO(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
