package com.sports.sportsplatform.Model.Booking;

public enum ReservationStatus {
    HOLD,           // 5-min temporary reservation before payment
    CONFIRMED,      // payment success
    CANCELLED,      // user cancelled or payment failed
    EXPIRED         // hold timed out (>= 5 minutes)
}
