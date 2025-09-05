package com.sports.sportsplatform.Repository;

import com.sports.sportsplatform.Model.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, Long> {
    Optional<District> findByNameIgnoreCaseAndStateId(String name, Long stateId);
    List<District> findByStateId(Long stateId);

}
