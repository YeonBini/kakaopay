package com.kakaopay.housingfund.fund.model;

import com.kakaopay.housingfund.common.Buildable;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkNotNull;

@Entity
public class HousingFund implements Comparable<HousingFund> {

    @Id @GeneratedValue
    @Column(name = "housing_fund_id")
    private Long id;

    private String year;

    private String month;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id")
    private Institute institute;

    private int amount;

    /**
     * 금액의 단위
     * 예 : 억, 천만,
     */
    @Enumerated(EnumType.STRING)
    private Unit unit;

    public HousingFund() {}

    private HousingFund(String year, String month, Institute institute, int amount, Unit unit) {
        checkNotNull(year, "Year must be provided");
        checkNotNull(month, "Month must be provided");
        checkNotNull(institute, "Institute must be provided");
        checkNotNull(amount, "Amount must be provided");
        checkNotNull(unit, "Unit must be provided");
        this.year = year;
        this.month = month;
        this.institute = institute;
        this.amount = amount;
        this.unit = unit;
    }

    @Override
    public int compareTo(HousingFund housingFund) {
        final Long compare = (housingFund.getAmount() * housingFund.getUnit().getKrAmount())
                - (this.getAmount() * this.getUnit().getKrAmount());
        return compare > 0 ? 1 : compare == 0 ? 0 : -1;
    }

    public static class Builder implements Buildable<HousingFund> {
        private String year;
        private String month;
        private Institute institute;
        private int amount;
        private Unit unit;

        public Builder year(String year) {
            this.year = year;
            return this;
        }

        public Builder month(String month) {
            this.month = month;
            return this;
        }

        public Builder institute(Institute institute) {
            this.institute = institute;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder unit(Unit unit) {
            this.unit = unit;
            return this;
        }
        @Override
        public HousingFund build() {
            return new HousingFund(year, month, institute, amount, unit);
        }
    }

    public Long getId() {
        return id;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public Institute getInstitute() {
        return institute;
    }

    public int getAmount() {
        return amount;
    }

    public Unit getUnit() {
        return unit;
    }
}
