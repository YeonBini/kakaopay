package com.kakaopay.housingfund.fund.model;

import com.kakaopay.housingfund.common.Buildable;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.*;
import static javax.persistence.CascadeType.ALL;

@Entity
public class Institute {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    protected Institute() {}

    protected Institute(Long id, String instituteName, String instituteCode, BankAttribute attribute, List<HousingFund> housingFunds) {
        checkNotNull(instituteName, "Institute name must be provided");
        checkNotNull(instituteCode, "Institute code must be provided");
        checkNotNull(attribute, "Bank attribute must be provided");
        checkNotNull(housingFunds, "Housing funds must not be null");
        this.id = id;
        this.instituteName = instituteName;
        this.instituteCode = instituteCode;
        this.attribute = attribute;
        Collections.sort(housingFunds);
        this.housingFunds = housingFunds;
    }

    public void addHousingFund(HousingFund housingFund) {
        this.housingFunds.add(housingFund);
    }

    public Map<String, Double> avgAmountByYear() {
        return this.housingFunds.stream()
                .collect(groupingBy(HousingFund::getYear, averagingInt(HousingFund::getAmount)));
    }

    public HousingFund maxAmount() {
        return housingFunds.get(0);
    }

    public HousingFund minAmount() {
        int size = this.housingFunds.size();
        return housingFunds.get(size-1);
    }

    public static class Builder implements Buildable<Institute> {
        private Long id;
        private String instituteName;
        private String instituteCode;
        private BankAttribute attribute;
        private List<HousingFund> housingFunds;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

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
            return new Institute(id, instituteName, instituteCode, attribute, housingFunds);
        }
    }

    public Long getId() {
        return id;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public String getInstituteCode() {
        return instituteCode;
    }

    public BankAttribute getAttribute() {
        return attribute;
    }

    public List<HousingFund> getHousingFunds() {
        return housingFunds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Institute)) return false;
        Institute institute = (Institute) o;
        return Objects.equals(id, institute.id) &&
                Objects.equals(instituteName, institute.instituteName) &&
                Objects.equals(instituteCode, institute.instituteCode) &&
                attribute == institute.attribute &&
                Objects.equals(housingFunds, institute.housingFunds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, instituteName, instituteCode, attribute, housingFunds);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("instituteName", instituteName)
                .append("instituteCode", instituteCode)
                .append("attribute", attribute)
                .append("housingFunds", housingFunds)
                .toString();
    }
}
