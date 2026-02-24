package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.test.pages.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardTransferTest {
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private static Process sutProcess;

    private static final String LOGIN = "vasya";
    private static final String PASSWORD = "qwerty123";
    private static final String VERIFICATION_CODE = "12345";
    private static final String CARD1 = "5559 0000 0000 0001";
    private static final String CARD2 = "5559 0000 0000 0002";

    @BeforeAll
    static void setUpAll() {
        Configuration.headless = false;
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 15000;

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-save-password-bubble");

        Configuration.browserCapabilities = options;

        startSUT();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
        var verificationPage = loginPage.validLogin(LOGIN, PASSWORD);
        dashboardPage = verificationPage.validVerify(VERIFICATION_CODE);
    }

    @AfterAll
    static void tearDownAll() {
        stopSUT();
        closeWebDriver();
    }

    private static void startSUT() {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "java",
                    "-jar",
                    "./artifacts/app-ibank-build-for-testers.jar"
            );
            sutProcess = pb.start();
            Thread.sleep(8000);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to start SUT", e);
        }
    }

    private static void stopSUT() {
        if (sutProcess != null && sutProcess.isAlive()) {
            sutProcess.destroy();
            try {
                sutProcess.waitFor(5000, java.util.concurrent.TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                sutProcess.destroyForcibly();
            }
        }
    }

    /**
     * Перевод 100 рублей с карты 2 на карту 1
     */
    @Test
    void shouldTransfer100FromCard2ToCard1() {
        int initialBalance1 = dashboardPage.getCardBalance(CARD1);
        int initialBalance2 = dashboardPage.getCardBalance(CARD2);
        int transferAmount = 100;

        var transferPage = dashboardPage.selectCardToTransfer(CARD1);
        dashboardPage = transferPage.validTransfer(transferAmount, CARD2);

        int finalBalance1 = dashboardPage.getCardBalance(CARD1);
        int finalBalance2 = dashboardPage.getCardBalance(CARD2);

        assertEquals(initialBalance1 + transferAmount, finalBalance1);
        assertEquals(initialBalance2 - transferAmount, finalBalance2);
    }

    /**
     * Перевод 500 рублей с карты 1 на карту 2
     */
    @Test
    void shouldTransfer500FromCard1ToCard2() {
        int initialBalance1 = dashboardPage.getCardBalance(CARD1);
        int initialBalance2 = dashboardPage.getCardBalance(CARD2);
        int transferAmount = 500;

        var transferPage = dashboardPage.selectCardToTransfer(CARD2);
        dashboardPage = transferPage.validTransfer(transferAmount, CARD1);

        int finalBalance1 = dashboardPage.getCardBalance(CARD1);
        int finalBalance2 = dashboardPage.getCardBalance(CARD2);

        assertEquals(initialBalance1 - transferAmount, finalBalance1);
        assertEquals(initialBalance2 + transferAmount, finalBalance2);
    }

    /**
     * Перевод 1000 рублей с карты 2 на карту 1
     */
    @Test
    void shouldTransfer1000FromCard2ToCard1() {
        int initialBalance1 = dashboardPage.getCardBalance(CARD1);
        int initialBalance2 = dashboardPage.getCardBalance(CARD2);
        int transferAmount = 1000;

        var transferPage = dashboardPage.selectCardToTransfer(CARD1);
        dashboardPage = transferPage.validTransfer(transferAmount, CARD2);

        int finalBalance1 = dashboardPage.getCardBalance(CARD1);
        int finalBalance2 = dashboardPage.getCardBalance(CARD2);

        assertEquals(initialBalance1 + transferAmount, finalBalance1);
        assertEquals(initialBalance2 - transferAmount, finalBalance2);
        assertTrue(finalBalance1 >= 0);
        assertTrue(finalBalance2 >= 0);
    }
}