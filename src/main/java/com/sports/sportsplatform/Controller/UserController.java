package com.sports.sportsplatform.Controller;

import com.sports.sportsplatform.Model.Event;
import com.sports.sportsplatform.Model.Game;
import com.sports.sportsplatform.Model.LearnandTrain.TrainingCenter;
import com.sports.sportsplatform.Model.Venues.Venue;
import com.sports.sportsplatform.Repository.UserRepository;
import com.sports.sportsplatform.Service.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final GameService gameService;
    private final EventService eventService;
    private final UserInteractionService userInteractionService;


    private final TrainingCenterService trainingCenterService;


    private final VenueService venueService;

    private final UserRepository userRepository;

    @GetMapping("/home")
    public String userHome(Model model, Principal principal, HttpServletResponse response) {

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");


        String username = principal.getName(); // Current logged-in user

        // ✅ Always show all games and all events
        List<Game> allGames = gameService.getAllGames();
        List<Event> allEvents = eventService.getAllEvents();

        model.addAttribute("games", allGames);
        model.addAttribute("events", allEvents);


//
        boolean isFirstTimeUser = userInteractionService.isFirstTimeUser(username);
//
//        if (isFirstTimeUser) {
//            // Fetch events with Cloudinary images
//            List<Event> featuredEvents = eventService.getTopFeaturedGames(10);
//
//            for (Event event : featuredEvents) {
//                System.out.println("Event Name: " + event.getName());
//                System.out.println("Image Path: " + event.getImagePath());
//            }
//
//
//            List<Event> eventsWithImages = featuredEvents.stream()
//                    .filter(e -> e.getImagePath() != null && !e.getImagePath().isEmpty()
//                            && e.getImagePath().startsWith("https://res.cloudinary.com"))
//                    .collect(Collectors.toList());
//
//            model.addAttribute("featuredGames", eventsWithImages);
//            System.out.println("Featured Games :" + eventsWithImages);
//
//            List<Venue> topVenues = venueService.getTopVenues(10);
//
//            List<Venue> venuesWithImages = topVenues.stream()
//                    .filter(v -> v.getImagePath() != null && !v.getImagePath().isEmpty()
//                            && v.getImagePath().startsWith("https://res.cloudinary.com"))
//                    .collect(Collectors.toList());
//
//            model.addAttribute("topVenues", venuesWithImages); // List<Venue>
//            System.out.println("Top Venues :" + venuesWithImages);
//
//
//            List<TrainingCenter> topTrainingCenters = trainingCenterService.getTopTrainingCenters(10);
//
//            List<TrainingCenter> centersWithImages = topTrainingCenters.stream()
//                    .filter(tc -> tc.getImageUrl() != null && !tc.getImageUrl().isEmpty()
//                            && tc.getImageUrl().startsWith("https://res.cloudinary.com"))
//                    .collect(Collectors.toList());
//
//            model.addAttribute("trainingCenters", centersWithImages);
//
//
//        } else {
//
//            Long userId = userRepository.findByEmail(username)
//                    .orElseThrow(() -> new RuntimeException("User not found"))
//                    .getId();
//
//            List<Event> personalizedEvents = userInteractionService.getPersonalizedFeaturedEvents(userId);
//
//            // ✅ Inline filtering for Cloudinary images
//            List<Event> filteredEvents = personalizedEvents.stream()
//                    .filter(e -> e.getImagePath() != null && e.getImagePath().startsWith("https://res.cloudinary.com"))
//                    .collect(Collectors.toList());
//
//            model.addAttribute("featuredGames", filteredEvents);
//
//            List<Venue> personalizedVenues = userInteractionService.getPersonalizedVenues(userId);
//
//            List<Venue> filteredVenues = personalizedVenues.stream()
//                    .filter(v -> v.getImagePath() != null && v.getImagePath().startsWith("https://res.cloudinary.com"))
//                    .collect(Collectors.toList());
//
//            model.addAttribute("topVenues", filteredVenues);
//
//            List<TrainingCenter> personalizedtrainingcenters = userInteractionService.getPersonalizedTrainingCenter(userId);
//
//            List<TrainingCenter> filteredCenter = personalizedtrainingcenters.stream()
//                    .filter(t -> t.getImageUrl() != null && t.getImageUrl().startsWith("https://res.cloudinary.com"))
//                    .collect(Collectors.toList());
//
//            model.addAttribute("trainingCenters", filteredCenter);
//
//        }
//
//        return "user-home"; // user-home.html in templates
//    }


        // Lets Use Multithreading for DB calls run concurrently (no waiting one after another).
        //
        //Filtering uses parallelStream(), so filtering large lists also happens across multiple CPU cores.
        //
        //UI load time reduces significantly when coming back to the page.

        if (isFirstTimeUser) {
            // Fire async requests in parallel
            CompletableFuture<List<Event>> eventsFuture = eventService.getTopFeaturedGamesAsync(10);
            CompletableFuture<List<Venue>> venuesFuture = venueService.getTopVenuesAsync(10);
            CompletableFuture<List<TrainingCenter>> centersFuture = trainingCenterService.getTopTrainingCentersAsync(10);

            // Wait for all to complete
            CompletableFuture.allOf(eventsFuture, venuesFuture, centersFuture).join();

            List<Event> featuredEvents = eventsFuture.join();
            List<Venue> topVenues = venuesFuture.join();
            List<TrainingCenter> topCenters = centersFuture.join();

            // Filtering (parallelStream for extra boost)
            List<Event> eventsWithImages = featuredEvents.parallelStream()
                    .filter(e -> e.getImagePath() != null && e.getImagePath().startsWith("https://res.cloudinary.com"))
                    .toList();

            List<Venue> venuesWithImages = topVenues.parallelStream()
                    .filter(v -> v.getImagePath() != null && v.getImagePath().startsWith("https://res.cloudinary.com"))
                    .toList();

            List<TrainingCenter> centersWithImages = topCenters.parallelStream()
                    .filter(tc -> tc.getImageUrl() != null && tc.getImageUrl().startsWith("https://res.cloudinary.com"))
                    .toList();

            model.addAttribute("featuredGames", eventsWithImages);
            model.addAttribute("topVenues", venuesWithImages);
            model.addAttribute("trainingCenters", centersWithImages);

        } else {
            Long userId = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getId();

            // Fire async requests in parallel
            CompletableFuture<List<Event>> eventsFuture = userInteractionService.getPersonalizedFeaturedEventsAsync(userId);
            CompletableFuture<List<Venue>> venuesFuture = userInteractionService.getPersonalizedVenuesAsync(userId);
            CompletableFuture<List<TrainingCenter>> centersFuture = userInteractionService.getPersonalizedTrainingCenterAsync(userId);

            // Wait for all to complete
            CompletableFuture.allOf(eventsFuture, venuesFuture, centersFuture).join();

            List<Event> personalizedEvents = eventsFuture.join();
            List<Venue> personalizedVenues = venuesFuture.join();
            List<TrainingCenter> personalizedCenters = centersFuture.join();

            List<Event> filteredEvents = personalizedEvents.parallelStream()
                    .filter(e -> e.getImagePath() != null && e.getImagePath().startsWith("https://res.cloudinary.com"))
                    .toList();

            List<Venue> filteredVenues = personalizedVenues.parallelStream()
                    .filter(v -> v.getImagePath() != null && v.getImagePath().startsWith("https://res.cloudinary.com"))
                    .toList();

            List<TrainingCenter> filteredCenters = personalizedCenters.parallelStream()
                    .filter(tc -> tc.getImageUrl() != null && tc.getImageUrl().startsWith("https://res.cloudinary.com"))
                    .toList();

            model.addAttribute("featuredGames", filteredEvents);
            model.addAttribute("topVenues", filteredVenues);
            model.addAttribute("trainingCenters", filteredCenters);
        }

        return "user-home"; // Thymeleaf template
    }

}












