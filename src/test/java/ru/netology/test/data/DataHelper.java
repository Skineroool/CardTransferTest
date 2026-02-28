package ru.netology.test.data;

public class DataHelper {

    private DataHelper() {}

    public static String getLogin() {
        return "vasya";
    }

    public static String getPassword() {
        return "qwerty123";
    }

    public static String getVerificationCode() {
        return "12345";
    }

    public static String getCard1() {
        return "5559000000000001";
    }

    public static String getCard2() {
        return "5559000000000002";
    }

    public static String getMaskedCard(String cardNumber) {
        return "**** **** **** " + cardNumber.substring(12);
    }

    // динамическая сумма (10% от баланса)
    public static int calculateTransferAmount(int balance) {
        return Math.max(balance / 10, 1);
    }
}