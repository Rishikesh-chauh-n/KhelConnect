package com.sports.sportsplatform.Service;


import com.sports.sportsplatform.Model.Venues.Venue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface VenueService {
    void saveVenue(Venue venue);
    List<Venue> getAllVenues();
    public CompletableFuture<List<Venue>> getTopVenuesAsync(int count);
    List<Venue> getTopVenues(int limit);
    public Venue getVenueById(Long id);
    public Map<Long, Map<LocalTime, Integer>> getBookedSlotsMap(Long venueId, LocalDate date);
}
