package ru.netology.travel;

import com.codeborne.selenide.Condition;
import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.*;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class FillForm {
 private SelenideElement heading = $(withText("Кредит по данным карты"));
    private SelenideElement cardNumber = $(withText("Номер карты")).parent().$(".input__control");
    private SelenideElement month = $(withText("Месяц")).parent().$(".input__control");
    private SelenideElement year = $(withText("Год")).parent().$(".input__control");
    private SelenideElement owner = $(withText("Владелец")).parent().$(".input__control");
    private SelenideElement codeCVV = $(withText("CVC/CVV")).parent().$(".input__control");
    private SelenideElement continueButton = $$("button").find(exactText("Продолжить"));
    private SelenideElement fieldError = $(".input_invalid .input__sub");
    private SelenideElement fieldErrorForCVV = $(withText("CVC/CVV")).parent().$(".input__sub");
    private SelenideElement errorMessage = $(".notification_status_error");
    private SelenideElement successMessage = $(".notification_status_ok");
    private SelenideElement closeMessage = $(".notification__closer");
    

    public void fillCorrectForm () {
        cardNumber.setValue(card.getCardNumber());
        month.setValue(card.getMonth());
        year.setValue(card.getYear());
        owner.setValue(card.getOwner());
        codeCVV.setValue(card.getCodeCVV());
        continueButton.click();
    }

     public void shouldGiveFieldErrorWhenIncorrectCardExpirationDate() {
        fieldError.shouldHave(Condition.text("Неверно указан срок действия карты"))
        .shouldBe(visible, Duration.ofMillis(15000));
    }
    
 public void shouldGiveFieldErrorWhenRequiredField() {
        fieldError.shouldHave(Condition.text("Поле обязательно для заполнения"))
         .shouldBe(visible, Duration.ofMillis(15000));
    }

    public void shouldGiveFieldErrorForCVVWhenRequiredField() {
        fieldErrorForCVV.shouldHave(Condition.text("Поле обязательно для заполнения"))
        .shouldBe(visible, Duration.ofMillis(15000));
    }

    public void shouldGiveFieldErrorWhenValueCannotBeLong() {
        fieldError.shouldHave(Condition.text("Значение поля не может быть длиннее 70 символов"))
       .shouldBe(visible, Duration.ofMillis(15000));
        
    }
    
    public void shouldGiveFieldErrorWhenNameIsNotInLatin() {
        fieldError.shouldHave(Condition.text("Значение поля может содержать только латинские буквы и дефис"))
                .shouldBe(visible, Duration.ofMillis(15000));
    
    
    }
 public void shouldGiveFieldErrorWhenIncorrectCVV() {
        fieldError.shouldHave(Condition.text("Неверно указан CVC/CVV"))
                .shouldBe(visible, Duration.ofMillis(15000));
    }

    public void shouldGiveFieldError() {
        fieldError.shouldBe(visible, Duration.ofMillis(15000));
    }

    public void shouldNotGiveFieldError() {
        fieldError.shouldNotBe(visible, Duration.ofMillis(15000));
    }

    public void shouldGiveErrorMessage() {
        errorMessage.shouldBe(visible, Duration.ofMillis(15000));
    }

    public void continueButton(){
        $(byText("Продолжить")).click();
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
