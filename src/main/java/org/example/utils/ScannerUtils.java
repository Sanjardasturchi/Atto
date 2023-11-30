package org.example.utils;

import org.example.colors.BackgroundColors;
import org.example.colors.StringColors;

import java.util.Scanner;

public class ScannerUtils {
    Scanner scanner = new Scanner(System.in);

    public String nextLine(String s) {
        System.out.print(s);
        String str = scanner.nextLine();
        return str;
    }
    public String nextLineWithColor(String s, String backgroundColor, String color) {
        System.out.println(backgroundColor+color+s+StringColors.ANSI_RESET);
//        System.out.println(s);
//        System.out.println(backgroundColor);
//        System.out.println(color);
        String str = scanner.nextLine();
        return str;
    }

    public int nextInt(String s) {
        int number;
        do {
            try {
                System.out.print(s);
                number = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("Enter only numbers");
                scanner.nextLine();
            }
        } while (true);
        return number;
    }
}
