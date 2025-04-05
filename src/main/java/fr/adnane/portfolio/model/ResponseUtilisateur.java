package fr.adnane.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Classe servant de réponse à certaines requêtes HTTP.
 */
@Data
@AllArgsConstructor
public class ResponseUtilisateur {
    /**
     * Code Réponse HTTP (200, 201, etc.)
     * <p>
     * Le type sera possiblement changer en HttpStatusCode ultérieurement
     */
    private Integer codeReponse;
    /**
     * Message de réponse.
     */
    private String messageReponse;
}
