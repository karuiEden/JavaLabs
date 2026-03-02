package ru.mephi.lab2.adds_tasks;

/**
 * Валидация данных
 */
class User{
    private int age = 20;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 0) throw new IllegalArgumentException("Age must be positive");
        this.age = age;
    }
}

//Пересчет полей
class Circle{
    private double r;
    private double area;

    Circle(double r){
        this.r = r;
        calc();
    }

    private void calc(){
        area = Math.PI * Math.pow(r, 2);
    }

    public double getRadius() {
        return r;
    }

    public void setRadius(double r) {
        if (r <= 0) throw new IllegalArgumentException("Radius must be positive");
        this.r = r;
        calc();
    }

    public double getArea() {
        return area;
    }
}

// Подгрузка
class CachedData{
    private String cache = null;

    public String getCache() {
        if(cache == null) load();
        return cache;
    }

    private void load(){
        cache = "Some data";
    }
}

public class GettersSetters {
    static void main() {
        User us = new User();
        IO.println(us.getAge());
        try {
            us.setAge(-10);
        } catch (Exception e) {
            IO.println(e.toString());
        }
        Circle c = new Circle(3.0);
        System.out.printf("%.2f", c.getArea());
        c.setRadius(10);
        System.out.printf("%.2f", c.getArea());

        CachedData data = new CachedData();
        System.out.println(data.getCache());
    }
}
