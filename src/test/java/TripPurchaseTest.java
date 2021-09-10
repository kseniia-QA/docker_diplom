
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;


public class TripPurchaseTest {

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
    }


    @Test
    public void shouldSubmitRequestToBuy() {
        SelenideElement form = $("[method=post]");
        $(byText("Купить")).click();
        form.$("[class=input__box] [placeholder='0000 0000 0000 0000']").setValue("4444 4444 4444 4441");
        form.$("[class=input__box] [placeholder='08']").setValue("10");
        form.$("[class=input__box] [placeholder='22']").setValue("23");
        form.$$("[class=input__control]").get(3).setValue("Anna");
        form.$("[class=input__box] [placeholder='999']").setValue("123");
        $(byText("Продолжить")).click();
        $("[class=notification__title]").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(exactText("Успешно"));
        $("[class=notification__content]").shouldBe(Condition.visible).shouldHave(exactText("Операция одобрена Банком."));

    }


    @Test
    public void shouldNotSubmitRequestToBuyWrongCardNumber() {
        SelenideElement form = $("[method=post]");
        $(byText("Купить")).click();
        form.$("[class=input__box] [placeholder='0000 0000 0000 0000']").setValue("4444 4444 4444 4442");
        form.$("[class=input__box] [placeholder='08']").setValue("10");
        form.$("[class=input__box] [placeholder='22']").setValue("23");
        form.$$("[class=input__control]").get(3).setValue("Anna");
        form.$("[class=input__box] [placeholder='999']").setValue("123");
        $(byText("Продолжить")).click();
        $("[class=notification__title]").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(exactText("Ошибка"));
    }

    @Test
    public void shouldNotSubmitRequestToBuyErrors() {
        SelenideElement form = $("[method=post]");
        $(byText("Купить")).click();
        form.$("[class=input__box] [placeholder='0000 0000 0000 0000']").setValue("4444 4444 4444 4441");
        form.$("[class=input__box] [placeholder='08']").setValue("90");
        form.$("[class=input__box] [placeholder='22']").setValue("10");
        form.$$("[class=input__control]").get(3).setValue("Anna");
        form.$("[class=input__box] [placeholder='999']").setValue("123");
        $(byText("Продолжить")).click();
        form.$$("[class=input__sub]").get(0).shouldHave(text("Неверно указан срок действия карты"));
        form.$$("[class=input__sub]").get(1).shouldHave(text("Истёк срок действия карты"));

    }


    @Test
    public void shouldNotSubmitRequestToBuyNoFields() {
        SelenideElement form = $("[method=post]");
        $(byText("Купить")).click();

        $(byText("Продолжить")).click();
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
        $(byText("Купить в кредит")).click();
        form.$("[class=input__box] [placeholder='0000 0000 0000 0000']").setValue("4444 4444 4444 4441");
        form.$("[class=input__box] [placeholder='08']").setValue("10");
        form.$("[class=input__box] [placeholder='22']").setValue("23");
        form.$$("[class=input__control]").get(3).setValue("Anna");
        form.$("[class=input__box] [placeholder='999']").setValue("123");
        $(byText("Продолжить")).click();
        $("[class=notification__title]").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(exactText("Успешно"));
        $("[class=notification__content]").shouldBe(Condition.visible).shouldHave(exactText("Операция одобрена Банком."));

    }

    @Test
    public void shouldNotSubmitRequestToBuyInCreditWrongCard() {
        SelenideElement form = $("[method=post]");
        $(byText("Купить в кредит")).click();
        form.$("[class=input__box] [placeholder='0000 0000 0000 0000']").setValue("4444 4444 4444 4442");
        form.$("[class=input__box] [placeholder='08']").setValue("10");
        form.$("[class=input__box] [placeholder='22']").setValue("23");
        form.$$("[class=input__control]").get(3).setValue("Anna");
        form.$("[class=input__box] [placeholder='999']").setValue("123");
        $(byText("Продолжить")).click();
        $("[class=notification__title]").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(exactText("Ошибка"));
    }

    @Test
    public void shouldNotSubmitRequestToBuyInCreditNoFields() {
        SelenideElement form = $("[method=post]");
        $(byText("Купить")).click();

        $(byText("Продолжить")).click();
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
        $(byText("Купить")).click();
        form.$("[class=input__box] [placeholder='0000 0000 0000 0000']").setValue("4444 4444 4444 4441");
        form.$("[class=input__box] [placeholder='08']").setValue("90");
        form.$("[class=input__box] [placeholder='22']").setValue("10");
        form.$$("[class=input__control]").get(3).setValue("Anna");
        form.$("[class=input__box] [placeholder='999']").setValue("123");
        $(byText("Продолжить")).click();
        form.$$("[class=input__sub]").get(0).shouldHave(text("Неверно указан срок действия карты"));
        form.$$("[class=input__sub]").get(1).shouldHave(text("Истёк срок действия карты"));

    }
}