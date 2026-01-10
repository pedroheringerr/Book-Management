package com.pedro.bookManagement.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.pedro.bookManagement.security.AuthEntryPointJwt;
import com.pedro.bookManagement.security.AuthTokenFilter;
import com.pedro.bookManagement.security.JwtUtil;
import com.pedro.bookManagement.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration implements WebMvcConfigurer {

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

		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**")
            .allowedOrigins("http://localhost:4200")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
		}

		@Bean
		public CorsConfigurationSource corsConfigurationSource() {
			CorsConfiguration config = new CorsConfiguration();

			config.setAllowedOrigins(List.of("http://localhost:4200"));
			config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
			config.setExposedHeaders(List.of("Authorization"));
			config.setAllowCredentials(true);

			UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
			source.registerCorsConfiguration("/**", config);
			return source;
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
							.cors(Customizer.withDefaults())
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
															.requestMatchers("/api/books/stats/**").permitAll()
															.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
															.requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                              .anyRequest().authenticated()
              );
      // Add the JWT Token filter before the UsernamePasswordAuthenticationFilter
      http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
      return http.build();
  }
}
