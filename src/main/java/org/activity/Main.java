package org.activity;

import org.activity.service.GithubService;
import org.activity.service.UserInputHandler;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(
        name = "github-activity",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Fetches GitHub user events."
)
public class Main implements Callable<Integer> {
    @Parameters(index = "0", description = "The GitHub username", defaultValue = "")
    private String username;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        GithubService githubService = GithubService.createDefault();
        UserInputHandler inputHandler = new UserInputHandler();

        // Use UserInputHandler to get a valid username
        username = inputHandler.getUsername(username);

        System.out.println("Fetching events for user: " + username);
        githubService.fetchGithubUserEvents(username);
        System.out.println("Events fetched for user: " + username);

        return 0;
    }
}