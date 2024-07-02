package br.com.josuemleite.forumhub.domain.dto.authentication;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(@NotBlank String email, @NotBlank String password) {
}
