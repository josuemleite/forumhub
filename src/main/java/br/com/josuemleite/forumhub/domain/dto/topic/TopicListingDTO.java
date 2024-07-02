package br.com.josuemleite.forumhub.domain.dto.topic;

import br.com.josuemleite.forumhub.domain.model.Topic;
import br.com.josuemleite.forumhub.domain.model.enums.TopicStatus;

import java.time.LocalDateTime;

public record TopicListingDTO(
        Long id,
        String title,
        String message,
        LocalDateTime creationDate,
        TopicStatus status) {

    public TopicListingDTO(Topic topic) {
        this(topic.getId(), topic.getTitle(), topic.getMessage(), topic.getCreationDate(), topic.getStatus());
    }
}
