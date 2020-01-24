package com.kakaopay.housingfund.fund.model.api.response.institute;

import java.util.Map;

public class InstituteFundResponse {
    private final String year;
    private final int totalAmount;
    private final Map<String, Integer> detailAmount;

    public InstituteFundResponse(String year, int totalAmount, Map<String, Integer> detailAmount) {
        this.year = year;
        this.totalAmount = totalAmount;
        this.detailAmount = detailAmount;
    }

    public String getYear() {
        return year;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public Map<String, Integer> getDetailAmount() {
        return detailAmount;
    }
}
