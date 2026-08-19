package com.ai.taskmanagement.ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.ai.taskmanagement.dto.AiResponse;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class AiServiceImpl implements AiService {

    @Override
    public AiResponse generateTask(String title) {

    	String apiKey = System.getenv("GEMINI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Gemini API key is missing");
        }

        String prompt = """
                Create a complete task based on the following task title.

                Task title:
                %s

                Generate:
                1. A clear and professional description for the task.
                2. A suitable priority: LOW, MEDIUM, or HIGH.
                3. A realistic estimated completion time.

                Return ONLY valid JSON in exactly this format:

                {
                  "title": "Build Login Page",
                  "description": "Create a responsive login page with secure authentication fields and proper validation.",
                  "priority": "HIGH",
                  "estimatedTime": "4 hours"
                }

                Rules:
                - Generate exactly ONE task.
                - Keep the original task title.
                - Do not create subtasks.
                - Do not ask for additional information.
                - Priority must be exactly LOW, MEDIUM, or HIGH.
                - estimatedTime should be realistic.
                - Description should clearly explain what needs to be done.
                - Do not add markdown.
                - Do not add any text outside the JSON.
                """.formatted(title);

        String jsonBody = """
                {
                  "contents": [
                    {
                      "parts": [
                        {
                          "text": "%s"
                        }
                      ]
                    }
                  ]
                }
                """.formatted(
                        prompt
                                .replace("\\", "\\\\")
                                .replace("\"", "\\\"")
                                .replace("\n", "\\n")
                );

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"
                    ))
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println("=================================");
            System.out.println("Gemini Status: " + response.statusCode());
            System.out.println("Gemini Response: " + response.body());
            System.out.println("=================================");

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(response.body());

            // Check API error first
            if (response.statusCode() != 200) {

                String errorMessage = "Gemini API error";

                if (root.get("error") != null) {

                    JsonNode errorNode = root.get("error");

                    if (errorNode.get("message") != null) {
                        errorMessage =
                                errorNode.get("message").asText();
                    }
                }

                throw new RuntimeException(
                        "Gemini API Error: " + errorMessage
                );
            }

            // Check candidates
            JsonNode candidates = root.get("candidates");

            if (candidates == null ||
                    candidates.isEmpty()) {

                throw new RuntimeException(
                        "Gemini returned no candidates. Response: "
                                + response.body()
                );
            }

            JsonNode content =
                    candidates.get(0).get("content");

            if (content == null) {

                throw new RuntimeException(
                        "Gemini response does not contain content"
                );
            }

            JsonNode parts =
                    content.get("parts");

            if (parts == null || parts.isEmpty()) {

                throw new RuntimeException(
                        "Gemini response does not contain parts"
                );
            }

            JsonNode textNode =
                    parts.get(0).get("text");

            if (textNode == null) {

                throw new RuntimeException(
                        "Gemini response does not contain text"
                );
            }

            String text = textNode.asText();

            System.out.println("AI Generated Text:");
            System.out.println(text);

            // Remove markdown code fences if Gemini adds them
            text = text.trim();

            if (text.startsWith("```json")) {
                text = text.substring(7);
            }

            if (text.startsWith("```")) {
                text = text.substring(3);
            }

            if (text.endsWith("```")) {
                text = text.substring(
                        0,
                        text.length() - 3
                );
            }

            text = text.trim();

            AiResponse responseObject =
                    mapper.readValue(
                            text,
                            AiResponse.class
                    );

            return responseObject;

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Gemini API call failed: " + e.getMessage(),
                    e
            );
        }
    }
}