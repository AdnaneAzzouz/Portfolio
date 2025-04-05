package fr.adnane.portfolio.controller.advice;

import fr.adnane.portfolio.exception.TechnicalException;
import fr.adnane.portfolio.exception.UtilisateurNotFoundException;
import fr.adnane.portfolio.model.ResponseUtilisateur;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

/**
 * Classe permettant d'attraper les erreurs qui sont retournées par les requêtes HTTP ou custom et traite le retour HTTP de manière approprié
 */
@RestControllerAdvice
@Slf4j
public class UtilisateurControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * Méthode attrapant l'exception MethodArgumentNotValidException, construit un message d'erreur et retour un code retour 422 avec le message d'erreur construit
     * @param ex Exception
     * @return 422
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    private ResponseEntity<ResponseUtilisateur> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        StringBuilder stringBuilder = new StringBuilder();
        for(FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            stringBuilder.append("\t- ").append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append("\n");
        }
        final String erreurMessage = stringBuilder.toString().strip();
        log.error(erreurMessage);
        return ResponseEntity.unprocessableEntity().body(new ResponseUtilisateur(HttpStatus.UNPROCESSABLE_ENTITY.value(), erreurMessage));
    }

    /**
     * Méthode attrapant l'exception personnalisée TechnicalException, récupère le message d'erreur et le retourne avec un code retour 500
     * @param ex Exception
     * @return 500
     */
    @ExceptionHandler(TechnicalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<ResponseUtilisateur> handleTechnicalException(TechnicalException ex) {
        final String erreurMessage = ExceptionUtils.getRootCause(ex).getLocalizedMessage();
        log.error(erreurMessage);
        return ResponseEntity.internalServerError().body(new ResponseUtilisateur(HttpStatus.INTERNAL_SERVER_ERROR.value(), erreurMessage));
    }

    /**
     * Méthode attrapant l'exception personnalisée UtilisateurNotFoundException, récupère le message d'erreur et le retourne avec un code retour 404
     * @param ex Exception
     * @return 404
     */
    @ExceptionHandler(UtilisateurNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<ResponseUtilisateur> handleUtilisateurNotFoundException(UtilisateurNotFoundException ex) {
        log.warn(ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseUtilisateur(HttpStatus.NOT_FOUND.value(), ex.getLocalizedMessage()));
    }
}
