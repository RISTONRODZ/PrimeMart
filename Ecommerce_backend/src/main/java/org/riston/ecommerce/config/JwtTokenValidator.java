package org.riston.ecommerce.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenValidator extends OncePerRequestFilter {

    private final SecretKey key;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response, 
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        String token = request.getHeader(JWT_CONSTANT.JWT_HEADER);

        // Guard Clause: If no JWT header is present or format is invalid, pass through to filter chain immediately
        if (token == null || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = token.substring(7);
        if (token.isEmpty() || token.equals("null")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("JWT received by validator: {}", token);
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String email = String.valueOf(claims.get("email"));
            String authorities = String.valueOf(claims.get("authorities"));
            List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

            log.info("JWT Email: {}, Authorities: {}", email, authorities);
            log.info("Request URI: {}", request.getRequestURI());
            log.info("Authentication authorities: {}", auth);

            Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auth);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Authentication set in SecurityContext: {}", SecurityContextHolder.getContext().getAuthentication());
        } catch (Exception e) {
            log.error("JWT validation failed", e);
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}