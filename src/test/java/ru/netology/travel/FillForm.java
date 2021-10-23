package ru.netology.travel;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.*;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class AppPage {
    private SelenideElement buyButton = $("[class=button__content]");
    private SelenideElement creditBuyButton = $$("[class=button__content]").get(1);
    private SelenideElement continueButton = $$("[class=button__content]").get(2);
    private SelenideElement cardNumber = $("[class=input__box] [placeholder='0000 0000 0000 0000']");
    private SelenideElement month = $("[class=input__box] [placeholder='08']");
    private SelenideElement year = $("[class=input__box] [placeholder='22']");
    private SelenideElement owner = $(By.xpath("//span[contains(text(), 'Владелец')]/following::input"));
    private SelenideElement code = $("[class=input__box] [placeholder='999']");
    private SelenideElement errorCardNumber = $(By.xpath("//span[@class='input__sub' and preceding-sibling::span[contains(text(), 'Номер карты')]]"));
    private SelenideElement errorMonth = $(By.xpath("//span[@class='input__sub' and preceding-sibling::span[contains(text(), 'Месяц')]]"));
    private SelenideElement errorYear = $(By.xpath("//span[@class='input__sub' and preceding-sibling::span[contains(text(), 'Год')]]"));
    private SelenideElement errorOwner = $(By.xpath("//span[@class='input__sub' and preceding-sibling::span[contains(text(), 'Владелец')]]"));
    private SelenideElement errorCode = $(By.xpath("//span[@class='input__sub' and preceding-sibling::span[contains(text(), 'CVC/CVV')]]"));

    private void autoFillForm(int number) {
        Random random = new Random();
        Faker faker = new Faker(new Locale("en"));
        switch (number) {
            case 1:
                cardNumber.setValue("4444 4444 4444 4441");
            case 2:
                cardNumber.setValue("4444 4444 4444 4442");
            default:
                cardNumber.setValue(faker.finance().creditCard());
        }
        month.setValue(String.format("%02d", random.nextInt(11) + 1));
        year.setValue(String.valueOf(random.nextInt(4) + 22));
        owner.setValue(faker.name().fullName());
        code.setValue(String.format("%03d", random.nextInt(999) + 1));
        continueButton.click();
    }

    private void manualFillForm(String inputCardNumber, String inputMonth, String inputYear, String inputOwner, String inputCode) {
        cardNumber.setValue(inputCardNumber);
        month.setValue(inputMonth);
        year.setValue(inputYear);
        owner.setValue(inputOwner);
        code.setValue(inputCode);
        continueButton.click();
    }

    public void autoDebitTour(int number) {
        buyButton.click();
        autoFillForm(number);
        SqlGetters.setCredit(false);
    }

    public void autoCreditTour(int number) {
        creditBuyButton.click();
        autoFillForm(number);
        SqlGetters.setCredit(true);
    }

    public void manualDebitTour(String inputCardNumber, String inputMonth, String inputYear, String inputOwner, String inputCode) {
        buyButton.click();
        manualFillForm(inputCardNumber, inputMonth, inputYear, inputOwner, inputCode);
        SqlGetters.setCredit(false);
    }

    public void manualCreditTour(String inputCardNumber, String inputMonth, String inputYear, String inputOwner, String inputCode) {
        creditBuyButton.click();
        manualFillForm(inputCardNumber, inputMonth, inputYear, inputOwner, inputCode);
        SqlGetters.setCredit(true);
    }

    public void validateErrors(Boolean cardNumberValidate, Boolean monthValidate, Boolean yearValidate, Boolean ownerValidate, Boolean codeValidate) {
        if (cardNumberValidate) {
            errorCardNumber.shouldBe(visible);
        } else {
            errorCardNumber.shouldNotBe(visible);
        }
        if (monthValidate) {
            errorMonth.shouldBe(visible);
        } else {
            errorMonth.shouldNotBe(visible);
        }
        if (yearValidate) {
            errorYear.shouldBe(visible);
        } else {
            errorYear.shouldNotBe(visible);
        }
        if (ownerValidate) {
            errorOwner.shouldBe(visible);
        } else {
            errorOwner.shouldNotBe(visible);
        }
        if (codeValidate) {
            errorCode.shouldBe(visible);
        } else {
            errorCode.shouldNotBe(visible);
        }
    }

    public void verifySuccess() {
        $("[class=notification__title]").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(exactText("Успешно"));
        $("[class=notification__content]").shouldBe(Condition.visible).shouldHave(exactText("Операция одобрена Банком."));
    }

    public void verifyError() {
        $$("[class=notification__title]").get(1).shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(exactText("Ошибка"));
        $$("[class=notification__content]").get(1).shouldBe(Condition.visible).shouldHave(exactText("Ошибка! Банк отказал в проведении операции."));
    }
}
