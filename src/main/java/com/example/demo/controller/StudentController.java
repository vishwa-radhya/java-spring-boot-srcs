package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ErrorResponse;
import com.example.demo.dto.StudentRequest;
import com.example.demo.model.Student;
import com.example.demo.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
        @PathVariable int id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok(student);
    }

    // @PostMapping
    // public ResponseEntity<Student> createStudent(
    //     @RequestBody Student student) {
    //     Student createdStudent = studentService.createStudent(student);
    //     return ResponseEntity
    //         .status(201)
    //         .body(createdStudent);
    // }

    @PostMapping
    public ResponseEntity<Student> createStudent(
        @Valid  @RequestBody StudentRequest studentRequest) {

        Student student = new Student();
        student.setName(studentRequest.getName());
        student.setEmail(studentRequest.getEmail());
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity
            .status(201)
            .body(createdStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable int id,
            @RequestBody Student student) {

        Student updatedStudent =
                studentService.updateStudent(id, student);

        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable int id) {

        boolean deleted = studentService.deleteStudent(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }


    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public  ResponseEntity<ErrorResponse> handeValidationException(MethodArgumentNotValidException ex){

    //     // String message = ex.getBindingResult()
    //     //                     .getFieldErrors()
    //     //                     .get(0)
    //     //                     .getDefaultMessage();

    //     List<String> errors = ex.getBindingResult()
    //                             .getFieldErrors()
    //                             .stream()
    //                             .map(error->error.getDefaultMessage())
    //                             .toList();
        
    //     ErrorResponse response = new ErrorResponse(400, errors);

    //     return ResponseEntity.badRequest().body(response);
    // }
}
