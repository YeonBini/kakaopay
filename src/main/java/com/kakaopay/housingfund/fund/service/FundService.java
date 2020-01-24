package com.kakaopay.housingfund.fund.service;

import com.kakaopay.housingfund.exception.DuplicateInstituteException;
import com.kakaopay.housingfund.fund.model.HousingFund;
import com.kakaopay.housingfund.fund.model.Institute;
import com.kakaopay.housingfund.fund.repository.HouseFundingRepository;
import com.kakaopay.housingfund.fund.repository.InstituteRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<Map<String, Integer>> findByInstituteMinMaxAvg(String instituteName) {
        final Optional<Institute> byInstituteName = instituteRepository.findByInstituteName(instituteName);
        Hibernate.initialize(byInstituteName.get().getHousingFunds());

        // 연도 기준을 avg값을 가져온다.
        final Map<String, Double> avgAmountMap = byInstituteName.get().avgAmountByYear();
        List<String> avgKeySetList = new ArrayList<>(avgAmountMap.keySet());
        Collections.sort(avgKeySetList, Comparator.comparing(avgAmountMap::get));
        int size = avgAmountMap.size();

        List<Map<String, Integer>> avgMapList = getMinMaxList(avgAmountMap, avgKeySetList, size);

        return avgMapList;

    }

    private List<Map<String, Integer>> getMinMaxList(Map<String, Double> avgAmountMap, List<String> avgKeySetList, int size) {
        List<Map<String, Integer>> avgMapList = new ArrayList<>();
        Map<String, Integer> minMap = new HashMap<>();
        Map<String, Integer> maxMap = new HashMap<>();
        minMap.put("year", Integer.parseInt(avgKeySetList.get(0)));
        minMap.put("amount", (int)Math.round(avgAmountMap.get(avgKeySetList.get(0))));
        maxMap.put("year", Integer.parseInt(avgKeySetList.get(size -1)));
        maxMap.put("amount", (int)Math.round(avgAmountMap.get(avgKeySetList.get(size -1))));

        avgMapList.add(minMap);
        avgMapList.add(maxMap);
        return avgMapList;
    }

    private boolean validateInstitute(Institute institute) {
        final Optional<Institute> byInstituteCode = instituteRepository.findByInstituteCode(institute.getInstituteCode());
        return byInstituteCode.isPresent();
    }


}
