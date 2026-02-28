package ru.netology.test.pages;

import com.codeborne.selenide.*;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private SelenideElement loginField = $("[data-test-id='login'] input");
    private SelenideElement passwordField = $("[data-test-id='password'] input");
    private SelenideElement loginButton = $("[data-test-id='action-login']");

    public VerificationPage login(String login, String password) {
        loginField.shouldBe(Condition.visible).setValue(login);
        passwordField.setValue(password);
        loginButton.shouldBe(Condition.enabled).click();
        return new VerificationPage();
    }
}