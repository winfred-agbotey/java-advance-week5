package com.mawulidev.week5labs.config;


import com.mawulidev.week5labs.services.UserDetailsServiceImpl;
import com.mawulidev.week5labs.utils.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;


/**
 * JWT Authentication Filter that intercepts incoming HTTP requests and validates the provided JWT token.
 * <p>
 * This class extends {@link OncePerRequestFilter} to ensure the filter is invoked once per request.
 * It extracts the JWT token from the `Authorization` header, validates it, and sets the authenticated user
 * in the {@link SecurityContextHolder} if the token is valid.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Filters each request to authenticate the user based on the JWT token found in the `Authorization` header.
     *
     * @param request     The {@link HttpServletRequest} object.
     * @param response    The {@link HttpServletResponse} object.
     * @param filterChain The {@link FilterChain} to pass the request and response to the next filter in the chain.
     * @throws ServletException If an error occurs during request processing.
     * @throws IOException      If an input or output exception occurs.
     */
    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {

            //get authorization token from request header
            Optional<String> authHeader = Optional.ofNullable(request.getHeader("Authorization"));
            authHeader.filter(head -> head.startsWith("Bearer "))
                    .map(head -> head.substring(7))
                    .ifPresent(token -> {
                        Optional<String> usernameOptional = Optional.ofNullable(jwtService.extractUsername(token));
                        // get user details from database
                        usernameOptional.filter(username -> SecurityContextHolder.getContext().getAuthentication() == null)
                                .ifPresent(username -> {
                                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                                    // check if token is valid
                                    if (Boolean.TRUE.equals(jwtService.validateToken(token, userDetails))) {
                                        UsernamePasswordAuthenticationToken authenticationToken =
                                                new UsernamePasswordAuthenticationToken(userDetails,
                                                        null,
                                                        userDetails.getAuthorities());
                                        // enforce authentication token with details of request
                                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                        // Update SecurityContextHolder or authentication token
                                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                                    }
                                });
                    });
            //add filter chain
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            request.setAttribute("error", ex);
            filterChain.doFilter(request, response);
        }
    }
}
