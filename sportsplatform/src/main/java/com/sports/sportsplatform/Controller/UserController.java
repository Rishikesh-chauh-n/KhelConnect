package com.sports.sportsplatform.Controller;

import com.sports.sportsplatform.Model.Event;
import com.sports.sportsplatform.Model.Game;
import com.sports.sportsplatform.Service.EventService;
import com.sports.sportsplatform.Service.GameService;
import com.sports.sportsplatform.Service.UserInteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final GameService gameService;
    private final EventService eventService;
    private final UserInteractionService userInteractionService;

    @GetMapping("/home")
    public String userHome(Model model, Principal principal) {
        String username = principal.getName(); // Current logged-in user

        // ✅ Always show all games and all events
        List<Game> allGames = gameService.getAllGames();
        List<Event> allEvents = eventService.getAllEvents();

        model.addAttribute("games", allGames);
        model.addAttribute("events", allEvents);


//
        boolean isFirstTimeUser = userInteractionService.isFirstTimeUser(username);
//
        if (isFirstTimeUser) {
            // Fetch events with Cloudinary images
            List<Event> featuredEvents = eventService.getTopFeaturedGames(10);

            for (Event event : featuredEvents) {
                System.out.println("Event Name: " + event.getName());
                System.out.println("Image Path: " + event.getImagePath());
            }


            List<Event> eventsWithImages = featuredEvents.stream()
                    .filter(e -> e.getImagePath() != null && !e.getImagePath().isEmpty()
                            && e.getImagePath().startsWith("https://res.cloudinary.com"))
                    .collect(Collectors.toList());

            model.addAttribute("featuredGames", eventsWithImages);
            System.out.println("Featured Games :" + eventsWithImages);
        }

//            List<Event> learnAndTrain = LearnandTrain.getLearnAndTrainPrograms(5);
//
//            model.addAttribute("featuredGames", featuredGames);
//            model.addAttribute("topVenues", topVenues);
//            model.addAttribute("learnAndTrain", learnAndTrain);
//        } else {
//            // Show personalized featured sections based on user interaction
            //            List<Game> personalizedGames = gameService.getGamesBasedOnUser(username);
            //            List<Event> personalizedVenues = eventService.getVenuesBasedOnUser(username);
            //            List<Event> personalizedLearn = eventService.getTrainingBasedOnUser(username);
            //
            //            model.addAttribute("featuredGames", personalizedGames);
            //            model.addAttribute("topVenues", personalizedVenues);
            //            model.addAttribute("learnAndTrain", personalizedLearn);
            //        }

            return "user-home"; // user-home.html in templates
        }

    }








