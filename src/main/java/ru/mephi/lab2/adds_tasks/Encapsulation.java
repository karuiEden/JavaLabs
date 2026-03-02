package ru.mephi.lab2.adds_tasks;

/**
 * Инкапсуляция — это принцип ООП, по которому объект объединяет состояние и операции над ним в единое целое и скрывает детали своей внутренней реализации,
 * предоставляя наружу минимальный, стабильный и безопасный интерфейс.
 */

class AccountBankWithout{
    public double balance;
}

class AccountBankWith{
    private double balance;

    public double getBalance() {
        return balance;
    }

    public void deposit(double money) {
        if (money < 0) throw new IllegalArgumentException("Quantity of money must be positive.");
        this.balance += money;
    }

    public void withdraw(double money){
        if (money < 0) throw new IllegalArgumentException("Quantity of money must be positive.");
        if (money > balance) throw new IllegalArgumentException("Quantity of money must be less, than the balance");
        balance -= money;
    }
}

public class Encapsulation {
    static void main() {
        AccountBankWithout acc = new AccountBankWithout();
        acc.balance = -10000; // Нарушение логики.

        AccountBankWith safe_acc = new AccountBankWith();
        try{
            safe_acc.deposit(-1000);
        } catch (Exception e){
            IO.println(e.toString());
        }
        safe_acc.deposit(1000);
        try {
            safe_acc.withdraw(10000);
        } catch (Exception e) {
            IO.println(e.toString());
        }
    }
}
