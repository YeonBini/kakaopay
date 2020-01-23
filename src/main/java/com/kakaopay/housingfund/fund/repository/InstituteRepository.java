package com.kakaopay.housingfund.fund.repository;

import com.kakaopay.housingfund.fund.model.Institute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InstituteRepository extends JpaRepository<Institute, Long> {

    @Query("select i from Institute i join fetch i.housingFunds h where i.instituteCode = :instituteCode")
    Optional<Institute> findByInstituteCode(@Param("instituteCode") String instituteCode);

    @Query("select i from Institute i join fetch i.housingFunds h where i.instituteName = :instituteName")
    Optional<Institute> findByInstituteName(@Param("instituteName") String instituteName);

}
