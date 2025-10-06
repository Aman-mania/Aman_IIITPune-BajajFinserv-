package com.example.client;

import com.example.dto.GenerateWebhookRequest;
import com.example.dto.GenerateWebhookResponse;
import com.example.dto.TestWebhookRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@Slf4j
public class WebhookClient {

    private final WebClient webClient;

    public WebhookClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public GenerateWebhookResponse generateWebhook(String url, GenerateWebhookRequest request) {
        log.debug("Generating webhook for URL: {}", url);
        try {
            return webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(GenerateWebhookResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Failed to generate webhook: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    public void submitFinalQuery(String webhookUrl, String jwtToken, String finalQuery) {
        log.debug("Submitting final query to webhook: {}", webhookUrl);
        TestWebhookRequest payload = new TestWebhookRequest(finalQuery);
        try {
            webClient.post()
                    .uri(webhookUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            log.debug("Final query submitted successfully");
        } catch (WebClientResponseException e) {
            log.error("Failed to submit final query: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }
}
