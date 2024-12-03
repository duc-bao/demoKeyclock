package com.ducbao.demokeyclock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final String[] PUBLIC_URLS = {"/register"};

    @Bean
    public SecurityFilterChain springSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request.requestMatchers(PUBLIC_URLS).permitAll()
                .anyRequest().authenticated());
        http.csrf(AbstractHttpConfigurer::disable);
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(
                jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        return http.build();
    }

    /**
     * jwtAuthenticationConverter -> Thằng này cho phép chúng ta customs lại role để lấy được role từ keycloak trả về
     * authenticationEntryPoint(new JwtAuthenticationEntryPoint())) ->  thằng này thì sẽ cho phép điều hướng nếu xác thực failed
     *
     * **/
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new CustomAuthoritiesConverter());
        return converter;
    }
}
