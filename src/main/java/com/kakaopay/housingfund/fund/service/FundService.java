package com.kakaopay.housingfund.fund.service;

import com.kakaopay.housingfund.exception.DuplicateInstituteException;
import com.kakaopay.housingfund.fund.model.HousingFund;
import com.kakaopay.housingfund.fund.model.Institute;
import com.kakaopay.housingfund.fund.repository.HouseFundingRepository;
import com.kakaopay.housingfund.fund.repository.InstituteRepository;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Proxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FundService {

    private final InstituteRepository instituteRepository;

    private final HouseFundingRepository houseFundingRepository;

    public FundService(InstituteRepository instituteRepository, HouseFundingRepository houseFundingRepository) {
        this.instituteRepository = instituteRepository;
        this.houseFundingRepository = houseFundingRepository;
    }

    @Transactional
    public Institute save(Institute institute) {
        if (validateInstitute(institute)) {
            throw new DuplicateInstituteException("Institute already exists");
        }
        ;
        return instituteRepository.save(institute);
    }

    @Transactional
    public int saveAll(List<Institute> institutes) {
        institutes.stream().forEach(this::validateInstitute);
        final List<Institute> savedInstitutes = instituteRepository.saveAll(institutes);
        return savedInstitutes.size();
    }

    @Transactional
    public void updateInstitute(Institute institute) {
    }

    @Transactional
    public void addHousingFund(Institute institute, HousingFund housingFund) {
        institute.addHousingFund(housingFund);
    }

    public List<Institute> findAll() {
        return instituteRepository.findAll();
    }

    public List<Institute> findAllFetchJoin() {
        List<Institute> all = instituteRepository.findAll();
        all.stream().forEach(a -> Hibernate.initialize(a.getHousingFunds()));
        return all;
    }

    public Optional<Institute> findByInstituteCode(String instituteCode) {
        return instituteRepository.findByInstituteCode(instituteCode);
    }

    public Optional<Institute> findByInstituteName(String instituteName) {
        return instituteRepository.findByInstituteName(instituteName);
    }

    private boolean validateInstitute(Institute institute) {
        final Optional<Institute> byInstituteCode = instituteRepository.findByInstituteCode(institute.getInstituteCode());
        return byInstituteCode.isPresent();
    }


}
