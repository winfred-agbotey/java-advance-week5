package com.mawulidev.week5labs.config;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * Custom implementation of {@link AuthenticationEntryPoint} that handles authentication
 * and authorization errors by delegating them to a {@link HandlerExceptionResolver}.
 * <p>
 * This component is annotated with {@code @Slf4j} for logging support and {@code @Component}
 * to make it available in the Spring context.
 */
@Slf4j
@Component("delegatedAuthenticationEntryPoint")
public class DelegatedAuthEntryPoint implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver resolver;

    public DelegatedAuthEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Handles authentication and authorization exceptions by delegating them
     * to the {@link HandlerExceptionResolver}.
     *
     * @param request       The {@link HttpServletRequest} object.
     * @param response      The {@link HttpServletResponse} object.
     * @param authException The {@link AuthenticationException} that triggered this entry point.
     * @throws IOException      If an input or output exception occurs.
     * @throws ServletException If a servlet exception occurs.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // Retrieve any authentication-related error from the request attribute "error".
        Object authError = request.getAttribute("error");

//        switch (authError) {
//            case ExpiredJwtException jwtException ->
//                    resolver.resolveException(request, response, null, new ExpiredJwtException(jwtException.getHeader(), jwtException.getClaims(),
//                            jwtException.getMessage()));
//            case MalformedJwtException malformedJwtException ->
//                    resolver.resolveException(request, response, null, new MalformedJwtException(malformedJwtException.getMessage()));
//            case SignatureException signatureException ->
//                    resolver.resolveException(request, response, null, new SignatureException(signatureException.getMessage()));
//            case BadCredentialsException badCredentialsException ->
//                    resolver.resolveException(request, response, null, new BadCredentialsException(badCredentialsException.getMessage()));
//            case AccessDeniedException accessDeniedException ->
//                    resolver.resolveException(request, response, null, new AccessDeniedException(accessDeniedException.getMessage()));
//            case InsufficientAuthenticationException insufficientAuthenticationException ->
//                    resolver.resolveException(request, response, null, new InsufficientAuthenticationException(insufficientAuthenticationException.getMessage()));
//            case null, default -> resolver.resolveException(request, response, null, authException);
//        }

        if (authError instanceof ExpiredJwtException jwtException) {
            resolver.resolveException(request, response, null, new ExpiredJwtException(jwtException.getHeader(), jwtException.getClaims(),
                    jwtException.getMessage()));
        } else if (authError instanceof MalformedJwtException malformedJwtException) {
            resolver.resolveException(request, response, null, new MalformedJwtException(malformedJwtException.getMessage()));
        } else if (authError instanceof SignatureException signatureException) {
            resolver.resolveException(request, response, null, new SignatureException(signatureException.getMessage()));
        } else if (authError instanceof BadCredentialsException badCredentialsException) {
            resolver.resolveException(request, response, null, new BadCredentialsException(badCredentialsException.getMessage()));
        } else if (authError instanceof AccessDeniedException accessDeniedException) {
            resolver.resolveException(request, response, null, new AccessDeniedException(accessDeniedException.getMessage()));
        } else if (authError instanceof InsufficientAuthenticationException insufficientAuthenticationException) {
            resolver.resolveException(request, response, null, new InsufficientAuthenticationException(insufficientAuthenticationException.getMessage()));
        } else {
            resolver.resolveException(request, response, null, authException);
        }
    }
}