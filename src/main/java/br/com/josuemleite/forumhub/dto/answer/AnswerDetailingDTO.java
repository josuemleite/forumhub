package br.com.josuemleite.forumhub.dto.answer;

import br.com.josuemleite.forumhub.dto.topic.TopicListingDTO;
import br.com.josuemleite.forumhub.dto.user.UserListingDTO;
import br.com.josuemleite.forumhub.model.Answer;

import java.time.LocalDateTime;

public record AnswerDetailingDTO(
        Long id,
        String message,
        TopicListingDTO topic,
        LocalDateTime creationDate,
        UserListingDTO author,
        Boolean solution) {

    public AnswerDetailingDTO(Answer answer) {
        this(answer.getId(), answer.getMessage(), new TopicListingDTO(answer.getTopic()), answer.getCreationDate(), new UserListingDTO(answer.getAuthor()), answer.getSolution());
    }
}
