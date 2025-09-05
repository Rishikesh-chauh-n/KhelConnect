package com.sports.sportsplatform.Service;

import com.sports.sportsplatform.Model.Event;
import com.sports.sportsplatform.Model.LearnandTrain.TrainingCenter;
import com.sports.sportsplatform.Model.User;
import com.sports.sportsplatform.Model.UserInteraction;
import com.sports.sportsplatform.Model.Venues.Venue;
import com.sports.sportsplatform.Repository.EventRepository;
import com.sports.sportsplatform.Repository.TrainingCenterRepository;
import com.sports.sportsplatform.Repository.UserInteractionRepository;
import com.sports.sportsplatform.Repository.VenueRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserInteractionService {

    private final UserInteractionRepository interactionRepo;
    private final EventService eventService;

    private final VenueRepository venueRepository;

    private final TrainingCenterRepository trainingCenterRepository;

    public void saveInteraction(User user, String keyword, String interactionType ) {
        Long userId = user.getId();

        long count = interactionRepo.countByUserId(userId);
        if (count >= 40) {
            // delete oldest one
                List<Long> oldestId = interactionRepo.findOldestInteractionIds(userId, PageRequest.of(0, 1));
            interactionRepo.deleteByIdIn(oldestId);
        }

        UserInteraction interaction = new UserInteraction();
        interaction.setUser(user);
        interaction.setKeyword(keyword);
        interaction.setTimestamp(LocalDateTime.now());
        interaction.setInteractionType(interactionType);

        interactionRepo.save(interaction);
    }


    public boolean isFirstTimeUser(String email) {
        // Option 1: Using existsByUsername (recommended if you create this query)
        return !interactionRepo.existsByUserEmail(email);

        // OR Option 2: By checking size of list (if no existsByUsername method)
        // List<UserInteraction> interactions = userInteractionRepository.findByUsername(username);
        // return interactions.isEmpty();
    }



    public List<Event> getPersonalizedFeaturedEvents(Long userId) {
        List<Object[]> topGames = interactionRepo.findTopGamesByUser(userId);
        int[] distribution = {4, 3, 2, 1};
        List<Event> result = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < topGames.size() && i < distribution.length; i++) {
            String gameName = (String) topGames.get(i)[0]; // keyword from query
            int limit = distribution[i];

            List<Event> eventsForGame = eventService.findByGameName(gameName);

            // Shuffle and take limit
            Collections.shuffle(eventsForGame, random);
            result.addAll(eventsForGame.stream()
                    .limit(limit)
                    .collect(Collectors.toList()));
        }

        // Ensure max 10 results
        return result.stream().limit(10).collect(Collectors.toList());
    }

    public List<Venue> getPersonalizedVenues(Long userId) {

        List<String> preferredGames = interactionRepo
                .findPreferredGamesByUserId(userId);

        List<Venue> allVenues = venueRepository.findAll();

        // 3️⃣ Rank venues by how many preferred games they offer and limit to 10
        return allVenues.stream()
                .sorted((v1, v2) -> {
                    long count1 = v1.getGameDetailsList().stream()
                            .filter(gd -> preferredGames.contains(gd.getGame().getName()))
                            .count();

                    long count2 = v2.getGameDetailsList().stream()
                            .filter(gd -> preferredGames.contains(gd.getGame().getName()))
                            .count();

                    return Long.compare(count2, count1); // Descending order
                })
                .limit(10) // ✅ Only top 10 venues
                .collect(Collectors.toList());

    }

    public List<TrainingCenter> getPersonalizedTrainingCenter(Long userId) {
        // 1️⃣ Get user's preferred games
        List<String> preferredGames = interactionRepo.findPreferredGamesByUserId(userId);

        // 2️⃣ Fetch all training centers
        List<TrainingCenter> allCenters = trainingCenterRepository.findAll();

        // 3️⃣ Sort centers by number of preferred games offered
        return allCenters.stream()
                .sorted((c1, c2) -> {
                    long count1 = c1.getSportsOffered().stream()
                            .filter(game -> preferredGames.contains(game.getName()))
                            .count();

                    long count2 = c2.getSportsOffered().stream()
                            .filter(game -> preferredGames.contains(game.getName()))
                            .count();

                    return Long.compare(count2, count1); // Descending
                })
                .limit(10) // ✅ Top 10 only
                .collect(Collectors.toList());
    }


    @Async
    @Transactional
    public CompletableFuture<List<Event>> getPersonalizedFeaturedEventsAsync(Long userId) {
        return CompletableFuture.completedFuture(getPersonalizedFeaturedEvents(userId));
    }

    @Async
    @Transactional
    public CompletableFuture<List<Venue>> getPersonalizedVenuesAsync(Long userId) {
        return CompletableFuture.completedFuture(getPersonalizedVenues(userId));
    }

    @Async
    @Transactional
    public CompletableFuture<List<TrainingCenter>> getPersonalizedTrainingCenterAsync(Long userId) {
        return CompletableFuture.completedFuture(getPersonalizedTrainingCenter(userId));
    }


}
