package com.sports.sportsplatform.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {

    // Key: "venueId-gameId-date", Value: list of active emitters
    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(Long venueId, Long gameId, String date) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30 min timeout
        String key = key(venueId, gameId, date);

        emitters.computeIfAbsent(key, k -> new ArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(key, emitter));
        emitter.onTimeout(() -> removeEmitter(key, emitter));

        return emitter;
    }

    private void removeEmitter(String key, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(key);
        if (list != null) {
            list.remove(emitter);
        }
    }

    public void sendUpdate(Long venueId, Long gameId, String date, Map<String, Object> payload) {
        String key = key(venueId, gameId, date);
        List<SseEmitter> list = emitters.get(key);
        if (list == null) return;

        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : list) {
            try {
                emitter.send(SseEmitter.event().name("slot-update").data(payload));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        }
        deadEmitters.forEach(emitter -> list.remove(emitter));
    }

    private String key(Long venueId, Long gameId, String date) {
        return venueId + "-" + gameId + "-" + date;
    }
}
