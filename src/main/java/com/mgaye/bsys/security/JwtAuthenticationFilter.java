package com.mgaye.bsys.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mgaye.bsys.model.User.KycStatus;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

// @Component
// @RequiredArgsConstructor
// public class AuthTokenFilter extends OncePerRequestFilter {

//     private final JwtTokenProvider tokenProvider;
//     private final UserDetailsServiceImpl userDetailsService;

//     @Override
//     protected void doFilterInternal(HttpServletRequest request,
//             HttpServletResponse response,
//             FilterChain filterChain)
//             throws ServletException, IOException {

//         try {
//             String jwt = parseJwt(request);
//             if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
//                 String username = tokenProvider.getUserNameFromJwtToken(jwt);

//                 UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                 UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                         userDetails,
//                         null,
//                         userDetails.getAuthorities());

//                 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                 SecurityContextHolder.getContext().setAuthentication(authentication);
//             }
//         } catch (Exception e) {
//             logger.error("Cannot set user authentication", e);
//         }

//         filterChain.doFilter(request, response);
//     }

//     private String parseJwt(HttpServletRequest request) {
//         String headerAuth = request.getHeader("Authorization");
//         if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
//             return headerAuth.substring(7);
//         }
//         return null;
//     }
// }

// @Component
// @RequiredArgsConstructor
// public class JwtAuthenticationFilter extends OncePerRequestFilter {
//     private final JwtTokenProvider tokenProvider;
//     private final UserDetailsServiceImpl userDetailsService;

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//             throws ServletException, IOException {
//         try {
//             String jwt = parseJwt(request);
//             if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
//                 String username = tokenProvider.getUserNameFromJwtToken(jwt);
//                 UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                 UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                         userDetails, null, userDetails.getAuthorities());
//                 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                 SecurityContextHolder.getContext().setAuthentication(authentication);
//             }
//         } catch (Exception e) {
//             logger.error("Cannot set user authentication: {}", e);
//         }
//         filterChain.doFilter(request, response);
//     }

//     private String parseJwt(HttpServletRequest request) {
//         String headerAuth = request.getHeader("Authorization");
//         if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
//             return headerAuth.substring(7);
//         }
//         return null;
//     }

// }

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = parseJwt(request);
            if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
                String username = tokenProvider.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}

// @Component
// public class JwtAuthenticationFilter extends OncePerRequestFilter {

// @Autowired
// private JwtTokenProvider tokenProvider;

// @Override
// protected void doFilterInternal(HttpServletRequest request,
// HttpServletResponse response,
// FilterChain filterChain)
// throws ServletException, IOException {

// try {
// String jwt = tokenProvider.parseJwt(request);
// if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
// Claims claims = tokenProvider.getAllClaims(jwt);

// // Build UserDetails from claims
// UserDetails userDetails = buildUserDetailsFromClaims(claims);

// // Create authentication token
// UsernamePasswordAuthenticationToken authentication = new
// UsernamePasswordAuthenticationToken(
// userDetails,
// null,
// userDetails.getAuthorities());

// authentication.setDetails(
// new WebAuthenticationDetailsSource().buildDetails(request));

// SecurityContextHolder.getContext().setAuthentication(authentication);
// }
// } catch (Exception e) {
// logger.error("Security exception in JWT filter", e);
// response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
// return;
// }

// filterChain.doFilter(request, response);
// }

// private UserDetails buildUserDetailsFromClaims(Claims claims) {
// // Extract authorities
// Collection<? extends GrantedAuthority> authorities = ((List<String>)
// claims.get("roles", List.class)).stream()
// .map(SimpleGrantedAuthority::new)
// .collect(Collectors.toSet());

// return new UserDetailsImpl(
// claims.get("id", String.class),
// claims.getSubject(), // email
// null, // password not stored
// KycStatus.valueOf(claims.get("kyc", String.class)),
// authorities,
// claims.get("ae", Boolean.class),
// claims.get("al", Boolean.class),
// claims.get("ce", Boolean.class),
// claims.get("en", Boolean.class));
// }
// }