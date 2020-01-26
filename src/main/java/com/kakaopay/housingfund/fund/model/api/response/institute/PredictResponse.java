package com.kakaopay.housingfund.fund.model.api.response.institute;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PredictResponse {
    private final String bank;
    private final int year;
    private final int month;
    private final int amount;

    public PredictResponse(String bank, int year, int month, int amount) {
        this.bank = bank;
        this.year = year;
        this.month = month;
        this.amount = amount;
    }

    public String getBank() {
        return bank;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("bank", bank)
                .append("year", year)
                .append("month", month)
                .append("amount", amount)
                .toString();
    }
}
