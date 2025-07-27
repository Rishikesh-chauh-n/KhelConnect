package com.sports.sportsplatform.Service.Impl;


import com.sports.sportsplatform.Model.Medal;
import com.sports.sportsplatform.Repository.MedalRepository;
import com.sports.sportsplatform.Service.MedalService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedalServiceImpl implements MedalService {

    private final MedalRepository medalRepository;

    public MedalServiceImpl(MedalRepository medalRepository) {
        this.medalRepository = medalRepository;
    }

    @Override
    public List<Medal> getAllMedals() {
        return medalRepository.findAll();
    }

    @Override
    public Medal getMedalById(Long id) {
        return medalRepository.findById(id).orElse(null);
    }

    @Override
    public void saveMedal(Medal medal) {
        medalRepository.save(medal);
    }

    @Override
    public void deleteMedal(Long id) {
        medalRepository.deleteById(id);
    }

    public void updateMedalVerification(Long medalId, boolean verified) {
        Optional<Medal> optionalMedal = medalRepository.findById(medalId);
        if (optionalMedal.isPresent()) {
            Medal medal = optionalMedal.get();
            medal.setVerified(verified);// if you want to store note
            medalRepository.save(medal);
        }
    }


}
