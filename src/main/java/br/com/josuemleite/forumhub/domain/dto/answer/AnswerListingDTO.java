package br.com.josuemleite.forumhub.domain.dto.answer;

import br.com.josuemleite.forumhub.domain.model.Answer;

public record AnswerListingDTO(
        Long id,
        String message,
        Long topicId,
        Long authorId,
        Boolean solution) {

    public AnswerListingDTO(Answer answer) {
        this(answer.getId(), answer.getMessage(), answer.getTopic().getId(), answer.getAuthor().getId(), answer.getSolution());
    }
}
