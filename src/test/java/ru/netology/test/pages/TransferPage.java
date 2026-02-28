package ru.netology.test.pages;

import com.codeborne.selenide.*;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private SelenideElement amountField = $("[data-test-id='amount'] input");
    private SelenideElement fromField = $("[data-test-id='from'] input");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");

    public TransferPage() {
        amountField.shouldBe(Condition.visible);
    }

    public DashboardPage transfer(int amount, String fromCard) {
        amountField.setValue(String.valueOf(amount));
        fromField.setValue(fromCard);
        transferButton.shouldBe(Condition.enabled).click();
        return new DashboardPage();
    }
}