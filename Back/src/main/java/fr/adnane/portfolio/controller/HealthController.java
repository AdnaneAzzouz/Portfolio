package fr.adnane.portfolio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HealthController {

    @GetMapping("/")
    public Mono<ResponseEntity<Void>> getHealth() {
        return Mono.just(ResponseEntity.ok().build());
    }
}
