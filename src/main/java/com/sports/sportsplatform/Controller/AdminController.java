package com.sports.sportsplatform.Controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sports.sportsplatform.Config.CloudinaryConfig;
import com.sports.sportsplatform.Model.*;
import com.sports.sportsplatform.Model.LearnandTrain.Achievement;
import com.sports.sportsplatform.Model.LearnandTrain.Trainee;
import com.sports.sportsplatform.Model.LearnandTrain.TrainingCenter;
import com.sports.sportsplatform.Model.Venues.Venue;
import com.sports.sportsplatform.Model.Venues.VenueGameDetails;
import com.sports.sportsplatform.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import  com.sports.sportsplatform.Config.CloudinaryConfig;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final StateService stateService;
    private final GameService gameService;
    private final EventService eventService;

    private final CertificateService certificateService;
    private final MedalService medalService;

    private final EmailSchedulerService emailSchedulerService;


    private final RankingService rankingService;

    private final VenueService venueService;

    private final Cloudinary cloudinary;

    private final DistrictService districtService;

    private final TrainingCenterService trainingCenterService;



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



        Venue venue = new Venue();

        venue.getGameDetailsList().add(new VenueGameDetails());


        model.addAttribute("districts", districtService.getAllDistricts());










        // TrainingCenter data is here

        TrainingCenter center = new TrainingCenter();

        // Initialize 1 trainee
        Trainee trainee = new Trainee();

        // Initialize 1 blank achievement for the trainee
        Achievement achievement = new Achievement();
        trainee.setAchievements(new ArrayList<>());
        trainee.getAchievements().add(achievement);

        // Add trainee to center
        trainee.setTrainingCenter(center); // Important to set the link back
        center.setTrainees(new ArrayList<>());
        center.getTrainees().add(trainee);

        // Finally add to model
        model.addAttribute("center", center);




        model.addAttribute("games", gameService.getAllGames());
        model.addAttribute("states", stateService.getAllStates());

        model.addAttribute("events", eventService.getAllEvents());
        model.addAttribute("venue", venue);











        return "admin-dashboard";
    }








    @GetMapping("/save-training-center")
    public String addTrainingCenter(Model model) {
        TrainingCenter center = new TrainingCenter();

        // Initialize 1 trainee
        Trainee trainee = new Trainee();

        // Initialize 1 blank achievement for the trainee
        Achievement achievement = new Achievement();
        trainee.setAchievements(new ArrayList<>());
        trainee.getAchievements().add(achievement);

        // Add trainee to center
        trainee.setTrainingCenter(center); // Important to set the link back
        center.setTrainees(new ArrayList<>());
        center.getTrainees().add(trainee);

        model.addAttribute("games", gameService.getAllGames());

        model.addAttribute("states", stateService.getAllStates());

        // Finally add to model
        model.addAttribute("center", center);

        return "add-training-center";
    }



//    @PostMapping("/save-training-center")
//    public String saveTrainingCenter(
//            @ModelAttribute TrainingCenter center,
//            @RequestParam("image") MultipartFile centerImage,
//            @RequestParam(value = "traineeImage", required = false) List<MultipartFile> traineeImages,
//            @RequestParam(value = "certificateImage", required = false) List<MultipartFile> certificateImages,
//            @RequestParam(value = "manualDistrictName", required = false) String manualDistrictName,
//            @RequestParam(value = "stateId", required = false) Long stateId,
//            @RequestParam(value = "sportsOffered", required = false) List<Long> selectedGameIds
//    ) {
//        try {
//            // ✅ Upload center image
//            if (!centerImage.isEmpty()) {
//                Map uploadResult = cloudinary.uploader().upload(centerImage.getBytes(),
//                        ObjectUtils.asMap("folder", "training_centers"));
//                center.setImageUrl((String) uploadResult.get("secure_url"));
//            }
//
//            // ✅ Set State for TrainingCenter
//            if (stateId != null) {
//                State state = stateService.getStateById(stateId);
//                center.setState(state); // <-- IMPORTANT
//            }
//
//            // ✅ Handle district
//            if (manualDistrictName != null && !manualDistrictName.trim().isEmpty()) {
//                Optional<District> existingDistrictOpt = districtService.findByNameAndStateId(manualDistrictName.trim(), stateId);
//                if (existingDistrictOpt.isPresent()) {
//                    center.setDistrict(existingDistrictOpt.get());
//                } else {
//                    District newDistrict = new District();
//                    newDistrict.setName(manualDistrictName.trim());
//                    if (stateId != null) {
//                        newDistrict.setState(center.getState());
//                    }
//                    District savedDistrict = districtService.saveDistrict(newDistrict);
//                    center.setDistrict(savedDistrict);
//                }
//            } else if (center.getDistrict() != null && center.getDistrict().getId() != null) {
//                center.setDistrict(districtService.getDistrictById(center.getDistrict().getId()));
//            } else {
//                return "redirect:/admin/save-training-center?errorCenter=NoDistrict";
//            }
//
//            // ✅ Set Sports offered
//            if (selectedGameIds != null && !selectedGameIds.isEmpty()) {
//                center.setSportsOffered(gameService.getGamesByIds(selectedGameIds));
//            } else {
//                center.setSportsOffered(new ArrayList<>());
//            }
//
//            // ✅ Link trainees & achievements
//            if (center.getTrainees() != null && traineeImages != null) {
//                for (int i = 0; i < center.getTrainees().size(); i++) {
//                    Trainee trainee = center.getTrainees().get(i);
//
//                    // ✅ Link trainee to training center
//                    trainee.setTrainingCenter(center);
//
//                    if (traineeImages.size() > i && !traineeImages.get(i).isEmpty()) {
//                        Map uploadResult = cloudinary.uploader().upload(
//                                traineeImages.get(i).getBytes(),
//                                ObjectUtils.asMap("folder", "trainee_images"));
//                        trainee.setImageUrl((String) uploadResult.get("secure_url"));
//                    }
//
//                    if (trainee.getAchievements() != null && certificateImages != null) {
//                        for (int j = 0; j < trainee.getAchievements().size(); j++) {
//                            Achievement achievement = trainee.getAchievements().get(j);
//
//                            // ✅ Link achievement to trainee
//                            achievement.setTrainee(trainee);
//
//                            int certificateIndex = i * trainee.getAchievements().size() + j;
//                            if (certificateImages.size() > certificateIndex &&
//                                    !certificateImages.get(certificateIndex).isEmpty()) {
//                                Map certUploadResult = cloudinary.uploader().upload(
//                                        certificateImages.get(certificateIndex).getBytes(),
//                                        ObjectUtils.asMap("folder", "certificate_images"));
//                                achievement.setCertificateImageUrl((String) certUploadResult.get("secure_url"));
//                            }
//                        }
//                    }
//                }
//            }
//
//            // ✅ Save the training center
//            trainingCenterService.saveTrainingCenter(center);
//
//            return "redirect:/admin/save-training-center?successCenter=true";
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "redirect:/admin/save-training-center?errorCenter=true";
//        }
//    }
//

    @PostMapping("/save-training-center")
    public String saveTrainingCenter(
            @ModelAttribute TrainingCenter center,
            @RequestParam("image") MultipartFile centerImage,
            @RequestParam(value = "traineeImage", required = false) List<MultipartFile> traineeImages,
            @RequestParam(value = "certificateImage", required = false) List<MultipartFile> certificateImages,
            @RequestParam(value = "manualDistrictName", required = false) String manualDistrictName,
            @RequestParam(value = "stateId", required = false) Long stateId,
            @RequestParam(value = "sportsOffered", required = false) List<Long> selectedGameIds
    ) {
        try {
            // ✅ Upload TrainingCenter image
            if (!centerImage.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(centerImage.getBytes(),
                        ObjectUtils.asMap("folder", "training_centers"));
                center.setImageUrl((String) uploadResult.get("secure_url"));
            }

            // ✅ State (must exist if district is linked)
            State state = null;
            if (stateId != null) {
                state = stateService.getStateById(stateId);
                center.setState(state);
            }

            // ✅ Handle District
            if (manualDistrictName != null && !manualDistrictName.trim().isEmpty()) {
                // check existing district
                Optional<District> existingDistrictOpt = districtService.findByNameAndStateId(
                        manualDistrictName.trim(), stateId);

                District district;
                if (existingDistrictOpt.isPresent()) {
                    district = existingDistrictOpt.get();
                } else {
                    // create new district under this state
                    district = new District();
                    district.setName(manualDistrictName.trim());
                    district.setState(state);
                    district = districtService.saveDistrict(district);
                }
                center.setDistrict(district);

            } else if (center.getDistrict() != null && center.getDistrict().getId() != null) {
                // existing district selected from dropdown
                center.setDistrict(districtService.getDistrictById(center.getDistrict().getId()));
            } else {
                // neither selected nor entered
                return "redirect:/admin/save-training-center?errorCenter=NoDistrict";
            }

            // ✅ Sports Offered
            if (selectedGameIds != null && !selectedGameIds.isEmpty()) {
                center.setSportsOffered(gameService.getGamesByIds(selectedGameIds));
            } else {
                center.setSportsOffered(new ArrayList<>());
            }

            // ✅ Trainees & Achievements
            if (center.getTrainees() != null && traineeImages != null) {
                for (int i = 0; i < center.getTrainees().size(); i++) {
                    Trainee trainee = center.getTrainees().get(i);
                    trainee.setTrainingCenter(center);

                    if (traineeImages.size() > i && !traineeImages.get(i).isEmpty()) {
                        Map uploadResult = cloudinary.uploader().upload(
                                traineeImages.get(i).getBytes(),
                                ObjectUtils.asMap("folder", "trainee_images"));
                        trainee.setImageUrl((String) uploadResult.get("secure_url"));
                    }

                    if (trainee.getAchievements() != null && certificateImages != null) {
                        for (int j = 0; j < trainee.getAchievements().size(); j++) {
                            Achievement achievement = trainee.getAchievements().get(j);
                            achievement.setTrainee(trainee);

                            int certIndex = i * trainee.getAchievements().size() + j;
                            if (certificateImages.size() > certIndex &&
                                    !certificateImages.get(certIndex).isEmpty()) {
                                Map certUploadResult = cloudinary.uploader().upload(
                                        certificateImages.get(certIndex).getBytes(),
                                        ObjectUtils.asMap("folder", "certificate_images"));
                                achievement.setCertificateImageUrl((String) certUploadResult.get("secure_url"));
                            }
                        }
                    }
                }
            }

            // ✅ Save TrainingCenter
            trainingCenterService.saveTrainingCenter(center);

            return "redirect:/admin/save-training-center?successCenter=true";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/save-training-center?errorCenter=true";
        }
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

//    // ✅ Save Event
//    @PostMapping("/add-event")
//    public String saveEvent(@ModelAttribute Event event,
//                            @RequestParam("image") MultipartFile imageFile) {
//        try {
//            // ✅ Upload image to Cloudinary
//
//            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
//                    ObjectUtils.asMap("folder", "event_images")); // folder on Cloudinary
//
//            // ✅ Get the secure URL from the response
//            String imageUrl = (String) uploadResult.get("secure_url");
//
//            // ✅ Set the image URL in the event object
//            event.setImagePath(imageUrl);
//
//            // ✅ Save the event to the database
//            eventService.saveEvent(event);
//
//            return "redirect:/admin/dashboard?successEvent=true";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "redirect:/admin/dashboard?errorEvent=true";
//        }
//    }



//    @Transactional
//    @PostMapping("/add-event")
//    public String saveEvent(@ModelAttribute Event event,
//                            @RequestParam("image") MultipartFile imageFile,
//                            @RequestParam(value = "districtId", required = false) Long districtId,
//                            @RequestParam(value = "manualDistrict", required = false) String manualDistrict) {
//        try {
//            District district = null;
//
//            // ✅ Handle district selection or creation
//            if (districtId != null) {
//                district = districtService.getDistrictById(districtId);
//            } else if (manualDistrict != null && !manualDistrict.trim().isEmpty()) {
//                district = districtService.findByNameAndStateId(manualDistrict.trim(), event.getState().getId())
//                        .orElseGet(() -> {
//                            District newDistrict = new District();
//                            newDistrict.setName(manualDistrict.trim());
//                            newDistrict.setState(event.getState());
//                            return districtService.saveDistrict(newDistrict);
//                        });
//            } else {
//                throw new RuntimeException("District is required");
//            }
//
//            event.setDistrict(district);
//
//            // ✅ Upload image
//            Map uploadResult = cloudinary.uploader().upload(
//                    imageFile.getBytes(),
//                    ObjectUtils.asMap("folder", "event_images")
//            );
//            event.setImagePath((String) uploadResult.get("secure_url"));
//
//            // ✅ Save the event
//            eventService.saveEvent(event);
//
//            return "redirect:/admin/dashboard?successEvent=true";
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "redirect:/admin/dashboard?errorEvent=true";
//        }
//    }


    @Transactional
    @PostMapping("/add-event")
    public String saveEvent(@ModelAttribute Event event,
                            @RequestParam("image") MultipartFile imageFile,
                            @RequestParam(value = "stateId", required = false) Long stateId,
                            @RequestParam(value = "districtId", required = false) Long districtId,
                            @RequestParam(value = "manualDistrict", required = false) String manualDistrict) {
        try {
            if (stateId == null) {
                throw new RuntimeException("State is required");
            }

            State state = stateService.getStateById(stateId);
            event.setState(state); // ✅ ensure Event has a State

            District district = null;

            // Handle district selection or creation
            if (districtId != null) {
                district = districtService.getDistrictById(districtId);
            } else if (manualDistrict != null && !manualDistrict.trim().isEmpty()) {
                district = districtService.findByNameAndStateId(manualDistrict.trim(), state.getId())
                        .orElseGet(() -> {
                            District newDistrict = new District();
                            newDistrict.setName(manualDistrict.trim());
                            newDistrict.setState(state);
                            return districtService.saveDistrict(newDistrict);
                        });
            } else {
                throw new RuntimeException("District is required");
            }

            event.setDistrict(district);

            // Upload image
            Map uploadResult = cloudinary.uploader().upload(
                    imageFile.getBytes(),
                    ObjectUtils.asMap("folder", "event_images")
            );
            event.setImagePath((String) uploadResult.get("secure_url"));

            // Save the event
            eventService.saveEvent(event);

            emailSchedulerService.scheduleEventEmail(event.getId(), 1);

            return "redirect:/admin/dashboard?successEvent=true";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/dashboard?errorEvent=true";
        }
    }


    @GetMapping("/save-venue")
    public String showAddVenueForm(Model model) {
        Venue venue = new Venue();

        // Add a default row so Thymeleaf can render game details
        venue.getGameDetailsList().add(new VenueGameDetails());

        model.addAttribute("venue", venue);
        model.addAttribute("states", stateService.getAllStates());
        model.addAttribute("districts", districtService.getAllDistricts());
        model.addAttribute("games", gameService.getAllGames());
        model.addAttribute("events", eventService.getAllEvents());

        return "add-venue"; // 👈 renders templates/admin/add-venue.html
    }



//
//    @PostMapping("/add-venuedetail")
//    public String saveVenue(@ModelAttribute Venue venue,
//                            @RequestParam("venueImage") MultipartFile venueImage,
//                            @RequestParam(value = "manualDistrictName", required = false) String manualDistrictName,
//                            @RequestParam(value = "stateId", required = false) Long stateId,
//                            @RequestParam(value = "amenitiesInput", required = false) String amenitiesInput,
//                            RedirectAttributes redirectAttributes) {
//
//        System.out.println("manualDistrictName: " + manualDistrictName);
//
//        try {
//            // ✅ Upload image
//            if (!venueImage.isEmpty()) {
//                Map uploadResult = cloudinary.uploader().upload(venueImage.getBytes(),
//                        ObjectUtils.asMap("folder", "venue_images"));
//                String imageUrl = (String) uploadResult.get("secure_url");
//                venue.setImagePath(imageUrl);
//            }
//
//            // ✅ Handle amenities manually
//            if (amenitiesInput != null && !amenitiesInput.isBlank()) {
//                List<String> amenities = Arrays.stream(amenitiesInput.split(","))
//                        .map(String::trim)
//                        .filter(s -> !s.isEmpty())
//                        .collect(Collectors.toList());
//                venue.setAmenities(amenities);
//            }
//
//            if (manualDistrictName != null && !manualDistrictName.trim().isEmpty()) {
//                String trimmedDistrictName = manualDistrictName.trim();
//
//                // ✅ Check if district already exists in the DB for the given state
//                Optional<District> existingDistrictOpt = districtService.findByNameAndStateId(trimmedDistrictName, stateId);
//
//                if (existingDistrictOpt.isPresent()) {
//                    venue.setDistrict(existingDistrictOpt.get());
//                } else {
//                    // If not found, then create a new one
//                    District newDistrict = new District();
//                    newDistrict.setName(trimmedDistrictName);
//
//                    if (stateId != null) {
//                        State state = stateService.getStateById(stateId);
//                        newDistrict.setState(state);
//                    }
//
//                    District savedDistrict = districtService.saveDistrict(newDistrict);
//                    venue.setDistrict(savedDistrict);
//                }
//
//            } else if (venue.getDistrict() != null && venue.getDistrict().getId() != null) {
//                District selectedDistrict = districtService.getDistrictById(venue.getDistrict().getId());
//                venue.setDistrict(selectedDistrict);
//            } else {
//                redirectAttributes.addFlashAttribute("error", "Please select or enter a district.");
//                return "redirect:/admin/save-venue";
//            }
//
//            // ✅ Link game details to venue so venue_id is not null
//            if (venue.getGameDetailsList() != null) {
//                for (VenueGameDetails detail : venue.getGameDetailsList()) {
//                    detail.setVenue(venue);
//                }
//            }
//
//// ✅ Save venue
//            venueService.saveVenue(venue);
//
//
//
//            return "redirect:/admin/save-venue?successVenue=true";
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "redirect:/admin/save-venue?errorVenue=true";
//        }
//    }
//



@PostMapping("/add-venuedetail")
public String saveVenue(@ModelAttribute Venue venue,
                        @RequestParam("venueImage") MultipartFile venueImage,
                        @RequestParam(value = "manualDistrictName", required = false) String manualDistrictName,
                        @RequestParam(value = "stateId", required = false) Long stateId,
                        @RequestParam(value = "amenitiesInput", required = false) String amenitiesInput,
                        RedirectAttributes redirectAttributes) {

    System.out.println("manualDistrictName: " + manualDistrictName);

    try {
        // ✅ Upload image
        if (!venueImage.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(venueImage.getBytes(),
                    ObjectUtils.asMap("folder", "venue_images"));
            String imageUrl = (String) uploadResult.get("secure_url");
            venue.setImagePath(imageUrl);
        }

        // ✅ Handle amenities manually
        if (amenitiesInput != null && !amenitiesInput.isBlank()) {
            List<String> amenities = Arrays.stream(amenitiesInput.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            venue.setAmenities(amenities);
        }

        // ✅ Handle district
        if (manualDistrictName != null && !manualDistrictName.trim().isEmpty()) {
            String trimmedDistrictName = manualDistrictName.trim();

            // Check if district already exists in the DB for the given state
            Optional<District> existingDistrictOpt = districtService.findByNameAndStateId(trimmedDistrictName, stateId);

            if (existingDistrictOpt.isPresent()) {
                venue.setDistrict(existingDistrictOpt.get());
            } else {
                // Create new district
                District newDistrict = new District();
                newDistrict.setName(trimmedDistrictName);

                if (stateId != null) {
                    State state = stateService.getStateById(stateId);
                    newDistrict.setState(state);

                    // Save district directly
                    newDistrict = districtService.saveDistrict(newDistrict);

                    venue.setDistrict(newDistrict);
                }
            }

        } else if (venue.getDistrict() != null && venue.getDistrict().getId() != null) {
            District selectedDistrict = districtService.getDistrictById(venue.getDistrict().getId());
            venue.setDistrict(selectedDistrict);
        } else {
            redirectAttributes.addFlashAttribute("error", "Please select or enter a district.");
            return "redirect:/admin/save-venue";
        }

        // ✅ Link game details to venue so venue_id is not null
        if (venue.getGameDetailsList() != null) {
            for (VenueGameDetails detail : venue.getGameDetailsList()) {
                detail.setVenue(venue);
            }
        }

        // ✅ Save venue
        venueService.saveVenue(venue);

        return "redirect:/admin/save-venue?successVenue=true";

    } catch (Exception e) {
        e.printStackTrace();
        return "redirect:/admin/save-venue?errorVenue=true";
    }
}





    @GetMapping("/update-rankings")
    public String updateRankings() {
        rankingService.updateUserRankings();
        return "redirect:/admin/dashboard";
    }




}
