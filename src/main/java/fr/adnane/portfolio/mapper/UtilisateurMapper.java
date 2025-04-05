package fr.adnane.portfolio.mapper;

import fr.adnane.portfolio.dto.UtilisateurDto;
import fr.adnane.portfolio.model.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Interface à créer pour Mapstruct.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UtilisateurMapper {

    /**
     * Transforme l'utilisateur en DTO. Utilisé en tant que corps de réponse.
     * @param utilisateur L'utilisateur à convertir
     * @return Un utilisateur DTO
     */
    UtilisateurDto toDto(final Utilisateur utilisateur);

    /**
     * Transforme l'utilisateurDTO en entité. Utilisé pour sauvegarder en base de données.
     * @param utilisateurDto L'utilisateur à convertir
     * @return Un utilisateur
     */
    @Mapping(target = "dateCreation", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estAdministrateur", ignore = true)
    Utilisateur toEntity(final UtilisateurDto utilisateurDto);
}
