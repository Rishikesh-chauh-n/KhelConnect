package com.sports.sportsplatform.Model.Booking;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "timeslot_locks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"game_detail_id", "date", "start_time"}))
public class TimeslotLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_detail_id", nullable = false)
    private Long gameDetailId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    protected TimeslotLock() {}

    public TimeslotLock(Long gameDetailId, LocalDate date, LocalTime startTime) {
        this.gameDetailId = gameDetailId;
        this.date = date;
        this.startTime = startTime;
    }

    public Long getId() { return id; }
    public Long getGameDetailId() { return gameDetailId; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
}
