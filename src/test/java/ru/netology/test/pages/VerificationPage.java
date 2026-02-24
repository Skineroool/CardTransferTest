package ru.netology.test.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeField = $("[data-test-id='code'] input");
    private SelenideElement verifyButton = $("[data-test-id='action-verify']");

    public DashboardPage validVerify(String code) {
        codeField.shouldBe(Condition.visible).setValue(code);
        verifyButton.shouldBe(Condition.enabled).click();
        return new DashboardPage();
    }
}