package com.example.demo.mapper;

import org.mapstruct.Mapper;

import com.example.demo.dto.StudentRequest;
import com.example.demo.dto.StudentResponseDTO;
import com.example.demo.model.Student;

@Mapper(componentModel = "spring")
public interface  StudentMapper {
    StudentResponseDTO toDTO(Student student);
    Student toEntity(StudentRequest studentRequest);
}
