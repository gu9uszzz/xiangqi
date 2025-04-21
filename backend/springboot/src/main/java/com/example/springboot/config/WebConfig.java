package com.example.springboot.config;

import com.example.springboot.interceptor.JwtInterceptor; // Import your interceptor
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor; // Inject the interceptor

//    // Define the PasswordEncoder Bean
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    // Configure JWT Interceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // Apply to all paths by default
                .excludePathPatterns( // Exclude paths that don't require authentication
                        "/user/login",       // Your login path
                        "/user/register",    // Your registration path
                        "/user/sendcode",    // Your send code path
                        // Add other public paths like static resources, swagger, etc.
                        "/files/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/error" // Exclude Spring Boot error mapping
                );
    }

    // Basic CORS Configuration (Adjust origins as needed for your Vue frontend)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow CORS for all paths
                .allowedOriginPatterns("http://localhost:8080") // Allow your Vue dev server origin *PATTERN*
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true) // Allow cookies/auth headers
                .maxAge(3600);
    }
}

