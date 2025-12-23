package com.pedro.bookManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.pedro.bookManagement.security.AuthEntryPointJwt;
import com.pedro.bookManagement.security.AuthTokenFilter;
import com.pedro.bookManagement.security.JwtUtil;
import com.pedro.bookManagement.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private final CustomUserDetailsService userDetailsService;

    private final AuthEntryPointJwt unauthorizedHandler;

		private final AuthTokenFilter authTokenFilter;

		public WebSecurityConfiguration(
        CustomUserDetailsService userDetailsService,
        AuthEntryPointJwt unauthorizedHandler,
				AuthTokenFilter authTokenFilter
    ) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
				this.authTokenFilter = authTokenFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
              .csrf(csrf -> csrf.disable()) // Disable CSRF
              .exceptionHandling(exceptionHandling ->
                      exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
              )
              .sessionManagement(sessionManagement ->
                      sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              )
              .authorizeHttpRequests(authorizeRequests ->
                      authorizeRequests
															.requestMatchers("/api/auth/**").permitAll()
															.requestMatchers("/api/books/**").authenticated()
															.requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                              .anyRequest().authenticated()
              );
      // Add the JWT Token filter before the UsernamePasswordAuthenticationFilter
      http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
      return http.build();
  }
}
