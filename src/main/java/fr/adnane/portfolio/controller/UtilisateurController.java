package fr.adnane.portfolio.controller;

import fr.adnane.portfolio.dto.UtilisateurDto;
import fr.adnane.portfolio.mapper.UtilisateurMapper;
import fr.adnane.portfolio.model.ResponseUtilisateur;
import fr.adnane.portfolio.model.Utilisateur;
import fr.adnane.portfolio.service.UtilisateurService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;

/**
 * Classe qui construit le Rest API pour que le projet FrontEnd puisse s'en servir.
 */
@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
public class UtilisateurController {
    public static final String USER_CREATED = "log-api-info-user-created";
    public static final String USER_UPDATED = "log-api-info-user-updated";
    private final UtilisateurService utilisateurService;
    private final UtilisateurMapper utilisateurMapper;
    private final MessageSource messageSource;

    /**
     * Requête POST créant un utilisateur à l'aide d'un corps de Requête "UtilisateurDTO"
     * @param utilisateurDto Classe DTO servant de corps de requête
     * @return 201 si passé, 422 si le corps de la requête a une erreur, 500 si le serveur a reçu une erreur non suspectée.
     */
    @PostMapping(path = "/utilisateur/request")
    public Mono<ResponseEntity<ResponseUtilisateur>> utilisateurPostWithRequestBody(@RequestBody @Valid UtilisateurDto utilisateurDto) {
        return utilisateurService
                .enregistrerUtilisateur(utilisateurDto)
                .map(utilisateur -> ResponseEntity.status(HttpStatus.CREATED).body(new ResponseUtilisateur(HttpStatus.CREATED.value(), messageSource.getMessage(USER_CREATED, new Object[]{utilisateur.getPseudo()}, Locale.getDefault()))));
    }

    /**
     * Requête POST créant un utilisateur à l'aide de paramètres requête.
     * @param nom Le nom souhaité
     * @param prenom Le prénom souhaité
     * @param mail Le mail souhaité
     * @param motDePasse Le mot de passe souhaité
     * @param pseudo Le pseudo souhaité
     * @return 201 si passé, 422 si le corps de la requête a une erreur, 500 si le serveur a reçu une erreur non suspectée.
     */
    @PostMapping(path = "/utilisateur")
    public Mono<ResponseEntity<ResponseUtilisateur>> utilisateurPostWithParams(@RequestParam(name = "lastName") String nom, @RequestParam(name = "firstName") String prenom, @RequestParam(name = "email") String mail, @RequestParam(name = "password") String motDePasse, @RequestParam String pseudo) {
        return utilisateurService
                .creerUtilisateur(nom, prenom, pseudo, mail, motDePasse)
                .map(utilisateur -> ResponseEntity.status(HttpStatus.CREATED).body(new ResponseUtilisateur(HttpStatus.CREATED.value(), messageSource.getMessage(USER_CREATED, new Object[]{utilisateur.getPseudo()}, Locale.getDefault()))));
    }

    /**
     * Requête GET récupérant un utilisateur par son ID
     * @param id L'id demandé
     * @return 302 si trouvé 404 l'utilisateur n'a pas été trouvé, 500 si le serveur a reçu une erreur non suspectée.
     */
    @GetMapping(path = "/utilisateur")
    public Mono<ResponseEntity<Utilisateur>> utilisateurGetWithId(@RequestParam String id) {
        return utilisateurService
                .recupererUtilisateur(id)
                .map(utilisateur -> ResponseEntity.status(HttpStatus.FOUND).body(utilisateur));
    }

    /**
     * Requête GET récupérant TOUS les utilisateurs
     * @return 200 si un utilisateur existe, 204 si aucun utilisateur n'existe, 500 si le serveur a reçu une erreur non suspectée.
     */
    @GetMapping(path = "/utilisateurs")
    public Mono<ResponseEntity<List<Utilisateur>>> utilisateurGetAll() {
        return utilisateurService
                .recupererUtilisateurs()
                .collectList()
                .map(utilisateurs -> ResponseEntity.status(utilisateurs.isEmpty() ? HttpStatus.NO_CONTENT :HttpStatus.OK).body(utilisateurs));
    }

    /**
     * Requête GET récupérant un utilisateur par son mail
     * @param mail Le mail demandé
     * @return 302 si trouvé 404 l'utilisateur n'a pas été trouvé, 500 si le serveur a reçu une erreur non suspectée.
     */
    @GetMapping(path = "/utilisateur/{mail}")
    public Mono<ResponseEntity<UtilisateurDto>> utilisateurGetWithMail(@PathVariable String mail) {
        return utilisateurService
                .rechercherUtilisateurParMail(mail)
                .map(utilisateur -> ResponseEntity.status(HttpStatus.FOUND).body(utilisateurMapper.toDto(utilisateur)));
    }

    /**
     * Requête GET récupérant un utilisateur par son pseudo et mot de passe (Sera utilisé en FRONT pour se connecter)
     * @param pseudo Le pseudo demandé
     * @param motDePasse Le mot de passe
     * @return 302 si trouvé 404 l'utilisateur n'a pas été trouvé, 500 si le serveur a reçu une erreur non suspectée.
     */
    @GetMapping(path = "/utilisateur/{pseudo}/{password}")
    public Mono<ResponseEntity<UtilisateurDto>> utilisateurGetWithPseudoAndPassword(@PathVariable(name = "pseudo") String pseudo, @PathVariable(name = "password") String motDePasse) {
        return utilisateurService
                .rechercherUtilisateur(pseudo, motDePasse)
                .map(utilisateur -> ResponseEntity.status(HttpStatus.FOUND).body(utilisateurMapper.toDto(utilisateur)));
    }

    /**
     * Requête PUT pour mettre à jour un utilisateur à l'aide d'un corps de Requête "UtilisateurDTO". Le mail et mot de passe est à renseigner pour détecter l'utilisateur à mettre à jour.
     * @param mail Le mail demandé
     * @param password Le mot de passe
     * @param utilisateurDto Classe DTO servant de corps de requête
     * @return 202 si passé, 422 si le corps de la requête a une erreur, 500 si le serveur a reçu une erreur non suspectée.
     */
    @PutMapping(path = "/utilisateur")
    public Mono<ResponseEntity<ResponseUtilisateur>> utilisateurPut(@RequestParam String mail, @RequestParam String password, @RequestBody @Valid UtilisateurDto utilisateurDto) {
        return utilisateurService
                .miseAJourUtilisateur(mail, password, utilisateurDto)
                .map(utilisateur -> ResponseEntity.accepted().body(new ResponseUtilisateur(HttpStatus.ACCEPTED.value(), messageSource.getMessage(USER_UPDATED, new Object[]{utilisateur.getPseudo()}, Locale.getDefault()))));
    }

    /**
     * Requête DELETE pour supprimer un utilisateur à l'aide d'un mail et mot de passe.
     * @param mail Le mail demandé
     * @param password Le mot de passe
     * @return 200 si passé, 500 si le serveur a reçu une erreur non suspectée.
     */
    @DeleteMapping(path = "/utilisateur")
    public Mono<ResponseEntity<ResponseUtilisateur>> utilisateurDelete(@RequestParam String mail, @RequestParam String password) {
        return utilisateurService
                .supprimerUtilisateur(mail, password)
                .then(Mono.fromCallable(() -> ResponseEntity.ok().body(new ResponseUtilisateur(HttpStatus.OK.value(), "L'utilisateur a été supprimé avec succès"))));
    }
}
