package com.sports.sportsplatform.Config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dv7c33dwz",
                "api_key", "592398757681472",
                "api_secret", "Z_lIdbritZbxHa57s702VqEBfqo"
        ));
    }
}
