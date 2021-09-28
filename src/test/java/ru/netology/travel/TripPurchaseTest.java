package ru.netology.travel;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


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
    public void shouldSubmitRequestToBuy() {
        SelenideElement form = $("[method=post]");
        PurchaseType e = new PurchaseType();
        e.buy();

        FillForm card = new FillForm();
        card.fillCorrectForm ();

        $("[class=notification__title]").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(exactText("Успешно"));
        $("[class=notification__content]").shouldBe(Condition.visible).shouldHave(exactText("Операция одобрена Банком."));

    }

    @Test
    public void shouldNotSubmitRequestToBuyWrongCardNumber() {
        SelenideElement form = $("[method=post]");
        PurchaseType e = new PurchaseType();
        e.buy();
        FillForm card = new FillForm();
        card.fillIncorrectCard ();
        $("[class=notification__title]").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(exactText("Ошибка"));
    }

    @Test
    public void shouldNotSubmitRequestToBuyErrors() {
        SelenideElement form = $("[method=post]");
        PurchaseType e = new PurchaseType();
        e.buy();
        FillForm card = new FillForm();
        card.fillIncorrectData ();
        form.$$("[class=input__sub]").get(0).shouldHave(text("Неверно указан срок действия карты"));
        form.$$("[class=input__sub]").get(1).shouldHave(text("Истёк срок действия карты"));

    }


    @Test
    public void shouldNotSubmitIncorrectData() {
        SelenideElement form = $("[method=post]");
        PurchaseType e = new PurchaseType();
        e.buy();
        FillForm card = new FillForm();
        card.fillIncorrectData();
        $("[class=notification__title]").shouldNotBe(visible);

    }


    @Test
    public void shouldNotSubmitRequestToBuyNoFields() {
        SelenideElement form = $("[method=post]");
        PurchaseType e = new PurchaseType();
        e.buy();
        FillForm button = new FillForm();
        button.continueButton();
        form.$$("[class=input__sub]").get(0).shouldHave(text("Поле обязательно для заполнения"));
        form.$$("[class=input__sub]").get(1).shouldHave(text("Поле обязательно для заполнения"));
        form.$$("[class=input__sub]").get(2).shouldHave(text("Поле обязательно для заполнения"));
        form.$$("[class=input__sub]").get(3).shouldHave(text("Поле обязательно для заполнения"));
        form.$$("[class=input__sub]").get(4).shouldHave(text("Поле обязательно для заполнения"));
        $("[class=notification__title]").shouldNotBe(visible);

    }


    @Test
    public void shouldSubmitRequestToBuyInCredit() {

        SelenideElement form = $("[method=post]");
        PurchaseType d = new PurchaseType();
        d.creditBuy();
        FillForm card = new FillForm();
        card.fillIncorrectCard ();

        $("[class=notification__title]").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(exactText("Успешно"));
        $("[class=notification__content]").shouldBe(Condition.visible).shouldHave(exactText("Операция одобрена Банком."));

    }



    @Test
    public void shouldNotSubmitRequestToBuyInCreditNoFields() {
        SelenideElement form = $("[method=post]");
        PurchaseType d = new PurchaseType();
        d.creditBuy();
        FillForm button = new FillForm();
        button.continueButton();
        form.$$("[class=input__sub]").get(0).shouldHave(text("Поле обязательно для заполнения"));
        form.$$("[class=input__sub]").get(1).shouldHave(text("Поле обязательно для заполнения"));
        form.$$("[class=input__sub]").get(2).shouldHave(text("Поле обязательно для заполнения"));
        form.$$("[class=input__sub]").get(3).shouldHave(text("Поле обязательно для заполнения"));
        form.$$("[class=input__sub]").get(4).shouldHave(text("Поле обязательно для заполнения"));
        $("[class=notification__title]").shouldNotBe(visible);

    }

    @Test
    public void shouldNotSubmitRequestToBuyInCreditErrors() {
        SelenideElement form = $("[method=post]");
        PurchaseType d = new PurchaseType();
        d.creditBuy();
        FillForm card = new FillForm();
        card.fillIncorrectData();

        form.$$("[class=input__sub]").get(0).shouldHave(text("Неверно указан срок действия карты"));
        form.$$("[class=input__sub]").get(1).shouldHave(text("Истёк срок действия карты"));

    }


    @Test
    void declinedDebitBuy() {
        var order = new FillForm();
        PurchaseType e = new PurchaseType();
        e.buy();
        FillForm card = new FillForm();
        card.fillIncorrectCard();


        order.verifySuccess();
        assertEquals("DECLINED", sqlGetters.getStatus(database));
    }

    @Test
    void successfulCreditBuy() {
        var order = new FillForm();
        PurchaseType e = new PurchaseType();
        e.creditBuy();
        FillForm card = new FillForm();
        card.fillCorrectForm();


        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));
    }

    @Test
    void declinedCreditBuy() {
        var order = new FillForm();
        PurchaseType e = new PurchaseType();
        e.creditBuy();
        FillForm card = new FillForm();
        card.fillIncorrectCard();


        order.verifyError();
        assertEquals("DECLINED", sqlGetters.getStatus(database));
    }
}
