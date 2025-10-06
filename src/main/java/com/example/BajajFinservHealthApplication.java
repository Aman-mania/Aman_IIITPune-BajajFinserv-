package com.example;

import com.example.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class BajajFinservHealthApplication {

    private final SolutionService solutionService;

    public static void main(String[] args) {
        SpringApplication.run(BajajFinservHealthApplication.class, args);
    }

    @Bean
    CommandLineRunner run() {
        return args -> solutionService.executeFlowOnStartup();
    }
}
