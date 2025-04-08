package com.mawulidev.week5labs.config;


import com.mawulidev.week5labs.repositories.UserRepository;
import com.mawulidev.week5labs.services.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for application-level security components.
 * <p>
 * This class defines and exposes essential beans such as:
 * <ul>
 *     <li>{@link UserDetailsService} - For loading user details.</li>
 *     <li>{@link AuthenticationProvider} - For authentication handling.</li>
 *     <li>{@link AuthenticationManager} - For managing authentication processes.</li>
 *     <li>{@link PasswordEncoder} - For encoding and verifying passwords.</li>
 * </ul>
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepository repository;

    //Create a Bean of userDetailsService
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(repository);
    }


    /**
     * Configures a {@link DaoAuthenticationProvider} bean.
     * <p>
     * This provider authenticates users using a data access object (DAO) pattern by:
     * <ul>
     *     <li>Using the {@link UserDetailsService} for fetching user details.</li>
     *     <li>Encoding passwords with {@link BCryptPasswordEncoder}.</li>
     * </ul>
     *
     * @return A configured {@link AuthenticationProvider} bean.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Exposes an {@link AuthenticationManager} bean to manage authentication processes.
     * <p>
     * This bean retrieves the {@link AuthenticationManager} from the provided
     * {@link AuthenticationConfiguration}.
     *
     * @param config The {@link AuthenticationConfiguration} containing authentication settings.
     * @return An {@link AuthenticationManager} bean.
     * @throws Exception If an error occurs while creating the authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures and exposes a {@link PasswordEncoder} bean using {@link BCryptPasswordEncoder}.
     * <p>
     * This encoder applies a secure hashing algorithm to passwords before storing them.
     *
     * @return A {@link PasswordEncoder} bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}