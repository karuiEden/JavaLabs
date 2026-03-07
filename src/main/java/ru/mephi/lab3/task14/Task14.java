package ru.mephi.lab3.task14;

import ru.mephi.lab3.task1.Employee;

import java.util.Arrays;
import java.util.Comparator;

public class Task14 {
    static void main() {

        Employee[] staff = new Employee[] {
                new Employee("Alice", 50000),
                new Employee("Bob", 60000),
                new Employee("Charlie", 55000)
        };

        Comparator<Employee> comp =
                Comparator.comparingDouble(Employee::getSalary)
                        .thenComparing(Employee::getName);

        Arrays.sort(staff, comp);
        Arrays.stream(staff).forEach(x -> IO.println(x.getName() + " " + x.getSalary()));
        IO.println();
        Arrays.sort(staff, comp.reversed());
        Arrays.stream(staff).forEach(x -> IO.println(x.getName() + " " + x.getSalary()));
    }
}
