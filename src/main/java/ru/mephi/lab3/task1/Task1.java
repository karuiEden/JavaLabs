package ru.mephi.lab3.task1;

import java.util.Arrays;

public class Task1 {

    static double average(Measurable[] objects){
        return Arrays.stream(objects).mapToDouble(Measurable::getMeasure).average().orElse(0);
    }

    static void main() {
        Employee[] staff = new Employee[] {
                new Employee("Alice", 50000),
                new Employee("Bob", 60000),
                new Employee("Charlie", 55000)
        };
        double averageSalary =  average(staff);
        IO.println("Average salary: " + averageSalary);
    }
}
