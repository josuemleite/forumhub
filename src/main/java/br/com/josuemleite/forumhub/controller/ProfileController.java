package br.com.josuemleite.forumhub.controller;

import br.com.josuemleite.forumhub.domain.dto.profile.ProfileCreationDTO;
import br.com.josuemleite.forumhub.domain.dto.profile.ProfileDetailingDTO;
import br.com.josuemleite.forumhub.domain.dto.profile.ProfileListingDTO;
import br.com.josuemleite.forumhub.domain.dto.profile.ProfileUpdatingDTO;
import br.com.josuemleite.forumhub.domain.model.Profile;
import br.com.josuemleite.forumhub.service.ProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/perfis")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class ProfileController {

    private final ProfileService service;

    @PostMapping
    public ResponseEntity<ProfileDetailingDTO> create(@RequestBody @Valid ProfileCreationDTO data, UriComponentsBuilder uriComponentsBuilder) {
        Optional<Profile> existingProfile = service.findByName(data.name());
        if (existingProfile.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ProfileDetailingDTO(existingProfile.get()));
        }
        ProfileDetailingDTO profileDetailingDTO = service.insert(data);
        URI uri = uriComponentsBuilder.path("/profiles/{id}").buildAndExpand(profileDetailingDTO.id()).toUri();
        return ResponseEntity.created(uri).body(profileDetailingDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ProfileListingDTO>> list(@PageableDefault(sort = {"name"}) Pageable pageable) {
        Page<ProfileListingDTO> page = service.findAll(pageable).map(ProfileListingDTO::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDetailingDTO> update(@PathVariable Long id, @RequestBody @Valid ProfileUpdatingDTO data) {
        Optional<Profile> existingProfile = service.findByName(data.name());
        if (existingProfile.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ProfileDetailingDTO(existingProfile.get()));
        }
        Optional<Profile> optProfile = service.findById(id);
        if (optProfile.isPresent()) {
            ProfileDetailingDTO profileDetailingDTO = service.update(id, data);
            return ResponseEntity.ok(profileDetailingDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Profile> optProfile = service.findById(id);
        if (optProfile.isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDetailingDTO> detail(@PathVariable Long id) {
        Optional<Profile> optProfile = service.findById(id);
        if (optProfile.isPresent()) {
            Profile profile = optProfile.get();
            return ResponseEntity.ok(new ProfileDetailingDTO(profile));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
