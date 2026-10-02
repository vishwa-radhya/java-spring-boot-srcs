package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ErrorResponse;
import com.example.demo.dto.StudentRequest;
import com.example.demo.dto.StudentResponseDTO;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.model.Student;
import com.example.demo.service.JpaStudentService;
import com.example.demo.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(
    name = "Student Management",
    description = "APIs for managing students"
)
@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final JpaStudentService jpaStudentService;
    private final StudentMapper studentMapper;

    public StudentController(StudentService studentService, JpaStudentService jpaStudentService,StudentMapper studentMapper) {
        this.studentService = studentService;
        this.jpaStudentService = jpaStudentService;
        this.studentMapper=studentMapper;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
        // return jpaStudentService.getAllStudents();
    }

    @GetMapping("/count")
    public long countStudents(){
        return jpaStudentService.countStudents();
    }

    @GetMapping("/email/{email}")
    // public String getStudentByEmail(@PathVariable String email) {
    //     return jpaStudentService.getStudentByEmail(email)
    //                 .map(Student::getName)
    //                 .orElse(email);
    // }
    // dto based
    public ResponseEntity<StudentResponseDTO> getStudentByEmail(
        @PathVariable String email
    ){
        Student student =
            jpaStudentService.getStudentByEmail(email);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        // StudentResponseDTO dto =
        //         new StudentResponseDTO(
        //                 s.getId(),
        //                 s.getName(),
        //                 s.getEmail()
        //         );

        // mapstruct mapper
        StudentResponseDTO dto = studentMapper.toDTO(student);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/search/{name}")
    public List<String> searchByName(@PathVariable String name) {
        return jpaStudentService.searchByName(name).stream()
                    .map(Student::getName).toList();
    }

    @GetMapping("/id/{id}")
    public List<String> findStudentsWithIdGreaterThan(@PathVariable int id) {
        return jpaStudentService.findStudentsWithIdGreaterThan(id)
                .stream()
                .map(Student::getName)
                .toList();
    }

    @GetMapping("/search")
    public List<String> searchByNameAndEmail(
            @Parameter(
                description = "Student name to search for",
                required = true
            )
            @RequestParam String name,
            @RequestParam String email) {

        return jpaStudentService
                .searchByNameAndEmail(name, email)
                .stream()
                .map(Student::getName)
                .toList();
    }

    @GetMapping("/exact/{name}")
    public List<String> findStudentsByExactName(@PathVariable String name) {
        return jpaStudentService.findStudentsByExactName(name)
                .stream()
                .map(Student::getName)
                .toList();
    }

    @GetMapping("/positional/{name}")
    public List<String> findByExactNamePositional(@PathVariable String name) {
        return jpaStudentService.findByExactNamePositional(name)
                .stream()
                .map(Student::getName)
                .toList();
    }

    @GetMapping("/native/{email}")
    public List<String> findByEmailNative(@PathVariable String email) {
        return jpaStudentService.findByEmailNative(email)
                .stream()
                .map(Student::getName)
                .toList();
    }

    // @GetMapping("/search/{name}")
    // public List<Student> searchByName(@PathVariable String name) {
    //     return jpaStudentService.searchByName(name);
    // }

    // @GetMapping("/email/{email}")
    // public Optional<Student> getStudentByEmail(@PathVariable String email) {
    //     return jpaStudentService.getStudentByEmail(email);
    // }
    @Operation(
        summary = "Get student by ID",
        description = "Retrieves a student using their databse ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Student found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Student.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Student not found"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
        @Parameter(
            description = "The unique ID of the student",
            required = true
        )
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

    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Student created successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation failed",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @PostMapping
    public ResponseEntity<Student> createStudent(
        @Valid  @RequestBody StudentRequest studentRequest) {

        // Student student = new Student();
        // student.setName(studentRequest.getName());
        // student.setEmail(studentRequest.getEmail());
        // Student createdStudent = studentService.createStudent(student);
        // dto mapper
        Student student = studentMapper.toEntity(studentRequest);
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity
            .status(201)
            .body(createdStudent);
    }

    // dto with jpaService
    @PostMapping("/db")
    public ResponseEntity<StudentResponseDTO> createStudentInDb(
            @Valid @RequestBody StudentRequest studentRequest) {

        Student student = studentMapper.toEntity(studentRequest);

        Student createdStudent = jpaStudentService.saveStudent(student);

        StudentResponseDTO response =
                studentMapper.toDTO(createdStudent);

        return ResponseEntity
                .status(201)
                .body(response);
    }

    @PutMapping("/db/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudentInDb(
            @PathVariable int id,
            @Valid @RequestBody StudentRequest studentRequest) {

        Student student = studentMapper.toEntity(studentRequest);

        Student updatedStudent =
                jpaStudentService.updateStudent(id, student);

        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }

        StudentResponseDTO response =
                studentMapper.toDTO(updatedStudent);

        return ResponseEntity.ok(response);
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

    @DeleteMapping("/db/{id}")
    public ResponseEntity<Void> deleteStudentFromDb(
            @PathVariable int id) {

        String deletedEmail = jpaStudentService.deleteStudent(id);

        if (deletedEmail == null) {
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
