package br.com.josuemleite.forumhub.service;

import br.com.josuemleite.forumhub.domain.dto.profile.ProfileCreationDTO;
import br.com.josuemleite.forumhub.domain.dto.profile.ProfileDetailingDTO;
import br.com.josuemleite.forumhub.domain.dto.profile.ProfileUpdatingDTO;
import br.com.josuemleite.forumhub.domain.model.Profile;
import br.com.josuemleite.forumhub.domain.model.User;
import br.com.josuemleite.forumhub.repository.ProfileRepository;
import br.com.josuemleite.forumhub.service.exceptions.DatabaseException;
import br.com.josuemleite.forumhub.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    private final UserService userService;

    public Page<Profile> findAll(Pageable pageable) {
        return profileRepository.findAll(pageable);
    }

    public Optional<Profile> findById(Long id) {
        return profileRepository.findById(id);
    }

    public Set<Profile> findProfilesById(Set<Long> profilesIds) {
        if (profilesIds == null) {
            return new HashSet<>();
        }
        Set<Profile> profiles = new HashSet<>();
        for (Long profileId : profilesIds) {
            profileRepository.findById(profileId).ifPresent(profiles::add);
        }
        return profiles;
    }

    public Optional<Profile> findByName(String name) {
        return profileRepository.findByName(name);
    }

    @Transactional
    public ProfileDetailingDTO insert(ProfileCreationDTO data) {
        Profile profile = new Profile();
        profile.setName(data.name());
        profileRepository.save(profile);
        return new ProfileDetailingDTO(profile);
    }

    @Transactional
    public void delete(Long id) {
        try {
            profileRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public ProfileDetailingDTO update(Long id, ProfileUpdatingDTO data) {
        try {
            Profile profile = profileRepository.getReferenceById(id);
            updateData(profile, data);
            profileRepository.save(profile);
            return new ProfileDetailingDTO(profile);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Profile profile, ProfileUpdatingDTO data) {
        if (data.name() != null && !data.name().isBlank()) {
            profile.setName(data.name());
        }
        if (data.users() != null) {
            Set<User> users = userService.findUsersById(data.users());
            Set<User> existingUsers = profile.getUsers();
            if (existingUsers != null) {
                profile.setUsers(existingUsers);
                profile.getUsers().addAll(users);
            } else {
                profile.setUsers(users);
            }
        }
    }
}
