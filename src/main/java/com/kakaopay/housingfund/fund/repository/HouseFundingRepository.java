package com.kakaopay.housingfund.fund.repository;

import com.kakaopay.housingfund.fund.model.HousingFund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HouseFundingRepository extends JpaRepository<HousingFund, Long> {

    Optional<HousingFund> findByYearAndMonthAndInstituteId(String year, String month, Long instituteId);
}
