package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Student;

@RestController 
public class TestController {
    
    @GetMapping("/hell")
    public ResponseEntity<String> hell(){
        return ResponseEntity.ok("Hell no");
    }
    // public String hell(){
    //     return "Hello from REST API";
    // }
    @GetMapping("/not-found")
    public ResponseEntity<String> notFound() {
        return ResponseEntity
            .status(404)
            .body("Student not found");
    }

    // @GetMapping("/students/{id}")
    // public String getStudent(@PathVariable int id) {
    //     return "Student ID: " + id;
    // }

    @GetMapping("/search")
    public String searchStudent(@RequestParam String name) {
        return "Searching for: " + name;
    }

    // @PostMapping("/students")
    // public String createStudent(@RequestBody Student student) {
    //     return "Created student: " + student.getName();
    // }

    // @PutMapping("/students/{id}")
    // public ResponseEntity<String> updateStudent(
    //     @PathVariable int id,
    //     @RequestBody Student student) {

    //      return ResponseEntity.ok("Updated student: " + id);
    // }

    // @DeleteMapping("/students/{id}")
    // public ResponseEntity<Void> deleteStudent(
    //     @PathVariable int id) {
    //     return ResponseEntity.noContent().build();
    // }
}
