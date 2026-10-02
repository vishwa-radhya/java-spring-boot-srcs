package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Department;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Service 
public class JpaStudentService {
    @PersistenceContext
    private EntityManager entityManager;

    private final StudentRepository studentRepository;

    private final TransactionHelperService transactionHelperService;

    private static final Logger log = LoggerFactory.getLogger(JpaStudentService.class);

    public JpaStudentService(StudentRepository studentRepository,
        TransactionHelperService transactionHelperService
    ){
        this.studentRepository = studentRepository;
        this.transactionHelperService=transactionHelperService;
    }

    // jpa data 
    // public List<Student> getAllStudents(){
        // return studentRepository.findAll();
    // }

    public long countStudents(){
        return studentRepository.count();
    }

    @Cacheable(value = "studentsByEmail",unless = "#result == null")
    public Student getStudentByEmail(String email){
        // System.out.println(">>> Executing database query for: " + email);
        log.info(
            "Executing database query for student email: {}",
            email
        );
        return studentRepository.findByEmail(email).orElse(null);
    }

    public List<Student> searchByName(String name) {
        return studentRepository.findByNameContaining(name);
    }

    public List<Student> findStudentsWithIdGreaterThan(int id) {
        return studentRepository.findByIdGreaterThan(id);
    }


    public List<Student> searchByNameAndEmail(String name, String email) {
        return studentRepository
                .findByNameContainingAndEmailContaining(name, email);
    }

    public List<Student> findStudentsByExactName(String name) {
        return studentRepository.findStudentsByExactName(name);
    }

    public List<Student> findByExactNamePositional(String name) {
        return studentRepository.findByExactNamePositional(name);
    }

    public List<Student> findByEmailNative(String email) {
        return studentRepository.findByEmailNative(email);
    }

    @Transactional
    public void createStudent() {

        Student student = new Student();

        student.setName("Vishwa");
        student.setEmail("vishwa@example.com");

        entityManager.persist(student);

        // student.setName("vishwa updated");

        // entityManager.detach(student);
        // student.setName("Detached name");

    }

    @Transactional
    public void testDetached() {

        Student student = entityManager.find(Student.class, 1);

        System.out.println("Student: " + student.getName());

        entityManager.detach(student);

        student.setName("Detached Name");
    }

    @Transactional
    public void testRemoved() {

        Student student = entityManager.find(Student.class, 2);

        System.out.println("Student: " + student.getName());

        entityManager.remove(student);
    }


    @Transactional
    public void testCascade() {

        Department department = new Department();
        department.setName("Engineering");

        Student student = new Student();
        student.setName("Alice");
        student.setEmail("alice@example.com");

        student.setDepartment(department);
        department.getStudents().add(student);

        entityManager.persist(department);
    }

    @Transactional
    public void testFetch() {

        Student student = entityManager.find(Student.class, 1);

        System.out.println("Student loaded");
        System.out.println(student.getDepartment().getName());
    }

    public List<Student> testJPQL(String name) {
        // SELECT s FROM Student s 
        // "SELECT s FROM Student s WHERE s.name = 'Alice'"
        // return entityManager
        //         .createQuery("SELECT s FROM Student s WHERE s.name = 'Alice'", Student.class)
        //         .getResultList();
        // TypedQuery<Student> query = 
        // entityManager.createQuery(
        //     "SELECT s FROM Student s WHERE s.name = :name",Student.class
        // );
        // query.setParameter("name", "Alice");
        // query.setParameter("name", name);
        TypedQuery<Student> query =
        entityManager.createQuery(
                "SELECT s FROM Student s ORDER BY s.name DESC",
                Student.class
        );
        return query.getResultList();
    }

    public List<Student> testJPQLJoin() {
        // SELECT s FROM Student s JOIN s.department d doesnt work for lazy entities
        TypedQuery<Student> query = entityManager.createQuery(
                "SELECT s FROM Student s JOIN FETCH s.department",
                Student.class
        );

        return query.getResultList();
    }

    public List<Object[]> testJPQLSelectJoin(){
        TypedQuery<Object[]> query = entityManager.createQuery(
            "SELECT s.name, d.name " +
            "FROM Student s " +
            "LEFT JOIN s.department d",
            Object[].class
        );
        return query.getResultList();
    }

    public List<Object[]> testJPQLGroupBy() {
        TypedQuery<Object[]> query = entityManager.createQuery(
                "SELECT d.name, COUNT(s) " +
                "FROM Student s " +
                "LEFT JOIN s.department d " +
                "GROUP BY d.name",
                Object[].class
        );
        return query.getResultList();
    }

    @Transactional
    public void addTestStudents() {

        Student student1 = new Student();
        student1.setName("Bob");
        student1.setEmail("bob@gmail.com");

        Student student2 = new Student();
        student2.setName("Charlie");
        student2.setEmail("charlie@gmail.com");

        Student student3 = new Student();
        student3.setName("David");
        student3.setEmail("david@gmail.com");

        entityManager.persist(student1);
        entityManager.persist(student2);
        entityManager.persist(student3);
    }


    // 11 transactions acid

    @Transactional
    public void testTransaction() {

        Student student1 = new Student();
        student1.setName("No Transaction Student112");
        student1.setEmail("notransaction112@gmail.com");

        studentRepository.save(student1);
        Student student2 = new Student();
        student2.setName("Rollback Student 200");
        student2.setEmail("rollback200@gmail.com");
        
        studentRepository.save(student2);
        throw new RuntimeException("Something went wrong");

        // ...................

        // Student student1 = new Student();
        // student1.setName("Transaction Student 1");
        // student1.setEmail("transaction1@gmail.com");

        // studentRepository.save(student1);

        // Student student2 = new Student();
        // student2.setName("Transaction Student 2");
        // student2.setEmail("transaction2@gmail.com");

        // studentRepository.save(student2);
    }

    @Transactional
    public void methodA() {
        Student student = new Student();
        student.setName("Outer Transaction");
        student.setEmail("outer@gmail.com");
        studentRepository.save(student);
        transactionHelperService.methodB();
        throw new RuntimeException("Outer transaction failed");
    }

    public Student saveStudent(Student student) {
        // logging + actuator crs
        // log.info("Creating student: {}",student.getName());
        // log.trace("TRACE: About to create student");
        // log.debug("DEBUG: Student name is {}", student.getName());
        // log.info("Creating student: {}", student.getName());
        // log.warn("WARN: Creating student operation started");
        // log.error("ERROR: Test error log");

        // return studentRepository.save(student);
        // try{
        Student savedStudent = studentRepository.save(student);

        // log.info("Student created successfully with id: {}",savedStudent.getId());

        return savedStudent;
        // }catch(Exception e){
            // log.error("Failed to create student: {}",student.getName(),e);
            // throw e; // coz existing exception handling can handle this if not it looks like succeded
        // }
    }
    @CachePut(value = "studentsByEmail",key = "#result.email")
    public Student updateStudent(int id,Student updatedStudent){
        Optional<Student> existingStudent = studentRepository.findById(id);
        if(existingStudent.isEmpty()){
            return null;
        }
        Student student = existingStudent.get();
        student.setName(updatedStudent.getName());
        student.setEmail(updatedStudent.getEmail());
        return studentRepository.save(student);
    }

    @CacheEvict(value = "studentsByEmail",key = "#result")
    public String deleteStudent(int id) {
        Optional<Student> student = studentRepository.findById(id);
        if(student.isEmpty()){
            return null;
        }
        String email = student.get().getEmail();
        studentRepository.deleteById(id);
        return email;
    }
}
