
package com.mgaye.bsys.security;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mgaye.bsys.model.User.KycStatus;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    // Use Base64-decoded key for stronger security
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        // Extract roles as simple strings
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("id", userPrincipal.getId())
                .claim("kyc", userPrincipal.getKycStatus().name())
                .claim("roles", roles)
                .claim("ae", userPrincipal.isAccountNonExpired()) // Account expiration status
                .claim("al", userPrincipal.isAccountNonLocked()) // Account lock status
                .claim("ce", userPrincipal.isCredentialsNonExpired()) // Credential expiration
                .claim("en", userPrincipal.isEnabled()) // Enabled status
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);

            Claims claims = claimsJws.getBody();

            // Critical banking security checks
            if (!verifyAccountStatus(claims)) {
                return false;
            }

            return true;
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Security exception during token validation", e);
        }
        return false;
    }

    // Enhanced account status verification
    private boolean verifyAccountStatus(Claims claims) {
        // 1. Check KYC status
        String kycStatus = claims.get("kyc", String.class);
        if (!KycStatus.VERIFIED.name().equals(kycStatus)) {
            logger.warn("Access attempt with non-verified KYC: {}", claims.getSubject());
            return false;
        }

        // 2. Check account lock status
        Boolean accountNonLocked = claims.get("al", Boolean.class);
        if (accountNonLocked == null || !accountNonLocked) {
            logger.warn("Access attempt with locked account: {}", claims.getSubject());
            return false;
        }

        // 3. Check account expiration
        Boolean accountNonExpired = claims.get("ae", Boolean.class);
        if (accountNonExpired == null || !accountNonExpired) {
            logger.warn("Access attempt with expired account: {}", claims.getSubject());
            return false;
        }

        // 4. Check credential expiration
        Boolean credentialsNonExpired = claims.get("ce", Boolean.class);
        if (credentialsNonExpired == null || !credentialsNonExpired) {
            logger.warn("Access attempt with expired credentials: {}",
                    claims.getSubject());
            return false;
        }

        // 5. Check enabled status
        Boolean enabled = claims.get("en", Boolean.class);
        if (enabled == null || !enabled) {
            logger.warn("Access attempt with disabled account: {}", claims.getSubject());
            return false;
        }

        return true;
    }

    public String getUserNameFromJwtToken(String token) {
        return getAllClaims(token).getSubject();
    }

    public Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        // Check cookie as fallback (for XSS protection)
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Add method to get user ID from token
    public String getUserIdFromToken(String token) {
        return getAllClaims(token).get("id", String.class);
    }
}
