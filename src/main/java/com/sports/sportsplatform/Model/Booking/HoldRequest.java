package com.sports.sportsplatform.Model.Booking;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class HoldRequest {
    private Long gameDetailId;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private Long userId;
}