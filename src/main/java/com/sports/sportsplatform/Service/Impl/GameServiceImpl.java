package com.sports.sportsplatform.Service.Impl;

import com.sports.sportsplatform.Model.Game;
import com.sports.sportsplatform.Repository.GameRepository;
import com.sports.sportsplatform.Service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepo;

    @Override
    public List<Game> getAllGames() {
        return gameRepo.findAll();
    }

    @Override
    public Game getGameById(Long id) {
        return gameRepo.findById(id).orElse(null);
    }

    @Override
    public void saveGame(Game game) {
        gameRepo.save(game);
    }

    @Override
    public void deleteGameById(Long id) {
        gameRepo.deleteById(id);
    }

    public List<Game> getGamesByIds(List<Long> ids) {
        return gameRepo.findAllById(ids);
    }



    @Override
    public List<Game> searchGames(String query) {
        return gameRepo.findByNameContainingIgnoreCase(query);
    }



}
