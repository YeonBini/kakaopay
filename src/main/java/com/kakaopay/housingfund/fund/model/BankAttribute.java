package com.kakaopay.housingfund.fund.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public enum BankAttribute {
    BANK("은행"), FUND("기금");

    private String value;

    BankAttribute(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("value", value)
                .toString();
    }
}
