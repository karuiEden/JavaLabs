package ru.mephi.lab6.task17;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Task17 {

    public static final class Employee implements Comparable<Employee> {
        private final String name;

        public Employee(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public int compareTo(Employee other) {
            return name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) throws Exception {
        Method declared = Employee.class.getDeclaredMethod("compareTo", Employee.class);
        System.out.println("declared: " + declared);
        System.out.println("  bridge: " + declared.isBridge()
                + ", synthetic: " + declared.isSynthetic());

        for (Method method : Employee.class.getDeclaredMethods()) {
            if (method.getName().equals("compareTo")) {
                System.out.println("found: " + method
                        + " bridge=" + method.isBridge()
                        + " synthetic=" + method.isSynthetic());
            }
        }

        System.out.println("methods found by name: "
                + Arrays.toString(Employee.class.getMethods()));

        Comparable<Employee> viaInterface = new Employee("Fred");
        System.out.println("compareTo via Comparable: "
                + viaInterface.compareTo(new Employee("Wilma")));

        System.out.println();
        System.out.println("javap -p -c:");
        printJavap();
    }

    private static void printJavap() throws Exception {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        String javap = System.getProperty("java.home") + File.separator + "bin"
                + File.separator + (isWindows ? "javap.exe" : "javap");

        if (!new File(javap).canExecute()) {
            System.out.println("javap not found at " + javap);
            return;
        }

        Process process = new ProcessBuilder(javap, "-p", "-c",
                "-cp", System.getProperty("java.class.path"), Employee.class.getName())
                .redirectErrorStream(true)
                .start();

        String output = new String(process.getInputStream().readAllBytes());
        process.waitFor();

        for (String line : output.split("\r?\n")) {
            if (line.contains("compareTo") || line.contains("checkcast")) {
                System.out.println("  " + line.strip());
            }
        }
    }
}
