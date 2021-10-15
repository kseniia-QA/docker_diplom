package ru.netology.travel;

import com.codeborne.selenide.Condition;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public final class SqlGetters {
    private static boolean isCredit;

    public static void setCredit(boolean credit) {
        isCredit = credit;
    }

    @SneakyThrows
    private Connection getConnection(String base) {
        if (base.equalsIgnoreCase("mysql")) {
            return DriverManager.getConnection("jdbc:mysql://217.25.88.206:3306/mysql", "user", "pass");
        } else {
            return DriverManager.getConnection("jdbc:mysql://217.25.88.206:3306/mysql", "user", "pass");
        }


    }
    @SneakyThrows
    private String getLastPaymentId(String base) {
        Connection conn = getConnection(base);
        var dataStmt = conn.createStatement().executeQuery("SELECT * FROM order_entity ORDER BY created DESC");
        dataStmt.next();
        return dataStmt.getString("payment_id");
    }

    public static String getStatus() {
        String status = "";
        val statusSQL = "SELECT status FROM credit_request_entity;";
        val runner = new QueryRunner();

        try (
                val conn = DriverManager.getConnection(url, user, password);
        ) {
            status = runner.query(conn, statusSQL, new ScalarHandler<>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return status;
    }

}
