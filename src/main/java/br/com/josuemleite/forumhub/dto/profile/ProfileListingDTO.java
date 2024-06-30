package br.com.josuemleite.forumhub.dto.profile;

import br.com.josuemleite.forumhub.model.Profile;

public record ProfileListingDTO(
        Long id,
        String name) {

    public ProfileListingDTO(Profile profile) {
        this(profile.getId(), profile.getName());
    }
}
