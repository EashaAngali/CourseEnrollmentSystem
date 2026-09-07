package com.example.courseenrollmentsystem.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

@Bean
    public SecurityFilterChain springSecurityFilterChain(
            HttpSecurity http) throws Exception {
      return   http.
              csrf(AbstractHttpConfigurer::disable
        ).authorizeHttpRequests(auth->auth.requestMatchers("/api/faculty/**").hasAnyRole("FACULTY", "ADMIN")
                      .requestMatchers("/api/teacher/**").hasAnyRole("TEACHER","FACULTY","ADMIN")
                      .requestMatchers("api/student/**").hasAnyRole("STUDENT","FACULTY","ADMIN")
                      .anyRequest().denyAll())

              .httpBasic(Customizer.withDefaults())
              .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
    UserDetailsService userDetailsService = new JdbcUserDetailsManager();
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
    authProvider.setPasswordEncoder(bCryptPasswordEncoder());
    return   authProvider;
    }
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetailsService userDetailsService = User.builder();
//    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
