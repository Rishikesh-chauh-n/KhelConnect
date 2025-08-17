package com.sports.sportsplatform.Service;



import com.sports.sportsplatform.Model.Game;

import java.util.List;

public interface GameService {
    List<Game> getAllGames();
    Game getGameById(Long id);
    void saveGame(Game game);
    void deleteGameById(Long id);
    List<Game> searchGames(String query);
    public List<Game> getGamesByIds(List<Long> ids);

}
