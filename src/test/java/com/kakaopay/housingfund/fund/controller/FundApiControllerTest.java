package com.kakaopay.housingfund.fund.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.housingfund.fund.model.BankAttribute;
import com.kakaopay.housingfund.fund.model.HousingFund;
import com.kakaopay.housingfund.fund.model.Institute;
import com.kakaopay.housingfund.fund.model.Unit;
import com.kakaopay.housingfund.fund.model.api.request.PredictRequest;
import com.kakaopay.housingfund.fund.service.FundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FundApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FundService fundService;

    private List<Institute> institutes = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Institute institute1;
        Institute institute2;
        institute1 = new Institute.Builder()
                .instituteCode("123")
                .instituteName("test")
                .attribute(BankAttribute.BANK)
                .housingFunds(new ArrayList<>())
                .build();
        institute2 = new Institute.Builder()
                .instituteCode("456")
                .instituteName("test2")
                .attribute(BankAttribute.BANK)
                .housingFunds(new ArrayList<>())
                .build();

        institute1.addHousingFund(new HousingFund.Builder()
                .year("2020")
                .month("1")
                .amount(100)
                .unit(Unit.HUNDRED_MILLION)
                .institute(institute1)
                .build());
        institute1.addHousingFund(new HousingFund.Builder()
                .year("2020")
                .month("2")
                .amount(1000)
                .unit(Unit.HUNDRED_MILLION)
                .institute(institute1)
                .build());
        institute1.addHousingFund(new HousingFund.Builder()
                .year("2019")
                .month("1")
                .amount(110)
                .unit(Unit.HUNDRED_MILLION)
                .institute(institute1)
                .build());

        institute2.addHousingFund(new HousingFund.Builder()
                .year("2020")
                .month("1")
                .amount(200)
                .unit(Unit.HUNDRED_MILLION)
                .institute(institute1)
                .build());
        institute2.addHousingFund(new HousingFund.Builder()
                .year("2020")
                .month("2")
                .amount(2000)
                .unit(Unit.HUNDRED_MILLION)
                .institute(institute1)
                .build());

        institutes.add(institute1);
        institutes.add(institute2);
    }

    @Test
    @DisplayName("주택금융기관 리스트")
    void findInstituteList() throws Exception {
        // when
        when(fundService.findAll()).thenReturn(institutes);

        // then
        mockMvc.perform(get("/institute/list").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response[0].instituteCode").value("123"))
                .andDo(print());

    }

    @Test
    @DisplayName("연도별 금융기관 지원금액 합계")
    void findInstituteByYear() throws Exception {
        // when
        when(fundService.findAllFetchJoin()).thenReturn(institutes);

        // then
        mockMvc.perform(get("/institute/housing-fund/list").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response[1].totalAmount").value("3300"))
                .andDo(print());
    }

    @Test
    @DisplayName("해당 연도 최대 금액을 지원한 금융기관")
    void findHousingFundMax() throws Exception {
        // when
        when(fundService.findAllFetchJoin()).thenReturn(institutes);

        // then
        mockMvc.perform(get("/institute/housing-fund/2020/max").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response.bank").value("test2"))
                .andDo(print());
    }


    @Test
    @DisplayName("2018년의 은행 별 주택금융 투자금액 예측")
    void predictFund() throws Exception {
        // given
        Institute institute = institutes.get(0);

        // when
        when(fundService.findByInstituteNameFetchJoin(any())).thenReturn(Optional.of(institute));

        // then
        mockMvc.perform(get("/institute/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .param("bank", "test")
                .param("month", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response.bank").isNotEmpty())
                .andDo(print());
    }
}