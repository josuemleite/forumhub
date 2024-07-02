package br.com.josuemleite.forumhub.domain.dto.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnswerCreationDTO(
        @NotBlank
        String message,
        @NotNull
        Long topicId,
        @NotNull
        Long authorId) {
}
