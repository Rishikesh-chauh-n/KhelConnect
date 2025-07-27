package com.sports.sportsplatform.Service;


import com.sports.sportsplatform.Model.Venue;

import java.util.List;

public interface VenueService {
    void saveVenue(Venue venue);
    List<Venue> getAllVenues();
}
