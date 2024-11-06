package org.activity.service;

import org.constants.Constants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GithubService {
    private static final Logger LOGGER = Logger.getLogger(GithubService.class.getName());
    private final HttpClient httpClient;
    private final Duration timeout;

    public GithubService(HttpClient httpClient, Duration timeout) {
        this.httpClient = httpClient;
        this.timeout = timeout;
    }

    public static GithubService createDefault() {
        HttpClient defaultClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        return new GithubService(defaultClient, Duration.ofSeconds(10));
    }

    public void fetchGithubUserEvents(String username) {
        try {
            HttpRequest request = createHttpRequest(username);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                displayActivity(response.body());
            } else {
                LOGGER.warning("Failed to fetch events: HTTP status " + response.statusCode());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            LOGGER.log(Level.SEVERE, "Error fetching GitHub user events", e);
            Thread.currentThread().interrupt();
        }
    }

    private HttpRequest createHttpRequest(String username) throws URISyntaxException {
        URI uri = new URI(Constants.BASE_URL + String.format(Constants.Endpoints.USER_EVENTS, username));
        return HttpRequest.newBuilder()
                .uri(uri)
                .timeout(timeout)
                .header("Accept", Constants.Headers.ACCEPT_JSON)
                .GET()
                .build();
    }

    private void displayActivity(String jsonResponse) {
        JSONArray events = new JSONArray(jsonResponse);

        for (int i = 0; i < events.length(); i++) {
            JSONObject event = events.getJSONObject(i);
            String type = event.optString("type");
            String repoName = event.optJSONObject("repo").optString("name", "Unknown Repository");

            parseAndLogEvent(type, repoName, event);
        }
    }

    private void parseAndLogEvent(String type, String repoName, JSONObject event) {
        switch (type) {
            case "PushEvent":
                JSONArray commits = event.optJSONObject("payload").optJSONArray("commits");
                int commitCount = (commits != null) ? commits.length() : 0;
                System.out.println("- Pushed " + commitCount + " commits to " + repoName);
                break;
            case "IssuesEvent":
                System.out.println("- Opened a new issue in " + repoName);
                break;
            case "WatchEvent":
                System.out.println("- Starred " + repoName);
                break;
            default:
                System.out.println("- " + type + " on " + repoName);
                break;
        }
    }
}