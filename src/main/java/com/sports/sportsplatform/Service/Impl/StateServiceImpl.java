package com.sports.sportsplatform.Service.Impl;

import com.sports.sportsplatform.Model.Game;
import com.sports.sportsplatform.Model.State;
import com.sports.sportsplatform.Repository.StateRepository;
import com.sports.sportsplatform.Service.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepo;

    @Override
    public List<State> getAllStates() {
        return stateRepo.findAll();
    }

    @Override
    public State getStateById(Long id) {
        return stateRepo.findById(id).orElse(null);
    }

    @Override
    public void saveState(State state) {
        stateRepo.save(state);
    }

    @Override
    public void deleteStateById(Long id) {
        stateRepo.deleteById(id);
    }

    @Override
    public List<State> searchStates(String query) {
        return stateRepo.findByNameContainingIgnoreCase(query);
    }
}
