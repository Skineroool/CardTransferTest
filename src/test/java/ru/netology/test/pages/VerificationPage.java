package ru.netology.test.pages;

import com.codeborne.selenide.*;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {

    private SelenideElement codeField = $("[data-test-id='code'] input");
    private SelenideElement verifyButton = $("[data-test-id='action-verify']");

    public VerificationPage() {
        codeField.shouldBe(Condition.visible);
    }

    public DashboardPage verify(String code) {
        codeField.setValue(code);
        verifyButton.shouldBe(Condition.enabled).click();
        return new DashboardPage();
    }
}