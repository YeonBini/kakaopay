package com.kakaopay.housingfund.fund.model;

public enum BankAttribute {
    BANK("은행"), FUND("기금");

    private String value;

    BankAttribute(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
