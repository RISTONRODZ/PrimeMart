package org.riston.ecommerce.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class AppConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, SecretKey secretKey)  {
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        // 1. Public Endpoints - No Authentication Required
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/admin/deals").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/home-categories").permitAll()
                        .requestMatchers("/api/v1/auth/**", "/api/v1/home").permitAll()
                        .requestMatchers("/api/v1/seller/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews").permitAll()

                        // 2. Admin-Only Endpoints
                        .requestMatchers(HttpMethod.POST, "/api/v1/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/v1/home-categories/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/admin/seller/{sellerId}/status/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/coupons/create").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/coupons/create").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/coupons/delete/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/v1/admin/deals/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/coupons/all").hasAuthority("ROLE_ADMIN")

                        // 3. Seller-Only Endpoints
                        .requestMatchers(HttpMethod.GET, "/api/v1/seller/**").hasAnyAuthority("ROLE_SELLER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/seller/**").hasAnyAuthority("ROLE_SELLER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/seller/**").hasAuthority("ROLE_SELLER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/seller/**").hasAuthority("ROLE_SELLER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/transactions/seller").hasAuthority("ROLE_SELLER")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/seller/products/{productId}").hasAuthority("ROLE_SELLER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/seller/verify/**").hasAuthority("ROLE_SELLER")

                        // 4. Customer-Only Endpoints
                        .requestMatchers(HttpMethod.GET, "/api/v1/cart/**").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/cart/**").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/cart/**").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/cart/**").hasAuthority("ROLE_CUSTOMER")

                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/orders/**").hasAuthority("ROLE_CUSTOMER")

                        .requestMatchers(HttpMethod.POST, "/api/v1/payment/**").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/payment/**").hasAuthority("ROLE_CUSTOMER")

                        .requestMatchers(HttpMethod.GET, "/api/v1/wishlist/**").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/wishlist/**").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/wishlist/**").hasAuthority("ROLE_CUSTOMER")

                        .requestMatchers(HttpMethod.POST, "/api/v1/reviews/**").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/**").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/reviews/**").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/reviews/{reviewId}").hasAuthority("ROLE_CUSTOMER")
                        // 5. User Profile - Customer Only
                        .requestMatchers("/api/v1/users/**").hasAuthority("ROLE_CUSTOMER")

                        // 6. Coupon - Customer Only
                        .requestMatchers("/api/v1/coupons/apply").hasAuthority("ROLE_CUSTOMER")
                        //swagger
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // 7. Secure-by-default Catch-all
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtTokenValidator(secretKey), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        return (HttpServletRequest request) -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:5173"));
            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            config.setExposedHeaders(Arrays.asList("Authorization", "X-Total-Count"));
            config.setMaxAge(3600L);
            return config;
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}