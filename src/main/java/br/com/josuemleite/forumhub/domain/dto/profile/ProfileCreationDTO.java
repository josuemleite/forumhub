package br.com.josuemleite.forumhub.domain.dto.profile;

import jakarta.validation.constraints.NotBlank;

public record ProfileCreationDTO(
        @NotBlank
        String name) {
}
