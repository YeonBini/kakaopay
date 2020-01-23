package com.kakaopay.housingfund.fund.controller;

import com.kakaopay.housingfund.fund.model.Institute;
import com.kakaopay.housingfund.fund.model.api.response.ApiResult;
import com.kakaopay.housingfund.fund.model.api.response.institute.InstituteResponse;
import com.kakaopay.housingfund.fund.service.FundService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.kakaopay.housingfund.fund.model.api.response.ApiResult.OK;
import static java.util.stream.Collectors.toList;

@RestController
public class FundApiController {

    private final FundService fundService;

    public FundApiController(FundService fundService) {
        this.fundService = fundService;
    }

    // 주택금융 공급 금융기관(은행) 목록을 출력하는 API 를 개발하세요
    @GetMapping("/institute/list")
    public ApiResult findInstituteList() {
        final List<Institute> allInstitute = fundService.findAll();
        final List<InstituteResponse> allInstituteResponse = allInstitute.stream()
                .map(i -> new InstituteResponse(i.getInstituteCode(), i.getInstituteName(), i.getAttribute().getValue()))
                .collect(toList());
        return OK(allInstituteResponse);
    }

    // 년도별 각 금융기관의 지원금액 합계를 출력하는 API 를 개발하세요.



}
