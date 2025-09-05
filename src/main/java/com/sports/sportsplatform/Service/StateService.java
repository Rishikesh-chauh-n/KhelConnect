package com.sports.sportsplatform.Service;

import com.sports.sportsplatform.Model.Game;
import com.sports.sportsplatform.Model.State;

import java.util.List;

public interface StateService {
    List<State> getAllStates();
    State getStateById(Long id);
    void saveState(State state);
    void deleteStateById(Long id);
    List<State> searchStates(String query);
}

