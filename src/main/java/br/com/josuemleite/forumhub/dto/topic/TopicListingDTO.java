package br.com.josuemleite.forumhub.dto.topic;

import br.com.josuemleite.forumhub.model.Topic;
import br.com.josuemleite.forumhub.model.enums.TopicStatus;

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
