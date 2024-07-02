package br.com.josuemleite.forumhub.domain.dto.user;

import br.com.josuemleite.forumhub.domain.model.User;

public record UserListingDTO(
        Long id,
        String name,
        String email) {

    public UserListingDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}
