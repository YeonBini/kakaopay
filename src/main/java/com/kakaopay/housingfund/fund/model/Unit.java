package com.kakaopay.housingfund.fund.model;

public enum Unit {
    HUNDRED_MILLION("억원", 100000000L),
    MILLION("백만원", 1000000L),
    TEN_THOUSAND("만", 10000L);

    private String krUnit;

    private Long krAmount;

    Unit(String krUnit, Long krAmount) {
        this.krUnit = krUnit;
        this.krAmount = krAmount;
    }

    public String getKrUnit() {
        return krUnit;
    }

    public Long getKrAmount() {
        return krAmount;
    }
}
