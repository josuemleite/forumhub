package br.com.josuemleite.forumhub.dto.course;

import br.com.josuemleite.forumhub.model.Course;

public record CourseListingDTO(
        Long id,
        String name,
        String category) {

    public CourseListingDTO(Course course) {
        this(course.getId(), course.getName(), course.getCategory());
    }
}
