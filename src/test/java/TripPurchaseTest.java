
import com.codeborne.selenide.SelenideElement;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import static com.codeborne.selenide.Condition.*;
        import static com.codeborne.selenide.Selenide.*;


public class TripPurchaseTest {

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
    }

    @Test
    public void shouldSubmitRequest() {
        SelenideElement form = $("[method=post]");
        form.$("[name=user]").setValue("");


    }
}