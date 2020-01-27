package com.kakaopay.housingfund.fund.controller;

import com.kakaopay.housingfund.fund.model.api.response.ApiResult;
import com.kakaopay.housingfund.fund.model.api.response.institute.ForeignBankAvg;
import com.kakaopay.housingfund.fund.service.FundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "외환은행 Api")
public class ForeignBankController implements BankApiControllerInterface {

    private final FundService fundService;

    public ForeignBankController(FundService fundService) {
        this.fundService = fundService;
    }

    @ApiOperation("외환은행 지원금액 최대, 최소")
    @Override
    @GetMapping("/institute/foreign-bank/min-max")
    public ApiResult findAvgMinMax() {
        final List<Map<String, Integer>> supportAmount = fundService.findByInstituteMinMaxAvg("외환은행");
        ForeignBankAvg foreignBankAvg = new ForeignBankAvg("외환은행", supportAmount);

        return ApiResult.OK(foreignBankAvg);
    }


}
