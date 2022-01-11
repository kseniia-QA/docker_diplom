package ru.netology.travel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    //  TODO    mysql    OR    postgresql
    private String database = "postgresql";

    SqlGetters sqlGetters = new SqlGetters();

    @BeforeEach
    public void setUpAll() {
        open("http://localhost:8080");
    }

    @Test
    void successfulDebitBuy() {
        var order = new AppPage();
        order.autoDebitTour(1);
        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));
    }

    @Test
    void declinedDebitBuy() {
        var order = new AppPage();
        order.autoDebitTour(2);
        order.verifyError();
        assertEquals("DECLINED", sqlGetters.getStatus(database));
    }

    @Test
    void successfulCreditBuy() {
        var order = new AppPage();
        order.autoCreditTour(1);
        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));
    }

    @Test
    void declinedCreditBuy() {
        var order = new AppPage();
        order.autoCreditTour(2);
        order.verifyError();
        assertEquals("DECLINED", sqlGetters.getStatus(database));
    }

    @Test
    void emptyForm() {
        var order = new AppPage();
        order.manualDebitTour("", "", "", "", "");
        order.validateErrors(true, true, true, true, true);
    }

    @Test
    void validateCardNumber() {
        var order = new AppPage();
        order.manualDebitTour("", "01", "23", "HALVA CARD", "457");
        order.validateErrors(true, false, false, false, false);
        order.manualDebitTour("4444444444444441", "", "", "", "");
        order.validateErrors(false, false, false, false, false);
        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));
    }

    @Test
    void wrongSymbolsCardNumber() {
        var order = new AppPage();
        order.manualDebitTour("abcdпрог!@#$)(*&", "01", "23", "HALVA CARD", "457");
        order.validateErrors(true, false, false, false, false);
    }

    @Test
    void validateMonth() {
        var order = new AppPage();
        order.manualDebitTour("4444444444444441", "", "23", "HALVA CARD", "457");
        order.validateErrors(false, true, false, false, false);
        order.manualDebitTour("", "01", "", "", "");
        order.validateErrors(false, false, false, false, false);
        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));
    }

    @Test
    void wrongSymbolsMonth() {
        var order = new AppPage();
        order.manualDebitTour("4444444444444441", "g%", "23", "HALVA CARD", "457");
        order.validateErrors(false, true, false, false, false);
    }

    @Test
    void zeroMonth() {
        var order = new AppPage();
        order.manualDebitTour("4444444444444441", "00", "23", "HALVA CARD", "457");
        order.validateErrors(false, true, false, false, false);
    }

    @Test
    void validateYear() {
        var order = new AppPage();
        order.manualDebitTour("4444444444444441", "01", "", "HALVA CARD", "457");
        order.validateErrors(false, false, true, false, false);
        order.manualDebitTour("", "", "22", "", "");
        order.validateErrors(false, false, false, false, false);
        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));
    }

    @Test
    void wrongSymbolsYear() {
        var order = new AppPage();
        order.manualDebitTour("4444444444444441", "01", "h@", "HALVA CARD", "457");
        order.validateErrors(false, false, true, false, false);
    }

    @Test
    void validateOwner() {
        var order = new AppPage();
        order.manualDebitTour("4444444444444441", "01", "23", "", "457");
        order.validateErrors(false, false, false, true, false);
        order.manualDebitTour("", "", "", "HALVA CARD", "");
        order.validateErrors(false, false, false, false, false);
        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));
    }

    @Test
    void wrongSymbolsOwner() {
        var order = new AppPage();
        order.manualDebitTour("4444444444444441", "01", "23", "213$@#", "457");
        order.validateErrors(false, false, false, true, false);
    }

    @Test
    void validateCode() {
        var order = new AppPage();
        order.manualDebitTour("4444444444444441", "01", "23", "HALVA CARD", "");
        order.validateErrors(false, false, false, false, true);
        order.manualDebitTour("", "", "", "", "457");
        order.validateErrors(false, false, false, false, false);
        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));
    }

    @Test
    void validateCodeWithBugs() {
        var order = new AppPage();
        order.manualDebitTour("4444444444444441", "01", "23", "HALVA CARD", "");
        order.validateErrors(false, false, false, true, true);
        order.manualDebitTour("", "", "", "", "457");
        order.validateErrors(false, false, false, true, false);
        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));
    }
}