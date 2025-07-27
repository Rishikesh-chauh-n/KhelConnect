package com.sports.sportsplatform.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sports.sportsplatform.Model.Event;
import com.sports.sportsplatform.Model.Game;
import com.sports.sportsplatform.Model.State;
import com.sports.sportsplatform.Model.User;
import com.sports.sportsplatform.Repository.UserRepository;
import com.sports.sportsplatform.Service.EventService;
import com.sports.sportsplatform.Service.GameService;
import com.sports.sportsplatform.Service.StateService;
import com.sports.sportsplatform.Service.UserInteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final GameService gameService;
    private final StateService stateService;
    private final EventService eventService;

    private final UserInteractionService userInteractionService;

    private final UserRepository userRepository;

    @GetMapping("/api/search/suggestions")
    @ResponseBody
    public List<String> getSuggestions(@RequestParam("term") String term) {
        List<String> suggestions = new ArrayList<>();

        for (Game game : gameService.searchGames(term)) {
            if (!suggestions.contains(game.getName())) {
                suggestions.add(game.getName());
            }
        }

        for (State state : stateService.searchStates(term)) {
            if (!suggestions.contains(state.getName())) {
                suggestions.add(state.getName());
            }
        }

        return suggestions.size() > 10 ? suggestions.subList(0, 10) : suggestions;
    }






    @GetMapping("/results")
    public String searchResults(@RequestParam("query") String query, RedirectAttributes redirectAttributes) {
        List<Game> matchingGames = gameService.searchGames(query);
        List<State> matchingStates = stateService.searchStates(query);

        if (!matchingGames.isEmpty()) {
            // Redirect to the first matching game's details page
            return "redirect:/games/" + matchingGames.get(0).getId();
        } else if (!matchingStates.isEmpty()) {
            // Redirect to the first matching state's details page
            return "redirect:/states/" + matchingStates.get(0).getId();
        } else {
            // If nothing found, redirect to homepage with an error message
            redirectAttributes.addFlashAttribute("message", "No match found for: " + query);
            return "redirect:/";
        }
    }



//    @GetMapping("/games/{id}")
//    public String showGameDetails(@PathVariable Long id, Model model) {
//        Game game = gameService.getGameById(id);
//        List<Event> events = eventService.getEventsByGameId(id);
//
//        model.addAttribute("game", game);
//        model.addAttribute("events", events); // This is sufficient if you use Thymeleaf to render in HTML
//        return "game-details";
//    }




    @GetMapping("/games/{id}")
    public String showGameDetails(@PathVariable Long id, Model model, Principal principal) {
        Game game = gameService.getGameById(id);
        List<Event> events = eventService.getEventsByGameId(id);

        model.addAttribute("game", game);
        model.addAttribute("events", events);

        // ✅ Save user interaction here
        if (principal != null) {
            String email = principal.getName(); // Principal returns email
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                userInteractionService.saveInteraction(user,  game.getName());
            }
        }

        return "game-details";
    }





//    @GetMapping("/states/{id}")
//    public String showStateDetails(@PathVariable Long id, Model model) {
//        State state = stateService.getStateById(id);
//        String stateName = state.getName(); // Assuming your State entity has a getName() method
//
//        List<Event> events = eventService.getEventsByLocation(stateName); // 🔥 Use state name, not ID
//
//        model.addAttribute("state", state);
//        model.addAttribute("events", events);
//        return "state-details";
//    }


    @GetMapping("/states/{id}")
    public String showStateDetails(@PathVariable Long id, Model model, Principal principal) {
        // Fetch the state using the ID
        State state = stateService.getStateById(id);
        String stateName = state.getName();

        // Get events based on the state's name (location)
        List<Event> events = eventService.getEventsByLocation(stateName);

        // Add state and events to the model
        model.addAttribute("state", state);
        model.addAttribute("events", events);

        // ✅ Fetch user using Principal (just like your settings method)
        if (principal != null) {
            String email = principal.getName();
            Optional<User> optionalUser = userRepository.findByEmail(email); // use repository directly

            if (optionalUser.isEmpty()) {
                return "redirect:/login"; // fallback, shouldn't happen if user is logged in
            }

            User user = optionalUser.get();

            // ✅ Save interaction
            userInteractionService.saveInteraction(user,  stateName);
        }

        return "state-details"; // refers to state-details.html
    }







}
