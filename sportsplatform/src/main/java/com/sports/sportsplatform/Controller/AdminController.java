package com.sports.sportsplatform.Controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sports.sportsplatform.Config.CloudinaryConfig;
import com.sports.sportsplatform.Model.*;
import com.sports.sportsplatform.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import  com.sports.sportsplatform.Config.CloudinaryConfig;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final StateService stateService;
    private final GameService gameService;
    private final EventService eventService;

    private final CertificateService certificateService;
    private final MedalService medalService;


    private final RankingService rankingService;

    private final VenueService venueService;

    private final Cloudinary cloudinary;


    // ✅ Unified Dashboard (All forms live here)

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Get all medals and convert images to Base64 strings
        List<Medal> medals = medalService.getAllMedals();
        List<String> medalImagesBase64 = medals.stream()
                .map(medal -> {
                    byte[] img = medal.getImageUrl();
                    if (img != null) {
                        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(img);
                    }
                    return null;
                })
                .collect(Collectors.toList());

        // Get all certificates and convert images to Base64 strings
        List<Certificate> certificates = certificateService.getAllCertificates();
        List<String> certificateImagesBase64 = certificates.stream()
                .map(cert -> {
                    byte[] img = cert.getImageUrl();
                    if (img != null) {
                        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(img);
                    }
                    return null;
                })
                .collect(Collectors.toList());

        // Add data to model
        model.addAttribute("medals", medals);
        model.addAttribute("medalImages", medalImagesBase64);
        model.addAttribute("certificates", certificates);
        model.addAttribute("certificateImages", certificateImagesBase64);

        // Existing attributes you had
        model.addAttribute("state", new State());
        model.addAttribute("game", new Game());
        model.addAttribute("event", new Event());
        model.addAttribute("venue", new Venue());


        model.addAttribute("games", gameService.getAllGames());
        model.addAttribute("states", stateService.getAllStates());

        model.addAttribute("events", eventService.getAllEvents());
        model.addAttribute("venues", venueService.getAllVenues());


        return "admin-dashboard";
    }


    // Controller Class

    @PostMapping("/verify-medal")
    public String verifyMedal(@RequestParam Long medalId,
                              @RequestParam String action) {
        boolean verified = action.equals("approve");
        medalService.updateMedalVerification(medalId, verified);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/verify-certificate")
    public String verifyCertificate(@RequestParam Long certificateId,
                                    @RequestParam String action) {
        boolean verified = action.equals("approve");
        certificateService.verifyCertificate(certificateId, verified);
        return "redirect:/admin/dashboard";
    }




    // ✅ Save State
    @PostMapping("/add-state")
    public String saveState(@ModelAttribute State state) {
        try {
            stateService.saveState(state);
            return "redirect:/admin/dashboard?successState=true";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?errorState=true";
        }
    }

    // ✅ Save Game
    @PostMapping("/add-game")
    public String saveGame(@ModelAttribute Game game) {
        try {
            gameService.saveGame(game);
            return "redirect:/admin/dashboard?successGame=true";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?errorGame=true";
        }
    }

    // ✅ Save Event
    @PostMapping("/add-event")
    public String saveEvent(@ModelAttribute Event event,
                            @RequestParam("image") MultipartFile imageFile) {
        try {
            // ✅ Upload image to Cloudinary

            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
                    ObjectUtils.asMap("folder", "event_images")); // folder on Cloudinary

            // ✅ Get the secure URL from the response
            String imageUrl = (String) uploadResult.get("secure_url");

            // ✅ Set the image URL in the event object
            event.setImagePath(imageUrl);

            // ✅ Save the event to the database
            eventService.saveEvent(event);

            return "redirect:/admin/dashboard?successEvent=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/dashboard?errorEvent=true";
        }
    }



    @PostMapping("/add-venue")
    public String saveVenue(@ModelAttribute Venue venue,
                            @RequestParam("venueImage") MultipartFile venueImage) {
        try {
            // ✅ Upload venue image to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(venueImage.getBytes(),
                    ObjectUtils.asMap("folder", "venue_images"));

            String imageUrl = (String) uploadResult.get("secure_url");
            venue.setImagePath(imageUrl);

            // ✅ Save venue
            venueService.saveVenue(venue);

            return "redirect:/admin/dashboard?successVenue=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/dashboard?errorVenue=true";
        }
    }




    @GetMapping("/update-rankings")
    public String updateRankings() {
        rankingService.updateUserRankings();
        return "redirect:/admin/dashboard";
    }




}
