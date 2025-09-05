package com.sports.sportsplatform.Controller;


import com.sports.sportsplatform.Model.Venues.Venue;
import com.sports.sportsplatform.Model.Venues.VenueGameDetails;
import com.sports.sportsplatform.Repository.BookedSlotVenueRepository;
import com.sports.sportsplatform.Service.VenueService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api/venues")
public class VenueAvailabilityController {

    private final VenueService venueService;

    public VenueAvailabilityController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping("/{venueId}/available-slots")
    public Map<String, Object> getAvailableSlotsForGame(
            @PathVariable Long venueId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long gameId) {

        Venue venue = venueService.getVenueById(venueId);
        Map<Long, Map<LocalTime, Integer>> bookedMap = venueService.getBookedSlotsMap(venueId, date);

        return venue.getGameDetailsList().stream()
                .filter(vgd -> vgd.getId().equals(gameId))
                .findFirst()
                .map(vgd -> {
                    Map<String, Object> gameMap = new HashMap<>();
                    gameMap.put("gameName", vgd.getGame().getName());
                    gameMap.put("slotsPerHour", vgd.getSlotsPerHour());

                    LocalTime start = LocalTime.parse(vgd.getStartTime());
                    LocalTime end = LocalTime.parse(vgd.getEndTime());

                    List<Map<String, Object>> slots = new ArrayList<>();
                    for (LocalTime time = start; !time.isAfter(end.minusHours(1)); time = time.plusHours(1)) {
                        int bookedCount = bookedMap
                                .getOrDefault(vgd.getId(), Collections.emptyMap())
                                .getOrDefault(time, 0);

                        int available = Math.max(vgd.getSlotsPerHour() - bookedCount, 0);

                        Map<String, Object> slotMap = new HashMap<>();
                        slotMap.put("timeRange", time + " - " + time.plusHours(1));
                        slotMap.put("availableSlots", available);

                        slots.add(slotMap);
                    }

                    gameMap.put("timeSlots", slots);
                    return gameMap;
                })
                .orElse(Collections.emptyMap());
    }


//    @GetMapping("/{venueId}/availability")
//    public List<Map<String, Object>> getMonthAvailability(
//            @PathVariable Long venueId,
//            @RequestParam Long gameId) {
//
//        Venue venue = venueService.getVenueById(venueId);
//        LocalDate today = LocalDate.now();
//        LocalDate firstDay = today.withDayOfMonth(1);
//        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
//
//        List<Map<String, Object>> response = new ArrayList<>();
//
//        for (LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
//            AtomicBoolean available = new AtomicBoolean(false);
//
//            if (!date.isBefore(today)) {
//                Map<Long, Map<LocalTime, Integer>> bookedMap = venueService.getBookedSlotsMap(venueId, date);
//
//                venue.getGameDetailsList().stream()
//                        .filter(vgd -> vgd.getGame().getId().equals(gameId))
//                        .findFirst()
//                        .ifPresent(vgd -> {
//                            LocalTime start = LocalTime.parse(vgd.getStartTime());
//                            LocalTime end = LocalTime.parse(vgd.getEndTime());
//
//                            for (LocalTime time = start; !time.isAfter(end.minusHours(1)); time = time.plusHours(1)) {
//                                int bookedCount = bookedMap
//                                        .getOrDefault(vgd.getId(), Collections.emptyMap())
//                                        .getOrDefault(time, 0);
//                                int availableSlots = Math.max(vgd.getSlotsPerHour() - bookedCount, 0);
//                                if (availableSlots > 0) {
//                                    available.set(true);
//                                    break;
//                                }
//                            }
//                        });
//            }
//
//            Map<String, Object> dayMap = new HashMap<>();
//            dayMap.put("date", date.toString());
//            dayMap.put("available", available.get());
//            response.add(dayMap);
//        }
//
//        return response;
//    }



    @GetMapping("/{venueId}/availability")
    public List<Map<String, Object>> getMonthAvailability(
            @PathVariable Long venueId,
            @RequestParam Long gameId) {

        Venue venue = venueService.getVenueById(venueId);
        LocalDate today = LocalDate.now();
        LocalDate firstDay = today.withDayOfMonth(1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        List<Map<String, Object>> response = new ArrayList<>();

        for (LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
            AtomicBoolean available = new AtomicBoolean(false);

            if (!date.isBefore(today)) {

                Map<Long, Map<LocalTime, Integer>> bookedMap = venueService.getBookedSlotsMap(venueId, date);

                venue.getGameDetailsList().stream()
                        .filter(vgd -> vgd.getId().equals(gameId))
                        .findFirst()
                        .ifPresent(vgd -> {
                            // Safety checks
                            if (vgd.getSlotsPerHour() > 0
                                    && vgd.getStartTime() != null && !vgd.getStartTime().isEmpty()
                                    && vgd.getEndTime() != null && !vgd.getEndTime().isEmpty()) {

                                LocalTime start = LocalTime.parse(vgd.getStartTime());
                                LocalTime end = LocalTime.parse(vgd.getEndTime());

                                for (LocalTime time = start; !time.isAfter(end.minusHours(1)); time = time.plusHours(1)) {
                                    int bookedCount = bookedMap
                                            .getOrDefault(vgd.getId(), Collections.emptyMap())
                                            .getOrDefault(time, 0);
                                    int availableSlots = Math.max(vgd.getSlotsPerHour() - bookedCount, 0);
                                    if (availableSlots > 0) {
                                        available.set(true);
                                        break;
                                    }
                                }
                            }
                        });
            }

            Map<String, Object> dayMap = new HashMap<>();
            dayMap.put("date", date.toString());
            dayMap.put("available", available.get());
            response.add(dayMap);
        }

        return response;
    }


}


