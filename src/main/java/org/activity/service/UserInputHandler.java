package org.activity.service;

import java.util.Scanner;

public class UserInputHandler {
    private final Scanner scanner = new Scanner(System.in);

    public String getUsername(String username) {
        while (username.isEmpty()) {
            System.out.println("Usage: github-activity <username>");
            String input = scanner.nextLine().trim();

            if (input.matches("^github-activity\\s+([a-zA-Z0-9_]{3,15})$")) {
                username = input.split("\\s+")[1];
            } else {
                System.out.println("Invalid input. Please use the format 'github-activity <username>'.");
            }
        }
        return username;
    }
}