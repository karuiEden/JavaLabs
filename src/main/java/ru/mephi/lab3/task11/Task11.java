package ru.mephi.lab3.task11;

import java.io.File;

public class Task11 {
    public static String[] filesWithExtension(File dir, String ext) {
        return dir.list((_, name) -> name.endsWith(ext));
    }

    static void main() {
        File dir = new File(".");
        String[] files = filesWithExtension(dir, ".xml");
        for (String file : files) {
            System.out.println(file);
        }
    }
}
