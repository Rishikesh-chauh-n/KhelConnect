package com.sports.sportsplatform;

import com.sports.sportsplatform.Model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator implements CommandLineRunner {


    @Override
    public void run(String... args) {
        String rawPassword = "Rishi@123";
        String hashedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println("Hashed Password: " + hashedPassword);

    }
}
