package ru.netology.test.pages;

import com.codeborne.selenide.*;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private ElementsCollection cards = $$(".list__item");

    public DashboardPage() {
        cards.shouldHave(CollectionCondition.size(2), Duration.ofSeconds(15));
    }

    public int getCardBalance(String maskedCardNumber) {
        SelenideElement card = cards.findBy(text(maskedCardNumber))
                .shouldBe(Condition.visible);

        String cardText = card.getText();

        Pattern pattern = Pattern.compile("баланс: (-?\\d+) р\\.");
        Matcher matcher = pattern.matcher(cardText);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        throw new RuntimeException("Баланс не найден: " + cardText);
    }

    public TransferPage selectCard(String maskedCardNumber) {
        cards.findBy(text(maskedCardNumber))
                .shouldBe(Condition.visible)
                .$(By.tagName("button"))
                .shouldBe(Condition.enabled)
                .click();

        return new TransferPage();
    }
}