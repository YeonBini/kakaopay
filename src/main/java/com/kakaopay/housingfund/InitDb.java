package com.kakaopay.housingfund;

import com.kakaopay.housingfund.fund.model.BankAttribute;
import com.kakaopay.housingfund.fund.model.HousingFund;
import com.kakaopay.housingfund.fund.model.Institute;
import com.kakaopay.housingfund.fund.model.Unit;
import com.kakaopay.housingfund.fund.service.FundService;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class InitDb {

    private final InitDbService initDbService;

    public InitDb(InitDbService initDbService) {
        this.initDbService = initDbService;
    }

    @PostConstruct
    public void init() throws IOException {
        initDbService.initInstitute();
        initDbService.initHousingFund();
    }

    @Component
    @Transactional
    static class InitDbService {

        private final FundService fundService;

        public InitDbService(FundService fundService) throws IOException {
            this.fundService = fundService;
        }

        private List<Institute> institutes = new ArrayList<>();

        private ClassPathResource resource;

        private BufferedReader br;

        {
            resource = new ClassPathResource("housing_fund.csv");
            br = Files.newBufferedReader(Paths.get(resource.getURI()));
            Charset.forName("UTF-8");
        }

        public void initInstitute() throws IOException {
            final String readInstitute = br.readLine();
            final String[] readInstituteArr = Arrays.copyOfRange(readInstitute.split(","), 2, 11);
            Arrays.stream(readInstituteArr)
                    .forEach(i -> {
                        i = i.replace("(억원)", "");
                        BankAttribute attribute = i.contains("기금") ? BankAttribute.FUND : BankAttribute.BANK;
                        final Institute institute = new Institute.Builder()
                                .instituteName(i)
                                .instituteCode("bnk" + String.format("%4d", RandomUtils.nextInt(1, 9999)))
                                .housingFunds(new ArrayList<>())
                                .attribute(attribute)
                                .build();
                        institutes.add(institute);
                    });
        }

        public void initHousingFund() throws IOException {
            String housingFunds;
            while (StringUtils.isNotEmpty((housingFunds = br.readLine()))) {
                final String[] housingFundArr = housingFunds.split(",");
                String year = housingFundArr[0];
                String month = housingFundArr[1];

                for (int i = 2; i < 11; i++) {
                    final String amount = housingFundArr[i].replaceAll("[a-zA-Z\",_$]", "");

                    final HousingFund housingFund = new HousingFund.Builder()
                            .year(year)
                            .month(month)
                            .amount(Integer.parseInt(amount))
                            .institute(institutes.get(i-2))
                            .unit(Unit.HUNDRED_MILLION)
                            .build();
                    institutes.get(i-2).addHousingFund(housingFund);
                }
            }
            fundService.saveAll(institutes);
        }
    }

}
