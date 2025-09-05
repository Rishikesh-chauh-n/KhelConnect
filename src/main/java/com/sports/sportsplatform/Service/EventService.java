package com.sports.sportsplatform.Service;

import com.sports.sportsplatform.Model.Event;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EventService {
    List<Event> getAllEvents();
    Event getEventById(Long id);
    void saveEvent(Event event);
    void deleteEventById(Long id);
    List<Event> getEventsByGameId(Long gameId);
    List<Event> getEventsByLocation(String location);
    public List<Event> getTopFeaturedGames(int count);
    List<Event> findByGameName(String gameName);
    public CompletableFuture<List<Event>> getTopFeaturedGamesAsync(int count);


    }
