package br.com.josuemleite.forumhub.domain.dto.profile;

import br.com.josuemleite.forumhub.domain.dto.user.UserListingDTO;
import br.com.josuemleite.forumhub.domain.model.Profile;
import br.com.josuemleite.forumhub.domain.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public record ProfileDetailingDTO(
        Long id,
        String name,
        Set<UserListingDTO> users) {

    public ProfileDetailingDTO(Profile profile) {
        this(profile.getId(), profile.getName(), convertUsersToDTOs(profile.getUsers()));
    }

    private static Set<UserListingDTO> convertUsersToDTOs(Set<User> users) {
        return users.stream()
                .map(UserListingDTO::new)
                .collect(Collectors.toSet());
    }
}
