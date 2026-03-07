package ru.mephi.lab3.task12;

import java.io.File;
import java.util.Arrays;

public class Task12 {
    static void main() {
        File[] files = new File(".").listFiles();
        assert files != null;
        Arrays.sort(files,
                (f1, f2) -> {
                    if (f1.isDirectory() != f2.isDirectory())
                        return f1.isDirectory() ? -1 : 1;
                    return f1.getPath().compareTo(f2.getPath());
        });

        for (File f : files) {
            IO.println(f);
        }
    }

}
