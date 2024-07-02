package br.com.josuemleite.forumhub.domain.dto.profile;

import br.com.josuemleite.forumhub.domain.model.Profile;

public record ProfileListingDTO(
        Long id,
        String name) {

    public ProfileListingDTO(Profile profile) {
        this(profile.getId(), profile.getName());
    }
}
