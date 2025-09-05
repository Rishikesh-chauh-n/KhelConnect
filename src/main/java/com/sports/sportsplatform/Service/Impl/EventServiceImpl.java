package com.sports.sportsplatform.Service.Impl;

import com.sports.sportsplatform.Model.Event;
import com.sports.sportsplatform.Repository.EventRepository;
import com.sports.sportsplatform.Service.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepo;

    @Override
    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepo.findById(id).orElse(null);
    }

    @Override
    public void saveEvent(Event event) {
        eventRepo.save(event);
    }

    @Override
    public void deleteEventById(Long id) {
        eventRepo.deleteById(id);
    }

    @Override
    public List<Event> getEventsByGameId(Long gameId) {
        return eventRepo.findByGameId(gameId);
    }

    @Override
    public List<Event> getEventsByLocation(String location) {
        return eventRepo.findByLocationContainingIgnoreCase(location);
    }

    public List<Event> findByGameName(String gameName) {
        return eventRepo.findByGameName(gameName);
    }


    @Async
    @Transactional
    public CompletableFuture<List<Event>> getTopFeaturedGamesAsync(int count) {
        return CompletableFuture.completedFuture(getTopFeaturedGames(count));
    }



    public List<Event> getTopFeaturedGames(int count) {
        List<Long> topGameIds = eventRepo.findTopGameIdsByEventCount();
        List<Event> featuredEvents = new ArrayList<>();

        int selected = 0;
        for (Long gameId : topGameIds) {
            if (selected >= count) break;

            Optional<Event> event = eventRepo.findFirstByGameIdOrderByDateAsc(gameId);
            if (event.isPresent()) {
                featuredEvents.add(event.get());
                selected++;
            }
        }

        return featuredEvents;
    }





}
