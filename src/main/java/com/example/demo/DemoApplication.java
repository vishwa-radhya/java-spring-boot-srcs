package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.demo.service.StudentService;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class,args);

        // ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class,args);
        // StudentService service = context.getBean(StudentService.class);
        // service.sayHello();
        // context.close();
    }
}
