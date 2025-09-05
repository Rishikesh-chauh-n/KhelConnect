//package com.sports.sportsplatform.Repository;
//
//import com.sports.sportsplatform.Model.Booking.Reservation;
//import com.sports.sportsplatform.Model.Booking.ReservationStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Collection;
//import java.util.Optional;
//
//public interface ReservationRepository extends JpaRepository<Reservation, Long> {
//
//    long countByGameDetailIdAndDateAndStartTimeAndStatusInAndExpiresAtAfter(
//            Long gameDetailId, LocalDate date, LocalTime startTime,
//            Collection<ReservationStatus> statuses, LocalDateTime now);
//
//    long countByGameDetailIdAndDateAndStartTimeAndStatus(
//            Long gameDetailId, LocalDate date, LocalTime startTime, ReservationStatus status);
//
//    Optional<Reservation> findByIdAndStatus(Long id, ReservationStatus status);
//
//    Optional<Reservation> findByPaymentTxnId(String paymentTxnId);
//}