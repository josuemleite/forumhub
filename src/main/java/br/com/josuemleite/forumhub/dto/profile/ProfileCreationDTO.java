package br.com.josuemleite.forumhub.dto.profile;

import jakarta.validation.constraints.NotBlank;

public record ProfileCreationDTO(
        @NotBlank
        String name) {
}
