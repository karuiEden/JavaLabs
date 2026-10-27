package ru.mephi.lab5.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    static void main() throws SQLException {
        String url = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/postgres");
        String user = System.getenv().getOrDefault("DB_USER", "postgres");
        String password = System.getenv().getOrDefault("DB_PASS", "");

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE IF NOT EXISTS tx_demo_accounts (id INTEGER PRIMARY KEY, owner VARCHAR(100) NOT NULL)");
                statement.execute("SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED ");
                statement.executeUpdate("INSERT INTO tx_demo_accounts (id, owner) VALUES (1, 'Alice'), (2, 'Bob') ON CONFLICT (id) DO NOTHING");
            }

            Map<Integer, String> before = readAccounts(connection);
            System.out.println("До транзакции:    " + before);

            connection.setAutoCommit(false);
            try {
                try (PreparedStatement insert = connection.prepareStatement(
                        "INSERT INTO tx_demo_accounts (id, owner) SELECT COALESCE(MAX(id), 2) + 1, ? FROM tx_demo_accounts");
                     PreparedStatement update = connection.prepareStatement(
                             "UPDATE tx_demo_accounts SET owner = ? WHERE id = ?")) {
                    insert.setString(1, "Charlie");
                    insert.executeUpdate();

                    update.setString(1, "Alice changed");
                    update.setInt(2, 1);
                    update.executeUpdate();
                }

                IO.println("Внутри транзакции: " + readAccounts(connection));
                throw new SQLException("Намеренная ошибка в середине транзакции");
            } catch (SQLException error) {
                IO.println("Ошибка: " + error.getMessage());
                connection.rollback();
                IO.println("Выполнен rollback");
            } finally {
                connection.setAutoCommit(true);
            }

            Map<Integer, String> after = readAccounts(connection);
            System.out.println("После rollback:   " + after);
            if (!before.equals(after)) {
                throw new IllegalStateException("Данные после rollback отличаются от исходных");
            }
            System.out.println("Проверка пройдена: INSERT и UPDATE не сохранились.");
        }
    }

    private static Map<Integer, String> readAccounts(Connection connection) throws SQLException {
        Map<Integer, String> accounts = new LinkedHashMap<>();
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("SELECT id, owner FROM tx_demo_accounts ORDER BY id")) {
            while (result.next()) {
                accounts.put(result.getInt("id"), result.getString("owner"));
            }
        }
        return accounts;
    }
}
