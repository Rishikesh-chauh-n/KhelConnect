package com.sports.sportsplatform.Service;

import com.sports.sportsplatform.Model.Booking.ReservationStatus;
import com.sports.sportsplatform.Model.BookingRequest;
import com.sports.sportsplatform.Model.Venues.BookedSlotVenue;
import com.sports.sportsplatform.Model.Venues.Venue;
import com.sports.sportsplatform.Model.Venues.VenueGameDetails;
import com.sports.sportsplatform.Repository.BookedSlotVenueRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

//@Service
//@RequiredArgsConstructor
//public class BookingService {
//
//    private final BookedSlotVenueRepository bookedSlotVenueRepository;
//
//    @Transactional
//    public BookedSlotVenue createHold(Long venueGameDetailsId, LocalDate date, LocalTime timeSlot, Long userId) {
//
//        boolean exists = bookedSlotVenueRepository.existsByVenueGameDetailsIdAndDateAndTimeSlotAndStatusIn(
//                venueGameDetailsId,
//                date,
//                timeSlot,
//                List.of(ReservationStatus.HOLD, ReservationStatus.CONFIRMED)
//        );
//        if (exists) {
//            throw new SlotUnavailableException("Slot is already held or booked.");
//        }
//
//        BookedSlotVenue booking = new BookedSlotVenue();
//        // Here you can either load VenueGameDetails via repo or set only its id
//        VenueGameDetails vgd = new VenueGameDetails();
//        vgd.setId(venueGameDetailsId);
//        booking.setVenueGameDetails(vgd);
//
//        booking.setDate(date);
//        booking.setTimeSlot(timeSlot);
//        booking.setUserId(userId);
//        booking.setStatus(ReservationStatus.HOLD);
//        booking.setHoldExpiry(LocalDateTime.now().plusMinutes(5));
//
//        return bookedSlotVenueRepository.save(booking);
//    }
//
//    @Transactional
//    public BookedSlotVenue confirmBooking(Long bookingId, String paymentReference) {
//        BookedSlotVenue booking = bookedSlotVenueRepository.findById(bookingId)
//                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));
//
//        if (booking.getStatus() != ReservationStatus.HOLD) {
//            throw new IllegalStateException("Booking is not in HOLD state");
//        }
//        if (booking.getHoldExpiry().isBefore(LocalDateTime.now())) {
//            throw new IllegalStateException("Hold expired");
//        }
//
//        booking.setStatus(ReservationStatus.CONFIRMED);
//        booking.setPaymentReference(paymentReference);
//        booking.setHoldExpiry(null);
//
//        return bookedSlotVenueRepository.save(booking);
//    }
//
//    @Transactional
//    public void cancelBooking(Long bookingId) {
//        bookedSlotVenueRepository.deleteById(bookingId);
//    }
//
//    @Scheduled(fixedRate = 60000)
//    @Transactional
//    public void releaseExpiredHolds() {
//        List<BookedSlotVenue> expiredHolds = bookedSlotVenueRepository
//                .findByStatusAndHoldExpiryBefore(ReservationStatus.HOLD, LocalDateTime.now());
//        expiredHolds.forEach(bookedSlotVenueRepository::delete);
//    }
//
//
//   public  static class SlotUnavailableException extends RuntimeException {
//        public SlotUnavailableException(String message) {
//            super(message);
//        }
//    }
//
//   public  static class BookingNotFoundException extends RuntimeException {
//        public BookingNotFoundException(String message) {
//            super(message);
//        }
//    }
//}
//


@Service
public class BookingService {

    private final BookedSlotVenueRepository bookedSlotVenueRepository;
    private final VenueService venueService;
    private final SseService sseService;

    public BookingService(BookedSlotVenueRepository bookedSlotVenueRepository,
                          VenueService venueService,
                          SseService sseService) {
        this.bookedSlotVenueRepository = bookedSlotVenueRepository;
        this.venueService = venueService;
        this.sseService = sseService;
    }

    @Transactional
    public boolean bookSlot(BookingRequest request) {
        Venue venue = venueService.getVenueById(request.getVenueId());
        VenueGameDetails game = venue.getGameDetailsList().stream()
                .filter(g -> g.getId().equals(request.getGameId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Game not found"));

        String[] times = request.getTimeRange().split(" - ");
        LocalTime start = LocalTime.parse(times[0]);

        // Pessimistic locking ensures only one thread can get this row
        BookedSlotVenue booking = bookedSlotVenueRepository.findForUpdate(
                venue.getId(), game.getId(), LocalDate.parse(request.getDate()), start
        ).orElseGet(() -> {
            BookedSlotVenue b = new BookedSlotVenue();
            b.setVenue(venue);
            b.setVenueGameDetails(game);
            b.setDate(LocalDate.parse(request.getDate()));
            b.setTimeSlot(start);
            b.setBookedCount(0);
            b.setStatus(ReservationStatus.CONFIRMED);
            return b;
        });

        int availableSlots = game.getSlotsPerHour() - booking.getBookedCount();
        if (availableSlots <= 0) {
            return false; // No slots left
        }

        // Increment booked count safely
        booking.setBookedCount(booking.getBookedCount() + 1);
        booking.setUserId(request.getUserId());
        booking.setStatus(ReservationStatus.CONFIRMED);

        bookedSlotVenueRepository.save(booking);

        // Push update to all clients subscribed to this slot
        Map<String, Object> updatedSlot = Map.of(
                "timeRange", request.getTimeRange(),
                "availableSlots", game.getSlotsPerHour() - booking.getBookedCount()
        );
        sseService.sendUpdate(venue.getId(), game.getId(), request.getDate(), updatedSlot);

        return true;
    }
}
