package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.demo.service.StudentService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;


@OpenAPIDefinition(
    info = @Info(
        title = "Student Management API",
        version = "1.0",
        description = "REST API for managing students"
    )
)
@SpringBootApplication
@EnableCaching 
@EnableScheduling 
public class DemoApplication {
    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class,args);

        // ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class,args);
        // StudentService service = context.getBean(StudentService.class);
        // service.sayHello();
        // context.close();
    }
}
