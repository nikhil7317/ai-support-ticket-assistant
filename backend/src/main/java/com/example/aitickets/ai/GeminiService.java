
package com.example.aitickets.ai;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.client.RestClientResponseException;

@Service
public class GeminiService {

    private final RestClient restClient;
    private final String apiKey;
    private final String model;
    private final String baseUrl;

    public GeminiService(
            @Value("${gemini.api-key}") String apiKey,
            @Value("${gemini.model}") String model,
            @Value("${gemini.base-url}") String baseUrl) {

        this.apiKey = apiKey;
        this.model = model;
        this.baseUrl = baseUrl;
        this.restClient = RestClient.builder().build();
    }

    public String generateResponse(String prompt) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Gemini API key is missing. Check GEMINI_API_KEY.");
        }

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                ),
                "generationConfig", Map.of(
                        "maxOutputTokens", 800
                )
        );

        JsonNode response = null;

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                response = restClient.post()
                        .uri(baseUrl + "/models/" + model + ":generateContent")
                        .header("x-goog-api-key", apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(requestBody)
                        .retrieve()
                        .body(JsonNode.class);

                break; // Request succeeded

            } catch (RestClientResponseException ex) {
                int status = ex.getStatusCode().value();

                // Retry only temporary errors
                if ((status != 503 && status != 429 && status != 500)
                        || attempt == 3) {
                    throw ex;
                }

                try {
                    Thread.sleep(1000L * (1L << (attempt - 1)));
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException(
                            "Gemini retry interrupted.", interrupted);
                }
            }
        }

        if (response == null) {
            throw new IllegalStateException(
                    "Gemini returned no response after retries.");
        }

        if (response == null) {
            throw new IllegalStateException(
                    "Gemini returned an empty response.");
        }

        JsonNode textNode = response.at(
                "/candidates/0/content/parts/0/text");

        if (textNode.isMissingNode() || textNode.isNull()) {
            throw new IllegalStateException(
                    "Gemini returned no text. Check the API response.");
        }

        return textNode.asText();
    }
}
