package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "student_profiles")
public class StudentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String bio;

    @OneToOne(mappedBy = "profile")
    private Student student;

     public  StudentProfile(){
    }
    public StudentProfile(int id,String bio){
        this.bio=bio;
        this.id=id;
    }
    public Student getStudent(){
        return this.student;
    }
    public void setStudent(Student student){
        this.student=student;
    }
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getBio(){
        return this.bio;
    }
    public void setBio(String bio){
        this.bio=bio;
    }
}
