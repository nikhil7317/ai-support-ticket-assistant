
package com.example.aitickets.ai;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;

@RestController
@RequestMapping("/api/ai")
public class AiController {

  private final GeminiService geminiService;

  public AiController(GeminiService geminiService) {
    this.geminiService = geminiService;
  }

  public record TextRequest(
          @NotBlank @Size(max = 4000) String description) {
  }

  public record ChatRequest(
          @NotBlank @Size(max = 4000) String question) {
  }

  public record AiResponse(String answer) {
  }

  @PostMapping("/analyze")
  public AiResponse analyze(@Valid @RequestBody TextRequest req) {

    String instructions =
            "You are an IT support ticket assistant. " +
                    "Return headings: Summary, Category, Priority " +
                    "(Low/Medium/High/Critical), Likely causes, " +
                    "Troubleshooting steps, Suggested reply. " +
                    "Be concise, do not claim certainty without evidence, " +
                    "and warn before risky changes.";

    return new AiResponse(
            call(instructions, req.description()));
  }

  @PostMapping("/chat")
  public AiResponse chat(@Valid @RequestBody ChatRequest req) {

    String instructions =
            "You are an IT support assistant. " +
                    "Give practical step-by-step troubleshooting. " +
                    "Ask for missing details when needed. " +
                    "Warn before destructive commands or production-impacting " +
                    "changes. Do not pretend to have inspected systems or logs.";

    return new AiResponse(
            call(instructions, req.question()));
  }

  private String call(String instructions, String input) {

    String prompt = instructions
            + "\n\nUser request:\n"
            + input;

    try {
      return geminiService.generateResponse(prompt);
    } catch (RestClientResponseException ex) {
      System.err.println(
              "Gemini HTTP status: " + ex.getStatusCode());
      System.err.println(
              "Gemini error response: "
                      + ex.getResponseBodyAsString());

      throw new IllegalStateException(
              "Gemini API request failed (HTTP "
                      + ex.getStatusCode().value()
                      + "). Check the backend console.");
    }
  }
}

