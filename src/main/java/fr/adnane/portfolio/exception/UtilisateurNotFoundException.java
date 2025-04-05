package fr.adnane.portfolio.exception;

import java.io.Serial;

/**
 * Classe d'exception pour intégrer les erreurs liées aux Utilisateurs non trouvées.
 */
public class UtilisateurNotFoundException extends ClassNotFoundException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UtilisateurNotFoundException(String message) {
        super(message);
    }
}
