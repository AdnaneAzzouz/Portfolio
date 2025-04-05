package fr.adnane.portfolio.service;

import fr.adnane.portfolio.dto.UtilisateurDto;
import fr.adnane.portfolio.model.Utilisateur;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface pour ajouter de la logique au repository.
 */
public interface UtilisateurService {

    // C(reate)
    Mono<Utilisateur> creerUtilisateur(String nom, String prenom, String pseudo, String mail, String motDePasse);
    Mono<Utilisateur> enregistrerUtilisateur(UtilisateurDto utilisateurDto);
    Mono<Utilisateur> enregistrerUtilisateur(Utilisateur utilisateur);

    // R(ead)
    Mono<Utilisateur> recupererUtilisateur(String id);
    Mono<Utilisateur> rechercherUtilisateur(String pseudo, String motDePasse);
    Mono<Utilisateur> rechercherUtilisateurParMail(String mail);
    Mono<Utilisateur> rechercherUtilisateurParMailEtMotDePasse(String mail, String motDePasse);

    Flux<Utilisateur> recupererUtilisateurs();

    // U(pdate)
    Mono<Utilisateur> miseAJourUtilisateur(String mail, String motDePasse, UtilisateurDto utilisateurDto);

    // Delete
    Mono<Void> supprimerUtilisateur(String mail, String motDePasse);

}
