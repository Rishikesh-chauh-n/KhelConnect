package com.sports.sportsplatform.Controller;

import com.sports.sportsplatform.Security.JwtUtil;
import com.sports.sportsplatform.Service.CustomUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @GetMapping("/customlogin")
    public String loginPage() {
        System.out.println(">>> Inside loginPage()");
        return "custom-login"; // return login.html
    }

    @PostMapping("/doLogin")
    public String loginUser(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpServletResponse response
    ) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            if (auth.isAuthenticated()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email); // email used here
                String token = jwtUtil.generateToken(userDetails.getUsername());

                Cookie cookie = new Cookie("jwt", token);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60);
                response.addCookie(cookie);

                if (userDetails.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                    return "redirect:/admin/dashboard";
                } else {
                    return "redirect:/user/home";
                }
            }

        } catch (Exception e) {
            return "redirect:/login?error";
        }

        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Invalidate cookie
        Cookie cookie = new Cookie("jwt", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/login";
    }
}
