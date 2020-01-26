package com.kakaopay.housingfund.fund.controller;

import com.kakaopay.housingfund.fund.model.HousingFund;
import com.kakaopay.housingfund.fund.model.Institute;
import com.kakaopay.housingfund.fund.model.api.request.InstituteSaveRequest;
import com.kakaopay.housingfund.fund.model.api.request.PredictRequest;
import com.kakaopay.housingfund.fund.model.api.response.ApiResult;
import com.kakaopay.housingfund.fund.model.api.response.institute.InstituteFundResponse;
import com.kakaopay.housingfund.fund.model.api.response.institute.InstituteResponse;
import com.kakaopay.housingfund.fund.model.api.response.institute.MaxHousingFundInstituteResonse;
import com.kakaopay.housingfund.fund.model.api.response.institute.PredictResponse;
import com.kakaopay.housingfund.fund.service.FundService;
import com.kakaopay.housingfund.util.predict.LinearRegressionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.kakaopay.housingfund.fund.model.api.response.ApiResult.OK;
import static java.util.stream.Collectors.*;

@RestController
@RequestMapping("institute")
public class FundApiController {

    private Logger logger = LoggerFactory.getLogger(FundApiController.class);

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
        logger.info("[findHousingFundMax] year = " + year);
        final List<Institute> allInstitute = fundService.findAllFetchJoin();
        final List<Detail> institutesGroupByYear = instituteGroupByYear(allInstitute);
        final List<Detail> detailsByYear = detailGroupByYear(institutesGroupByYear).get(year);

        checkNotNull(detailsByYear, "Year does not match");
        final Detail maxInstitute = detailsByYear.stream().max(Comparator.comparingInt(Detail::getAmount)).get();

        final MaxHousingFundInstituteResonse max = new MaxHousingFundInstituteResonse(Integer.parseInt(maxInstitute.getYear()), maxInstitute.getInstituteName());

        return OK(max);
    }

    // 기관을 추가하는 컨트롤러?

    // 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API 개발
    // -> 입력 요청 리스트를 받아서 저장하는 로직 작성
    @PostMapping("housing-fund/save")
    public ApiResult saveInstituteInfo(@RequestBody List<InstituteSaveRequest> saveRequest) {
        saveRequest.stream().forEach(
                request -> {
                    logger.info("[saveInstituteInfo] " +request.toString());
                    fundService.updateHousingFund(request.getBank(), request.getYear(), request.getMonth(), request.getAmount());
                }
        );

        return OK("HousingFunds are registered");
    }

    @GetMapping("predict")
    public ApiResult predictFund(@RequestBody PredictRequest predictRequest) {
        final Institute institute = fundService.findByInstituteNameFetchJoin(predictRequest.getBank()).get();
        final List<Double> amountByMonth = getAmountByMonth(predictRequest, institute);

        int result = getPredictionResult(amountByMonth);
        final PredictResponse predictResponse = new PredictResponse(institute.getInstituteCode(), 2018, Integer.parseInt(predictRequest.getMonth()), result);

        return OK(predictResponse);
    }

    private int getPredictionResult(List<Double> amountByMonth) {
        int size = amountByMonth.size();
        double[] time = new double[size];
        double[] amount = new double[size];
        for(int i=0; i<size; i++) {
            time[i] = i+1;
            amount[i] = amountByMonth.get(i);
        }

        LinearRegressionModel model = new LinearRegressionModel(time, amount);
        model.compute();
        double[] coefficients = model.getCoefficients();
        logger.debug("[predictFund] slope : " +coefficients[1] + ", intercept : " +coefficients[0]);
        return (int) (coefficients[0] + coefficients[1] * size);
    }

    private List<Double> getAmountByMonth(@RequestBody PredictRequest predictRequest, Institute institute) {
        final List<Double> amountByMonth = new ArrayList<>();
        institute.getHousingFunds().forEach(housingFund -> {
            if(housingFund.getMonth().equals(predictRequest.getMonth())) {
                amountByMonth.add((double) housingFund.getAmount());
            }
        });
        return amountByMonth;
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
