package com.kakaopay.housingfund.fund.model.api.request;

public class InstituteSaveRequest {
    private String bank;

    private String year;

    private String month;

    private int amount;

    public InstituteSaveRequest(String bank, String year, String month, int amount) {
        this.bank = bank;
        this.year = year;
        this.month = month;
        this.amount = amount;
    }

    public String getBank() {
        return bank;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public int getAmount() {
        return amount;
    }
}
