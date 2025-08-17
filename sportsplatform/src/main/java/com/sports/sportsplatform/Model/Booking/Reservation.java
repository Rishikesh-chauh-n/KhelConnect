package com.sports.sportsplatform.Model.Booking;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reservations", indexes = {
        @Index(name = "idx_gd_date_time_status", columnList = "game_detail_id,date,start_time,status"),
        @Index(name = "idx_expires_at", columnList = "expires_at")
})
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who & what
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "venue_id", nullable = false)
    private Long venueId;

    @Column(name = "game_detail_id", nullable = false)
    private Long gameDetailId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    // Money & payment
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_txn_id")
    private String paymentTxnId;

    // State
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ReservationStatus status;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt; // only used for HOLD

    // Audit
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Reservation() {}

    public Reservation(Long userId, Long venueId, Long gameDetailId, LocalDate date, LocalTime startTime,
                       BigDecimal amount, String paymentTxnId, ReservationStatus status, LocalDateTime expiresAt) {
        this.userId = userId;
        this.venueId = venueId;
        this.gameDetailId = gameDetailId;
        this.date = date;
        this.startTime = startTime;
        this.amount = amount;
        this.paymentTxnId = paymentTxnId;
        this.status = status;
        this.expiresAt = expiresAt;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getVenueId() { return venueId; }
    public Long getGameDetailId() { return gameDetailId; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public BigDecimal getAmount() { return amount; }
    public String getPaymentTxnId() { return paymentTxnId; }
    public ReservationStatus getStatus() { return status; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setStatus(ReservationStatus status) { this.status = status; }
    public void setPaymentTxnId(String paymentTxnId) { this.paymentTxnId = paymentTxnId; }
}
