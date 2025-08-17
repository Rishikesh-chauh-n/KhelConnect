//package com.sports.sportsplatform.Repository;
//
//import com.sports.sportsplatform.Model.Booking.TimeslotLock;
//import jakarta.persistence.LockModeType;
//import org.springframework.data.jpa.repository.Lock;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.Optional;
//
//public interface TimeslotLockRepository extends JpaRepository<TimeslotLock, Long> {
//
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    Optional<TimeslotLock> findByGameDetailIdAndDateAndStartTime(Long gameDetailId, LocalDate date, LocalTime startTime);
//
//    Optional<TimeslotLock> findByGameDetailIdAndDateAndStartTimeAndIdNotNull(Long gameDetailId, LocalDate date, LocalTime startTime);
//}
