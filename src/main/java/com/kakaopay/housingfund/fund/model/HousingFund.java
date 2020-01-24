package com.kakaopay.housingfund.fund.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kakaopay.housingfund.common.Buildable;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.persistence.FetchType.LAZY;

@Entity
@BatchSize(size = 100)
public class HousingFund implements Comparable<HousingFund> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "housing_fund_id")
    private Long id;

    private String year;

    private String month;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "institute_id")
    @JsonIgnore
    private Institute institute;

    private int amount;

    /**
     * 금액의 단위
     * 예 : 억, 천만,
     */
    @Enumerated(EnumType.STRING)
    private Unit unit;

    protected HousingFund() {}

    protected HousingFund(Long id, String year, String month, Institute institute, int amount, Unit unit) {
        checkNotNull(year, "Year must be provided");
        checkNotNull(month, "Month must be provided");
        checkNotNull(institute, "Institute must be provided");
        checkNotNull(amount, "Amount must be provided");
        checkNotNull(unit, "Unit must be provided");
        this.id = id;
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
        private Long id;
        private String year;
        private String month;
        private Institute institute;
        private int amount;
        private Unit unit;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

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
            return new HousingFund(id, year, month, institute, amount, unit);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HousingFund)) return false;
        HousingFund that = (HousingFund) o;
        return amount == that.amount &&
                Objects.equals(id, that.id) &&
                Objects.equals(year, that.year) &&
                Objects.equals(month, that.month) &&
                Objects.equals(institute, that.institute) &&
                unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, year, month, institute, amount, unit);
    }
}
