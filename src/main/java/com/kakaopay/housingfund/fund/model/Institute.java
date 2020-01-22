package com.kakaopay.housingfund.fund.model;

import com.kakaopay.housingfund.common.Buildable;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.persistence.CascadeType.ALL;

@Entity
public class Institute {
    @Id @GeneratedValue
    @Column(name = "institute_id")
    private Long id;

    private String instituteName;

    private String instituteCode;

    /**
     * 기관들의 속성을 의미
     * 예 : 기금, 은행 등
     */
    @Enumerated(EnumType.STRING)
    private BankAttribute attribute;

    @OneToMany(mappedBy = "institute", cascade = ALL)
    private List<HousingFund> housingFunds;

    public Institute() {}

    private Institute(String instituteName, String instituteCode, BankAttribute attribute, List<HousingFund> housingFunds) {
        checkNotNull(instituteName, "Institute name must be provided");
        checkNotNull(instituteCode, "Institute code must be provided");
        checkNotNull(attribute, "Bank attribute must be provided");
        this.instituteName = instituteName;
        this.instituteCode = instituteCode;
        this.attribute = attribute;
        Collections.sort(housingFunds);
        this.housingFunds = housingFunds;
    }

    public HousingFund maxAmount() {
        return housingFunds.get(0);
    }

    public HousingFund minAmount() {
        int size = this.housingFunds.size();
        return housingFunds.get(size-1);
    }

    public static class Builder implements Buildable<Institute> {

        private String instituteName;
        private String instituteCode;
        private BankAttribute attribute;
        private List<HousingFund> housingFunds;

        public Builder instituteName(String instituteName) {
            this.instituteName = instituteName;
            return this;
        }

        public Builder instituteCode(String instituteCode) {
            this.instituteCode = instituteCode;
            return this;
        }

        public Builder attribute(BankAttribute attribute) {
            this.attribute = attribute;
            return this;
        }

        public Builder housingFunds(List<HousingFund> housingFunds) {
            this.housingFunds = housingFunds;
            return this;
        }

        @Override
        public Institute build() {
            return new Institute(instituteName, instituteCode, attribute, housingFunds);
        }
    }


}
