package ru.netology.travel;

import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.*;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class PurchaseType {



//    private WebDriver driver;

   public void buy() {
        $(byText("Купить")).click();
    }

   public void creditBuy() {
        $(byText("Купить в кредит")).click();
    }


    }


