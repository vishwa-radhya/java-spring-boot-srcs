package com.example.demo;

import java.util.List;

// import jakarta.persistence.EntityManager;
// import jakarta.persistence.PersistenceContext;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
// import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Student;
import com.example.demo.repository.DepartmentRepository;
// import com.example.demo.model.Student;
import com.example.demo.service.JpaStudentService;

@Component 
public class JpaExperiment implements CommandLineRunner {
    
    private final JpaStudentService studentService;
    private final DepartmentRepository departmentRepository;
    // public  JpaExperiment(JpaStudentService studentService){
    //     this.studentService=studentService;
    // }
    public  JpaExperiment(JpaStudentService studentService,
        DepartmentRepository departmentRepository
    ){
        this.studentService=studentService;
        this.departmentRepository = departmentRepository;
    }
    

    @Override
    public void run(String... args){
        // studentService.createStudent();
        // studentService.testDetached();
        // studentService.testRemoved();


        // studentService.testCascade();
        // studentService.testFetch();


        // List<Student> students = studentService.testJPQL("Bob");
        // students.forEach(student -> 
        //     System.out.println(student.getName()+" "+student.getEmail())
        // );
        // List<Student> students = studentService.testJPQLJoin();
        // students.forEach(student -> 
        //     System.out.println(student.getName()+" -> "+student.getDepartment().getName())
        // );

        List<Object[]> results = studentService.testJPQLGroupBy();
        for (Object[] result : results) {
            System.out.println(
                    result[0] + " -> " + result[1]
            );
        }

        // studentService.addTestStudents();
        
    }
}
