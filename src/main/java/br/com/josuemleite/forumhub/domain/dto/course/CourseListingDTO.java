package br.com.josuemleite.forumhub.domain.dto.course;

import br.com.josuemleite.forumhub.domain.model.Course;

public record CourseListingDTO(
        Long id,
        String name,
        String category) {

    public CourseListingDTO(Course course) {
        this(course.getId(), course.getName(), course.getCategory());
    }
}
