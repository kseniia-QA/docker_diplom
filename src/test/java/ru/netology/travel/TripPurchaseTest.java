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
    public void shouldSubmitRequestToBuy() {
        PurchaseType notCredit = new PurchaseType();
        notCredit .buy();

        FillForm card = new FillForm();
        card.fillCorrectForm ();

        $("[class=notification__title]").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(exactText("Успешно"));
        $("[class=notification__content]").shouldBe(Condition.visible).shouldHave(exactText("Операция одобрена Банком."));

    }

    @Test
    public void shouldNotSubmitRequestToBuyWrongCardNumber() {
    
        PurchaseType notCredit  = new PurchaseType();
        notCredit .buy();
        FillForm card = new FillForm();
        card.fillIncorrectCard ();
        $("[class=notification__title]").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(exactText("Ошибка"));
    }

    @Test
    public void shouldNotSubmitRequestToBuyErrorsYearAndMonth() {
  
        PurchaseType notCredit  = new PurchaseType();
        notCredit .buy();
        FillForm card = new FillForm();
        card.fillIncorrectData ();
        form.$$("[class=input__sub]").get(0).shouldHave(text("Неверно указан срок действия карты"));
        form.$$("[class=input__sub]").get(1).shouldHave(text("Истёк срок действия карты"));

    }


    @Test
    public void shouldNotSubmitIncorrectData() {
   
        PurchaseType notCredit  = new PurchaseType();
        notCredit .buy();
        FillForm card = new FillForm();
        card.fillIncorrectData();
        $("[class=notification__title]").shouldNotBe(visible);

    }


    @Test
    public void shouldNotSubmitRequestToBuyNoFields() {
    
        PurchaseType notCredit  = new PurchaseType();
        notCredit .buy();
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

      
        PurchaseType credit = new PurchaseType();
        credit.creditBuy();
        FillForm card = new FillForm();
        card.fillIncorrectCard ();

        $("[class=notification__title]").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(exactText("Успешно"));
        $("[class=notification__content]").shouldBe(Condition.visible).shouldHave(exactText("Операция одобрена Банком."));

    }



    @Test
    public void shouldNotSubmitRequestToBuyInCreditNoFields() {
      
        PurchaseType credit = new PurchaseType();
        credit.creditBuy();
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
    public void shouldNotSubmitRequestToBuyInCreditErrorsYearAndMonth() {
  
        PurchaseType credit = new PurchaseType();
        credit.creditBuy();
        FillForm card = new FillForm();
        card.fillIncorrectData();

        form.$$("[class=input__sub]").get(0).shouldHave(text("Неверно указан срок действия карты"));
        form.$$("[class=input__sub]").get(1).shouldHave(text("Истёк срок действия карты"));

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
