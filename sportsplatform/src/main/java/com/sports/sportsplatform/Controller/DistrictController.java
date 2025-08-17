package com.sports.sportsplatform.Controller;

import com.sports.sportsplatform.Model.District;
import com.sports.sportsplatform.Repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/districts")
public class DistrictController {

    @Autowired
    private DistrictRepository districtRepository;

    @GetMapping("/by-state/{stateId}")
    public List<District> getDistrictsByState(@PathVariable Long stateId) {
        return districtRepository.findByStateId(stateId);
    }
}

