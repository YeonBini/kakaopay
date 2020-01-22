package com.kakaopay.housingfund;

import com.kakaopay.housingfund.fund.model.BankAttribute;
import com.kakaopay.housingfund.fund.model.HousingFund;
import com.kakaopay.housingfund.fund.model.Institute;
import com.kakaopay.housingfund.fund.model.Unit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InstituteTest {

    private Institute institute;

    @BeforeEach
    void setUp() {
        // given
        institute = createInstitute("일반은행", "BNK-0001", new ArrayList<>());

        List<HousingFund> housingFunds = new ArrayList<>();
        housingFunds.add(createHousingFund("2020", "01",institute, 1000));
        housingFunds.add(createHousingFund("2020", "02",institute, 990));

        institute = createInstitute("일반은행", "BNK-0001", housingFunds);
    }

    @Test
    @DisplayName("주택 금융 - 기관별 지원 금액 중 가장 큰 값을 리턴")
    void maxAmount_test() {
        // when
        final HousingFund housingFund = institute.maxAmount();

        // then
        assertEquals(housingFund.getAmount(), 1000);
    }

    @Test
    @DisplayName("주택 금융 - 기관별 지원 금액 중 가장 작ㅇ 값을 리턴")
    void minAmount_test() {
        // when
        final HousingFund housingFund = institute.minAmount();

        // then
        assertEquals(housingFund.getAmount(), 990);
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
