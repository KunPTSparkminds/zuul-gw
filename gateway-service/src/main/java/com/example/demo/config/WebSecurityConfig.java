package com.example.demo.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.demo.filter.AuthenticationFilter;
import com.example.demo.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

	@Override
    protected void configure(HttpSecurity http) throws Exception {
       http.csrf()
       .disable()
       .cors()
       .and()
       .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
       .and().exceptionHandling()
       .and()
       .addFilterBefore(
               new AuthenticationFilter(jwtTokenProvider),
               UsernamePasswordAuthenticationFilter.class
       )
       .authorizeRequests()
       .antMatchers(HttpMethod.POST,"/**/login", "/register", "/applicant/api/applications").permitAll()
       .antMatchers(HttpMethod.GET,"/applicant/api/applications").hasAnyRole("ROLE_STAFF")
       .antMatchers("/auth/admin/test").hasAnyRole("ADMIN", "STAFF")
       .antMatchers("/auth/log").hasAnyAuthority("logManagement")
       .anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers(HttpMethod.GET, "/actuator/health/liveness", "/actuator/health/readiness")
            .antMatchers("/");
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_STAFF > ROLE_USER");
        return roleHierarchy;
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
