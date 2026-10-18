package ru.mephi.lab6.task22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Task22 {

    public static <V, T extends Throwable> V doWork(Callable<V> c, Supplier<? extends T> exceptionConstructor)
            throws T {
        try {
            return c.call();
        } catch (Throwable realEx) {
            T declared = exceptionConstructor.get();
            if (declared != realEx) {
                try {
                    declared.initCause(realEx);
                } catch (IllegalStateException ignored) {
                    // у уже созданного исключения причина задана
                }
            }
            throw declared;
        }
    }

    public static String readAll(Path path) {
        return Task22.<String, RuntimeException>doWork(
                () -> new String(Files.readAllBytes(path)),
                RuntimeException::new);
    }

    static void main() throws IOException {
        String value = Task22.<String, RuntimeException>doWork(() -> "ok", RuntimeException::new);
        System.out.println("success: " + value);

        try {
            Task22.<String, IllegalArgumentException>doWork(() -> {
                throw new IOException("disk on fire");
            }, IllegalArgumentException::new);
        } catch (IllegalArgumentException ex) {
            System.out.println("IllegalArgumentException: " + ex.getMessage()
                    + ", cause: " + ex.getCause());
        }

        try {
            Task22.<String, RuntimeException>doWork(() -> {
                throw new IllegalArgumentException("bad input");
            }, RuntimeException::new);
        } catch (RuntimeException ex) {
            System.out.println("RuntimeException passed through: " + ex.getMessage()
                    + ", cause: " + ex.getCause());
        }

        try {
            Task22.<String, IllegalStateException>doWork(() -> {
                throw new IllegalArgumentException("inner");
            }, IllegalStateException::new);
        } catch (IllegalStateException ex) {
            System.out.println("declared type wins: " + ex.getMessage()
                    + ", cause: " + ex.getCause());
        }

        try {
            Task22.<String, RuntimeException>doWork(() -> "fine", RuntimeException::new);
        } catch (Throwable ex) {
            System.out.println("unexpected " + ex);
        }

        Path temp = null;
        try {
            temp = Files.createTempFile("task22", ".txt");
            Files.writeString(temp, "hello from readAll");

            String text = readAll(temp);
            System.out.println("readAll: " + text.strip());
        } catch (IOException ex) {
            System.out.println("io problem: " + ex);
        }
        if (temp != null) {
            Files.deleteIfExists(temp);
        }

        try {
            Task22.<String, RuntimeException>doWork(
                    () -> readAll(Path.of("no-such-file.txt")), RuntimeException::new);
        } catch (RuntimeException ex) {
            System.out.println("missing file becomes: " + ex.getClass().getSimpleName()
                    + ", cause: " + ex.getCause().getClass().getSimpleName());
        }
    }
}
