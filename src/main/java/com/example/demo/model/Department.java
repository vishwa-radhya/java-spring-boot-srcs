package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity 
@Table(name = "departments")
public class Department {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "department",cascade = CascadeType.ALL)
    private List<Student> students= new ArrayList<>();

    public Department(){
    }

    public Department(int id,String name){
        this.id=id;
        this.name=name;
    }

    public List<Student> getStudents(){
        return this.students;
    }

    public void setStudents(List<Student> students){
        this.students=students;
    }

    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name=name;
    }
}
