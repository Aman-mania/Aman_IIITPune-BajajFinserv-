package com.example.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class SqlQueryProvider {

    public String loadQueryForQuestion(int questionId) {
        String resource = "sql/question" + questionId + ".sql";
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new ClassPathResource(resource).getInputStream(), StandardCharsets.UTF_8))) {
            return br.lines()
                     .filter(l -> !l.trim().startsWith("--"))
                     .collect(Collectors.joining("\n"))
                     .trim();
        } catch (Exception e) {
            throw new RuntimeException("Unable to load SQL file: " + resource, e);
        }
    }
}
