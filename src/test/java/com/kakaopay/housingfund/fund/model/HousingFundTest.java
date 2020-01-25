package com.kakaopay.housingfund.fund.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HousingFundTest {

    @Test
    void updateHousingFund() {
        // given
        Institute institute = createInstitute("일반은행", "BNK-0001", new ArrayList<>());
        final HousingFund housingFund = createHousingFund("2020", "01", institute, 1000);
        institute.addHousingFund(housingFund);

        // when
        housingFund.updateHousingFund(2000);

        // then
        assertEquals(2000, housingFund.getAmount());
    }

    private HousingFund createHousingFund(String year, String month, Institute institute, int amount) {
        HousingFund housingFund = new HousingFund.Builder()
                .year(year)
                .month(month)
                .institute(institute)
                .amount(amount)
                .unit(Unit.HUNDRED_MILLION)
                .build();
        return housingFund;
    }

    private Institute createInstitute(String instituteName, String instituteCode, List<HousingFund> housingFunds) {
        return new Institute.Builder()
                .instituteName(instituteName)
                .instituteCode(instituteCode)
                .attribute(BankAttribute.BANK)
                .housingFunds(housingFunds)
                .build();
    }
}