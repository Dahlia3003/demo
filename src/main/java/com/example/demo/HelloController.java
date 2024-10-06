package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class HelloController {

    @Autowired
    private GreetingRepository greetingRepository;

    @GetMapping("/hello")
    public String hello() {
        Optional<Greeting> greeting = greetingRepository.findById(1L); // Giả sử ID = 1 chứa "Hello World"
        return greeting.map(Greeting::getMessage).orElse("Greeting not found");
    }
}
