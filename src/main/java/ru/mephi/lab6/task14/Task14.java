package ru.mephi.lab6.task14;

import java.util.ArrayList;
import java.util.List;

public class Task14 {

    public static <T extends AutoCloseable> void closeAll(List<T> elements) throws Exception {
        List<Exception> failures = new ArrayList<>();

        for (T element : elements) {
            try {
                element.close();
            } catch (Exception ex) {
                failures.add(ex);
            }
        }

        if (failures.isEmpty()) {
            return;
        }

        Exception first = failures.getFirst();
        for (int i = 1; i < failures.size(); i++) {
            first.addSuppressed(failures.get(i));
        }
        throw first;
    }

    public static class Resource implements AutoCloseable {
        private final String name;
        private final boolean broken;
        private boolean closed;

        Resource(String name, boolean broken) {
            this.name = name;
            this.broken = broken;
        }

        @Override
        public void close() {
            closed = true;
            if (broken) {
                throw new IllegalStateException(name + " failed to close");
            }
        }

        @Override
        public String toString() {
            return name + (closed ? "[closed]" : "[open]");
        }
    }

    static void main() {
        List<Resource> ok = List.of(
                new Resource("r1", false),
                new Resource("r2", false));
        try {
            closeAll(ok);
            System.out.println("all closed: " + ok);
        } catch (Exception ex) {
            System.out.println("unexpected: " + ex);
        }

        List<Resource> mixed = new ArrayList<>(List.of(
                new Resource("r1", false),
                new Resource("bad1", true),
                new Resource("r3", false),
                new Resource("bad2", true)));
        try {
            closeAll(mixed);
            System.out.println("no exception");
        } catch (Exception ex) {
            System.out.println("thrown: " + ex.getMessage());
            System.out.println("suppressed: " + ex.getSuppressed().length);
            for (Throwable suppressed : ex.getSuppressed()) {
                System.out.println("  " + suppressed.getMessage());
            }
            System.out.println("closed anyway: " + mixed);
        }

        List<Resource> allBroken = List.of(
                new Resource("bad1", true),
                new Resource("bad2", true),
                new Resource("bad3", true));
        try {
            closeAll(allBroken);
        } catch (Exception ex) {
            System.out.println("first: " + ex.getMessage());
            System.out.println("suppressed: " + ex.getSuppressed().length);
            System.out.println("closed anyway: " + allBroken);
        }

        try {
            closeAll(new ArrayList<Resource>());
            System.out.println("empty list: no exception");
        } catch (Exception ex) {
            System.out.println("empty: " + ex);
        }
    }
}
