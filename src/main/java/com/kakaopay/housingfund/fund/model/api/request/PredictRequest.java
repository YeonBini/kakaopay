package com.kakaopay.housingfund.fund.model.api.request;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PredictRequest {
    private final String bank;
    private final String month;

    public PredictRequest(String bank, String month) {
        this.bank = bank;
        this.month = month;
    }

    public String getBank() {
        return bank;
    }

    public String getMonth() {
        return month;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("bank", bank)
                .append("month", month)
                .toString();
    }
}
