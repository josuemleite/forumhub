package br.com.josuemleite.forumhub.domain.dto.course;

import java.util.Set;

public record CourseUpdatingDTO(
        String name,
        String category,
        Set<Long> topics) {
}
