package br.com.josuemleite.forumhub.dto.profile;

import br.com.josuemleite.forumhub.dto.user.UserListingDTO;
import br.com.josuemleite.forumhub.model.Profile;
import br.com.josuemleite.forumhub.model.User;

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
