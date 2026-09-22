package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.exception.DuplicateStudentException;
import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.model.Student;

@Service
public class StudentService {

    private final List<Student> students = new ArrayList<>();

    public StudentService() {
        students.add(new Student(1, "Vishwa", "vishwa@example.com"));
        students.add(new Student(2, "Rahul", "rahul@example.com"));
        students.add(new Student(3, "Anil", "anil@example.com"));
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public Student getStudentById(int id) {
        // for (Student student : students) {
        //     if (student.getId() == id) {
        //         return student;
        //     }
        // }
        // return null;

        return students.stream()
                        .filter(student -> student.getId() == id)
                        .findFirst()
                        .orElseThrow(()-> new StudentNotFoundException("Student with id "+id+" not found"));
    }

    public Student createStudent(Student student) {
        // students.add(student);
        // return student;
        boolean emailExists = students.stream()
                                .anyMatch(existingStudent->
                                    existingStudent.getEmail().equalsIgnoreCase(student.getEmail())
                                );
        if(emailExists){
            throw new DuplicateStudentException(
                "Student with email "+student.getEmail() + " already exists"
            );
        }
        students.add(student);
        return student;
    }

    public Student updateStudent(int id, Student updatedStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == id) {
                students.set(i, updatedStudent);
                return updatedStudent;
            }
        }
        return null;
    }

    public boolean deleteStudent(int id) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == id) {
                students.remove(i);
                return true;
            }
        }
        return false;
    }

    public void sayHello() {
        System.out.println("Hello from StudentService");
    }
}
