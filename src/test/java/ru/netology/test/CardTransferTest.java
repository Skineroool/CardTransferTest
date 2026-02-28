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
        Configuration.headless = false;
        Configuration.timeout = 15000;
        startSUT();
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

        int balance2 = dashboard.getCardBalance(masked2);

        int amount = balance2 + 1000;

        dashboard = dashboard
                .selectCard(masked1)
                .transfer(amount, card2);

        int finalBalance2 = dashboard.getCardBalance(masked2);

        assertTrue(finalBalance2 >= 0,
                "Баланс стал отрицательным! Приложение допускает уход в минус.");
    }

    private static void startSUT() {
        try {
            sutProcess = new ProcessBuilder(
                    "java",
                    "-jar",
                    "./artifacts/app-ibank-build-for-testers.jar"
            ).start();

            Thread.sleep(7000);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void stopSUT() {
        if (sutProcess != null && sutProcess.isAlive()) {
            sutProcess.destroy();
        }
    }
}