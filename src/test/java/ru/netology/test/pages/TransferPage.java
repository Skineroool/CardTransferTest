package ru.netology.test.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amountField = $("[data-test-id='amount'] input");
    private SelenideElement fromField = $("[data-test-id='from'] input");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");

    public DashboardPage validTransfer(int amount, String fromCard) {
        amountField.shouldBe(Condition.visible, Duration.ofSeconds(10)).setValue(String.valueOf(amount));
        fromField.shouldBe(Condition.visible, Duration.ofSeconds(10)).setValue(fromCard);
        transferButton.shouldBe(Condition.enabled, Duration.ofSeconds(10)).click();
        return new DashboardPage();
    }

    public boolean isAmountFieldVisible() {
        try {
            return amountField.shouldBe(Condition.visible, Duration.ofSeconds(5)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}