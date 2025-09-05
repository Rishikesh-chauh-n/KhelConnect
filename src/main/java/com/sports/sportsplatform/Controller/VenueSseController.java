package com.sports.sportsplatform.Controller;

import com.sports.sportsplatform.Service.SseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/venues")
public class VenueSseController {

    private final SseService sseService;

    public VenueSseController(SseService sseService) {
        this.sseService = sseService;
    }

    // Client subscribes to updates for a specific venue & game
    @GetMapping("/subscribe-availability")
    public SseEmitter subscribeAvailability(@RequestParam Long venueId,
                                            @RequestParam Long gameId,
                                            @RequestParam String date) {
        return sseService.createEmitter(venueId, gameId, date);
    }
}

