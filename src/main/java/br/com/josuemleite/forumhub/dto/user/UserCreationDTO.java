package br.com.josuemleite.forumhub.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UserCreationDTO(
        @NotBlank
        String name,
        @Email
        String email,
        @NotBlank
        String password,
        Set<Long> profiles,
        Set<Long> topics,
        Set<Long> answers) {
}
