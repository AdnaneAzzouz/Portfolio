package fr.adnane.portfolio;

import fr.adnane.portfolio.controller.UtilisateurController;
import fr.adnane.portfolio.mapper.UtilisateurMapperImpl;
import fr.adnane.portfolio.model.Utilisateur;
import fr.adnane.portfolio.service.UtilisateurService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.reactive.ReactiveManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = UtilisateurController.class, excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class, ReactiveManagementWebSecurityAutoConfiguration.class })
@Import({UtilisateurMapperImpl.class})
class PortfolioApplicationTests {

    @MockitoBean
    private UtilisateurService utilisateurService;

    @Autowired
    private WebTestClient webClient;
    @Autowired
    private MessageSource messageSource;

    @Test
    void when_paramIsOk_then_utilisateurPost_isCreated() {
        Mockito
                .when(utilisateurService.creerUtilisateur("Jean", "Castor", "j€anC@stor", "jean-castor@gmail.com", "123456@!Azz"))
                .thenReturn(Mono.just(Utilisateur.builder()
                        .nom("Jean")
                        .prenom("Castor")
                        .mail("jean-castor@gmail.com")
                        .pseudo("j€anC@stor")
                        .motDePasse("123456@!Azz")
                        .build()));

        webClient
                .post()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/utilisateur")
                                .queryParam("lastName", "Jean")
                                .queryParam("firstName", "Castor")
                                .queryParam("password", "123456@!Azz")
                                .queryParam("email", "jean-castor@gmail.com")
                                .queryParam("pseudo", "j€anC@stor")
                .build())
                .exchange()
                .expectStatus().isCreated();
    }
}
