package ru.mephi.lab2.task9;

/**
 * Class Car. It must be mutable because its state can change continuously.
 */
class Car{
    private double miles;
    private double fuel;
    private final double efficiency;

    public Car(double efficiency){
        if (efficiency <= 0) throw new IllegalArgumentException("Efficency must be positive");
        this.efficiency = efficiency;
    }

    void drive(double distance){
        if (distance < 0) throw new IllegalArgumentException("Distance must be positive");
        double fuelNeeded = distance / efficiency;
        if (fuelNeeded > fuel) throw new IllegalStateException("Not enough fuel");
        fuel -= fuelNeeded;
        miles += distance;
    }

    public double getMiles() {
        return miles;
    }

    public double getFuel() {
        return fuel;
    }

    public void addGas(double gallons) {
        if (gallons < 0) throw new IllegalArgumentException("Fuel must be positive");
        fuel += gallons;
    }
}

public class Task9 {
    static void main() {
        Car car = new Car(10);
        car.addGas(1000);
        car.drive(100);
        System.out.println(car.getMiles());
        System.out.println(car.getFuel());
    }
}
