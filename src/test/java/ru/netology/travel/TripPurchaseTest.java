package ru.netology.travel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;


public class TripPurchaseTest {


    private String database = "postgresql";

    SqlGetters sqlGetters = new SqlGetters();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");

    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
    }


    @Test
    void declinedDebitBuy() {
        var order = new FillForm();
        PurchaseType noCredit = new PurchaseType();
        noCredit.buy();
        FillForm card = new FillForm();
        card.fillIncorrectCard();


        order.verifySuccess();
        assertEquals("DECLINED", sqlGetters.getStatus(database));
    }

    @Test
    void successfulCreditBuy() {
        var order = new FillForm();
        PurchaseType noCredit = new PurchaseType();
        noCredit.creditBuy();
        FillForm card = new FillForm();
        card.fillCorrectForm();


        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));
    }

    @Test
    void declinedCreditBuy() {
        var order = new FillForm();
        PurchaseType noCredit = new PurchaseType();
        noCredit.creditBuy();
        FillForm card = new FillForm();
        card.fillIncorrectCard();


        order.verifyError();
        assertEquals("DECLINED", sqlGetters.getStatus(database));
    }
}
