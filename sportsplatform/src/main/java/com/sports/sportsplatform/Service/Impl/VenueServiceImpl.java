package com.sports.sportsplatform.Service.Impl;

import com.sports.sportsplatform.Model.Venue;
import com.sports.sportsplatform.Service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    @Override
    public void saveVenue(Venue venue) {
        venueRepository.save(venue);
    }

    @Override
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }
}

