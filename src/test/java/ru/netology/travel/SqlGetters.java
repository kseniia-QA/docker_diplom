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

    @SneakyThrows
    public String getStatus(String base) {
        Connection conn = getConnection(base);
        ResultSet dataStmt = null;
        if (!isCredit) {
            dataStmt = conn.createStatement().executeQuery("SELECT * FROM payment_entity ORDER BY created DESC");
        } else {
            dataStmt = conn.createStatement().executeQuery("SELECT * FROM credit_request_entity ORDER BY created DESC");
        }
        String status = null;
        boolean flag = false;
        try (var rs = dataStmt) {
            while (rs.next()) {
                status = rs.getString("status");
                String transactionId = null;
                if (!isCredit) {
                    transactionId = rs.getString("transaction_id");
                } else {
                    transactionId = rs.getString("bank_id");
                }
                if (transactionId.equalsIgnoreCase(getLastPaymentId(base))) {
                    break;
                } else {
                    flag = true;
                }
            }
            if (flag) {
                status = null;
                System.out.println("Не найдено такой транзакции");
            }
        }
        return status;
    }


}
