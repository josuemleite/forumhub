package br.com.josuemleite.forumhub.repository;

import br.com.josuemleite.forumhub.domain.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByName(String name);
}
