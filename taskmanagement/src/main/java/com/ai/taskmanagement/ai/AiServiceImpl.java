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

        String apiKey = System.getenv("Gemini-API");

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

            System.out.println("Gemini Status: " + response.statusCode());
            System.out.println("Gemini Response: " + response.body());

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(response.body());

            String text = root
                    .get("candidates")
                    .get(0)
                    .get("content")
                    .get("parts")
                    .get(0)
                    .get("text")
                    .asText();

            AiResponse responseObject =
                    mapper.readValue(text, AiResponse.class);

            return responseObject;

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException("Gemini API call failed", e);
        }
    }
}