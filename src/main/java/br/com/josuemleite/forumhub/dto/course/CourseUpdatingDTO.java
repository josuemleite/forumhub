package br.com.josuemleite.forumhub.dto.course;

import java.util.Set;

public record CourseUpdatingDTO(
        String name,
        String category,
        Set<Long> topics) {
}
