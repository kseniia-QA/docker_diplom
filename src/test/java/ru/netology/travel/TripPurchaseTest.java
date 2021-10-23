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
        order.autoDebitTour(1);
        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));
  
    }

    @Test
    public void shouldNotSubmitRequestToBuyWrongCardNumber() {
    
        PurchaseType notCredit  = new PurchaseType();
        notCredit .buy();
        order.autoDebitTour(2);
        order.verifyError();
        assertEquals("DECLINED", sqlGetters.getStatus(database));
    }
    

    @Test
    public void shouldNotSubmitRequestToBuyErrorsYearAndMonth() {
  
        PurchaseType notCredit  = new PurchaseType();
        notCredit .buy();
        order.manualDebitTour("abcdпрог!@#$)(*&", "01", "23", "HALVA CARD", "457");
        order.validateErrors(true, false, false, false, false);
    }

    


    @Test
    public void shouldNotSubmitIncorrectData() {
   
        PurchaseType notCredit  = new PurchaseType();
        notCredit .buy();
        order.manualDebitTour("4444444444444441", "", "23", "HALVA CARD", "457");
        order.validateErrors(false, true, false, false, false);
        order.manualDebitTour("", "01", "", "", "");
        order.validateErrors(false, false, false, false, false);
        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));
    }

    


    @Test
    public void shouldNotSubmitRequestToBuyNoFields() {
    
        PurchaseType notCredit  = new PurchaseType();
        notCredit .buy();
        order.manualDebitTour("", "", "", "", "");
        order.validateErrors(true, true, true, true, true);
    

    }


    @Test
    public void shouldSubmitRequestToBuyInCredit() {

      
        PurchaseType credit = new PurchaseType();
        credit.creditBuy();
        order.autoCreditTour(1);
        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));

    }
    

@Test
    public void shouldNotSubmitRequestToBuyInCreditWrongCardNumber() {
    
        PurchaseType notCredit  = new PurchaseType();
        notCredit .buy();
        order.autoCreditTour(2);
        order.verifyError();
        assertEquals("DECLINED", sqlGetters.getStatus(database));
    }

   
       
        
        @Test
    void wrongSymbolsMonth() {
        PurchaseType credit = new PurchaseType();
        credit.creditBuy();
        order.manualDebitTour("4444444444444441", "g%", "23", "HALVA CARD", "457");
        order.validateErrors(false, true, false, false, false);
    }

    
    
    @Test
    void zeroMonth() {
        PurchaseType credit = new PurchaseType();
        credit.creditBuy();
        order.manualDebitTour("4444444444444441", "00", "23", "HALVA CARD", "457");
        order.validateErrors(false, true, false, false, false);
    }

   

 @Test
    void validateYear() {
     PurchaseType credit = new PurchaseType();
        credit.creditBuy();
        order.manualDebitTour("4444444444444441", "01", "", "HALVA CARD", "457");
        order.validateErrors(false, false, true, false, false);
        order.manualDebitTour("", "", "22", "", "");
        order.validateErrors(false, false, false, false, false);
        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));
    }
   
 @Test
    void wrongSymbolsYear() {
         PurchaseType credit = new PurchaseType();
        credit.creditBuy();
        order.manualDebitTour("4444444444444441", "01", "h@", "HALVA CARD", "457");
        order.validateErrors(false, false, true, false, false);
    }

    @Test
    void validateOwner() {
       PurchaseType credit = new PurchaseType();
        credit.creditBuy();
        order.manualDebitTour("4444444444444441", "01", "23", "", "457");
        order.validateErrors(false, false, false, true, false);
        order.manualDebitTour("", "", "", "HALVA CARD", "");
        order.validateErrors(false, false, false, false, false);
        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));
    }

    @Test
    void wrongSymbolsOwner() {
       PurchaseType credit = new PurchaseType();
        credit.creditBuy();
        order.manualDebitTour("4444444444444441", "01", "23", "213$@#", "457");
        order.validateErrors(false, false, false, true, false);
    }

    @Test
    void validateCode() {
       PurchaseType credit = new PurchaseType();
        credit.creditBuy();
        order.manualDebitTour("4444444444444441", "01", "23", "HALVA CARD", "");
        order.validateErrors(false, false, false, false, true);
        order.manualDebitTour("", "", "", "", "457");
        order.validateErrors(false, false, false, false, false);
        order.verifySuccess();
        assertEquals("APPROVED", sqlGetters.getStatus(database));
    }
