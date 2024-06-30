package br.com.josuemleite.forumhub.dto.profile;

import java.util.Set;

public record ProfileUpdatingDTO(
        String name,
        Set<Long> users) {
}
