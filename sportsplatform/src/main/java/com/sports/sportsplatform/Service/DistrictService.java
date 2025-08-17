package com.sports.sportsplatform.Service;



import com.sports.sportsplatform.Model.District;
import java.util.List;
import java.util.Optional;

public interface DistrictService {
    List<District> getAllDistricts();

    District saveDistrict(District district);


    District getDistrictById(Long id);
    Optional<District> findByNameAndStateId(String districtName, Long stateId);
}

