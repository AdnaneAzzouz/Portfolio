package fr.adnane.portfolio.repository;

import fr.adnane.portfolio.model.Utilisateur;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Interface pour créer la connexion entre l'application et la base de données. Permet aussi d'insérer des requêtes précises.
 */
@Repository
public interface UtilisateurRepository extends ReactiveMongoRepository<Utilisateur, String> {

    Mono<Utilisateur> findByMail(String email);
}
