package com.sports.sportsplatform.Controller;

import com.sports.sportsplatform.Model.Game;
import com.sports.sportsplatform.Model.User;
import com.sports.sportsplatform.Model.UserSettings;
import com.sports.sportsplatform.Repository.GameRepository;
import com.sports.sportsplatform.Repository.StateRepository;
import com.sports.sportsplatform.Repository.UserRepository;
import com.sports.sportsplatform.Repository.UserSettingsRepository;
import com.sports.sportsplatform.Service.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/settings")
public class SettingController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSettingsRepository userSettingsRepository;


    @Autowired
    private UserSettingsService userSettingsService;

    @Autowired
    private StateRepository stateRepository;


    // Show the settings page
    @GetMapping
    public String showSettingsPage(Model model, Principal principal) {
        String email = principal.getName();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return "redirect:/login"; // safety check
        }

        User user = optionalUser.get();

        // Get all available games
        List<Game> games = gameRepository.findAll();

        // Find or create user settings
        UserSettings settings = userSettingsRepository.findByUser(user)
                .orElseGet(() -> {
                    UserSettings newSettings = new UserSettings();
                    newSettings.setUser(user);
                    return newSettings;
                });

        model.addAttribute("user", user);
        model.addAttribute("settings", settings);
        model.addAttribute("games", games);

        model.addAttribute("states", stateRepository.findAll());


        return "setting"; // refers to setting.html
    }

//    // Save the settings
//    @PostMapping("/update-preferences")
//    public String updatePreferences(
//            Principal principal,
//            @RequestParam String prefer1,
//            @RequestParam String prefer2,
//            @RequestParam String prefer3,
//            RedirectAttributes redirectAttributes
//    ) {
//        String email = principal.getName();
//        userSettingsService.updatePreferences(email, prefer1, prefer2, prefer3);
//        redirectAttributes.addFlashAttribute("preferenceSuccess", "✅ Preferences saved successfully.");
//        return "redirect:/settings";
//    }

//    @PostMapping("/update-notifications")
//    public String updateNotifications(
//            Principal principal,
//            @RequestParam List<String> games,
//            RedirectAttributes redirectAttributes
//    ) {
//        String email = principal.getName();
//        userSettingsService.updateNotificationGames(email, games);
//        redirectAttributes.addFlashAttribute("notificationSuccess", "✅ Email Notifications updated successfully.");
//        return "redirect:/settings";
//    }



    @PostMapping("/update-notifications")
    public String updateNotifications(
            Principal principal, // logged-in user
            @RequestParam(value = "stateId", required = false) Long stateId,
            @RequestParam(value = "districtId", required = false) Long districtId,
            @RequestParam(value = "gameIds", required = false) List<Long> gameIds,
            RedirectAttributes redirectAttributes
    ) {
        // Step 1: Get user's email from Principal
        String email = principal.getName();

        // Step 2: Send to service to update DB
        userSettingsService.updateNotificationGames(email, stateId, districtId, gameIds);

        // Step 3: Send feedback message to UI
        redirectAttributes.addFlashAttribute("notificationSuccess",
                "✅ Email Notifications updated successfully.");

        // Step 4: Redirect to settings page
        return "redirect:/settings";
    }




    @PostMapping("/update-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {

        String email = principal.getName();
        boolean success = userSettingsService.updatePassword(email, oldPassword, newPassword);

        if (!success) {
            redirectAttributes.addFlashAttribute("passwordError", "❌ Old password is incorrect.");
        } else {
            redirectAttributes.addFlashAttribute("passwordSuccess", "✅ Password updated successfully.");
        }

        return "redirect:/settings";  // This will now redirect instead of returning view
    }




}
