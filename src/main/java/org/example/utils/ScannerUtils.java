package org.example.utils;

import org.example.colors.BackgroundColors;
import org.example.colors.StringColors;

import java.time.LocalDate;
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
                System.out.println(BackgroundColors.WHITE_BACKGROUND+"Enter only numbers"+StringColors.ANSI_RESET);
                scanner.nextLine();
            }
        } while (true);
        return number;
    }

    public LocalDate nextLocalDate(String s) {
        LocalDate result=null;
        do {
            try {
                System.out.print(s);
                result = LocalDate.parse(scanner.nextLine());
            } catch (Exception e) {
                System.out.println(BackgroundColors.WHITE_BACKGROUND + "Please enter the date in the format" + BackgroundColors.RED_BACKGROUND + " \"yyyy-mm-dd\"!" + BackgroundColors.WHITE_BACKGROUND + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + StringColors.ANSI_RESET);
            }
        }while (result==null);
        return result;
    }
}
