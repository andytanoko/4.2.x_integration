package com.gridnode.gtas.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

public class UIManager {

    private static String getLineFromConsole(String prompt) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(prompt + ": ");
        System.out.flush();
        try {
            return reader.readLine();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getString(String prompt) {
        String input = getLineFromConsole(prompt);
        while (input == null || input.length() == 0)
            input = getLineFromConsole("Input Required! Please re-enter.\n"+prompt);
        return input;
    }

    public static String getOptionalString(String prompt, String optStr) {
        return getLineFromConsole(prompt + " "+optStr);
    }

    public static char getCharacter(String prompt) {
        char ch = '\0';
        String input = getLineFromConsole(prompt);
        do {
            if (input == null || input.length() == 0 || input.length() > 1)
                input = getLineFromConsole("Invalid character input! Please re-enter.\n" + prompt);
            else {
                ch = input.charAt(0);
            }
        } while (ch == '\0');
        return ch;
    }


}