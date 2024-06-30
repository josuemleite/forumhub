package br.com.josuemleite.forumhub.dto.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record TopicCreationDTO(
        @NotBlank
        String title,
        @NotBlank
        String message,
        @NotNull
        Long authorId,
        @NotNull
        Long courseId,
        Set<Long> answers) {
}
