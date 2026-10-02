package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

@Service 
public class TransactionHelperService {
    private final StudentRepository studentRepository;

    public TransactionHelperService(StudentRepository studentRepository){
        this.studentRepository=studentRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void methodB(){
        Student student = new Student();
        student.setName("Inner New Transaction");
        student.setEmail("innernew@gmail.com");
        studentRepository.save(student);
    }
}
