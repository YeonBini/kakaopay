package com.kakaopay.housingfund.fund.controller;

import com.kakaopay.housingfund.fund.model.HousingFund;
import com.kakaopay.housingfund.fund.model.Institute;
import com.kakaopay.housingfund.fund.model.api.response.ApiResult;
import com.kakaopay.housingfund.fund.model.api.response.institute.InstituteFundResponse;
import com.kakaopay.housingfund.fund.model.api.response.institute.InstituteResponse;
import com.kakaopay.housingfund.fund.model.api.response.institute.MaxHousingFundInstituteResonse;
import com.kakaopay.housingfund.fund.service.FundService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.kakaopay.housingfund.fund.model.api.response.ApiResult.OK;
import static java.util.stream.Collectors.*;

@RestController
@RequestMapping("institute")
public class FundApiController {

    private final FundService fundService;

    public FundApiController(FundService fundService) {
        this.fundService = fundService;
    }

    // 주택금융 공급 금융기관(은행) 목록을 출력하는 API 를 개발하세요
    @GetMapping("list")
    public ApiResult findInstituteList() {
        final List<Institute> allInstitute = fundService.findAll();
        final List<InstituteResponse> allInstituteResponse = allInstitute.stream()
                .map(i -> new InstituteResponse(i.getInstituteCode(), i.getInstituteName(), i.getAttribute().getValue()))
                .collect(toList());
        return OK(allInstituteResponse);
    }

    // 년도별 각 금융기관의 지원금액 합계를 출력하는 API 를 개발하세요.
    @GetMapping("housing-fund/list")
    public ApiResult findInstituteByYear() {
        final List<Institute> allInstitute = fundService.findAllFetchJoin();
        final List<Detail> institutesGroupByYear = instituteGroupByYear(allInstitute);

        Map<String, List<Detail>> detailGroupByYear = detailGroupByYear(institutesGroupByYear);
        List<InstituteFundResponse> instituteFundResponseList = getInstituteFundResponses(detailGroupByYear);

        return OK(instituteFundResponseList);
    }

    // 각 년도별 각 기관의 전체 지원 금액 중에서 가장 큰 금액의 기관명을 출력하는 API 개발
    @GetMapping("housing-fund/{year}/max")
    public ApiResult findHousingFundMax(@PathVariable("year") String year) {
        final List<Institute> allInstitute = fundService.findAllFetchJoin();
        final List<Detail> institutesGroupByYear = instituteGroupByYear(allInstitute);
        final List<Detail> detailsByYear = detailGroupByYear(institutesGroupByYear).get(year);

        checkNotNull(detailsByYear, "Year does not match");
        final Detail maxInstitute = detailsByYear.stream().max(Comparator.comparingInt(Detail::getAmount)).get();

        final MaxHousingFundInstituteResonse max = new MaxHousingFundInstituteResonse(Integer.parseInt(maxInstitute.getYear()), maxInstitute.getInstituteName());

        return OK(max);
    }

    private List<InstituteFundResponse> getInstituteFundResponses(Map<String, List<Detail>> detailGroupByYear) {
        List<InstituteFundResponse> instituteFundResponseList = new ArrayList<>();
        for (String year : detailGroupByYear.keySet()) {
            int totalAmount = 0;
            Map<String, Integer> detailAmount = new HashMap<>();
            for (Detail d : detailGroupByYear.get(year)) {
                totalAmount += d.getAmount();

                detailAmount.put(d.getInstituteName(), d.amount);
            }
            instituteFundResponseList.add(new InstituteFundResponse(year, totalAmount, detailAmount));
        }
        Collections.sort(instituteFundResponseList, Comparator.comparing(InstituteFundResponse::getYear));
        return instituteFundResponseList;
    }

    private Map<String, List<Detail>> detailGroupByYear(List<Detail> institutesGroupByYear) {
        Map<String, List<Detail>> detailGroupByYear = new HashMap<>();
        for (Detail d : institutesGroupByYear) {
            String year = d.getYear();
            List<Detail> details;
            if (detailGroupByYear.containsKey(year)) {
                details = detailGroupByYear.get(year);
            } else {
                details = new ArrayList<>();
            }
            details.add(d);
            detailGroupByYear.put(year, details);
        }
        return detailGroupByYear;
    }

    private List<Detail> instituteGroupByYear(List<Institute> allInstitute) {
        return allInstitute.stream()
                    .map(institute -> {
                        final Map<String, Integer> groupByYear = institute.getHousingFunds().stream().collect(
                                groupingBy(HousingFund::getYear, summingInt(HousingFund::getAmount)));

                        final List<Detail> details = groupByYear.entrySet().stream()
                                .map(e -> new Detail(institute.getInstituteName(), e.getKey(), e.getValue()))
                                .collect(toList());

                        return details;
                    })
                    .flatMap(details -> details.stream())
                    .collect(toList());
    }

    private class Detail {
        private final String instituteName;
        private final String year;
        private final int amount;

        public Detail(String instituteName, String year, int amount) {
            this.instituteName = instituteName;
            this.year = year;
            this.amount = amount;
        }

        public String getInstituteName() {
            return instituteName;
        }

        public String getYear() {
            return year;
        }

        public int getAmount() {
            return amount;
        }

    }


}
