package com.sports.sportsplatform.Service.Impl;

import com.sports.sportsplatform.Model.District;
import com.sports.sportsplatform.Repository.DistrictRepository;
import com.sports.sportsplatform.Service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;

    @Override
    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }


    public District saveDistrict(District district) {
        return districtRepository.save(district); // saves to DB and returns the saved entity
    }

    public District getDistrictById(Long id) {
        return districtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("District not found with id: " + id));
    }

    public Optional<District> findByNameAndStateId(String districtName, Long stateId) {
        return districtRepository.findByNameIgnoreCaseAndStateId(districtName, stateId);
    }


}
