package com.sports.sportsplatform.Model;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    private Long venueId;
    private Long gameId;
    private String date;     // yyyy-MM-dd
    private String timeRange; // "09:00 - 10:00"
    private Long userId;

    // getters/setters
}

