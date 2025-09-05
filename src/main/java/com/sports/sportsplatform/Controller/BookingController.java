package com.sports.sportsplatform.Controller;

import com.sports.sportsplatform.Model.BookingRequest;
import com.sports.sportsplatform.Model.Venues.BookedSlotVenue;
import com.sports.sportsplatform.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/bookings")
//@RequiredArgsConstructor
//public class BookingController {
//
//    private final BookingService bookingService;
//
//    private final DummyPaymentGateway paymentGateway; //
//
//    // Step 1: Create a 5-min hold on a slot
//    @PostMapping("/hold")
//    public ResponseEntity<BookedSlotVenue> createHold(
//            @RequestParam Long venueGameDetailsId,
//            @RequestParam String date,       // format: YYYY-MM-DD
//            @RequestParam String timeSlot,   // format: HH:mm
//            @RequestParam Long userId
//    ) {
//        BookedSlotVenue booking = bookingService.createHold(
//                venueGameDetailsId,
//                LocalDate.parse(date),
//                LocalTime.parse(timeSlot),
//                userId
//        );
//        return ResponseEntity.ok(booking);
//    }
//
//    // Step 2: Confirm the booking after payment success
//    @PostMapping("/confirm/{bookingId}")
//    public ResponseEntity<BookedSlotVenue> confirmBooking(
//            @PathVariable Long bookingId,
//            @RequestParam String paymentReference
//    ) {
//        BookedSlotVenue booking = bookingService.confirmBooking(bookingId, paymentReference);
//        return ResponseEntity.ok(booking);
//    }
//
//    // Step 3: Cancel booking (manual cancel)
//    @DeleteMapping("/{bookingId}")
//    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
//        bookingService.cancelBooking(bookingId);
//        return ResponseEntity.noContent().build();
//    }
//
//
//    @PostMapping("/book-and-pay")
//    public ResponseEntity<?> bookAndPay(
//            @RequestParam Long venueGameDetailsId,
//            @RequestParam String date,   // format: yyyy-MM-dd
//            @RequestParam String timeSlot, // format: HH:mm
//            @RequestParam Long userId,
//            @RequestParam double amount,
//            @RequestParam(defaultValue = "UPI") String paymentMethod
//    ) {
//        try {
//            // 1. Create HOLD booking
//            BookedSlotVenue holdBooking = bookingService.createHold(
//                    venueGameDetailsId,
//                    LocalDate.parse(date),
//                    LocalTime.parse(timeSlot),
//                    userId
//            );
//
//            // 2. Process payment
//            boolean paymentSuccess = paymentGateway.processPayment(
//                    holdBooking.getId(),
//                    amount,
//                    paymentMethod
//            );
//
//            if (!paymentSuccess) {
//                // Cancel hold if payment fails
//                bookingService.cancelBooking(holdBooking.getId());
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body("Payment failed. Slot released.");
//            }
//
//            // 3. Confirm booking
//            String paymentReference = UUID.randomUUID().toString();
//            BookedSlotVenue confirmedBooking =
//                    bookingService.confirmBooking(holdBooking.getId(), paymentReference);
//
//            // 4. Return success
//            return ResponseEntity.ok(confirmedBooking);
//
//        } catch (BookingService.SlotUnavailableException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
//        }
//    }
//
//}


@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book")
    public ResponseEntity<Map<String, Object>> bookSlot(@RequestBody BookingRequest request) {
        try {
            boolean success = bookingService.bookSlot(request);
            if (success) {
                return ResponseEntity.ok(Map.of("status", "success", "message", "Your ticket is booked!"));
            } else {
                return ResponseEntity.ok(Map.of("status", "fail", "message", "No slots left."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
}
