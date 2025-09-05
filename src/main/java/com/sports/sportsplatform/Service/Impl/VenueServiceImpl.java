package com.sports.sportsplatform.Service.Impl;

import com.sports.sportsplatform.Model.Venues.Venue;
import com.sports.sportsplatform.Repository.BookedSlotVenueRepository;
import com.sports.sportsplatform.Repository.VenueRepository;
import com.sports.sportsplatform.Service.VenueService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final BookedSlotVenueRepository bookedSlotVenueRepository;

    @Override
    public void saveVenue(Venue venue) {
        venueRepository.save(venue);
    }

    @Override
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }


    @Override
    public List<Venue> getTopVenues(int limit) {
        List<Venue> allVenues = venueRepository.findAllVenues();
        Collections.shuffle(allVenues); // randomly shuffle
        return allVenues.stream().limit(limit).collect(Collectors.toList());


    }

    public Venue getVenueById(Long id) {
        return venueRepository.findByIdWithGames(id)
                .orElseThrow(() -> new RuntimeException("Venue not found"));
    }


    public Map<Long, Map<LocalTime, Integer>> getBookedSlotsMap(Long venueId, LocalDate date) {
        List<Object[]> bookedData = bookedSlotVenueRepository.findBookedCountsForVenueAndDate(venueId, date);

        Map<Long, Map<LocalTime, Integer>> bookedMap = new HashMap<>();
        for (Object[] row : bookedData) {
            Long gameId = (Long) row[0];
            LocalTime time = (LocalTime) row[1];
            Integer count = ((Number) row[2]).intValue();
            bookedMap.computeIfAbsent(gameId, k -> new HashMap<>()).put(time, count);
        }
        return bookedMap;
    }


    @Async
    @Transactional
    public CompletableFuture<List<Venue>> getTopVenuesAsync(int count) {
        return CompletableFuture.completedFuture(getTopVenues(count));
    }



}

