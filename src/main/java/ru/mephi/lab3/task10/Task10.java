package ru.mephi.lab3.task10;

import java.io.File;
import java.io.FileFilter;

public class Task10 {

    public static File[] getSubdirectoriesLambda(File dir) {
        return dir.listFiles(file -> file.isDirectory());
    }

    public static File[] getSubdirectoriesRef(File dir) {
        return dir.listFiles(File::isDirectory);
    }

    public static File[] getSubdirectoriesAnon(File dir) {
        return dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
    }

    static void main() {
        File dir = new File(".");
        //Lambda
        File[] subdirsLam = getSubdirectoriesLambda(dir);
        for (File f : subdirsLam) {
            IO.println(f.getName());
        }
        IO.println();
        //Method reference
        File[] subdirsRef = getSubdirectoriesRef(dir);
        for (File f : subdirsRef) {
            IO.println(f.getName());
        }
        IO.println();
        //Anonymous class
        File[] subdirsAnon = getSubdirectoriesAnon(dir);
        for (File f : subdirsAnon) {
            IO.println(f.getName());
        }
    }

}
