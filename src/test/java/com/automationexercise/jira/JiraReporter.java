package com.automationexercise.jira;

import com.automationexercise.config.ConfigManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

public class JiraReporter {
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public boolean isConfigured() {
        return !ConfigManager.JIRA_BASE_URL.isBlank()
                && !ConfigManager.JIRA_USER_EMAIL.isBlank()
                && !ConfigManager.JIRA_API_TOKEN.isBlank();
    }

    public void createOrUpdate(JiraTestCase metadata, String actualResult) {
        try {
            if (ConfigManager.JIRA_ISSUE_KEY.isBlank()) {
                createIssue(metadata, actualResult);
            } else {
                updateIssue(ConfigManager.JIRA_ISSUE_KEY, metadata, actualResult);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Jira reporting failed", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Jira reporting failed", e);
        }
    }

    private void createIssue(JiraTestCase metadata, String actualResult)
            throws IOException, InterruptedException {
        ObjectNode fields = mapper.createObjectNode();
        fields.putObject("project").put("key", ConfigManager.JIRA_PROJECT_KEY);
        fields.putObject("issuetype").put("name", "Task");
        fields.put("summary", metadata.summary());
        fields.set("description", buildDescription(metadata, actualResult));
        fields.put(ConfigManager.JIRA_EPIC_LINK_FIELD, ConfigManager.JIRA_EPIC_KEY);

        JsonNode response = send("POST", "/rest/api/3/issue", fields);
        System.out.println("Created Jira Task: " + response.path("key").asText());
    }

    private void updateIssue(String issueKey, JiraTestCase metadata, String actualResult)
            throws IOException, InterruptedException {
        ObjectNode fields = mapper.createObjectNode();
        fields.set("description", buildDescription(metadata, actualResult));

        send("PUT", "/rest/api/3/issue/" + issueKey, fields);
        System.out.println("Updated Jira Task: " + issueKey);
    }

    private ObjectNode buildDescription(JiraTestCase metadata, String actualResult) {
        StringBuilder description = new StringBuilder();
        description.append("## Acceptance Criteria\n");
        for (String criterion : metadata.acceptanceCriteria()) {
            description.append("- ").append(criterion).append("\n");
        }

        description.append("\n## Steps to Reproduce\n");
        int stepNumber = 1;
        for (String step : metadata.steps()) {
            description.append(stepNumber++).append(". ").append(step).append("\n");
        }

        description.append("\n## Expected Result\n")
                .append(metadata.expectedResult())
                .append("\n\n## Actual Result\n")
                .append(actualResult)
                .append("\n\n## Execution Details\n")
                .append("Execution time: ").append(java.time.Instant.now()).append("\n")
                .append("Test environment: ").append(System.getProperty("os.name")).append("\n")
                .append("Java: ").append(System.getProperty("java.version")).append("\n");
        ObjectNode document = mapper.createObjectNode();
        document.put("type", "doc");
        document.put("version", 1);
        var content = document.putArray("content");
        for (String line : description.toString().split("\\R")) {
            ObjectNode paragraph = content.addObject();
            paragraph.put("type", "paragraph");
            paragraph.putArray("content").addObject().put("type", "text").put("text", line);
        }
        return document;
    }

    private JsonNode send(String method, String path, ObjectNode body)
            throws IOException, InterruptedException {
        String credentials = ConfigManager.JIRA_USER_EMAIL + ":" + ConfigManager.JIRA_API_TOKEN;
        String authorization = "Basic " + Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ConfigManager.JIRA_BASE_URL.replaceAll("/+$", "") + path))
                .timeout(Duration.ofSeconds(30))
                .header("Authorization", authorization)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method(method, HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("Jira API returned HTTP " + response.statusCode() + ": " + response.body());
        }
        return response.body().isBlank() ? mapper.createObjectNode() : mapper.readTree(response.body());
    }
}
