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


    public void fillName() {
        $$("[class=input__control]").get(3).setValue("Anna");
    }

    public void fillWrongCard() {
        $("[class=input__box] [placeholder='0000 0000 0000 0000']").setValue("4444 4444 4444 4442");
    }

    public void fillCard() {
        $("[class=input__box] [placeholder='0000 0000 0000 0000']").setValue("4444 4444 4444 4441");
    }


    public void fillDate() {
        $("[class=input__box] [placeholder='22']").setValue("23");
    }

    public void fillMonth() {
        $("[class=input__box] [placeholder='08']").setValue("10");
    }

    public void fillCVC() {
        $("[class=input__box] [placeholder='999']").setValue("123");
    }

    public void fillWrongDate() {
        $("[class=input__box] [placeholder='08']").setValue("90");
    }

    public void fillWrongMonth() {
        $("[class=input__box] [placeholder='22']").setValue("10");
    }

    public void continueButton() {
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
