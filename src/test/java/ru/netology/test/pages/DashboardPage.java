package ru.netology.test.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceEnd = " р.";

    public DashboardPage() {
        // Увеличено время ожидания и добавлена проверка видимости
        cards.shouldHave(CollectionCondition.size(2), Duration.ofSeconds(15));
    }

    public int getCardBalance(String cardNumber) {
        String cardNumberMask = "**** **** **** " + cardNumber.substring(15, 19);
        SelenideElement card = cards.findBy(text(cardNumberMask));
        card.shouldBe(Condition.visible, Duration.ofSeconds(10));
        String cardText = card.text();

        String balancePattern = balanceStart + "(\\d+)" + balanceEnd;
        Pattern pattern = Pattern.compile(balancePattern);
        Matcher matcher = pattern.matcher(cardText);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            // Если не нашли с плюсом, пробуем найти с минусом (для бага)
            String negativeBalancePattern = balanceStart + "-(\\d+)" + balanceEnd;
            Pattern negativePattern = Pattern.compile(negativeBalancePattern);
            Matcher negativeMatcher = negativePattern.matcher(cardText);
            if (negativeMatcher.find()) {
                return -Integer.parseInt(negativeMatcher.group(1));
            }
            throw new RuntimeException("Не удалось найти баланс в тексте: " + cardText);
        }
    }

    public TransferPage selectCardToTransfer(String cardNumber) {
        String cardNumberMask = "**** **** **** " + cardNumber.substring(15, 19);
        SelenideElement card = cards.findBy(text(cardNumberMask));
        card.shouldBe(Condition.visible, Duration.ofSeconds(10))
                .$(By.xpath(".//button"))
                .shouldBe(Condition.enabled, Duration.ofSeconds(10))
                .click();
        return new TransferPage();
    }
}