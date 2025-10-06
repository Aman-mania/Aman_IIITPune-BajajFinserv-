package com.example.dto;

import lombok.Data;

@Data
public class GenerateWebhookResponse {
    private String webhook;
    private String accessToken;
}
