package br.com.josuemleite.forumhub.domain.dto.course;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record CourseCreationDTO(
        @NotBlank
        String name,
        @NotBlank
        String category,
        Set<Long> topics) {
}
