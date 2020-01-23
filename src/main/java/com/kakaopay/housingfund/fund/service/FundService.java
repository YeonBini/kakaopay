package com.kakaopay.housingfund.fund.service;

import com.kakaopay.housingfund.exception.DuplicateInstituteException;
import com.kakaopay.housingfund.fund.model.HousingFund;
import com.kakaopay.housingfund.fund.model.Institute;
import com.kakaopay.housingfund.fund.repository.HouseFundingRepository;
import com.kakaopay.housingfund.fund.repository.InstituteRepository;
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
        validateInstitute(institute);
        return instituteRepository.save(institute);
    }

    @Transactional
    public void saveAll(List<Institute> institutes) {
        institutes.stream().forEach(this::validateInstitute);
        instituteRepository.saveAll(institutes);
    }

    @Transactional
    public void addHousingFund(Institute institute, HousingFund housingFund) {
        institute.addHousingFund(housingFund);
    }

    public List<Institute> findAll() {
        return instituteRepository.findAll();
    }


    public Optional<Institute> findByInstituteCode(String instituteCode) {
        return instituteRepository.findByInstituteCode(instituteCode);
    }

    public Optional<Institute> findByInstituteName(String instituteName) {
        return instituteRepository.findByInstituteName(instituteName);
    }

    private void validateInstitute(Institute institute) {
        final Optional<Institute> byInstituteCode = instituteRepository.findByInstituteCode(institute.getInstituteCode());
        if(byInstituteCode.isPresent()) {
            throw new DuplicateInstituteException("Institute already exists");
        }
    }


}
