package fr.adnane.portfolio.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Classe principale qui permet d'instancier les utilisateurs dans la base de données avec les données nécessaires.
 */
@Document(collection = "Utilisateur")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
public class Utilisateur {

    @Id
    private String id;

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
    @Min(value = 8, message = "Le mot de passe doit contenir {min} caractères minimum")
    private String motDePasse;

    @Builder.Default
    private Boolean estAdministrateur = false;

    @Builder.Default
    private LocalDateTime dateCreation = LocalDateTime.now();
}
