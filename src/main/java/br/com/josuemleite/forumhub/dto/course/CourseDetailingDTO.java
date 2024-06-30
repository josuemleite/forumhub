package br.com.josuemleite.forumhub.dto.course;

import br.com.josuemleite.forumhub.dto.topic.TopicListingDTO;
import br.com.josuemleite.forumhub.model.Course;
import br.com.josuemleite.forumhub.model.Topic;

import java.util.Set;
import java.util.stream.Collectors;

public record CourseDetailingDTO(
        Long id,
        String name,
        String category,
        Set<TopicListingDTO> topics) {

    public CourseDetailingDTO(Course course) {
        this(course.getId(), course.getName(), course.getCategory(), convertTopicToDTOs(course.getTopics()));
    }

    private static Set<TopicListingDTO> convertTopicToDTOs(Set<Topic> topics) {
        return topics.stream()
                .map(TopicListingDTO::new)
                .collect(Collectors.toSet());
    }
}
