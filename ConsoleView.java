package view;

import java.util.Scanner;

public class ConsoleView {

    private final Scanner scanner =
            new Scanner(System.in);

    public void showMenu() {
        System.out.println();
        System.out.println("1. Вычислить");
        System.out.println("2. История");
        System.out.println("3. Сохранить всю историю");
        System.out.println("4. Сохранить выбранные записи");
        System.out.println("5. Выход");
        System.out.print("Выберите пункт меню: ");
    }

    public int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; 
        }
    }

    public String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public void print(String text) {
        System.out.println(text);
    }
}