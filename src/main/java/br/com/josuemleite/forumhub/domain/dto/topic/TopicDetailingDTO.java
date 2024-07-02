package br.com.josuemleite.forumhub.domain.dto.topic;

import br.com.josuemleite.forumhub.domain.dto.answer.AnswerListingDTO;
import br.com.josuemleite.forumhub.domain.dto.course.CourseListingDTO;
import br.com.josuemleite.forumhub.domain.dto.user.UserListingDTO;
import br.com.josuemleite.forumhub.domain.model.Answer;
import br.com.josuemleite.forumhub.domain.model.Topic;
import br.com.josuemleite.forumhub.domain.model.enums.TopicStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record TopicDetailingDTO(
        Long id,
        String title,
        String message,
        LocalDateTime creationDate,
        TopicStatus status,
        UserListingDTO author,
        CourseListingDTO course,
        Set<AnswerListingDTO> answers) {

    public TopicDetailingDTO(Topic topic) {
        this(topic.getId(), topic.getTitle(), topic.getMessage(), topic.getCreationDate(), topic.getStatus(), new UserListingDTO(topic.getAuthor()), new CourseListingDTO(topic.getCourse()), convertAnswersToDTOs(topic.getAnswers()));
    }

    private static Set<AnswerListingDTO> convertAnswersToDTOs(Set<Answer> answers) {
        return answers.stream()
                .map(AnswerListingDTO::new)
                .collect(Collectors.toSet());
    }
}
