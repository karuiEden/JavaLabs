package ru.mephi.lab5.chained_exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class User {
    private final String name;
    private final int age;

    User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name + " (" + age + ")";
    }
}

class CsvParseException extends Exception {

    CsvParseException(String message) {
        super(message);
    }

    CsvParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

class UserImportException extends Exception {

    UserImportException(String message, Throwable cause) {
        super(message, cause);
    }
}

class UserImporter {

    List<User> importUsers(List<String> lines) throws UserImportException {
        try {
            return parse(lines);
        } catch (CsvParseException e) {
            throw new UserImportException("Ошибка импорта пользователей", e);
        }
    }

    private List<User> parse(List<String> lines) throws CsvParseException {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");

            if (parts.length != 2) {
                throw new CsvParseException(
                        "Строка " + (i + 1) + ": ожидалось 'имя,возраст'"
                );
            }

            String name = parts[0].trim();
            String ageText = parts[1].trim();

            try {
                int age = Integer.parseInt(ageText);
                users.add(new User(name, age));
            } catch (NumberFormatException e) {
                throw new CsvParseException(
                        "Строка " + (i + 1) + ": возраст не является числом",
                        e
                );
            }
        }

        return users;
    }
}

public class ChainExceptions {

    static void main() {
        List<String> csvLines = Arrays.asList(
                "Ivan,25",
                "Maria,abc"
        );

        UserImporter importer = new UserImporter();

        try {
            List<User> users = importer.importUsers(csvLines);
            System.out.println("Импортировано пользователей: " + users);
        } catch (UserImportException e) {
            System.out.println("Верхнеуровневая ошибка: " + e.getMessage());
            System.out.println();
            printChain(e);
        }
    }

    private static void printChain(Throwable e) {
        while (e != null) {
            System.out.println(
                    "- " + e.getClass().getSimpleName() + ": " + e.getMessage()
            );
            e = e.getCause();
        }
    }
}