package com.kakaopay.housingfund.fund.model.api.response.institute;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ForeignBankAvg<T> {
    private String bank;

    private List<T> supportAmount;

    public ForeignBankAvg(String bank, List<T> supportAmount) {
        this.bank = bank;
        this.supportAmount = supportAmount;
    }

    public boolean addSupportAmount(T supportAmount) {
        return this.supportAmount.add(supportAmount);
    }

    public String getBank() {
        return bank;
    }

    @JsonProperty("support_amount")
    public List<T> getSupportAmount() {
        return supportAmount;
    }
}
