package fr.adnane.portfolio.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Cette classe configure l'aspect Spring Security
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    /**
     * Actuellement : Désactive les requêtes de sécurité pour permettre aux requêtes HTTP de passer.
     * <p>
     * Ultérieurement : Sécurise les requêtes et la connexion au site internet.
     * @param http Le serveur Http de Spring Security
     * @return La chaine web filtré pour Spring Security
     */
    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(exchange -> exchange.anyExchange().permitAll())
                .build();
    }

    /**
     * Instancie l'encodeur de mot de passe par défaut.
     * @return Le bean pour l'encodeur de mot de passe.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
