package com.kakaopay.housingfund.fund.repository;

import com.kakaopay.housingfund.fund.model.HousingFund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseFundingRepository extends JpaRepository<HousingFund, Long> {

}
