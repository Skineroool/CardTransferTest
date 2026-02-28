package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import ru.netology.test.data.DataHelper;
import ru.netology.test.pages.*;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardTransferTest {

    private static Process sutProcess;
    private DashboardPage dashboard;

    @BeforeAll
    static void setupAll() {
        startSUT();
    }

    private static void startSUT() {
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");

        var loginPage = new LoginPage();
        var verificationPage = loginPage.login(
                DataHelper.getLogin(),
                DataHelper.getPassword()
        );

        dashboard = verificationPage.verify(
                DataHelper.getVerificationCode()
        );
    }

    @AfterAll
    static void tearDownAll() {
        stopSUT();
        closeWebDriver();
    }

    private static void stopSUT() {
    }


    @Test
    void shouldTransferFromCard2ToCard1() {

        String card1 = DataHelper.getCard1();
        String card2 = DataHelper.getCard2();

        String masked1 = DataHelper.getMaskedCard(card1);
        String masked2 = DataHelper.getMaskedCard(card2);

        int balance1 = dashboard.getCardBalance(masked1);
        int balance2 = dashboard.getCardBalance(masked2);

        int amount = DataHelper.calculateTransferAmount(balance2);

        dashboard = dashboard
                .selectCard(masked1)
                .transfer(amount, card2);

        assertEquals(balance1 + amount,
                dashboard.getCardBalance(masked1));

        assertEquals(balance2 - amount,
                dashboard.getCardBalance(masked2));
    }


    @Test
    void shouldTransferFromCard1ToCard2() {

        String card1 = DataHelper.getCard1();
        String card2 = DataHelper.getCard2();

        String masked1 = DataHelper.getMaskedCard(card1);
        String masked2 = DataHelper.getMaskedCard(card2);

        int balance1 = dashboard.getCardBalance(masked1);
        int balance2 = dashboard.getCardBalance(masked2);

        int amount = DataHelper.calculateTransferAmount(balance1);

        dashboard = dashboard
                .selectCard(masked2)
                .transfer(amount, card1);

        assertEquals(balance1 - amount,
                dashboard.getCardBalance(masked1));

        assertEquals(balance2 + amount,
                dashboard.getCardBalance(masked2));
    }


    @Test
    void shouldNotAllowNegativeBalance() {


        String card1 = DataHelper.getCard1();
        String card2 = DataHelper.getCard2();

        String masked1 = DataHelper.getMaskedCard(card1);
        String masked2 = DataHelper.getMaskedCard(card2);


        int balance1Before = dashboard.getCardBalance(masked1);
        int balance2Before = dashboard.getCardBalance(masked2);

        int amount = balance2Before + 1000;


        dashboard = dashboard
                .selectCard(masked1)
                .transfer(amount, card2);

        int balance1After = dashboard.getCardBalance(masked1);
        int balance2After = dashboard.getCardBalance(masked2);

        assertEquals(balance1Before, balance1After,
                "Баланс первой карты изменился! Операция с недопустимой суммой обработана.");
        assertEquals(balance2Before, balance2After,
                "Баланс второй карты изменился! Операция с недопустимой суммой обработана.");
    }

    
}