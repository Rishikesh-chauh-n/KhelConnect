package com.sports.sportsplatform.Service;

import com.sports.sportsplatform.Model.Event;
import com.sports.sportsplatform.Model.User;
import com.sports.sportsplatform.Model.UserSettings;
import com.sports.sportsplatform.Repository.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventService eventService;

    private final JavaMailSender mailSender;



    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private DistrictRepository districtRepository;

    private final PasswordEncoder passwordEncoder;


    public UserSettings saveSettings(UserSettings settings) {
        return userSettingsRepository.save(settings);
    }



    public List<UserSettings> getAllSettings() {
        return userSettingsRepository.findAll();
    }

    public void deleteSettings(Long id) {
        userSettingsRepository.deleteById(id);
    }


//    public UserSettings updatePreferences(String email, String prefer1, String prefer2, String prefer3) {
//        Optional<User> userOpt = userRepository.findByEmail(email);
//        if (userOpt.isEmpty()) return null;
//
//        User user = userOpt.get();
//        Optional<UserSettings> optional = userSettingsRepository.findByUser(user);
//        if (optional.isPresent()) {
//            UserSettings settings = optional.get();
//            settings.setPrefer1(prefer1);
//            settings.setPrefer2(prefer2);
//            settings.setPrefer3(prefer3);
//            return userSettingsRepository.save(settings);
//        }
//        return null;
//    }


    public void updateNotificationGames(String email, Long stateId, Long districtId, List<Long> gameIds) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        var settings = userSettingsRepository.findByUser(user)
                .orElse(new UserSettings());
        settings.setUser(user);

        if (stateId != null) {
            settings.setNotificationState(stateRepository.findById(stateId).orElse(null));
        }
        if (districtId != null) {
            settings.setNotificationDistrict(districtRepository.findById(districtId).orElse(null));
        }
        if (gameIds != null && !gameIds.isEmpty()) {
            settings.setNotificationGames(new HashSet<>(gameRepository.findAllById(gameIds)));
        }

        userSettingsRepository.save(settings);
    }


    public boolean updatePassword(String email, String oldPassword, String newPassword) {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isPresent()) {
            User user = optional.get();

            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                return false; // old password doesn't match
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public  void sendEventEmails(Long eventId) {
        Event event = eventService.getEventById(eventId);

        // ✅ Use the new query that handles multiple games & avoids duplicates
        List<User> usersToNotify = userSettingsRepository.findMatchingUsersForGames(
                event.getState().getId(),
                event.getDistrict().getId(),
                List.of(event.getGame().getId()) // can handle multiple games if needed
        );

        if (usersToNotify.isEmpty()) {
            System.out.println("ℹ No matching users for event " + event.getName());
            return;
        }

        String subject = "🎉 New Event: " + event.getName();
        String htmlContent = "<div style='font-family: Arial, sans-serif;'>" +
                "<h2 style='color:#2c3e50;'>" + event.getName() + "</h2>" +
                "<img src='" + event.getImagePath() + "' alt='Event Image' style='max-width:100%; border-radius:10px;'/>" +
                "<p><strong>Date:</strong> " + event.getDate() + "</p>" +
                "<p><strong>Location:</strong> " + event.getLocation() + "</p>" +
                "<p><strong>Game:</strong> " + event.getGame().getName() + "</p>" +
                "<br/><p>Don't miss this amazing event happening near you!</p>" +
                "</div>";

        // ✅ Send in parallel but avoid blocking main thread
        usersToNotify.parallelStream().forEach(user -> {
            try {
                sendHtmlEmail(user.getEmail(), subject, htmlContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println("✅ Sent event emails to " + usersToNotify.size() + " users.");
    }


    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();    //Mime stands for Multipurpose Internet mail extensions(which means it can handle plain text, Html content, Attachments,Inline resources, Multiple recipent)
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true means HTML

        mailSender.send(message);
    }

}
