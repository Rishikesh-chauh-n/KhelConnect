package com.sports.sportsplatform.Repository;

import com.sports.sportsplatform.Model.LearnandTrain.TrainingCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingCenterRepository extends JpaRepository<TrainingCenter, Long> {


    @Query("SELECT tc FROM TrainingCenter tc " +
            "LEFT JOIN tc.sportsOffered s " +
            "GROUP BY tc " +
            "ORDER BY COUNT(s) DESC")
    List<TrainingCenter> findTopTrainingCenters();

}
