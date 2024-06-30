package br.com.josuemleite.forumhub.dto.user;

import br.com.josuemleite.forumhub.model.User;

public record UserListingDTO(
        Long id,
        String name,
        String email) {

    public UserListingDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}
