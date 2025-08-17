package com.sports.sportsplatform.Service;

import com.sports.sportsplatform.Model.LearnandTrain.TrainingCenter;
import com.sports.sportsplatform.Repository.TrainingCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingCenterService {

    @Autowired
    private TrainingCenterRepository trainingCenterRepository;

    public void saveTrainingCenter(TrainingCenter center) {
        trainingCenterRepository.save(center);
    }


    public List<TrainingCenter> getTopTrainingCenters(int limit) {
        return trainingCenterRepository.findTopTrainingCenters()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public TrainingCenter getTrainingCenterById(Long id) {
        return trainingCenterRepository.findById(id)
                .orElse(null);
    }
}
