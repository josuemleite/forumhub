package br.com.josuemleite.forumhub.dto.topic;

import br.com.josuemleite.forumhub.model.enums.TopicStatus;

import java.util.Set;

public record TopicUpdatingDTO(
        String title,
        String message,
        TopicStatus status,
        Long authorId,
        Long courseId,
        Set<Long> answers) {
}
