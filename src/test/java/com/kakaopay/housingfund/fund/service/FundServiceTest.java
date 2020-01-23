package com.kakaopay.housingfund.fund.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kakaopay.housingfund.fund.model.BankAttribute;
import com.kakaopay.housingfund.fund.model.HousingFund;
import com.kakaopay.housingfund.fund.model.Institute;
import com.kakaopay.housingfund.fund.model.Unit;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FundServiceTest {

    @Autowired
    private FundService fundService;

    private List<Institute> institutes = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // given
        Institute institute1 = createInstitute("일반은행", "BNK-0001", new ArrayList<>());
        Institute institute2 = createInstitute("이반은행", "BNK-0002", new ArrayList<>());

        institute1.addHousingFund(createHousingFund("2020", "01", institute1, 1000));
        institute1.addHousingFund(createHousingFund("2020", "02", institute1, 990));
        institute2.addHousingFund(createHousingFund( "2020", "01", institute2, 2000));
        institute2.addHousingFund(createHousingFund( "2020", "02", institute2, 20));

        institutes.add(institute1);
        institutes.add(institute2);
    }

    @Test
    @DisplayName("금융 기관 단건 저장 테스트")
    @Order(0)
    void save() {
        // given
        final Institute institute = createInstitute("test", "test-001", new ArrayList<>());

        // when
        Institute savedInstitute = fundService.save(institute);

        // then
        assertEquals(institute, savedInstitute);
    }

    @Test
    @DisplayName("금융 기관 리스트 저장 테스트")
    @Rollback(false)
    @Order(1)
    void saveAll() throws JsonProcessingException {
        // when
        fundService.saveAll(institutes);

        // then
        final List<Institute> all = fundService.findAll();
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("주택 펀드 추가 테스트")
    @Order(2)
    void addHousingFund() {
        // given
        final Institute institute = fundService.findByInstituteCode("BNK-0001").get();
        final HousingFund housingFund = createHousingFund("2020", "03", institute, 50);

        // when
        fundService.addHousingFund(institute, housingFund);

        // then
        final Institute updatedInstitute = fundService.findByInstituteCode("BNK-0001").get();
        assertEquals(updatedInstitute.getHousingFunds().size(), 3);
    }

    @Test
    @DisplayName("기관 코드 조회 테스트")
    @Order(3)
    void findByInstituteCode() {
        // then
        final Institute institute = fundService.findByInstituteCode("BNK-0001").get();
        assertEquals(institute.getInstituteName(), "일반은행");
        assertEquals(institute.getInstituteCode(), "BNK-0001");
    }

    @Test
    @DisplayName("기관 이름 조회 테스트")
    @Order(4)
    void findByInstituteName() {
        // then
        final Institute institute = fundService.findByInstituteName("일반은행").get();
        assertEquals(institute.getInstituteName(), "일반은행");
        assertEquals(institute.getInstituteCode(), "BNK-0001");
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