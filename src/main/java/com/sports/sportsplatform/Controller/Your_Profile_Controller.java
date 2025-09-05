package com.sports.sportsplatform.Controller;



import com.sports.sportsplatform.Model.*;
import com.sports.sportsplatform.Repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class Your_Profile_Controller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private MedalRepository medalRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private GameRepository gameRepository;

    @GetMapping("/your-profile")
    public String viewProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = userProfileRepository.findByUser(user).orElse(null);

        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
            profile = userProfileRepository.save(profile); // ✅ Save new profile
        }

        List<Medal> medals = medalRepository.findByUserProfile(profile);
        List<Certificate> certificates = certificateRepository.findByUserProfile(profile);

        List<Game> games = gameRepository.findAll();
        model.addAttribute("games", games);


        model.addAttribute("user", user);
        model.addAttribute("profile", profile);
        model.addAttribute("medals", medals);
        model.addAttribute("certificates", certificates);

        return "Your-Profile";
    }




    // ✅ Select Game
    @PostMapping("/select-game")
    public String selectGame(@RequestParam("gameId") Long gameId, Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserProfile profile = userProfileRepository.findByUser(user)
                .orElse(new UserProfile());

        Game selGame = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        profile.setUser(user);
        profile.setSelectedGame(selGame);
        userProfileRepository.save(profile);

        return "redirect:/user/your-profile";
    }



    // 🏅 Upload Medal
    @PostMapping("/upload-medal")
    public String uploadMedal(@RequestParam("type") String type,
                              @RequestParam("level") String level,
                              @RequestParam("image") MultipartFile image,
                              Principal principal) throws IOException {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserProfile profile = userProfileRepository.findByUser(user).orElseThrow();

        Medal medal = new Medal();
        medal.setType(type);
        medal.setLevel(level);
        medal.setVerified(false);
        medal.setUserProfile(profile);
        medal.setImageUrl(image.getBytes()); // Save as blob or handle file path

        medalRepository.save(medal);
        return "redirect:/user/your-profile";

    }

    // 📄 Upload Certificate
    @PostMapping("/upload-certificate")
    public String uploadCertificate(@RequestParam("image") MultipartFile image,
                                    Principal principal) throws IOException {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserProfile profile = userProfileRepository.findByUser(user).orElseThrow();

        Certificate cert = new Certificate();
        cert.setVerified(false);
        cert.setUserProfile(profile);
        cert.setImageUrl(image.getBytes());

        certificateRepository.save(cert);
        return "redirect:/user/your-profile";

    }

    // 🔍 Optional: Serve Medal Image
    @GetMapping("/medal/{id}/image")
    @ResponseBody
    public byte[] viewMedalImage(@PathVariable Long id) {
        Medal medal = medalRepository.findById(id).orElseThrow();
        return medal.getImageUrl();
    }

    // 🔍 Optional: Serve Certificate Image
    @GetMapping("/certificate/{id}/image")
    @ResponseBody
    public byte[] viewCertificateImage(@PathVariable Long id) {
        Certificate cert = certificateRepository.findById(id).orElseThrow();
        return cert.getImageUrl();
    }





}

