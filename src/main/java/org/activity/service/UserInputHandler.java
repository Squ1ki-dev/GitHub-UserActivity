package org.activity.service;

import org.constants.Constants;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInputHandler {
    private final Pattern pattern = Pattern.compile(Constants.COMMAND);

    private final Scanner scanner = new Scanner(System.in);

    public String getUsername(String username) {
        while (username.isEmpty()) {
            System.out.println("Usage: github-activity <username>");
            String input = scanner.nextLine().trim();

            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                username = matcher.group(1);
            } else {
                System.out.println("Invalid input. Please use the format 'github-activity <username>'.");
            }
        }
        return username;
    }
}