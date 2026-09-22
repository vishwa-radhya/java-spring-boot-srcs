package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StudentRequest {

    // @NotBlank 
    @NotBlank(message = "Name must not be blank") 
    @Size(min=2,max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Email(message = "Email must be valid")
    @NotBlank 
    private String email;


    public StudentRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
