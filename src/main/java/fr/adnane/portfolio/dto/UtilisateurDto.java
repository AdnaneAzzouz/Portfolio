package fr.adnane.portfolio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Classe DTO qui sera utilisé en tant que corps de la requête.
 */
@Data
@AllArgsConstructor
public class UtilisateurDto {

    @NonNull
    @NotNull(message = "Le nom d'utilisateur doit être renseigné")
    @NotEmpty(message = "Le nom d'utilisateur ne doit pas être vide")
    private String nom;

    @NonNull
    @NotNull(message = "Le prénom d'utilisateur doit être renseigné")
    @NotEmpty(message = "Le prénom d'utilisateur ne doit pas être vide")
    private String prenom;

    @NonNull
    @NotNull(message = "Le pseudo doit être renseigné")
    @NotEmpty(message = "Le pseudo ne doit pas être vide")
    private String pseudo;

    @NonNull
    @NotNull(message = "Le mail doit être renseigné")
    @NotEmpty(message = "Le mail ne doit pas être vide")
    @Email(message = "Le mail renseigné est invalide.")
    private String mail;

    @NonNull
    @NotNull(message = "Le mot de passe doit être renseigné")
    @NotEmpty(message = "Le mot de passe ne doit pas être vide")
    @Size(min = 8, message = "Le mot de passe doit contenir {min} caractères minimum")
    private String motDePasse;

}
