package ru.mephi.lab3.task2;
import ru.mephi.lab3.task1.*;

import java.util.Arrays;
import java.util.Comparator;

public class Task2 {
    static Measurable largest(Measurable[] objects){
        Employee[] staff = new Employee[] {};
        if (objects instanceof Employee[]) staff = (Employee[]) objects;
        return Arrays.stream(staff).max(Comparator.comparingDouble(Measurable::getMeasure)).orElse(null);
    }

    static void main() {
        Employee[] staff = new Employee[] {
                new Employee("Alice", 50000),
                new Employee("Bob", 60000),
                new Employee("Charlie", 55000)
        };
        Measurable largest = largest(staff);
        Employee largestEmployee = (Employee) largest;
        IO.println("Employee with largest salary: " + largestEmployee.getName());
    }
}
