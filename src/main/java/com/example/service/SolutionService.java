package com.example.service;

import com.example.client.WebhookClient;
import com.example.domain.Solution;
import com.example.dto.GenerateWebhookRequest;
import com.example.dto.GenerateWebhookResponse;
import com.example.repository.SolutionRepository;
import com.example.util.SqlQueryProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolutionService {

    private final WebhookClient webhookClient;
    private final SolutionRepository repository;
    private final SqlQueryProvider queryProvider;

    @Value("${app.generate-webhook-url}")
    private String generateWebhookUrl;

    @Value("${app.regNo}")
    private String regNo;

    @Value("${app.name}")
    private String name;

    @Value("${app.email}")
    private String email;

    @Value("${app.test-webhook-url}")
    private String testWebhookUrl;

    public void executeFlowOnStartup() {
        try {
            log.info("Starting Bajaj Finserv Health Challenge Flow...");
            
            // Step 1: Generate webhook
            log.info("Step 1: Generating webhook...");
            GenerateWebhookRequest request = new GenerateWebhookRequest(name, regNo, email);
            GenerateWebhookResponse response = webhookClient.generateWebhook(generateWebhookUrl, request);
            
            log.info("Webhook generated successfully. Webhook URL: {}", response.getWebhook());
            
            // Step 2: Determine question ID based on regNo
            int questionId = determineQuestionIdFromRegNo(regNo);
            log.info("Determined question ID: {} for regNo: {}", questionId, regNo);
            
            // Step 3: Load and prepare SQL query
            String finalQuery = queryProvider.loadQueryForQuestion(questionId);
            log.info("Loaded SQL query for question {}: {}", questionId, finalQuery);
            
            // Step 4: Save solution to database
            Solution solution = Solution.builder()
                    .regNo(regNo)
                    .questionId(questionId)
                    .finalQuery(finalQuery)
                    .submittedAt(OffsetDateTime.now())
                    .build();
            repository.save(solution);
            log.info("Solution saved to database with ID: {}", solution.getId());
            
            // Step 5: Submit final query to webhook
            log.info("Step 5: Submitting final query to webhook...");
            webhookClient.submitFinalQuery(testWebhookUrl, response.getAccessToken(), finalQuery);
            
            log.info("Challenge completed successfully! Final query submitted.");
            
        } catch (Exception e) {
            log.error("Error executing challenge flow: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to execute challenge flow", e);
        }
    }

    private int determineQuestionIdFromRegNo(String regNoStr) {
        // Extract last two digits from regNo
        String digits = regNoStr.replaceAll("\\D+", "");
        int lastTwo = digits.length() >= 2 ? 
                Integer.parseInt(digits.substring(digits.length() - 2)) : 
                Integer.parseInt(digits);
        
        // Return 1 for odd, 2 for even
        return (lastTwo % 2 == 1) ? 1 : 2;
    }
}
