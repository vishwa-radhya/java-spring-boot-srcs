package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Student;

public interface  StudentRepository extends JpaRepository<Student,Integer>{ // <Entity this repo manages, type of entity primary key>
    // now spring data provides methods like save, findById, findAll, existsById, deleteById, count ....
    // without us implementing them


    // method names describe queries
    Optional<Student> findByEmail(String email); // find -> By -> Email

    List<Student> findByNameContaining(String name);

    List<Student> findByIdGreaterThan(int id);

    List<Student> findByNameContainingAndEmailContaining(
            String name,
            String email
    );

    // jpql in data jpa 
    @Query("SELECT s FROM Student s WHERE s.name = :name")
    List<Student> findStudentsByExactName(@Param("name") String name);

    @Query("SELECT s FROM Student s WHERE s.name = ?1")
    List<Student> findByExactNamePositional(String name);

    // native sql query
    @Query(
        value = "SELECT * FROM students WHERE student_email = :email",
        nativeQuery = true
    )
    List<Student> findByEmailNative(@Param("email") String email);
}  
