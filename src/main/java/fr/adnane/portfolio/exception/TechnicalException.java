package fr.adnane.portfolio.exception;

import java.io.Serial;

/**
 * Classe d'exception pour intégrer les erreurs techniques critiques.
 */
public class TechnicalException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TechnicalException(String message, Throwable cause) {
        super(message, cause);
    }
}
