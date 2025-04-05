package fr.adnane.portfolio.service.impl;

import fr.adnane.portfolio.dto.UtilisateurDto;
import fr.adnane.portfolio.exception.TechnicalException;
import fr.adnane.portfolio.exception.UtilisateurNotFoundException;
import fr.adnane.portfolio.mapper.UtilisateurMapper;
import fr.adnane.portfolio.model.Utilisateur;
import fr.adnane.portfolio.repository.UtilisateurRepository;
import fr.adnane.portfolio.service.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Locale;

/**
 * Classe d'implémentation de "UtilisateurService".
 */
@Service
@AllArgsConstructor
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {

    public static final String LOG_DEBUG_USER_SAVED = "log-debug-user-saved";
    public static final String LOG_DEBUG_USER_FOUND = "log-debug-user-found";
    public static final String LOG_DEBUG_USER_UPDATED = "log-debug-user-updated";
    public static final String LOG_DEBUG_USER_DELETED = "log-debug-user-deleted";
    public static final String LOG_ERROR_USER_NOT_FOUND = "log-error-user-not-found";
    public static final String PSEUDO_ADMIN_SUFFIX = "-portfolioAdmin";
    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<Utilisateur> creerUtilisateur(String nom, String prenom, String pseudo, String mail, String motDePasse) {
        motDePasse = passwordEncoder.encode(motDePasse);
        return enregistrerUtilisateur(Utilisateur.builder().nom(nom).prenom(prenom).pseudo(pseudo).mail(mail).motDePasse(motDePasse).estAdministrateur(pseudo.endsWith(PSEUDO_ADMIN_SUFFIX)).build());
    }

    @Override
    public Mono<Utilisateur> enregistrerUtilisateur(UtilisateurDto utilisateurDto) {
        utilisateurDto.setMotDePasse(passwordEncoder.encode(utilisateurDto.getMotDePasse()));
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDto);
        utilisateur.setEstAdministrateur(utilisateur.getPseudo().endsWith(PSEUDO_ADMIN_SUFFIX));
        return enregistrerUtilisateur(utilisateur);
    }

    @Override
    public Mono<Utilisateur> enregistrerUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository
                .save(utilisateur)
                .onErrorMap(err -> new TechnicalException("Erreur Critique", err))
                .doOnSuccess(utilisateurSuccess -> log.debug(messageSource.getMessage(LOG_DEBUG_USER_SAVED, new Object[]{utilisateurSuccess}, Locale.getDefault())));
    }

    @Override
    public Mono<Utilisateur> recupererUtilisateur(String id) {
        return utilisateurRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new UtilisateurNotFoundException(messageSource.getMessage(LOG_ERROR_USER_NOT_FOUND, null, Locale.getDefault()))))
                .doOnSuccess(utilisateur -> log.debug(messageSource.getMessage(LOG_DEBUG_USER_FOUND, new Object[]{utilisateur}, Locale.getDefault())));
    }

    @Override
    public Mono<Utilisateur> rechercherUtilisateur(String pseudo, String motDePasse) {
        return recupererUtilisateurs()
                .filter(utilisateur -> utilisateur.getPseudo().equalsIgnoreCase(pseudo) && passwordEncoder.matches(motDePasse, utilisateur.getMotDePasse()))
                .singleOrEmpty()
                .switchIfEmpty(Mono.error(new UtilisateurNotFoundException(messageSource.getMessage(LOG_ERROR_USER_NOT_FOUND, null, Locale.getDefault()))))
                .doOnSuccess(utilisateur -> log.debug(messageSource.getMessage(LOG_DEBUG_USER_FOUND, new Object[]{utilisateur}, Locale.getDefault())));
    }

    @Override
    public Mono<Utilisateur> rechercherUtilisateurParMail(String mail) {
        return utilisateurRepository
                .findByMail(mail)
                .switchIfEmpty(Mono.error(new UtilisateurNotFoundException(messageSource.getMessage(LOG_ERROR_USER_NOT_FOUND, null, Locale.getDefault()))))
                .doOnSuccess(utilisateur -> log.debug(messageSource.getMessage(LOG_DEBUG_USER_FOUND, new Object[]{utilisateur}, Locale.getDefault())));
    }

    @Override
    public Mono<Utilisateur> rechercherUtilisateurParMailEtMotDePasse(String mail, String motDePasse) {
        return recupererUtilisateurs()
                .filter(utilisateur -> utilisateur.getMail().equalsIgnoreCase(mail) && passwordEncoder.matches(motDePasse, utilisateur.getMotDePasse()))
                .singleOrEmpty()
                .switchIfEmpty(Mono.error(new UtilisateurNotFoundException(messageSource.getMessage(LOG_ERROR_USER_NOT_FOUND, null, Locale.getDefault()))))
                .doOnSuccess(utilisateur -> log.debug(messageSource.getMessage(LOG_DEBUG_USER_FOUND, new Object[]{utilisateur}, Locale.getDefault())));
    }

    @Override
    public Flux<Utilisateur> recupererUtilisateurs() {
        return utilisateurRepository.findAll().onErrorMap(err -> new TechnicalException("Erreur Critique", err));
    }

    @Override
    public Mono<Utilisateur> miseAJourUtilisateur(String mail, String motDePasse, UtilisateurDto utilisateurDto) {
        return rechercherUtilisateurParMailEtMotDePasse(mail, motDePasse)
                .flatMap(utilisateur -> {
                    Utilisateur utilisateurUpdated = utilisateurMapper.toEntity(utilisateurDto);
                    utilisateurUpdated.setId(utilisateur.getId());
                    utilisateurUpdated.setEstAdministrateur(utilisateur.getEstAdministrateur());
                    utilisateurUpdated.setMotDePasse(passwordEncoder.encode(utilisateurUpdated.getMotDePasse()));
                    return enregistrerUtilisateur(utilisateurUpdated);
                })
                .doOnSuccess(utilisateur -> log.debug(messageSource.getMessage(LOG_DEBUG_USER_UPDATED, new Object[]{utilisateur}, Locale.getDefault())));
    }

    @Override
    public Mono<Void> supprimerUtilisateur(String mail, String motDePasse) {
        return rechercherUtilisateurParMailEtMotDePasse(mail, motDePasse)
                .flatMap(utilisateur -> utilisateurRepository.deleteById(utilisateur.getId()))
                .doOnSuccess(ignored -> log.debug(messageSource.getMessage(LOG_DEBUG_USER_DELETED, new Object[]{mail}, Locale.getDefault())));
    }
}
