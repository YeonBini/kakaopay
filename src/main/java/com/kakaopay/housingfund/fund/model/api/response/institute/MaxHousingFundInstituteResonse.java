package com.kakaopay.housingfund.fund.model.api.response.institute;

public class MaxHousingFundInstituteResonse {
    private int year;
    private String bank;

    public MaxHousingFundInstituteResonse(int year, String bank) {
        this.year = year;
        this.bank = bank;
    }

    public int getYear() {
        return year;
    }

    public String getBank() {
        return bank;
    }
}
