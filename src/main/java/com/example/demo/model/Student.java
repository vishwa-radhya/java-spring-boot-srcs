package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private int id;

    @Column(name = "student_name")
    private String name;

    @Column(name = "student_email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

    @OneToOne(fetch = FetchType.LAZY)
    private StudentProfile profile;

    @ManyToMany 
    private List<Course> courses = new ArrayList<>();

    public Student() {
    }

    public Student(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    public StudentProfile getProfile() {
        return profile;
    }

    public void setProfile(StudentProfile profile) {
        this.profile = profile;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public Department getDepartment(){
        return this.department;
    }

    public void setDepartment(Department department){
        this.department=department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
