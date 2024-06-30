package br.com.josuemleite.forumhub.dto.user;

import java.util.Set;

public record UserUpdatingDTO(
        String name,
        String email,
        String password,
        Set<Long> profiles,
        Set<Long> topics,
        Set<Long> answers) {
}
