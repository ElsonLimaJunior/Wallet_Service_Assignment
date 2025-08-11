package com.elson.wallet.infrastructure.config.security;

import com.elson.wallet.infrastructure.adapter.out.persistence.repository.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserJpaRepository userJpaRepository;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserJpaRepository userJpaRepository) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userJpaRepository = userJpaRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilitar CSRF, pois usamos JWT (stateless)
                .csrf(AbstractHttpConfigurer::disable)
                // Definir a política de sessão como STATELESS, o servidor não guarda estado de sessão.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Definir as regras de autorização para os endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Endpoints de autenticação são públicos
                        .anyRequest().authenticated() // Todas as outras requisições exigem autenticação
                )
                // Adicionar nosso filtro JWT antes do filtro padrão do Spring
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Implementação do serviço que busca um usuário no banco de dados.
        // O "username" que ele recebe é, na verdade, nosso UUID do usuário.
        return username -> {
            UUID userId = UUID.fromString(username);
            return userJpaRepository.findById(userId)
                    .map(user -> new org.springframework.security.core.userdetails.User(
                            user.getId().toString(),
                            user.getPassword(),
                            Collections.emptyList() // Aqui poderíamos adicionar Roles/Authorities se tivéssemos
                    ))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + username));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Define o codificador de senhas que usaremos. BCrypt é o padrão ouro.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Expõe o gerenciador de autenticação do Spring como um Bean para ser usado no AuthController
        return config.getAuthenticationManager();
    }
}